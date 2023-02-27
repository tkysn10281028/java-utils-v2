package com.sono.myproj.process.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ValidationUtils {
	/**
	 * 正しいURLの形式であるかをチェックする.
	 * 
	 * @param text
	 * @return
	 * @see D
	 */
	public boolean validateUrl(String text) {
		if (StringUtils.isEmpty(text)) {
			return false;
		}
		Pattern p = Pattern.compile("^\\/.*\\/.*\\..+$");
		Matcher m = p.matcher(text);
		return m.find();
	}

	/**
	 * <pre>
	 * 渡されるインデックスの有効性チェック.
	 * インデックスがリストの長さより大きい場合は無効とみなす.
	 * </pre>
	 * 
	 * @param targetList
	 * @param index
	 * @return
	 */
	public boolean validateLoopIndex(List<String> targetList, Integer index) {
		if (index < targetList.size()) {
			return true;
		}
		return false;
	}

	/**
	 * <pre>
	 * 渡されるカーソルの有効性チェック.
	 * カーソルがリストの長さより大きい場合は無効とみなす.
	 * </pre>
	 * 
	 * @param targetList
	 * @param index
	 * @return
	 */
	public boolean validateCursor(List<String> targetList, Integer cursor) {
		if (cursor < targetList.size()) {
			return true;
		}
		return false;
	}
}
