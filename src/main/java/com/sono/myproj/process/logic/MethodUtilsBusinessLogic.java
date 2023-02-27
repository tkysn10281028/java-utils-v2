package com.sono.myproj.process.logic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sono.myproj.process.dto.PrgElementInfoDto;
import com.sono.myproj.process.utils.CommentRemoveUtils;
import com.sono.myproj.process.utils.ErrorUtils;
import com.sono.myproj.process.utils.ListUtils;
import com.sono.myproj.process.utils.ParenthesisUtils;
import com.sono.myproj.process.utils.ReplaceWordUtils;
import com.sono.myproj.process.utils.ValidationUtils;

@Component
public class MethodUtilsBusinessLogic implements BusinessLogic {
	@Autowired
	protected ParenthesisUtils parenthesisUtils;
	@Autowired
	protected ReplaceWordUtils replaceWordUtils;
	@Autowired
	protected ListUtils listUtils;
	@Autowired
	protected ValidationUtils validationUtils;
	Logger logger = LoggerFactory.getLogger(MethodUtilsBusinessLogic.class);
	protected Scanner scanner = null;
	private String prcTargetStr = null;
	private Integer cursor = -1;

	/**
	 * <pre>
	 * プロセスから渡されたロジッククラスのエントリポイント
	 * 関連項目の抽象度一覧（目安）
	 * AAAA：別クラスとのやり取りをするレベル
	 * AAA：当該ロジッククラスの制御を行う処理　事前・メイン・事後処理
	 * AA：当該ロジッククラスのプロセス毎の具体的な処理を実装する
	 * A：プロセス内の具体的な処理を細かく記述する
	 * B：
	 * C：Bの処理に依存する文字列変換処理などの低レベル処理
	 * D：当該クラスに依存しない共通で使用できる低レベル処理
	 * </pre>
	 * 
	 * @see AAAA
	 */
	public void executeMethodUtils() {
		try {

			this.beforeProcess();
			this.mainProcess();
			this.afterProcess();
		} catch (Exception e) {
			logger.error("\n" + ErrorUtils.getStackTrace(e));
			System.out.println("異常終了しました。システム管理者に問い合わせてください。");
		}

	}

	/**
	 * <pre>
	 * 事前処理
	 * 標準入力されたファイルのURLから中身の文字列を生成する
	 * </pre>
	 * 
	 * @throws Exception
	 * 
	 * @see AAA
	 */
	@Override
	public void beforeProcess() throws Exception {
		initEnvAndPrintFirstLog();
		logger.info("before process started.");
		this.prcTargetStr = getFileStringFromInputUrl(scanner.nextLine()).stream().collect(Collectors.joining("\n"));
	}

	/**
	 * <pre>
	 * データ生成のメイン処理
	 * メイン処理
	 * </pre>
	 * 
	 * @see AAA
	 */
	@Override
	public void mainProcess() throws Exception {
		logger.info("main process started.");
		var result = beforeMainProcess(this.prcTargetStr);
		var prgElementList = generatePrgElementList(result);
		var parenthesisDtoList = generatePrgElementDtoList(prgElementList);
		System.out.println(prgElementList);
	}

	/**
	 * <pre>
	 * 終了処理
	 * 生成されたデータを出力する
	 * </pre>
	 * 
	 * @see AAA
	 */
	@Override
	public void afterProcess() {
		logger.info("after process started.");
	}

	/**
	 * <pre>
	 * 事前処理の主ロジック.
	 * ファイルURLを受け取って中身を返却する.
	 * </pre>
	 * 
	 * @param inputWord
	 * @return
	 * @throws Exception
	 * @see AA
	 */
	protected List<String> getFileStringFromInputUrl(String inputWord) throws Exception {
		var url = replaceWordUtils.replaceIntoBlank(inputWord);
		if (!validationUtils.validateUrl(url)) {
			throw new IllegalArgumentException("URL Validation Failed. Not Proper Url.");
		}
		var outputStrList = Files.readAllLines(Paths.get(url));
		return outputStrList;
	}

