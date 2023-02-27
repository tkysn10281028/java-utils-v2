package com.sono.myproj.process.dto;

import lombok.Data;

/**
 * 最初の左カッコと閉じの右カッコのDTOクラス.
 * 
 * @author entakuya
 */
@Data
public class LeftAndRightParenthesisDto {
	/**
	 * 左カッコのインデックス位置
	 */
	private Integer leftParenthesisPosition;

	/**
	 * 右カッコのインデックス位置
	 */
	private Integer rightParenthesisPosition;

	public LeftAndRightParenthesisDto(Integer leftParenthesisPosition, Integer rightParenthesisPosition) {
		this.leftParenthesisPosition = leftParenthesisPosition;
		this.rightParenthesisPosition = rightParenthesisPosition;
	}
}
