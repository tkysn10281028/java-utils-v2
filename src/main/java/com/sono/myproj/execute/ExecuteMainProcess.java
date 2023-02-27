package com.sono.myproj.execute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sono.myproj.process.MainProcess;
import com.sono.myproj.process.impl.MethodUtilsProcessImpl;

@Component
public class ExecuteMainProcess {
	@Autowired
	@Qualifier("methodUtilsProcessImpl")
	MainProcess mainProcess;
	Logger logger = LoggerFactory.getLogger(MethodUtilsProcessImpl.class);

	public void executeMainProcess(String[] paramters) {
		mainProcess.execute(paramters);
	}
}
