package com.sono.myproj.process.impl;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sono.myproj.process.MainProcess;
import com.sono.myproj.process.logic.MethodUtilsBusinessLogic;

@Component
public class MethodUtilsProcessImpl implements MainProcess {
	Logger logger = LoggerFactory.getLogger(MethodUtilsProcessImpl.class);
	@Autowired
	MethodUtilsBusinessLogic methodUtilsBusinessLogic;

	@Override
	public void execute(String[] parameters) {
		this.executeParameters(parameters);
		methodUtilsBusinessLogic.executeMethodUtils();
	}

	@Override
	public void executeParameters(String[] parameters) {
		Arrays.asList(parameters).forEach(str -> {
			logger.info("Do Not Use Argument : {}", str);
		});
	}
}
