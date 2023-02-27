package com.sono.myproj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import com.sono.myproj.execute.ExecuteMainProcess;

@Configuration
@ComponentScan
public class Main {
	Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {

		try (GenericApplicationContext context = new AnnotationConfigApplicationContext(Main.class)) {
			var executeMainProc = context.getBean(ExecuteMainProcess.class);
			executeMainProc.executeMainProcess(args);
		}
	}
}
