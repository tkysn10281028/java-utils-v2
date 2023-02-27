package com.sono.myproj.process.dto;

import java.util.List;

import lombok.Data;

@Data
public class PrgElementInfoDto {

	/**
	 * 名前
	 */
	private String elementName;

	/**
	 * 引数
	 */
	private List<String> signature;

	/**
	 * 戻り値
	 */
	private String returnVal;

	/**
	 * 本体
	 */
	private String prgBody;

	/**
	 * 階層数
	 */
	private Integer hierarchyCount;

	/**
	 * 子要素
	 */
	private List<PrgElementInfoDto> prgElementList;
}
