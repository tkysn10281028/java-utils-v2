package com.sono.myproj.process;

import org.springframework.stereotype.Component;

@Component
public interface MainProcess {
	public void execute(String[] parameters);

	public void executeParameters(String[] parameters);
}
