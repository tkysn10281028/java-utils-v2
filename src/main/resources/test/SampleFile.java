package com.sono.process.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sono.process.dto.methodutils.ClassInfoDto;
import com.sono.process.dto.methodutils.LeftAndRightParenthesisDto;
import com.sono.process.dto.methodutils.MethodInfoDto;
import com.sono.process.dto.methodutils.ReservedWordDto;
import com.sono.process.utils.CommentRemoveUtils;

/**
 * メソッドを処理するクラス。
 * 
 * @author entakuya
 */
@Component
public class MethodUtilsBusinessLogic implements BusinessLogic {
	@Autowired
	CommentRemoveUtils commentRemoveUtils;

	ReservedWordDto reservedWordDto = new ReservedWordDto();
	Logger logger = LoggerFactory.getLogger(MethodUtilsBusinessLogic.class);
	protected Scanner scanner = null;
	protected String inputWord;
	protected Integer presentCursor = -1;
	protected ClassInfoDto classInfoDto;

	/**
	 * 各事前実行・メイン処理・最終処理の実行順番の制御及びビジネスロジックのエントリポイント
	 * 
	 * @see 抽象度：AAA
	 */
	public void executeMethodUtils() {
		this.beforeProcess();
		this.mainProcess();
		this.afterProcess();
	}

	/**
	 * 事前実行処理を実施 文字列の読み取りを行い、メイン処理使用の文字列を格納する
	 * 
	 * @see 抽象度:AA
	 */
	@Override
	public void beforeProcess() {
		logger.info("started beforeProcess in MethodUtilsBusinessLogic");
		initEnvAndPrintFirstLog();
		this.inputWord = scanner.next();
	}

