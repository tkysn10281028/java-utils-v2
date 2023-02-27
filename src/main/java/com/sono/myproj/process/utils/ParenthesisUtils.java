package com.sono.myproj.process.utils;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sono.myproj.process.dto.CharReader;
import com.sono.myproj.process.dto.LeftAndRightParenthesisDto;

@Component
public class ParenthesisUtils {
	/**
	 * 全てのカッコの左右にスペースを付加する
	 * 
	 * @param parenthesisedWords
	 * @return
	 */
	public String replaceParenthesisIntoSpaced(String parenthesisedWords) {
		var replaced1 = StringUtils.replace(parenthesisedWords, "(", " ( ");
		var replaced2 = StringUtils.replace(replaced1, ")", " ) ");
		var replaced3 = StringUtils.replace(replaced2, "{", " { ");
		var replaced4 = StringUtils.replace(replaced3, "}", " } ");
		return replaced4;
	}

	/**
	 * 「{」「}」を検知する
	 * 
	 * @param inputWord
	 * @return {の時false }の時true それ以外はnull
	 * @see 抽象度：D
	 */
	public Boolean judgeIsRightBracket(String inputWord) {
		if (inputWord.equals("{")) {
			return false;
		} else if (inputWord.equals("}")) {
			return true;
		} else {
			return null;
		}
	}

	/**
	 * カッコの個数が全体で左右一致しているかをチェック
	 * 
	 * @param text
	 * @return
	 * @throws IOException
	 * @see B
	 */
	public boolean scanMiddleParenthesis(String text) throws IOException {
		int leftParenthesisCount = 0;
		int rightParenthesisCount = 0;
		CharReader r = new CharReader(text);
		while (!r.endOfFile()) {
			char c = r.read();
			switch (c) {
			case '{':
				leftParenthesisCount++;
				continue;
			case '}':
				rightParenthesisCount++;
				continue;
			default:
				continue;
			}
		}
		if (leftParenthesisCount == rightParenthesisCount) {
			return true;
		}
		return false;
	}

	/**
	 * カッコの個数が全体で左右一致しているかをチェック
	 * 
	 * @param text
	 * @return
	 * @throws IOException
	 * @see B
	 */
	public boolean scanRoundParenthesis(String text) throws IOException {
		int leftParenthesisCount = 0;
		int rightParenthesisCount = 0;
		CharReader r = new CharReader(text);
		while (!r.endOfFile()) {
			char c = r.read();
			switch (c) {
			case '(':
				leftParenthesisCount++;
				continue;
			case ')':
				rightParenthesisCount++;
				continue;
			default:
				continue;
			}
		}
		if (leftParenthesisCount == rightParenthesisCount) {
			return true;
		}
		return false;
	}

	/**
	 * <pre>
	 * 始まりと終わりの()を取得する.
	 * {のインデックスからスタートして上に遡り、()がいて最初に左右の数が一致した場合に始まりと終わりのインデックスを取得する.
	 * 1.()の数が合わないうちに{か}が返った場合は0,0を返却する.
	 * 2.()の数がともにゼロなのに{}が返った場合は0,0で返却する.
	 * </pre>
	 */
	public LeftAndRightParenthesisDto generateFirstAndLastParenthesisCount(List<String> targetList,
			Integer startIndex) {
		var leftAndRightDto = new LeftAndRightParenthesisDto(0, 0);
		var leftParenthesisCount = 0;
		var rightParenthesisCount = 0;
		for (int i = startIndex - 1; i >= 0; i--) {
			var target = targetList.get(i);
			if (target.equals("{") || target.equals("}")) {
				if (leftParenthesisCount == rightParenthesisCount
						&& (leftParenthesisCount != 0 && rightParenthesisCount != 0)) {
					break;
				} else {
					leftAndRightDto = new LeftAndRightParenthesisDto(0, 0);
					break;
				}

			}
			if (target.equals("(")) {
				leftParenthesisCount++;
				leftAndRightDto.setLeftParenthesisPosition(i);
			} else if (target.equals(")")) {
				rightParenthesisCount++;
				leftAndRightDto.setRightParenthesisPosition(i);
			} else {
				continue;
			}

		}
		return leftAndRightDto;
	}

}
