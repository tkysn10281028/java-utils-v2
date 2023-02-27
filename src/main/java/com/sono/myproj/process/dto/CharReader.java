package com.sono.myproj.process.dto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * 文字リーダ {@link #endOfFile()}でファイルの終わりを検出する。
 * {@link #read()}で一文字を返すが、ファイル終端では{@link IOException}が発生する。
 */
public class CharReader {
	BufferedReader r;

	public CharReader(String text) {
		r = new BufferedReader(new StringReader(text));
	}

	public boolean endOfFile() throws IOException {
		r.mark(1);
		int c = r.read();
		if (c < 0)
			return true;
		r.reset();
		return false;
	}

	public char read() throws IOException {
		r.mark(1);
		int c = r.read();
		if (c < 0)
			throw new IOException("Truncated source file");
		return (char) c;
	}

	public void unread() throws IOException {
		r.reset();
	}

	public String getRest() throws IOException {
		StringBuilder s = new StringBuilder();
		while (!endOfFile())
			s.append(read());
		return s.toString();
	}
}
