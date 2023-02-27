package com.sono.myproj.process.logic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sono.myproj.process.dto.PrgElementInfoDto;
import com.sono.myproj.process.utils.CommentRemoveUtils;
import com.sono.myproj.process.utils.ErrorUtils;
import com.sono.myproj.process.utils.ParenthesisUtils;
import com.sono.myproj.process.utils.ReplaceWordUtils;
import com.sono.myproj.process.utils.ValidationUtils;

@Component
public class MethodUtilsBusinessLogic implements BusinessLogic {
	@Autowired
	ParenthesisUtils parenthesisUtils;
	@Autowired
	ReplaceWordUtils replaceWordUtils;

	@Autowired
	ValidationUtils validationUtils;
	Logger logger = LoggerFactory.getLogger(MethodUtilsBusinessLogic.class);
	protected Scanner scanner = null;
	private String prcTargetStr = null;
	private Integer cursor = 0;

	

	public void executeMethodUtils() {
		try {

			this.beforeProcess();
			this.mainProcess();
			this.afterProcess();
		} catch (Exception e) {
			logger.error("" + ErrorUtils.getStackTrace(e));
			System.out.println("");
		}

	}

	

	@Override
	public void beforeProcess() throws Exception {
		initEnvAndPrintFirstLog();
		logger.info("");
		this.prcTargetStr = getFileStringFromInputUrl(scanner.nextLine()).stream().collect(Collectors.joining(""));
	}

	

	@Override
	public void mainProcess() throws Exception {
		logger.info("");
		var result = beforeMainProcess(this.prcTargetStr);
		var prgElementList = generatePrgElementList(result);
		var parenthesisDtoList = generatePrgElementDtoList(prgElementList);
		System.out.println(prgElementList);
	}

	

	@Override
	public void afterProcess() {
		logger.info("");
	}

	

	protected List<String> getFileStringFromInputUrl(String inputWord) throws Exception {
		var url = replaceWordUtils.replaceIntoBlank(inputWord);
		if (!validationUtils.validateUrl(url)) {
			throw new IllegalArgumentException("");
		}
		var outputStrList = Files.readAllLines(Paths.get(url));
		return outputStrList;
	}

	

	protected String beforeMainProcess(String inputWord) throws Exception {
		var replacedStr = replaceWordUtils.replaceQuotedWord(CommentRemoveUtils.remove(inputWord));
		if (parenthesisUtils.scanRoundParenthesis(replacedStr) && parenthesisUtils.scanMiddleParenthesis(replacedStr)) {
			return replacedStr;
		} else {
			throw new IllegalArgumentException("");
		}
	}

	

	protected List<String> generatePrgElementList(String text) {
		var spaced = parenthesisUtils.replaceParenthesisIntoSpaced(text);
		return Arrays.asList(spaced.split("")).collect(Collectors.toList());
	}

	

	protected List<PrgElementInfoDto> generatePrgElementDtoList(List<String> prgElementList) {
		return new ArrayList<>();
	}

	

	protected List<PrgElementInfoDto> generatePrgElementDtoListRecursion(List<String> targetList, Integer startIndex) {
		var prgElementInfo = new PrgElementInfoDto();
		var prgElementInfoList = new ArrayList<PrgElementInfoDto>();
		List<PrgElementInfoDto> prgElementInfoListForOutput = new ArrayList<PrgElementInfoDto>();
		for (int i = startIndex; i < targetList.size(); i++) {
			var target = targetList.get(i);
			
			if (parenthesisUtils.judgeIsRightBracket(target) == null) {
				continue;
			}
			
			else if (!parenthesisUtils.judgeIsRightBracket(target)) {
				var result = generatePrgElementDtoListRecursion(targetList, i);
				addIntoPrgElementInfoList(result, prgElementInfoList);
				continue;
			}
			
			else if (parenthesisUtils.judgeIsRightBracket(target)) {
				prgElementInfoListForOutput = setPrgElementInfo(prgElementInfo, prgElementInfoList);
				break;
			}
		}
		return prgElementInfoListForOutput;
	}

	

	protected List<PrgElementInfoDto> setPrgElementInfo(PrgElementInfoDto prgElementInfoDto,
			List<PrgElementInfoDto> prgElementInfoDtos) {

		var prgElementInfoListForOutput = new ArrayList<PrgElementInfoDto>();
		prgElementInfoDto.setElementName("");
		prgElementInfoDto.setSignature(new ArrayList<>());
		prgElementInfoDto.setReturnVal("");
		prgElementInfoDto.setPrgBody("");
		prgElementInfoDto.setPrgElementList(prgElementInfoDtos);
		prgElementInfoListForOutput.add(prgElementInfoDto);
		return prgElementInfoListForOutput;
	}

	

	protected void addIntoPrgElementInfoList(List<PrgElementInfoDto> addList, List<PrgElementInfoDto> addTarget) {
		addList.forEach((target) -> {
			addTarget.add(target);
		});
	}

	

	protected void initEnvAndPrintFirstLog() {
		this.scanner = new Scanner(System.in);
		System.out.println("");
	}

}

public class DummyClass{
	public void A(){
		public void B(){
			public void D(){
				public void G(){
					
				}
				public void H(){
					
				}
				public void I(){
					
				}
				public void J(){
					
				}
			}
			public void E(){
				
			}
			public void F(){
				
			}
		}
		public void C(){
			
		}
	}
}