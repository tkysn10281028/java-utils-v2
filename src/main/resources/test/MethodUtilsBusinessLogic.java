package com.sono.myproj.process.logic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sono.myproj.process.dto.CharReader;
import com.sono.myproj.process.utils.CommentRemoveUtils;

@Component
public class MethodUtilsBusinessLogic implements BusinessLogic {
	Logger logger = LoggerFactory.getLogger(MethodUtilsBusinessLogic.class);
	protected Scanner scanner = null;
	private String prcTargetStr = null;

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
			logger.error("\n" + getStackTrace(e));
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
		System.out.println(result);
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
		var url = replaceIntoBlank(inputWord);
		if (!validateUrl(url)) {
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
		var replacedStr = replaceQuotedWord(CommentRemoveUtils.remove(inputWord));

		return replacedStr;
	}

	protected boolean scanMiddleParenthesis(String text) {
		CharReader r = new CharReader(text);
		return false;
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

	/**
	 * <pre>
	 * 標準入力で渡された文字列から不要な要素を削除する.
	 * null安全性のためStrinUtilsを使用.
	 * ・スペース
	 * ・全角スペース
	 * ・改行
	 * ・タブ文字
	 * </pre>
	 * 
	 * @param text
	 * @return 変換後
	 * @see C
	 */
	protected String replaceIntoBlank(String text) {
		var spaceReplaced = StringUtils.replace(text, " ", "");
		var fullSizeSpaceReplaced = StringUtils.replace(spaceReplaced, "　", "");
		var lineReplaced = StringUtils.replace(fullSizeSpaceReplaced, "\n", "");
		var tabReplaced = StringUtils.replace(lineReplaced, "\t", "");
		return tabReplaced;
	}

	/**
	 * 引用符有の場合中身を全て空白にする.
	 * 
	 * @param str
	 * @return
	 * @see D
	 */
	protected String replaceQuotedWord(String str) {
		return str.replaceAll("\".*\"", "\"\"").replaceAll("\'.*\'", "\'\'");
	}

	/**
	 * 正しいURLの形式であるかをチェックする.
	 * 
	 * @param text
	 * @return
	 * @see D
	 */
	protected boolean validateUrl(String text) {
		if (StringUtils.isEmpty(text)) {
			return false;
		}
		Pattern p = Pattern.compile("^\\/.*\\/.*\\..+$");
		Matcher m = p.matcher(text);
		return m.find();
	}

	/**
	 * <pre>
	 * エラーを受け取って文字列を生成する
	 * </pre>
	 * 
	 * @param e
	 * @return
	 * @see D
	 */
	private static String getStackTrace(Exception e) {
		StackTraceElement[] list = e.getStackTrace();
		StringBuilder b = new StringBuilder();
		b.append(e.getClass()).append(":").append(e.getMessage()).append("\n");
		for (StackTraceElement s : list) {
			b.append(s.toString()).append("\n");
		}
		return b.toString();
	}
}