	/**
	 * メイン処理を実施 事前処理の文字列を読み取って処理する
	 * 
	 * @see 抽象度：AA
	 */
	@Override
	public void mainProcess() {
		logger.info("started mainProcess in MethodUtilsBusinessLogic");
		try {
			readInputWordAndPrint(this.inputWord);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 事後処理を実施
	 * 
	 * @see 抽象度：AA
	 */
	@Override
	public void afterProcess() {
		logger.info("started afterProcess in MethodUtilsBusinessLogic");
	}

	/**
	 * 事前処理として余計な文字列の除去を行い、文字列を処理
	 * 
	 * @param inputWord 入力文字列
	 * @throws IOException
	 * @see 抽象度：A
	 */
	protected void readInputWordAndPrint(String inputWord) throws IOException {
		var tmp = CommentRemoveUtils.remove(inputWord);
		var inputWordList = splitInputWordBySpaceAndFilterBlank(
				replaceParenthesisIntoSpaced(replaceWordIntoSpace(replaceQuotedWord(tmp))));
		var inputWordListSize = inputWordList.size();
		if (scanRoundParenthesis(inputWordList, inputWordListSize)
				&& scanMiddleParenthesis(inputWordList, inputWordListSize)) {
			generateClassInfo(inputWordList);
			this.presentCursor = 0;
			var methodList = generateMethodInfoList(this.classInfoDto.getClassPart());
			outputAllInfo(classInfoDto, methodList);
		} else {
			throw new IllegalArgumentException("Not Correct number of Parenthesis.");
		}
	}

	/**
	 * 与えられた文字列の情報からクラス情報DTOを作成して返却
	 * 
	 * @param inputWordList 事前処理済みの文字列リスト
	 * @return 文字列から読み取ったクラス情報DTO
	 * @see 抽象度：B
	 */
	protected ClassInfoDto generateClassInfo(List<String> inputWordList) {
		this.classInfoDto = new ClassInfoDto();
		for (int i = 0; i < inputWordList.size(); i++) {
			var inputWord = inputWordList.get(i);
			// classのキーワードかつリストの最後ではない場合
			if ((inputWord.equals("class") || inputWord.equals("interface")) && i < inputWordList.size() - 1) {
				this.classInfoDto.setClassName(inputWordList.get(i + 1));
				var leftBracketAfterList = generateSplitListAfterPart(inputWordList, i);
				var classPartList = generateSplitListByFromAndAfterNum(leftBracketAfterList,
						getFirstCursorCountLeftBracket(leftBracketAfterList),
						getLastCursorCountRightBracket(leftBracketAfterList));
				this.classInfoDto.setClassPart(classPartList);
			}
		}
		if (StringUtils.isEmpty(this.classInfoDto.getClassName())) {
			throw new IllegalArgumentException("No Class Found.");
		}
		return this.classInfoDto;
	}

	/**
	 * クラス内部で存在しているメソッドのリストDTOを返却
	 * 
	 * @param classInfoPart
	 * @return
	 * @see 抽象度：B
	 */
	protected List<MethodInfoDto> generateMethodInfoList(List<String> classInfoPart) {
		var info = generateMethodImplementsLoop(classInfoPart, 0);
		return info;
	}

	/**
	 * 読み取ったデータを出力する.
	 * 
	 * @param classInfoDto
	 * @param methodInfoDtos
	 */
	protected void outputAllInfo(ClassInfoDto classInfoDto, List<MethodInfoDto> methodInfoDtos) {
		outputAllInfoSystemOut(classInfoDto, methodInfoDtos);
	}

	/**
	 * 読み取りデータを標準出力する
	 * 
	 * @param classInfoDto
	 * @param methodInfoDtos
	 */
	protected void outputAllInfoSystemOut(ClassInfoDto classInfoDto, List<MethodInfoDto> methodInfoDtos) {
		System.out.println("クラス名: " + classInfoDto.getClassName());
		System.out.println("->");
		outputAllMethodInfoDtos(methodInfoDtos, 0);
	}

	/**
	 * メソッド情報を再起的に呼び出して標準出力する.
	 * 
	 * @param methodInfoDtos
	 */
	protected void outputAllMethodInfoDtos(List<MethodInfoDto> methodInfoDtos, Integer hierarchyCount) {
		if (methodInfoDtos.size() <= 0) {
			return;
		}
		hierarchyCount++;
		for (int i = 0; i < methodInfoDtos.size(); i++) {
			outputAllMethodInfo(methodInfoDtos.get(i), hierarchyCount);
			outputAllMethodInfoDtos(methodInfoDtos.get(i).getMethodInfoList(), hierarchyCount);
		}
	}

	/**
	 * メソッド情報を全て出力.
	 * 
	 * @param methodInfoDto
	 */
	protected void outputAllMethodInfo(MethodInfoDto methodInfoDto, Integer hierarchyCount) {
		if (!judgeProperMethodForOutput(methodInfoDto)) {
			return;
		}
		var sb = new StringBuilder();
		for (int i = 0; i < hierarchyCount; i++) {
			sb.append("\t\t\t");
		}
		System.out.println(sb.toString() + "名前: " + methodInfoDto.getMethodName());
		var returnVal = StringUtils.isEmpty(methodInfoDto.getMethodReturnVal()) ? ""
				: methodInfoDto.getMethodReturnVal();
		var signature = methodInfoDto.getMethodSignature() == null ? ""
				: String.join("  ,  ", methodInfoDto.getMethodSignature());
		System.out.println(sb.toString() + "戻り値: " + returnVal);
		System.out.println(sb.toString() + "引数: " + signature);
		System.out.println("\n");
	}

	/**
	 * <pre>
	 * 出力に適したメソッド情報かを判定する
	 * 以下のパターンについて除外する(随時更新予定)
	 * 1 メソッド名がnullの場合
	 * 2 メソッドの戻り値が;
	 * 3 メソッドの戻り値がreturn
	 * 
	 * </pre>
	 * 
	 * @param methodInfoDto
	 * @return
	 */
	protected boolean judgeProperMethodForOutput(MethodInfoDto methodInfoDto) {
		if (StringUtils.isEmpty(methodInfoDto.getMethodName())) {
			return false;
		}
		if (StringUtils.isNotEmpty(methodInfoDto.getMethodReturnVal())
				&& (methodInfoDto.getMethodReturnVal().equals(";")
						|| methodInfoDto.getMethodReturnVal().equals("return"))) {
			return false;
		}
		return true;
	}

	/**
	 * リストからメソッド情報のリストを返却
	 * 
	 * @param targetlist 取得元のリスト
	 * @param index      リストの個別要素取得用インデックス
	 * @return メソッド情報リスト
	 * @see 抽象度：Bー
	 */
	protected List<MethodInfoDto> generateMethodImplementsLoop(List<String> targetlist, Integer startIndex) {
		var methodInfoDto = new MethodInfoDto();
		List<MethodInfoDto> methodInfoDtos = new ArrayList<MethodInfoDto>();
		List<MethodInfoDto> methodInfoListOutput = new ArrayList<MethodInfoDto>();
		for (int i = startIndex; i < targetlist.size() || this.presentCursor < targetlist.size(); i++) {
			setCursorToPresent();
			if (this.presentCursor == targetlist.size()) {
				methodInfoListOutput = methodInfoDtos;
				continue;
			}
			var target = targetlist.get(i);
			if (judgeIsRightBracket(target) == null) {
				continue;
			} else if (judgeIsRightBracket(target)) {
				var parenthesisDto = generateFirstAndLastParenthesisCount(targetlist, startIndex);
				var methodPart = generateSplitListByFromAndAfterNum(targetlist, startIndex, i);
				var methodName = generateMethodName(targetlist, parenthesisDto);
				var methodSignature = generateMethodSignature(targetlist, parenthesisDto);
				var methodReturnVal = generateMethodReturnVal(targetlist, parenthesisDto);
				methodInfoListOutput = setMethodRelatedInfoAndGenerateNewMethodList(methodInfoDto, methodInfoDtos,
						methodPart, methodName, methodSignature, methodReturnVal);
				break;
			} else if (!judgeIsRightBracket(target)) {
				var resultList = generateMethodImplementsLoop(targetlist, i + 1);
				resultList.forEach((result) -> {
					methodInfoDtos.add(result);
				});
				i = this.presentCursor;
				continue;
			} else {
				continue;
			}
		}
		return methodInfoListOutput;
	}

	/**
	 * 文字列のリストを受け取って「{」の文字がいればそのインデックスを返却 最初から数える
	 * 
	 * @param inputWordList 文字列リスト
	 * @return {が出現するリストのインデックス
	 * @see 抽象度：C
	 */
	protected Integer getFirstCursorCountLeftBracket(List<String> inputWordList) {
		var firstCursorCount = 0;
		for (int i = 0; i < inputWordList.size(); i++) {
			if (inputWordList.get(i).equals("{")) {
				firstCursorCount = i + 1;
				break;
			}
		}
		return firstCursorCount;
	}

	/**
	 * 文字列のリストを受け取って「}」の文字がいればそのインデックスを返却 最後から数える
	 * 
	 * @param inputWordList
	 * @return }が出現するリストのインデックス
	 * @see 抽象度：C
	 */
	protected Integer getLastCursorCountRightBracket(List<String> inputWordList) {
		var lastCursorCount = 0;
		for (int i = inputWordList.size() - 1; i >= 0; i--) {
			if (inputWordList.get(i).equals("}")) {
				lastCursorCount = i;
				break;
			}
		}
		return lastCursorCount;
	}

	/**
	 * 「{」「}」を検知する
	 * 
	 * @param inputWord
	 * @return {の時false }の時true それ以外はnull
	 * @see 抽象度：D
	 */
	protected Boolean judgeIsRightBracket(String inputWord) {
		if (inputWord.equals("{")) {
			return false;
		} else if (inputWord.equals("}")) {
			return true;
		} else {
			return null;
		}
	}

	/**
	 * 指定したインデックスまでのカッコを検索して数があわない場合はfalseを返す
	 * 
	 * @param targetList
	 * @return 抽象度：C
	 */
	protected boolean scanRoundParenthesis(List<String> targetList, Integer endIndex) {
		int leftParenthesisCount = 0;
		int rightParenthesisCount = 0;
		for (int i = 0; i < endIndex; i++) {
			String target = targetList.get(i);
			if (target.equals("(")) {
				leftParenthesisCount++;
			} else if (target.equals(")")) {
				rightParenthesisCount++;
			} else {
				continue;
			}
		}
		if (leftParenthesisCount == rightParenthesisCount) {
			return true;
		}
		return false;
	}

	/**
	 * 指定したインデックスまでのカッコを検索して数があわない場合はfalseを返す
	 * 
	 * @param targetList
	 * @return
	 * @see 抽象度：C
	 */
	protected boolean scanMiddleParenthesis(List<String> targetList, Integer endIndex) {
		int leftParenthesisCount = 0;
		int rightParenthesisCount = 0;
		for (int i = 0; i < endIndex; i++) {
			String target = targetList.get(i);
			if (target.equals("{")) {
				leftParenthesisCount++;
			} else if (target.equals("}")) {
				rightParenthesisCount++;
			} else {
				continue;
			}
		}
		if (leftParenthesisCount == rightParenthesisCount) {
			return true;
		}
		return false;
	}

	/**
	 * グローバル変数のカーソル位置を変更する
	 * 
	 * @param list
	 * @see 抽象度：D
	 */
	protected void setCursorToPresent() {
		this.presentCursor++;
	}

	/**
	 * メソッドの必要な情報をメソッド情報DTOに詰めてからリストを返却する
	 * 
	 * @param methodInfoDto   詰める先のメソッド情報 DTO
	 * @param methodInfoDtos  子メソッド情報
	 * @param methodPart      メソッド本体部分
	 * @param methodName      メソッド名
	 * @param methodSignature メソッド引数
	 * @param methodReturnVal メソッド戻り値
	 * @return メソッド情報DTOのリスト
	 * @see 抽象度：C
	 */
	protected List<MethodInfoDto> setMethodRelatedInfoAndGenerateNewMethodList(MethodInfoDto methodInfoDto,
			List<MethodInfoDto> methodInfoDtos, List<String> methodPart, String methodName,
			List<String> methodSignature, String methodReturnVal) {
		methodInfoDto.setMethodInfoList(methodInfoDtos);
		methodInfoDto.setMethodPart(methodPart);
		methodInfoDto.setMethodName(methodName);
		methodInfoDto.setMethodSignature(methodSignature);
		methodInfoDto.setMethodReturnVal(methodReturnVal);
		var methodInfoDtosOutput = new ArrayList<MethodInfoDto>();
		methodInfoDtosOutput.add(methodInfoDto);
		return methodInfoDtosOutput;
	}

	/**
	 * メソッド名を取得する。
	 * 
	 * @param targetlist
	 * @param startIndex
	 * @return メソッド名
	 * @see 抽象度：C
	 */
	protected String generateMethodName(List<String> targetlist,
			LeftAndRightParenthesisDto leftAndRightParenthesisDto) {
		if (judgeIsLeftPositionZeroOrTargetNull(leftAndRightParenthesisDto)) {
			return null;
		}
		var methodName = getOnePreviousWordFromList(targetlist,
				leftAndRightParenthesisDto.getLeftParenthesisPosition());

		return methodName;
	}

	/**
	 * メソッド引数を取得する。
	 * 
	 * @param targetlist
	 * @param startIndex
	 * @return
	 * @see 抽象度：C
	 */
	protected List<String> generateMethodSignature(List<String> targetlist,
			LeftAndRightParenthesisDto leftAndRightParenthesisDto) {
		if (judgeIsLeftPositionZeroOrTargetNull(leftAndRightParenthesisDto)) {
			return null;
		}
		if (judgeIsReservedWordOrClassName(
				getOnePreviousWordFromList(targetlist, leftAndRightParenthesisDto.getLeftParenthesisPosition()))) {
			return null;
		}
		var list = generateSplitListByFromAndAfterNum(targetlist,
				leftAndRightParenthesisDto.getLeftParenthesisPosition(),
				leftAndRightParenthesisDto.getRightParenthesisPosition());
		return Arrays.asList(String.join(" ", list).replaceAll("\\(", "").replaceAll("\\)", "").split(","));
	}

	/**
	 * メソッド戻り値を取得する
	 * 
	 * @param targetlist
	 * @param startIndex
	 * @return
	 * @see 抽象度：C
	 */
	protected String generateMethodReturnVal(List<String> targetlist,
			LeftAndRightParenthesisDto leftAndRightParenthesisDto) {
		if (judgeIsLeftPositionZeroOrTargetNull(leftAndRightParenthesisDto)) {
			return null;
		}
		if (judgeIsReservedWordOrClassName(
				getOnePreviousWordFromList(targetlist, leftAndRightParenthesisDto.getLeftParenthesisPosition()))) {
			return null;
		}
		return getTwoPreviousWordFromList(targetlist, leftAndRightParenthesisDto.getLeftParenthesisPosition());
	}

	/**
	 * 渡されたインデックスから上に遡って、最初の)及びその)の閉じ(のDTOを返却する
	 * 
	 * @param targetList 対象のリスト
	 * @param startIndex 遡る最初のインデックス
	 * @return ()のDTO
	 * @see 抽象度：C
	 */
	protected LeftAndRightParenthesisDto generateFirstAndLastParenthesisCount(List<String> targetList,
			Integer startIndex) {
		var parenthesisCountDto = new LeftAndRightParenthesisDto(0, 0, 0, 0);
		// 渡されたリストの先頭から読み込む予定のインデックスまでの()の数をスキャン->合わない場合はnullを返却
		if (!scanRoundParenthesis(targetList, startIndex)) {
			return null;
		}
		for (int i = startIndex; i > 0; i--) {
			if (judgeIsBreakForParenthesisCountMethod(parenthesisCountDto)) {
				break;
			}
			var target = targetList.get(i);
			setFirstRightParenthesisPosition(target, parenthesisCountDto, i);
			setParenthesisCountAndRightPosition(target, parenthesisCountDto, i);
		}
		return parenthesisCountDto;
	}

	/**
	 * 全てのカッコの左右にスペースを付加する
	 * 
	 * @param parenthesisedWords
	 * @return
	 */
	protected String replaceParenthesisIntoSpaced(String parenthesisedWords) {
		var replaced1 = StringUtils.replace(parenthesisedWords, "(", " ( ");
		var replaced2 = StringUtils.replace(replaced1, ")", " ) ");
		var replaced3 = StringUtils.replace(replaced2, "{", " { ");
		var replaced4 = StringUtils.replace(replaced3, "}", " } ");
		return replaced4;
	}

	/**
	 * DTO内のかっこの数をみて、両方とも0でないかつ一致していた場合にブレイクする
	 * 
	 * @param parenthesisDto
	 * @return ループを抜けるかどうか
	 * @see 抽象度：D
	 */
	protected boolean judgeIsBreakForParenthesisCountMethod(LeftAndRightParenthesisDto parenthesisDto) {
		var leftParenthesisCount = parenthesisDto.getLeftParenthesisCount();
		var rightParenthesisCount = parenthesisDto.getRightParenthesisCount();
		if (leftParenthesisCount != 0 && rightParenthesisCount != 0 && leftParenthesisCount == rightParenthesisCount) {
			return true;
		}
		return false;
	}

	/**
	 * <pre>
	 * 最初の右カッコの位置を設定する.
	 * 既に右カッコをスキャンしている場合には位置はセットされないため最初にスキャンした位置が入る。
	 * </pre>
	 * 
	 * @param target          判定対象の文字列
	 * @param parenthesisDto  カッコ情報を保持するDTO
	 * @param presentPosition 現在のリスト上の位置
	 * @see 抽象度：D
	 */
	protected void setFirstRightParenthesisPosition(String target, LeftAndRightParenthesisDto parenthesisDto,
			Integer presentPosition) {
		var rightParenthesisCount = parenthesisDto.getRightParenthesisCount();
		if (target.equals(")") && rightParenthesisCount == 0) {
			parenthesisDto.setRightParenthesisPosition(presentPosition);
		}
	}

	/**
	 * <pre>
	 * 左カッコの数及び位置をセットする。
	 * 右カッコの場合は数のみセットする(setFirstRightParenthesisPositionで既にセットしているため)
	 * </pre>
	 * 
	 * @param target          判定対象の文字列
	 * @param parenthesisDto  カッコ情報を保持するDTO
	 * @param presentPosition 現在のリスト上の位置
	 * @see 抽象度：D
	 */
	protected void setParenthesisCountAndRightPosition(String target, LeftAndRightParenthesisDto parenthesisDto,
			Integer presentPosition) {
		if (target.equals("(")) {
			parenthesisDto.setLeftParenthesisCount(parenthesisDto.getLeftParenthesisCount() + 1);
			parenthesisDto.setLeftParenthesisPosition(presentPosition);
		} else if (target.equals(")")) {
			parenthesisDto.setRightParenthesisCount(parenthesisDto.getRightParenthesisCount() + 1);
		}
	}

	/**
	 * 渡されたインデックスの1つ前の文字列をリストから取得
	 * 
	 * @param targetList 取得対象文字リスト
	 * @param index      対象インデックス
	 * @return
	 * @see 抽象度：D
	 */
	protected String getOnePreviousWordFromList(List<String> targetList, Integer index) {
		if (targetList.size() <= 0) {
			return null;
		}
		if (index == 0) {
			return targetList.get(0);
		} else {
			return targetList.get(index - 1);
		}
	}

	/**
	 * <pre>
	 * 渡されたインデックスの2つ前の文字列をリストから取得.
	 * いない場合はそのインデックスの文字列を取得する.
	 * 戻り値の取得用.
	 * </pre>
	 * 
	 * 
	 * @param targetList
	 * @param index
	 * @return
	 * @see 抽象度：D
	 */
	protected String getTwoPreviousWordFromList(List<String> targetList, Integer index) {
		if (targetList.size() <= 0) {
			return null;
		}
		if (index == 0 || index == 1) {
			return targetList.get(index);
		} else {
			return targetList.get(index - 2);
		}
	}

	/**
	 * 渡された文字が予約語かクラス名だった場合にtrueを返却する
	 * 
	 * @param target
	 * @return
	 */
	protected boolean judgeIsReservedWordOrClassName(String target) {
		var reservedWords = reservedWordDto.getReservedWordList();
		var className = classInfoDto.getClassName();
		if (reservedWords.size() <= 0 || StringUtils.isEmpty(className)) {
			throw new IllegalArgumentException("Class Name Null or Reserved Words Are Empty");
		}
		if (reservedWords.stream().filter(word -> word.equals(target)).findAny().isPresent()
				|| target.equals(className)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 丸括弧dtoの有効性判定.
	 * 
	 * @param dto
	 * @return
	 */
	protected boolean judgeIsLeftPositionZeroOrTargetNull(LeftAndRightParenthesisDto dto) {
		if (ObjectUtils.isEmpty(dto) || dto.getLeftParenthesisCount() == 0 || dto.getRightParenthesisCount() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * リストを与えられたインデックスの後半部分まで切り取る
	 * 
	 * @param list     切り取り対象リスト
	 * @param splitNum 切り取りする最初の位置
	 * @return 切り取ったリスト
	 * @see 抽象度：D
	 */
	protected List<String> generateSplitListAfterPart(List<String> list, Integer splitNum) {
		if (splitNum >= list.size()) {
			throw new IllegalArgumentException("Cannot Split List.");
		}
		return new ArrayList<String>(list.subList(splitNum, list.size()));
	}

	/**
	 * リストを指定された位置で切り取りする
	 * 
	 * @param list     切り取り対象のリスト
	 * @param fromNum  切り取りのスタート位置
	 * @param AfterNum 切り取りの終了位置
	 * @return 切り取ったリスト
	 * @see 抽象度：D
	 */
	protected List<String> generateSplitListByFromAndAfterNum(List<String> list, Integer fromNum, Integer AfterNum) {
		return new ArrayList<String>(list.subList(fromNum, AfterNum));
	}

	/**
	 * 特定の単語をスペースに変換する
	 * 
	 * @param 変換対象
	 * @return 変換後
	 * @see 抽象度：D
	 */
	protected String replaceWordIntoSpace(String inputWord) {
		var replaced1 = StringUtils.replace(inputWord, "\n", " ");
		var replaced2 = StringUtils.replace(replaced1, "\t", " ");
		return replaced2;
	}

	protected String replaceQuotedWord(String str) {
		return str.replaceAll("\".*\"", "").replaceAll("\'.*\'", "");
	}

	/**
	 * 空白部分の除去とスペースで単語を区切ってリストに変換
	 * 
	 * @param inputWord 変換対象
	 * @return 変換後
	 * @see 抽象度：D
	 */
	protected List<String> splitInputWordBySpaceAndFilterBlank(String inputWord) {
		return Arrays.asList(inputWord.split(" ")).stream().filter(word -> !word.equals(""))
				.collect(Collectors.toList());
	}

	/**
	 * 本処理の初期処理
	 * 
	 * @see 抽象度：D
	 */
	protected void initEnvAndPrintFirstLog() {
		this.scanner = new Scanner(System.in);
		this.scanner.useDelimiter(Pattern.compile(";;;;"));
		System.out.println("Input Your Program Here->");
	}

	/**
	 * 本処理の終了処理
	 * 
	 * @see 抽象度：D
	 */
	protected void closeEnv() {
		this.scanner.close();
	}

	/**
	 * デバッグ用メソッド. デバッグ時はここにBP貼ってね
	 * 
	 * @param obj
	 * @see 抽象度：D
	 */
	protected void evacuationForDebug(Object obj) {
		var evacObj = obj;
	}
}
