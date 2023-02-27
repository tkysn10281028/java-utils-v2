package com.sono.myproj.process.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ReplaceWordUtils {
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
	public String replaceIntoBlank(String text) {
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
	public String replaceQuotedWord(String str) {
		return str.replaceAll("\".*\"", "\"\"").replaceAll("\'.*\'", "\'\'");
	}
}
