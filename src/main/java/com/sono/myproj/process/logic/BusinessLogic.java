package com.sono.myproj.process.logic;

import org.springframework.stereotype.Component;

@Component
public interface BusinessLogic {
	public void beforeProcess() throws Exception;

	public void mainProcess() throws Exception;

	public void afterProcess() throws Exception;
}
