package com.sono.myproj.process.utils;

import org.springframework.stereotype.Component;

@Component
public class ErrorUtils {

	/**
	 * <pre>
	 * エラーを受け取って文字列を生成する
	 * </pre>
	 * 
	 * @param e
	 * @return
	 * @see D
	 */
	public static String getStackTrace(Exception e) {
		StackTraceElement[] list = e.getStackTrace();
		StringBuilder b = new StringBuilder();
		b.append(e.getClass()).append(":").append(e.getMessage()).append("\n");
		for (StackTraceElement s : list) {
			b.append(s.toString()).append("\n");
		}
		return b.toString();
	}
}
