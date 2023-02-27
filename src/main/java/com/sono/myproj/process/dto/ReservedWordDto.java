package com.sono.myproj.process.dto;

import java.util.List;

import lombok.Data;

@Data
public class ReservedWordDto {
	private List<String> reservedWordList;

	public ReservedWordDto() {
		this.reservedWordList = List.of("abstract", "continue", "for", "new", "switch", "assert", "default", "if",
				"package", "synchronized", "boolean", "do", "goto", "private", "this", "break", "double", "implements",
				"protected", "throw", "byte", "else", "import", "public", "throws", "case", "enum", "instanceof",
				"return", "transient", "catch", "extends", "int", "short", "try", "char", "final", "interface",
				"static", "void", "class", "finally", "long", "strictfp", "volatile", "const", "float", "native",
				"super", "while", "_", "->");
	}
}