	/**
	 * <pre>
	 * メイン処理の事前処理
	 * 標準入力の文字列のコメント及び引用符の中身を除去
	 * プログラム中の左右カッコ数が同数であることを確認する.
	 * </pre>
	 * 
	 * @param inputWord
	 * @return
	 * @throws IOException
	 * @see AA
	 */
	protected String beforeMainProcess(String inputWord) throws Exception {
		var replacedStr = replaceWordUtils.replaceQuotedWord(CommentRemoveUtils.remove(inputWord));
		if (parenthesisUtils.scanRoundParenthesis(replacedStr) && parenthesisUtils.scanMiddleParenthesis(replacedStr)) {
			return replacedStr;
		} else {
			throw new IllegalArgumentException("Not Proper Parenthesis Count.");
		}
	}

	/**
	 * @return
	 */
	protected List<String> generatePrgElementList(String text) {
		var spaced = parenthesisUtils.replaceParenthesisIntoSpaced(text);
		return Arrays.asList(spaced.split(" ")).stream().filter(word -> !word.equals("")).collect(Collectors.toList());
	}

	/**
	 * @param prgElementList
	 * @return
	 */
	protected List<PrgElementInfoDto> generatePrgElementDtoList(List<String> prgElementList) {
		this.cursor = 0;
		var resultlist = generatePrgElementDtoListRecursion(prgElementList, 0, 0);
		return resultlist;
	}

	/**
	 * 再起的に呼び出す用
	 * 
	 * @param targetList
	 * @param startIndex
	 * @return
	 */
	protected List<PrgElementInfoDto> generatePrgElementDtoListRecursion(List<String> targetList, Integer startIndex,
			Integer hierarchyCount) {
		var prgElementInfo = new PrgElementInfoDto();
		var prgElementInfoList = new ArrayList<PrgElementInfoDto>();
		List<PrgElementInfoDto> prgElementInfoListForOutput = new ArrayList<PrgElementInfoDto>();
		hierarchyCount++;
		// ループインデックスが処理するリストのサイズを超える場合は空リストを返却する.
		if (!validationUtils.validateLoopIndex(targetList, startIndex)
				|| !validationUtils.validateLoopIndex(targetList, this.cursor)) {
			return prgElementInfoListForOutput;
		}
		for (int i = startIndex; i < targetList.size(); i++) {
			setCursor();
			var target = targetList.get(i);
			// 左右カッコどちらでもない時は次のループへ
			if (parenthesisUtils.judgeIsRightBracket(target) == null) {
				continue;
			}
			// 左カッコだった時は再起処理してから次のループに行く
			else if (!parenthesisUtils.judgeIsRightBracket(target)) {
				var result = generatePrgElementDtoListRecursion(targetList, i + 1, hierarchyCount);
				addIntoPrgElementInfoList(result, prgElementInfoList);
				i = this.cursor;

				// カーソルが最終行に到達していた場合の処理
				if (this.cursor >= targetList.size() - 1) {
					prgElementInfoListForOutput = prgElementInfoList;
					System.out.println();
				}
				continue;
			}
			// 右カッコだった時は必要な要素をセットしてdtoリストを返却する
			else if (parenthesisUtils.judgeIsRightBracket(target)) {
				prgElementInfoListForOutput = setPrgElementInfo(targetList, prgElementInfo, prgElementInfoList,
						startIndex, i, hierarchyCount);
				break;
			}
		}
		return prgElementInfoListForOutput;
	}

