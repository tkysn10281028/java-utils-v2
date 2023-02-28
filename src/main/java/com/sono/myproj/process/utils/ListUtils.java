package com.sono.myproj.process.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sono.myproj.process.dto.ReservedWordDto;

@Component
public class ListUtils {
	private ReservedWordDto reservedWordDto = new ReservedWordDto();
	@Autowired
	public ParenthesisUtils parenthesisUtils;

	/**
	 * リストを指定された位置で切り取りする
	 * 
	 * @param list     切り取り対象のリスト
	 * @param fromNum  切り取りのスタート位置
	 * @param AfterNum 切り取りの終了位置
	 * @return 切り取ったリスト
	 * @see 抽象度：D
	 */
	public List<String> generateSplitListByFromAndAfterNum(List<String> list, Integer fromNum, Integer AfterNum) {
		return new ArrayList<String>(list.subList(fromNum, AfterNum + 1));
	}

	/**
	 * classかinterfaceのキーワードがいた場合、その1つ後のキーワードを取得する.
	 * 
	 * @param targetList
	 * @param startIndex
	 * @return
	 */
	public String findClassAfterWord(List<String> targetList, Integer startIndex) {
		String outputStr = null;
		for (int i = startIndex; i >= 0; i--) {
			var target = targetList.get(i);
			if ((target.equals("class") || target.equals("interface")) && i < startIndex) {
				outputStr = targetList.get(i + 1);
				break;
			}
		}
		return outputStr;
	}

	/**
	 * <pre>
	 * リスト内の指定インデックスの一つ手前の文字列を返す.
	 * 一つ手前がいないときは指定インデックスの文字列を返す.
	 * </pre>
	 * 
	 * @param targetList
	 * @param startIndex
	 * @return
	 */
	public String findOnePreviousWord(List<String> targetList, Integer startIndex) {
		if (startIndex <= 0) {
			return targetList.get(startIndex);
		} else {
			var x = targetList.get(startIndex - 1);
			return targetList.get(startIndex - 1);

		}
	}

	/**
	 * <pre>
	 * ()の手前の文字列(()がいないときは開始インデックス)からスタートして予約語を探索する.
	 * いなかったときはnullを返却する.
	 * </pre>
	 * 
	 * @param targetList
	 * @param startIndex
	 * @return
	 */
	public String findReservedWord(List<String> targetList, Integer startIndex) {
		var parenthesisDto = parenthesisUtils.generateFirstAndLastParenthesisCount(targetList, startIndex);
		String outputWord = null;
		if (parenthesisDto.getLeftParenthesisPosition() == 0 && parenthesisDto.getRightParenthesisPosition() == 0) {
			parenthesisDto.setLeftParenthesisPosition(startIndex);
		}
		for (int i = parenthesisDto.getLeftParenthesisPosition(); i >= 0; i--) {
			if (targetList.get(i).equals("{") || targetList.get(i).equals("{")) {
				break;
			}
			var evacuationIndex = i;
			var optWord = reservedWordDto.getReservedWordList().stream()
					.filter(word -> word.equals(targetList.get(evacuationIndex))).findAny();
			if (optWord.isPresent()) {
				outputWord = optWord.get();
				break;
			}
		}
		return outputWord;
	}
}
