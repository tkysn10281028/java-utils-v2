package com.sono.myproj.process.utils;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.stereotype.Component;

import com.sono.myproj.process.dto.CharReader;

/**
 * Javaソースからコメントを削除した文字列を取得する。
 */
@Component
public class CommentRemoveUtils {

	/** Javaソースのエンコーディング */
	public static final Charset CHARSET = Charset.forName("UTF-8");

	/**
	 * 指定Javaソース・ファイルを読み込み、コメントを除去した後のテキストを返す
	 * ブロックコメントの場合、改行が含まれない場合は一つの半角スペースに、含まれる場合には一つの改行に置換する。
	 * 
	 * @param path 対象とするファイル
	 * @return コメント除去した後のファイル文字列。改行コードは"\n"
	 * @throws IOException
	 */
	public static String remove(String text) throws IOException {
		StringBuffer stripped = new StringBuffer();
		CharReader r = new CharReader(text);

		while (!r.endOfFile()) {
			char c = r.read();
			switch (c) {
			case '"': // 文字列定数開始
			case '\'': // 文字定数開始
				stripped.append(skipStringOrChar(c, r));
				break;
			case '/': // コメント開始の可能性
				switch (r.read()) {
				case '*': // ブロックコメント開始
					stripped.append(skipBlockComment(r));
					break;
				case '/': // 行コメント開始
					stripped.append(skipLineComment(r));
					break;
				default: // コメントではない
					stripped.append(c);
					break;
				}
				break;
			default: // 注目しない文字
				stripped.append(c);
				continue;
			}
		}
		return stripped.toString();
	}

	/**
	 * Javaのブロックコメントをスキップする。 コメント途中に改行の無い場合には空白一文字に置き換える。 改行のある場合には改行一文字に置き換える。
	 * 
	 * @param r
	 * @return
	 */
	static String skipBlockComment(CharReader r) throws IOException {
		boolean hasNewLine = false;
		while (true) {
			char c = r.read();
			if (c == '*') {
				c = r.read();
				if (c == '/')
					break;
				r.unread();
				continue;
			}
			if (c == '\n') {
				hasNewLine = true;
				continue;
			}
		}
		return hasNewLine ? "\n" : " ";
	}

	/**
	 * Javaのラインコメントをスキップする
	 * 
	 * @param r
	 * @return
	 */
	static String skipLineComment(CharReader r) throws IOException {
		while (!r.endOfFile()) {
			if (r.read() == '\n')
				return "\n";
		}
		return "";
	}

	/**
	 * 文字列あるいは文字定数をスキップする
	 * 
	 * @param start 開始文字、'"'あるいは'\''
	 * @param r     リーダ
	 * @return 文字列あるいは文字定数
	 * @throws IOException
	 */
	static String skipStringOrChar(char start, CharReader r) throws IOException {
		StringBuilder output = new StringBuilder();
		output.append(start);
		while (true) {
			if (r.endOfFile())
				break;
			char c = r.read();
			if (c == start) {
				output.append(c);
				break;
			}
			output.append(c);
			if (c == '\\')
				output.append(r.read());
		}
		return output.toString();
	}
}