	/**
	 * <pre>
	 *  右カッコだった場合の処理.
	 *  DTOに必要な情報をセットする.
	 * </pre>
	 * 
	 * @param prgElementInfoDto
	 * @param prgElementInfoDtos
	 * @return 情報が全て入ったdtoのリストを返却する.
	 */
	protected List<PrgElementInfoDto> setPrgElementInfo(List<String> targetList, PrgElementInfoDto prgElementInfoDto,
			List<PrgElementInfoDto> prgElementInfoDtos, Integer startIndex, Integer presentindex,
			Integer hierarchyCount) {

		var prgElementInfoListForOutput = new ArrayList<PrgElementInfoDto>();
		prgElementInfoDto.setElementName(generateElementName(targetList, startIndex, hierarchyCount));
		prgElementInfoDto.setSignature(generateElementSignature(targetList, startIndex, hierarchyCount));
		prgElementInfoDto.setReturnVal(generateReturnVal(targetList, startIndex, hierarchyCount));
		prgElementInfoDto.setPrgBody(generatePrgBody(targetList, startIndex, presentindex));
		prgElementInfoDto.setPrgElementList(prgElementInfoDtos);
		prgElementInfoListForOutput.add(prgElementInfoDto);
		return prgElementInfoListForOutput;
	}

	/**
	 * <pre>
	 * 左カッコだった場合の処理.追加対象にdtoを追加する
	 * </pre>
	 * 
	 * @param addList
	 * @param addTarget
	 */
	protected void addIntoPrgElementInfoList(List<PrgElementInfoDto> addList, List<PrgElementInfoDto> addTarget) {
		addList.forEach((target) -> {
			addTarget.add(target);
		});
	}

	/**
	 * カーソルを+1する
	 */
	protected void setCursor() {
		this.cursor++;
	}

	/**
	 * メソッドの本体部分を作成する.
	 * 
	 * @param targetList
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	protected String generatePrgBody(List<String> targetList, Integer startIndex, Integer endIndex) {
		var list = listUtils.generateSplitListByFromAndAfterNum(targetList, startIndex, endIndex - 1);
		return String.join("\n", list).replaceAll("\t", "");
	}

	/**
	 * <pre>
	 * 与えられたリストの{の手前から調べてメソッド名を探索する.
	 * 階層構造毎に処理を分岐させる.
	 * </pre>
	 * 
	 * @param targetList
	 * @param startIndex
	 * @param hierarchyCount
	 * @return
	 */
	protected String generateElementName(List<String> targetList, Integer startIndex, Integer hierarchyCount) {
		if (hierarchyCount <= 0) {
			return null;
		}
		switch (hierarchyCount) {
		case 0:
			return null;
		case 1:
			return listUtils.findClassAfterWord(targetList, startIndex);
		case 2:
			return listUtils.findOnePreviousWord(targetList, parenthesisUtils
					.generateFirstAndLastParenthesisCount(targetList, startIndex).getLeftParenthesisPosition());
		default:
			return listUtils.findReservedWord(targetList, startIndex);
		}
	}

	/**
	 * <pre>
	 * 与えられたリストの{の手前から調べてメソッド引数を探索する.
	 * 階層構造毎に処理を分岐させる.
	 * </pre>
	 * 
	 * @param targetList
	 * @param startIndex
	 * @param hierarchyCount
	 * @return
	 */
	protected List<String> generateElementSignature(List<String> targetList, Integer startIndex,
			Integer hierarchyCount) {
		return null;
	}

	/**
	 * <pre>
	 * 与えられたリストの{の手前から調べてメソッド戻り値を探索する.
	 * 階層構造毎に処理を分岐させる.
	 * </pre>
	 * 
	 * @param targetList
	 * @param startIndex
	 * @param hierarchyCount
	 * @return
	 */
	protected String generateReturnVal(List<String> targetList, Integer startIndex, Integer hierarchyCount) {
		return null;
	}

	/**
	 * 本処理の初期処理
	 * 
	 * @see 抽象度：C
	 */
	protected void initEnvAndPrintFirstLog() {
		this.scanner = new Scanner(System.in);
		System.out.println("urlを入力してください." + "\n" + "->");
	}

}
