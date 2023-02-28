package com.sono.myproj.process.logic;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import com.sono.myproj.process.dto.LeftAndRightParenthesisDto;
import com.sono.myproj.process.utils.CommentRemoveUtils;
import com.sono.myproj.process.utils.ListUtils;
import com.sono.myproj.process.utils.ParenthesisUtils;
import com.sono.myproj.process.utils.ReplaceWordUtils;
import com.sono.myproj.process.utils.ValidationUtils;

public class MethodUtilsBusinessLogicTest {
	MethodUtilsBusinessLogic utilsBusinessLogic = new MethodUtilsBusinessLogic();
	ParenthesisUtils parenthesisUtils = new ParenthesisUtils();
	ReplaceWordUtils replaceWordUtils = new ReplaceWordUtils();
	ValidationUtils validationUtils = new ValidationUtils();
	ListUtils listUtils = new ListUtils();
	{
		utilsBusinessLogic.parenthesisUtils = new ParenthesisUtils();
		utilsBusinessLogic.replaceWordUtils = new ReplaceWordUtils();
		utilsBusinessLogic.validationUtils = new ValidationUtils();
		utilsBusinessLogic.listUtils = new ListUtils();
		utilsBusinessLogic.listUtils.parenthesisUtils = new ParenthesisUtils();
	}

	String url1 = "/Applications/Eclipse_2022-03.app/Contents/workspace/method-utils-v2/src/main/resources/test/SampleFile.java";
	String url2 = "/Applications/Eclipse_2022-03.app/Contents/workspace/method-utils-v2/src/main/resources/test/MethodUtilsBusinessLogic.java";
	String url3 = "/Applications/Eclipse_2022-03.app/Contents/workspace/method-utils-v2/src/main/resources/test/MethodUtilsProcessImpl.java";
	String url4 = "/Applications/Eclipse_2022-03.app/Contents/workspace/method-utils-v2/src/main/resources/test/SampleForPrgElementDtoTest.java";
	String url5 = "/Applications/Eclipse_2022-03.app/Contents/workspace/method-utils-v2/src/main/resources/test/SampleForPrgElementDtoTest2.java";
	String url6 = "/Applications/Eclipse_2022-03.app/Contents/workspace/method-utils-v2/src/main/resources/test/SampleForPrgElementDtoTest3.java";
	String url7 = "/Applications/Eclipse_2022-03.app/Contents/workspace/method-utils-v2/src/main/resources/test/SampleForPrgElementDtoTest4.java";
	String url8 = "/Applications/Eclipse_2022-03.app/Contents/workspace/method-utils-v2/src/main/resources/test/SampleForPrgElementDtoTest5.java";
	String url9 = "/Applications/Eclipse_2022-03.app/Contents/workspace/method-utils-v2/src/main/resources/test/SampleForPrgElementDtoTest6.java";
	String url10 = "/Applications/Eclipse_2022-03.app/Contents/workspace/method-utils-v2/src/main/resources/test/SampleForPrgElementDtoTest7.java";
	String url11 = "/Applications/Eclipse_2022-03.app/Contents/workspace/method-utils-v2/src/main/resources/test/SampleForPrgElementDtoTest8.java";
	String url12 = "/Applications/Eclipse_2022-03.app/Contents/workspace/method-utils-v2/src/main/resources/test/SampleForPrgElementDtoTest9.java";

	@Test
	public void testValidateUrl() {

		var text1 = "/method-utils-v2/src/test/java/com/sono/myproj/process/logic/MethodUtilsBusinessLogicTest.java";
		var text2 = "method-utils-v2/src/test/java/com/sono/myproj/process/logic/MethodUtilsBusinessLogicTest.java";
		var text3 = "/method-utils-v2/src/test/java/com/sono/myproj/process/logic/MethodUtilsBusinessLogicTest.";
		var text4 = "MethodUtilsBusinessLogicTest.java";
		var text5 = "";
		String text6 = null;
		var text7 = "/method-utils-v2/src/test/java";
		var text8 = "/method-utils-v2 / src / test /    java    / com/   sono  /   myproj  /   process /  logic / MethodUtils BusinessLogicTest.java";
		var text9 = "/　method　-u　ti　l　s-　v　2/　s　rc　/　te　s　　　　t　/　j　a　v　a　/c　o　m　　/　s　on　o　/　m　y　p　r　o　j/　p　r　o　c　es　s　/　l　o　g　ic　/MethodUtilsBusinessLogicTest.java";
		var text10 = "/method-utils-v"
				+ "\n2/src/test/java/com/sono/myproj/\nprocess/logic/MethodUtilsBusinessLogicTest.java";
		var text11 = "/method-utils-v2/src/test/java/\tcom/son\to/mypr\toj/pro\tcess/logic/Me\tehodUtilsBusinessLogicTest.java";

		var result1 = validationUtils.validateUrl(replaceWordUtils.replaceIntoBlank(text1));
		var result2 = validationUtils.validateUrl(replaceWordUtils.replaceIntoBlank(text2));
		var result3 = validationUtils.validateUrl(replaceWordUtils.replaceIntoBlank(text3));
		var result4 = validationUtils.validateUrl(replaceWordUtils.replaceIntoBlank(text4));
		var result5 = validationUtils.validateUrl(replaceWordUtils.replaceIntoBlank(text5));
		var result6 = validationUtils.validateUrl(replaceWordUtils.replaceIntoBlank(text6));
		var result7 = validationUtils.validateUrl(replaceWordUtils.replaceIntoBlank(text7));
		var result8 = validationUtils.validateUrl(replaceWordUtils.replaceIntoBlank(text8));
		var result9 = validationUtils.validateUrl(replaceWordUtils.replaceIntoBlank(text9));
		var result10 = validationUtils.validateUrl(replaceWordUtils.replaceIntoBlank(text10));
		var result11 = validationUtils.validateUrl(replaceWordUtils.replaceIntoBlank(text11));

		assertTrue(result1);
		assertTrue(!result2);
		assertTrue(!result3);
		assertTrue(!result4);
		assertTrue(!result5);
		assertTrue(!result6);
		assertTrue(!result7);
		assertTrue(result8);
		assertTrue(result9);
		assertTrue(result10);
		assertTrue(result11);
	}

	@Test
	public void testStringRemoveInBeforeMainProcess() throws IOException {
		var expectedResult1 = "packagecom.sono.process.logic;importjava.io.IOException;importjava.util.ArrayList;importjava.util.Arrays;importjava.util.List;importjava.util.Scanner;importjava.util.regex.Pattern;importjava.util.stream.Collectors;importorg.apache.commons.lang3.ObjectUtils;importorg.apache.commons.lang3.StringUtils;importorg.slf4j.Logger;importorg.slf4j.LoggerFactory;importorg.springframework.beans.factory.annotation.Autowired;importorg.springframework.stereotype.Component;importcom.sono.process.dto.methodutils.ClassInfoDto;importcom.sono.process.dto.methodutils.LeftAndRightParenthesisDto;importcom.sono.process.dto.methodutils.MethodInfoDto;importcom.sono.process.dto.methodutils.ReservedWordDto;importcom.sono.process.utils.CommentRemoveUtils;@ComponentpublicclassMethodUtilsBusinessLogicimplementsBusinessLogic{@AutowiredCommentRemoveUtilscommentRemoveUtils;ReservedWordDtoreservedWordDto=newReservedWordDto();Loggerlogger=LoggerFactory.getLogger(MethodUtilsBusinessLogic.class);protectedScannerscanner=null;protectedStringinputWord;protectedIntegerpresentCursor=-1;protectedClassInfoDtoclassInfoDto;publicvoidexecuteMethodUtils(){this.beforeProcess();this.mainProcess();this.afterProcess();}@OverridepublicvoidbeforeProcess(){logger.info(\"\");initEnvAndPrintFirstLog();this.inputWord=scanner.next();}@OverridepublicvoidmainProcess(){logger.info(\"\");try{readInputWordAndPrint(this.inputWord);}catch(IOExceptione){e.printStackTrace();}}@OverridepublicvoidafterProcess(){logger.info(\"\");}protectedvoidreadInputWordAndPrint(StringinputWord)throwsIOException{vartmp=CommentRemoveUtils.remove(inputWord);varinputWordList=splitInputWordBySpaceAndFilterBlank(replaceParenthesisIntoSpaced(replaceWordIntoSpace(replaceQuotedWord(tmp))));varinputWordListSize=inputWordList.size();if(scanRoundParenthesis(inputWordList,inputWordListSize)&&scanMiddleParenthesis(inputWordList,inputWordListSize)){generateClassInfo(inputWordList);this.presentCursor=0;varmethodList=generateMethodInfoList(this.classInfoDto.getClassPart());outputAllInfo(classInfoDto,methodList);}else{thrownewIllegalArgumentException(\"\");}}protectedClassInfoDtogenerateClassInfo(List<String>inputWordList){this.classInfoDto=newClassInfoDto();for(inti=0;i<inputWordList.size();i++){varinputWord=inputWordList.get(i);if((inputWord.equals(\"\"))&&i<inputWordList.size()-1){this.classInfoDto.setClassName(inputWordList.get(i+1));varleftBracketAfterList=generateSplitListAfterPart(inputWordList,i);varclassPartList=generateSplitListByFromAndAfterNum(leftBracketAfterList,getFirstCursorCountLeftBracket(leftBracketAfterList),getLastCursorCountRightBracket(leftBracketAfterList));this.classInfoDto.setClassPart(classPartList);}}if(StringUtils.isEmpty(this.classInfoDto.getClassName())){thrownewIllegalArgumentException(\"\");}returnthis.classInfoDto;}protectedList<MethodInfoDto>generateMethodInfoList(List<String>classInfoPart){varinfo=generateMethodImplementsLoop(classInfoPart,0);returninfo;}protectedvoidoutputAllInfo(ClassInfoDtoclassInfoDto,List<MethodInfoDto>methodInfoDtos){outputAllInfoSystemOut(classInfoDto,methodInfoDtos);}protectedvoidoutputAllInfoSystemOut(ClassInfoDtoclassInfoDto,List<MethodInfoDto>methodInfoDtos){System.out.println(\"\"+classInfoDto.getClassName());System.out.println(\"\");outputAllMethodInfoDtos(methodInfoDtos,0);}protectedvoidoutputAllMethodInfoDtos(List<MethodInfoDto>methodInfoDtos,IntegerhierarchyCount){if(methodInfoDtos.size()<=0){return;}hierarchyCount++;for(inti=0;i<methodInfoDtos.size();i++){outputAllMethodInfo(methodInfoDtos.get(i),hierarchyCount);outputAllMethodInfoDtos(methodInfoDtos.get(i).getMethodInfoList(),hierarchyCount);}}protectedvoidoutputAllMethodInfo(MethodInfoDtomethodInfoDto,IntegerhierarchyCount){if(!judgeProperMethodForOutput(methodInfoDto)){return;}varsb=newStringBuilder();for(inti=0;i<hierarchyCount;i++){sb.append(\"\");}System.out.println(sb.toString()+\"\"+methodInfoDto.getMethodName());varreturnVal=StringUtils.isEmpty(methodInfoDto.getMethodReturnVal())?\"\":methodInfoDto.getMethodReturnVal();varsignature=methodInfoDto.getMethodSignature()==null?\"\":String.join(\"\",methodInfoDto.getMethodSignature());System.out.println(sb.toString()+\"\"+returnVal);System.out.println(sb.toString()+\"\"+signature);System.out.println(\"\");}protectedbooleanjudgeProperMethodForOutput(MethodInfoDtomethodInfoDto){if(StringUtils.isEmpty(methodInfoDto.getMethodName())){returnfalse;}if(StringUtils.isNotEmpty(methodInfoDto.getMethodReturnVal())&&(methodInfoDto.getMethodReturnVal().equals(\"\")||methodInfoDto.getMethodReturnVal().equals(\"\"))){returnfalse;}returntrue;}protectedList<MethodInfoDto>generateMethodImplementsLoop(List<String>targetlist,IntegerstartIndex){varmethodInfoDto=newMethodInfoDto();List<MethodInfoDto>methodInfoDtos=newArrayList<MethodInfoDto>();List<MethodInfoDto>methodInfoListOutput=newArrayList<MethodInfoDto>();for(inti=startIndex;i<targetlist.size()||this.presentCursor<targetlist.size();i++){setCursorToPresent();if(this.presentCursor==targetlist.size()){methodInfoListOutput=methodInfoDtos;continue;}vartarget=targetlist.get(i);if(judgeIsRightBracket(target)==null){continue;}elseif(judgeIsRightBracket(target)){varparenthesisDto=generateFirstAndLastParenthesisCount(targetlist,startIndex);varmethodPart=generateSplitListByFromAndAfterNum(targetlist,startIndex,i);varmethodName=generateMethodName(targetlist,parenthesisDto);varmethodSignature=generateMethodSignature(targetlist,parenthesisDto);varmethodReturnVal=generateMethodReturnVal(targetlist,parenthesisDto);methodInfoListOutput=setMethodRelatedInfoAndGenerateNewMethodList(methodInfoDto,methodInfoDtos,methodPart,methodName,methodSignature,methodReturnVal);break;}elseif(!judgeIsRightBracket(target)){varresultList=generateMethodImplementsLoop(targetlist,i+1);resultList.forEach((result)->{methodInfoDtos.add(result);});i=this.presentCursor;continue;}else{continue;}}returnmethodInfoListOutput;}protectedIntegergetFirstCursorCountLeftBracket(List<String>inputWordList){varfirstCursorCount=0;for(inti=0;i<inputWordList.size();i++){if(inputWordList.get(i).equals(\"\")){firstCursorCount=i+1;break;}}returnfirstCursorCount;}protectedIntegergetLastCursorCountRightBracket(List<String>inputWordList){varlastCursorCount=0;for(inti=inputWordList.size()-1;i>=0;i--){if(inputWordList.get(i).equals(\"\")){lastCursorCount=i;break;}}returnlastCursorCount;}protectedBooleanjudgeIsRightBracket(StringinputWord){if(inputWord.equals(\"\")){returnfalse;}elseif(inputWord.equals(\"\")){returntrue;}else{returnnull;}}protectedbooleanscanRoundParenthesis(List<String>targetList,IntegerendIndex){intleftParenthesisCount=0;intrightParenthesisCount=0;for(inti=0;i<endIndex;i++){Stringtarget=targetList.get(i);if(target.equals(\"\")){leftParenthesisCount++;}elseif(target.equals(\"\")){rightParenthesisCount++;}else{continue;}}if(leftParenthesisCount==rightParenthesisCount){returntrue;}returnfalse;}protectedbooleanscanMiddleParenthesis(List<String>targetList,IntegerendIndex){intleftParenthesisCount=0;intrightParenthesisCount=0;for(inti=0;i<endIndex;i++){Stringtarget=targetList.get(i);if(target.equals(\"\")){leftParenthesisCount++;}elseif(target.equals(\"\")){rightParenthesisCount++;}else{continue;}}if(leftParenthesisCount==rightParenthesisCount){returntrue;}returnfalse;}protectedvoidsetCursorToPresent(){this.presentCursor++;}protectedList<MethodInfoDto>setMethodRelatedInfoAndGenerateNewMethodList(MethodInfoDtomethodInfoDto,List<MethodInfoDto>methodInfoDtos,List<String>methodPart,StringmethodName,List<String>methodSignature,StringmethodReturnVal){methodInfoDto.setMethodInfoList(methodInfoDtos);methodInfoDto.setMethodPart(methodPart);methodInfoDto.setMethodName(methodName);methodInfoDto.setMethodSignature(methodSignature);methodInfoDto.setMethodReturnVal(methodReturnVal);varmethodInfoDtosOutput=newArrayList<MethodInfoDto>();methodInfoDtosOutput.add(methodInfoDto);returnmethodInfoDtosOutput;}protectedStringgenerateMethodName(List<String>targetlist,LeftAndRightParenthesisDtoleftAndRightParenthesisDto){if(judgeIsLeftPositionZeroOrTargetNull(leftAndRightParenthesisDto)){returnnull;}varmethodName=getOnePreviousWordFromList(targetlist,leftAndRightParenthesisDto.getLeftParenthesisPosition());returnmethodName;}protectedList<String>generateMethodSignature(List<String>targetlist,LeftAndRightParenthesisDtoleftAndRightParenthesisDto){if(judgeIsLeftPositionZeroOrTargetNull(leftAndRightParenthesisDto)){returnnull;}if(judgeIsReservedWordOrClassName(getOnePreviousWordFromList(targetlist,leftAndRightParenthesisDto.getLeftParenthesisPosition()))){returnnull;}varlist=generateSplitListByFromAndAfterNum(targetlist,leftAndRightParenthesisDto.getLeftParenthesisPosition(),leftAndRightParenthesisDto.getRightParenthesisPosition());returnArrays.asList(String.join(\"\"));}protectedStringgenerateMethodReturnVal(List<String>targetlist,LeftAndRightParenthesisDtoleftAndRightParenthesisDto){if(judgeIsLeftPositionZeroOrTargetNull(leftAndRightParenthesisDto)){returnnull;}if(judgeIsReservedWordOrClassName(getOnePreviousWordFromList(targetlist,leftAndRightParenthesisDto.getLeftParenthesisPosition()))){returnnull;}returngetTwoPreviousWordFromList(targetlist,leftAndRightParenthesisDto.getLeftParenthesisPosition());}protectedLeftAndRightParenthesisDtogenerateFirstAndLastParenthesisCount(List<String>targetList,IntegerstartIndex){varparenthesisCountDto=newLeftAndRightParenthesisDto(0,0,0,0);if(!scanRoundParenthesis(targetList,startIndex)){returnnull;}for(inti=startIndex;i>0;i--){if(judgeIsBreakForParenthesisCountMethod(parenthesisCountDto)){break;}vartarget=targetList.get(i);setFirstRightParenthesisPosition(target,parenthesisCountDto,i);setParenthesisCountAndRightPosition(target,parenthesisCountDto,i);}returnparenthesisCountDto;}protectedStringreplaceParenthesisIntoSpaced(StringparenthesisedWords){varreplaced1=StringUtils.replace(parenthesisedWords,\"\");varreplaced2=StringUtils.replace(replaced1,\"\");varreplaced3=StringUtils.replace(replaced2,\"\");varreplaced4=StringUtils.replace(replaced3,\"\");returnreplaced4;}protectedbooleanjudgeIsBreakForParenthesisCountMethod(LeftAndRightParenthesisDtoparenthesisDto){varleftParenthesisCount=parenthesisDto.getLeftParenthesisCount();varrightParenthesisCount=parenthesisDto.getRightParenthesisCount();if(leftParenthesisCount!=0&&rightParenthesisCount!=0&&leftParenthesisCount==rightParenthesisCount){returntrue;}returnfalse;}protectedvoidsetFirstRightParenthesisPosition(Stringtarget,LeftAndRightParenthesisDtoparenthesisDto,IntegerpresentPosition){varrightParenthesisCount=parenthesisDto.getRightParenthesisCount();if(target.equals(\"\")&&rightParenthesisCount==0){parenthesisDto.setRightParenthesisPosition(presentPosition);}}protectedvoidsetParenthesisCountAndRightPosition(Stringtarget,LeftAndRightParenthesisDtoparenthesisDto,IntegerpresentPosition){if(target.equals(\"\")){parenthesisDto.setLeftParenthesisCount(parenthesisDto.getLeftParenthesisCount()+1);parenthesisDto.setLeftParenthesisPosition(presentPosition);}elseif(target.equals(\"\")){parenthesisDto.setRightParenthesisCount(parenthesisDto.getRightParenthesisCount()+1);}}protectedStringgetOnePreviousWordFromList(List<String>targetList,Integerindex){if(targetList.size()<=0){returnnull;}if(index==0){returntargetList.get(0);}else{returntargetList.get(index-1);}}protectedStringgetTwoPreviousWordFromList(List<String>targetList,Integerindex){if(targetList.size()<=0){returnnull;}if(index==0||index==1){returntargetList.get(index);}else{returntargetList.get(index-2);}}protectedbooleanjudgeIsReservedWordOrClassName(Stringtarget){varreservedWords=reservedWordDto.getReservedWordList();varclassName=classInfoDto.getClassName();if(reservedWords.size()<=0||StringUtils.isEmpty(className)){thrownewIllegalArgumentException(\"\");}if(reservedWords.stream().filter(word->word.equals(target)).findAny().isPresent()||target.equals(className)){returntrue;}else{returnfalse;}}protectedbooleanjudgeIsLeftPositionZeroOrTargetNull(LeftAndRightParenthesisDtodto){if(ObjectUtils.isEmpty(dto)||dto.getLeftParenthesisCount()==0||dto.getRightParenthesisCount()==0){returntrue;}else{returnfalse;}}protectedList<String>generateSplitListAfterPart(List<String>list,IntegersplitNum){if(splitNum>=list.size()){thrownewIllegalArgumentException(\"\");}returnnewArrayList<String>(list.subList(splitNum,list.size()));}protectedList<String>generateSplitListByFromAndAfterNum(List<String>list,IntegerfromNum,IntegerAfterNum){returnnewArrayList<String>(list.subList(fromNum,AfterNum));}protectedStringreplaceWordIntoSpace(StringinputWord){varreplaced1=StringUtils.replace(inputWord,\"\");varreplaced2=StringUtils.replace(replaced1,\"\");returnreplaced2;}protectedStringreplaceQuotedWord(Stringstr){returnstr.replaceAll(\"\");}protectedList<String>splitInputWordBySpaceAndFilterBlank(StringinputWord){returnArrays.asList(inputWord.split(\"\")).collect(Collectors.toList());}protectedvoidinitEnvAndPrintFirstLog(){this.scanner=newScanner(System.in);this.scanner.useDelimiter(Pattern.compile(\"\"));System.out.println(\"\");}protectedvoidcloseEnv(){this.scanner.close();}protectedvoidevacuationForDebug(Objectobj){varevacObj=obj;}}";
		var expectedResult2 = "packagecom.sono.myproj.process.logic;importjava.io.IOException;importjava.nio.file.Files;importjava.nio.file.Paths;importjava.util.List;importjava.util.Scanner;importjava.util.regex.Matcher;importjava.util.regex.Pattern;importjava.util.stream.Collectors;importorg.apache.commons.lang3.StringUtils;importorg.slf4j.Logger;importorg.slf4j.LoggerFactory;importorg.springframework.stereotype.Component;importcom.sono.myproj.process.dto.CharReader;importcom.sono.myproj.process.utils.CommentRemoveUtils;@ComponentpublicclassMethodUtilsBusinessLogicimplementsBusinessLogic{Loggerlogger=LoggerFactory.getLogger(MethodUtilsBusinessLogic.class);protectedScannerscanner=null;privateStringprcTargetStr=null;publicvoidexecuteMethodUtils(){try{this.beforeProcess();this.mainProcess();this.afterProcess();}catch(Exceptione){logger.error(\"\"+getStackTrace(e));System.out.println(\"\");}}@OverridepublicvoidbeforeProcess()throwsException{initEnvAndPrintFirstLog();logger.info(\"\");this.prcTargetStr=getFileStringFromInputUrl(scanner.nextLine()).stream().collect(Collectors.joining(\"\"));}@OverridepublicvoidmainProcess()throwsException{logger.info(\"\");varresult=beforeMainProcess(this.prcTargetStr);System.out.println(result);}@OverridepublicvoidafterProcess(){logger.info(\"\");}protectedList<String>getFileStringFromInputUrl(StringinputWord)throwsException{varurl=replaceIntoBlank(inputWord);if(!validateUrl(url)){thrownewIllegalArgumentException(\"\");}varoutputStrList=Files.readAllLines(Paths.get(url));returnoutputStrList;}protectedStringbeforeMainProcess(StringinputWord)throwsException{varreplacedStr=replaceQuotedWord(CommentRemoveUtils.remove(inputWord));returnreplacedStr;}protectedbooleanscanMiddleParenthesis(Stringtext){CharReaderr=newCharReader(text);returnfalse;}protectedvoidinitEnvAndPrintFirstLog(){this.scanner=newScanner(System.in);System.out.println(\"\");}protectedStringreplaceIntoBlank(Stringtext){varspaceReplaced=StringUtils.replace(text,\"\");varfullSizeSpaceReplaced=StringUtils.replace(spaceReplaced,\"\");varlineReplaced=StringUtils.replace(fullSizeSpaceReplaced,\"\");vartabReplaced=StringUtils.replace(lineReplaced,\"\");returntabReplaced;}protectedStringreplaceQuotedWord(Stringstr){returnstr.replaceAll(\"\");}protectedbooleanvalidateUrl(Stringtext){if(StringUtils.isEmpty(text)){returnfalse;}Patternp=Pattern.compile(\"\");Matcherm=p.matcher(text);returnm.find();}privatestaticStringgetStackTrace(Exceptione){StackTraceElement[]list=e.getStackTrace();StringBuilderb=newStringBuilder();b.append(e.getClass()).append(\"\");for(StackTraceElements:list){b.append(s.toString()).append(\"\");}returnb.toString();}}";
		var expectedResult3 = "packagecom.sono.myproj.process.impl;importjava.util.Arrays;importorg.slf4j.Logger;importorg.slf4j.LoggerFactory;importorg.springframework.beans.factory.annotation.Autowired;importorg.springframework.stereotype.Component;importcom.sono.myproj.process.MainProcess;importcom.sono.myproj.process.logic.MethodUtilsBusinessLogic;@ComponentpublicclassMethodUtilsProcessImplimplementsMainProcess{Loggerlogger=LoggerFactory.getLogger(MethodUtilsProcessImpl.class);@AutowiredMethodUtilsBusinessLogicmethodUtilsBusinessLogic;@Overridepublicvoidexecute(String[]parameters){this.executeParameters(parameters);methodUtilsBusinessLogic.executeMethodUtils();}@OverridepublicvoidexecuteParameters(String[]parameters){Arrays.asList(parameters).forEach(str->{logger.info(\"\",str);});}}";
		var result1 = generateInputWordFromFileUrlForTest(url1);
		var result2 = generateInputWordFromFileUrlForTest(url2);
		var result3 = generateInputWordFromFileUrlForTest(url3);

		assertEquals(expectedResult1, result1.replaceAll("\n", "").replaceAll("\t", "").replaceAll(" ", ""));
		assertEquals(expectedResult2, result2.replaceAll("\n", "").replaceAll("\t", "").replaceAll(" ", ""));
		assertEquals(expectedResult3, result3.replaceAll("\n", "").replaceAll("\t", "").replaceAll(" ", ""));
		var scanMiddleResult1 = parenthesisUtils.scanMiddleParenthesis(result1);
		var scanMiddleResult2 = parenthesisUtils.scanMiddleParenthesis(result2);
		var scanMiddleResult3 = parenthesisUtils.scanMiddleParenthesis(result3);
		var scanRoundResult1 = parenthesisUtils.scanRoundParenthesis(result1);
		var scanRoundResult2 = parenthesisUtils.scanRoundParenthesis(result1);
		var scanRoundResult3 = parenthesisUtils.scanRoundParenthesis(result1);
		assertTrue(scanMiddleResult1);
		assertTrue(scanMiddleResult2);
		assertTrue(scanMiddleResult3);
		assertTrue(scanRoundResult1);
		assertTrue(scanRoundResult2);
		assertTrue(scanRoundResult3);

	}

	@Test
	public void testGenerateMiddleParenthesisDtoList() throws IOException {
		var result1 = utilsBusinessLogic
				.generatePrgElementDtoList(generatePrgElementList(generateInputWordFromFileUrlForTest(url4)));
		var result2 = utilsBusinessLogic
				.generatePrgElementDtoList(generatePrgElementList(generateInputWordFromFileUrlForTest(url5)));
		var result3 = utilsBusinessLogic
				.generatePrgElementDtoList(generatePrgElementList(generateInputWordFromFileUrlForTest(url6)));
		var x = generatePrgElementList(generateInputWordFromFileUrlForTest(url7));
		var result4 = utilsBusinessLogic
				.generatePrgElementDtoList(generatePrgElementList(generateInputWordFromFileUrlForTest(url7)));
		var result5 = utilsBusinessLogic
				.generatePrgElementDtoList(generatePrgElementList(generateInputWordFromFileUrlForTest(url8)));
		var result6 = utilsBusinessLogic
				.generatePrgElementDtoList(generatePrgElementList(generateInputWordFromFileUrlForTest(url9)));
		var result7 = utilsBusinessLogic
				.generatePrgElementDtoList(generatePrgElementList(generateInputWordFromFileUrlForTest(url10)));
		// クラス内にメソッドが並列
		assertEquals(1, result1.size());
		assertEquals(4, result1.get(0).getPrgElementList().size());
		assertEquals(0, result1.get(0).getPrgElementList().get(0).getPrgElementList().size());
		assertEquals(0, result1.get(0).getPrgElementList().get(1).getPrgElementList().size());
		assertEquals(0, result1.get(0).getPrgElementList().get(2).getPrgElementList().size());
		assertEquals(0, result1.get(0).getPrgElementList().get(3).getPrgElementList().size());

		// メソッド1つに複数のメソッドがネスト
		assertEquals(1, result2.size());
		assertEquals(1, result2.get(0).getPrgElementList().size());
		assertEquals(2, result2.get(0).getPrgElementList().get(0).getPrgElementList().size());
		assertEquals(3,
				result2.get(0).getPrgElementList().get(0).getPrgElementList().get(0).getPrgElementList().size());
		assertEquals(4, result2.get(0).getPrgElementList().get(0).getPrgElementList().get(0).getPrgElementList().get(0)
				.getPrgElementList().size());

		// クラス名のみ存在
		assertEquals(1, result3.size());
		assertEquals(0, result3.get(0).getPrgElementList().size());

		// メソッド1つのみ
		assertEquals(1, result4.size());
		assertEquals(1, result4.get(0).getPrgElementList().size());
		assertEquals(0, result4.get(0).getPrgElementList().get(0).getPrgElementList().size());

		// 何もいない場合
		assertEquals(0, result5.size());

		// 実際のファイルの場合1
		assertEquals(1, result6.size());
		assertEquals(2, result6.get(0).getPrgElementList().size());
		assertEquals(0, result6.get(0).getPrgElementList().get(0).getPrgElementList().size());
		assertEquals(1, result6.get(0).getPrgElementList().get(1).getPrgElementList().size());

		// 実際のファイルの場合2 クラスが2ついる場合も併せて
		assertEquals(2, result7.size());
		assertEquals(12, result7.get(0).getPrgElementList().size());
		// executeMethodUtils
		assertEquals(2, result7.get(0).getPrgElementList().get(0).getPrgElementList().size());
		assertEquals(0,
				result7.get(0).getPrgElementList().get(0).getPrgElementList().get(0).getPrgElementList().size());
		assertEquals(0,
				result7.get(0).getPrgElementList().get(0).getPrgElementList().get(1).getPrgElementList().size());
		// beforeProcess
		assertEquals(0, result7.get(0).getPrgElementList().get(1).getPrgElementList().size());
		// mainProcess
		assertEquals(0, result7.get(0).getPrgElementList().get(2).getPrgElementList().size());
		// afterProcess
		assertEquals(0, result7.get(0).getPrgElementList().get(3).getPrgElementList().size());
		// getFileStringFromInputUrl
		assertEquals(1, result7.get(0).getPrgElementList().get(4).getPrgElementList().size());
		assertEquals(0,
				result7.get(0).getPrgElementList().get(4).getPrgElementList().get(0).getPrgElementList().size());
		// beforeMainProcess
		assertEquals(2, result7.get(0).getPrgElementList().get(5).getPrgElementList().size());
		assertEquals(0,
				result7.get(0).getPrgElementList().get(5).getPrgElementList().get(0).getPrgElementList().size());
		assertEquals(0,
				result7.get(0).getPrgElementList().get(5).getPrgElementList().get(1).getPrgElementList().size());
		// generatePrgElementList
		assertEquals(0, result7.get(0).getPrgElementList().get(6).getPrgElementList().size());
		// generatePrgElementDtoList
		assertEquals(0, result7.get(0).getPrgElementList().get(7).getPrgElementList().size());
		// generatePrgElementDtoListRecursions
		assertEquals(1, result7.get(0).getPrgElementList().get(8).getPrgElementList().size());
		assertEquals(3,
				result7.get(0).getPrgElementList().get(8).getPrgElementList().get(0).getPrgElementList().size());
		assertEquals(0, result7.get(0).getPrgElementList().get(8).getPrgElementList().get(0).getPrgElementList().get(0)
				.getPrgElementList().size());
		assertEquals(0, result7.get(0).getPrgElementList().get(8).getPrgElementList().get(0).getPrgElementList().get(1)
				.getPrgElementList().size());
		assertEquals(0, result7.get(0).getPrgElementList().get(8).getPrgElementList().get(0).getPrgElementList().get(2)
				.getPrgElementList().size());
		// setPrgElementInfo
		assertEquals(0, result7.get(0).getPrgElementList().get(9).getPrgElementList().size());
		// addIntoPrgElementInfoList
		assertEquals(1, result7.get(0).getPrgElementList().get(10).getPrgElementList().size());
		assertEquals(0,
				result7.get(0).getPrgElementList().get(10).getPrgElementList().get(0).getPrgElementList().size());
		// initEnvAndPrintFirstLog
		assertEquals(0, result7.get(0).getPrgElementList().get(11).getPrgElementList().size());
		// DummyClass
		assertEquals(1, result7.get(1).getPrgElementList().size());
		assertEquals(2, result7.get(1).getPrgElementList().get(0).getPrgElementList().size());
		assertEquals(3,
				result7.get(1).getPrgElementList().get(0).getPrgElementList().get(0).getPrgElementList().size());
		assertEquals(4, result7.get(1).getPrgElementList().get(0).getPrgElementList().get(0).getPrgElementList().get(0)
				.getPrgElementList().size());
	}

	@Test
	public void testGenerateMethodName() throws IOException {
		var result6 = utilsBusinessLogic
				.generatePrgElementDtoList(generatePrgElementList(generateInputWordFromFileUrlForTest(url9)));
		var result7 = utilsBusinessLogic
				.generatePrgElementDtoList(generatePrgElementList(generateInputWordFromFileUrlForTest(url10)));
		var result8 = utilsBusinessLogic
				.generatePrgElementDtoList(generatePrgElementList(generateInputWordFromFileUrlForTest(url11)));
		var result9 = utilsBusinessLogic
				.generatePrgElementDtoList(generatePrgElementList(generateInputWordFromFileUrlForTest(url12)));
		var className6 = result6.get(0).getElementName();
		var execute6 = result6.get(0).getPrgElementList().get(0).getElementName();
		var executeParameters6 = result6.get(0).getPrgElementList().get(1).getElementName();
		assertEquals("MethodUtilsProcessImpl", className6);
		assertEquals("execute", execute6);
		assertEquals("executeParameters", executeParameters6);

		var className7 = result7.get(0).getElementName();
		var executeMethodUtils7 = result7.get(0).getPrgElementList().get(0).getElementName();
		var try7 = result7.get(0).getPrgElementList().get(0).getPrgElementList().get(0).getElementName();
		var catch7 = result7.get(0).getPrgElementList().get(0).getPrgElementList().get(1).getElementName();
		var beforeProcess7 = result7.get(0).getPrgElementList().get(1).getElementName();
		var mainProcess7 = result7.get(0).getPrgElementList().get(2).getElementName();
		var afterProcess7 = result7.get(0).getPrgElementList().get(3).getElementName();
		var getFileStringFromInputUrl7 = result7.get(0).getPrgElementList().get(4).getElementName();
		var if74 = result7.get(0).getPrgElementList().get(4).getPrgElementList().get(0);
		var beforeMainProcess7 = result7.get(0).getPrgElementList().get(5).getElementName();
		var if75 = result7.get(0).getPrgElementList().get(5).getPrgElementList().get(0).getElementName();
		var else75 = result7.get(0).getPrgElementList().get(5).getPrgElementList().get(1).getElementName();
		var generatePrgElementList7 = result7.get(0).getPrgElementList().get(6).getElementName();
		var generatePrgElementDtoList7 = result7.get(0).getPrgElementList().get(7).getElementName();
		var generatePrgElementDtoListRecursion7 = result7.get(0).getPrgElementList().get(8).getElementName();
		var for78 = result7.get(0).getPrgElementList().get(8).getPrgElementList().get(0).getElementName();
		var if78 = result7.get(0).getPrgElementList().get(8).getPrgElementList().get(0).getPrgElementList().get(0)
				.getElementName();
		var if78of2 = result7.get(0).getPrgElementList().get(8).getPrgElementList().get(0).getPrgElementList().get(1)
				.getElementName();
		var if78of3 = result7.get(0).getPrgElementList().get(8).getPrgElementList().get(0).getPrgElementList().get(2)
				.getElementName();
		var setPrgElementInfo7 = result7.get(0).getPrgElementList().get(9).getElementName();
		var addIntoPrgElementInfoList7 = result7.get(0).getPrgElementList().get(10).getElementName();
		var lambda7 = result7.get(0).getPrgElementList().get(10).getPrgElementList().get(0).getElementName();
		var initEnvAndPrintFirstLog7 = result7.get(0).getPrgElementList().get(11).getElementName();
		assertEquals("MethodUtilsBusinessLogic", className7);
		assertEquals("executeMethodUtils", executeMethodUtils7);
		assertEquals("try", try7);
		assertEquals("catch", catch7);
		assertEquals("beforeProcess", beforeProcess7);
		assertEquals("mainProcess", mainProcess7);
		assertEquals("afterProcess", afterProcess7);
		assertEquals("getFileStringFromInputUrl", getFileStringFromInputUrl7);
		assertEquals("if", if74);
		assertEquals("beforeMainProcess", beforeMainProcess7);
		assertEquals("if", if75);
		assertEquals("else", else75);
		assertEquals("generatePrgElementList", generatePrgElementList7);
		assertEquals("generatePrgElementDtoList", generatePrgElementDtoList7);
		assertEquals("generatePrgElementDtoListRecursion", generatePrgElementDtoListRecursion7);
		assertEquals("for", for78);
		assertEquals("if", if78);
		assertEquals("if", if78of2);
		assertEquals("if", if78of3);
		assertEquals("setPrgElementInfo", setPrgElementInfo7);
		assertEquals("addIntoPrgElementInfoList", addIntoPrgElementInfoList7);
		assertEquals("->", lambda7);
		assertEquals("initEnvAndPrintFirstLog", initEnvAndPrintFirstLog7);

		var className8 = result8.get(0).getElementName();
		var B = result8.get(0).getPrgElementList().get(0).getElementName();
		var C = result8.get(0).getPrgElementList().get(1).getElementName();
		var forC = result8.get(0).getPrgElementList().get(1).getPrgElementList().get(0).getElementName();
		var D = result8.get(0).getPrgElementList().get(2).getElementName();
		var Dif = result8.get(0).getPrgElementList().get(2).getPrgElementList().get(0).getElementName();
		var Dfor = result8.get(0).getPrgElementList().get(2).getPrgElementList().get(1).getElementName();
		var E = result8.get(0).getPrgElementList().get(3).getElementName();
		var F = result8.get(0).getPrgElementList().get(4).getElementName();
		var Ffor = result8.get(0).getPrgElementList().get(4).getPrgElementList().get(0).getElementName();
		var Fif1 = result8.get(0).getPrgElementList().get(4).getPrgElementList().get(0).getPrgElementList().get(0)
				.getElementName();
		var Fifif = result8.get(0).getPrgElementList().get(4).getPrgElementList().get(0).getPrgElementList().get(0)
				.getPrgElementList().get(0).getElementName();
		var Fifif2 = result8.get(0).getPrgElementList().get(4).getPrgElementList().get(0).getPrgElementList().get(0)
				.getPrgElementList().get(1).getElementName();
		var Fifelse = result8.get(0).getPrgElementList().get(4).getPrgElementList().get(0).getPrgElementList().get(0)
				.getPrgElementList().get(2).getElementName();
		var Fif2 = result8.get(0).getPrgElementList().get(4).getPrgElementList().get(0).getPrgElementList().get(1)
				.getElementName();
		var Felse = result8.get(0).getPrgElementList().get(4).getPrgElementList().get(0).getPrgElementList().get(1)
				.getElementName();
		var Fswitch = result8.get(0).getPrgElementList().get(4).getPrgElementList().get(1).getElementName();
		assertEquals("A", className8);
		assertEquals("B", B);
		assertEquals("C", C);
		assertEquals("for", forC);
		assertEquals("D", D);
		assertEquals("if", Dif);
		assertEquals("for", Dfor);
		assertEquals("E", E);
		assertEquals("F", F);
		assertEquals("for", Ffor);
		assertEquals("if", Fif1);
		assertEquals("if", Fifif);
		assertEquals("if", Fifif2);
		assertEquals("else", Fifelse);
		assertEquals("if", Fif2);
		assertEquals("else", Felse);
		assertEquals("switch", Fswitch);

		var className9 = result9.get(0).getElementName();
		var remove9 = result9.get(0).getPrgElementList().get(0).getElementName();
		var removeWhile = result9.get(0).getPrgElementList().get(0).getPrgElementList().get(0).getElementName();
		var removeWhileSwitch = result9.get(0).getPrgElementList().get(0).getPrgElementList().get(0).getPrgElementList()
				.get(0).getElementName();
		var removeWhileSwitchSwitch = result9.get(0).getPrgElementList().get(0).getPrgElementList().get(0)
				.getPrgElementList().get(0).getPrgElementList().get(0).getElementName();
		var skipBlockComment9 = result9.get(0).getPrgElementList().get(1).getElementName();
		var skipBlockCommentWhile = result9.get(0).getPrgElementList().get(1).getPrgElementList().get(0)
				.getElementName();
		var skipBlockCommentWhileIf = result9.get(0).getPrgElementList().get(1).getPrgElementList().get(0)
				.getPrgElementList().get(0);
		var skipBlockCommentWhileIf2 = result9.get(0).getPrgElementList().get(1).getPrgElementList().get(0)
				.getPrgElementList().get(1);
		var skipLineComment9 = result9.get(0).getPrgElementList().get(2).getElementName();
		var skipLineCommentWhile = result9.get(0).getPrgElementList().get(2).getPrgElementList().get(0);
		var skipStringOrChar9 = result9.get(0).getPrgElementList().get(3).getElementName();
		var skipStringOrCharWhile = result9.get(0).getPrgElementList().get(3).getPrgElementList().get(0)
				.getElementName();
		var skipStringOrCharWhileIf = result9.get(0).getPrgElementList().get(3).getPrgElementList().get(0)
				.getPrgElementList().get(0).getElementName();
		var skipStringOrCharWhileIf2 = result9.get(0).getPrgElementList().get(3).getPrgElementList().get(0)
				.getPrgElementList().get(1).getElementName();
		assertEquals("CommentRemoveUtils", className9);
		assertEquals("remove", remove9);
		assertEquals("while", removeWhile);
		assertEquals("switch", removeWhileSwitch);
		assertEquals("switch", removeWhileSwitchSwitch);
		assertEquals("skipBlockComment", skipBlockComment9);
		assertEquals("while", skipBlockCommentWhile);
		assertEquals("if", skipBlockCommentWhileIf);
		assertEquals("if", skipBlockCommentWhileIf2);
		assertEquals("skipLineComment", skipLineComment9);
		assertEquals("while", skipLineCommentWhile);
		assertEquals("skipStringOrChar", skipStringOrChar9);
		assertEquals("while", skipStringOrCharWhile);
		assertEquals("if", skipStringOrCharWhileIf);
		assertEquals("", skipStringOrCharWhileIf2);

	}

	@Test
	public void testGenerateSignature() throws IOException {
		var result6 = utilsBusinessLogic
				.generatePrgElementDtoList(generatePrgElementList(generateInputWordFromFileUrlForTest(url9)));
		var result7 = utilsBusinessLogic
				.generatePrgElementDtoList(generatePrgElementList(generateInputWordFromFileUrlForTest(url10)));
		var result8 = utilsBusinessLogic
				.generatePrgElementDtoList(generatePrgElementList(generateInputWordFromFileUrlForTest(url11)));
		var result9 = utilsBusinessLogic
				.generatePrgElementDtoList(generatePrgElementList(generateInputWordFromFileUrlForTest(url12)));
		var className6 = result6.get(0).getSignature();
		var execute6 = result6.get(0).getPrgElementList().get(0).getSignature();
		var executeParameters6 = result6.get(0).getPrgElementList().get(1).getSignature();
		assertNull(className6);
		assertEquals(Arrays.asList("String[] parameters"), execute6);
		assertEquals(Arrays.asList("String[] parameters"), executeParameters6);

		var className7 = result7.get(0).getSignature();
		var executeMethodUtils7 = result7.get(0).getPrgElementList().get(0).getSignature();
		var try7 = result7.get(0).getPrgElementList().get(0).getPrgElementList().get(0).getSignature();
		var catch7 = result7.get(0).getPrgElementList().get(0).getPrgElementList().get(1).getSignature();
		var beforeProcess7 = result7.get(0).getPrgElementList().get(1).getSignature();
		var mainProcess7 = result7.get(0).getPrgElementList().get(2).getSignature();
		var afterProcess7 = result7.get(0).getPrgElementList().get(3).getSignature();
		var getFileStringFromInputUrl7 = result7.get(0).getPrgElementList().get(4).getSignature();
		var if74 = result7.get(0).getPrgElementList().get(4).getPrgElementList().get(0);
		var beforeMainProcess7 = result7.get(0).getPrgElementList().get(5).getSignature();
		var if75 = result7.get(0).getPrgElementList().get(5).getPrgElementList().get(0).getSignature();
		var else75 = result7.get(0).getPrgElementList().get(5).getPrgElementList().get(1).getSignature();
		var generatePrgElementList7 = result7.get(0).getPrgElementList().get(6).getSignature();
		var generatePrgElementDtoList7 = result7.get(0).getPrgElementList().get(7).getSignature();
		var generatePrgElementDtoListRecursion7 = result7.get(0).getPrgElementList().get(8).getSignature();
		var for78 = result7.get(0).getPrgElementList().get(8).getPrgElementList().get(0).getSignature();
		var if78 = result7.get(0).getPrgElementList().get(8).getPrgElementList().get(0).getPrgElementList().get(0)
				.getSignature();
		var if78of2 = result7.get(0).getPrgElementList().get(8).getPrgElementList().get(0).getPrgElementList().get(1)
				.getSignature();
		var if78of3 = result7.get(0).getPrgElementList().get(8).getPrgElementList().get(0).getPrgElementList().get(2)
				.getSignature();
		var setPrgElementInfo7 = result7.get(0).getPrgElementList().get(9).getSignature();
		var addIntoPrgElementInfoList7 = result7.get(0).getPrgElementList().get(10).getSignature();
		var lambda7 = result7.get(0).getPrgElementList().get(11).getPrgElementList().get(0);
		var initEnvAndPrintFirstLog7 = result7.get(0).getPrgElementList().get(12).getSignature();
		assertNull(className7);
		assertEquals(0, executeMethodUtils7.size());
		assertNull(try7);
		assertNull(catch7);
		assertEquals(0, beforeProcess7.size());
		assertEquals(0, mainProcess7.size());
		assertEquals(0, afterProcess7.size());
		assertEquals(Arrays.asList("String inputWord"), getFileStringFromInputUrl7);
		assertNull(if74);
		assertEquals(Arrays.asList("String inputWord"), beforeMainProcess7);
		assertNull(if75);
		assertNull(else75);
		assertEquals(Arrays.asList("String text"), generatePrgElementList7);
		assertEquals(Arrays.asList("List<String> prgElementList"), generatePrgElementDtoList7);
		assertEquals(Arrays.asList("List<String> targetList", "Integer startIndex"),
				generatePrgElementDtoListRecursion7);
		assertNull(for78);
		assertNull(if78);
		assertNull(if78of2);
		assertNull(if78of3);
		assertEquals(Arrays.asList("PrgElementInfoDto prgElementInfoDto", "List<PrgElementInfoDto> prgElementInfoDtos"),
				setPrgElementInfo7);
		assertEquals(Arrays.asList("List<PrgElementInfoDto> addList", "List<PrgElementInfoDto> addTarget"),
				addIntoPrgElementInfoList7);
		assertNull(lambda7);
		assertEquals(0, initEnvAndPrintFirstLog7.size());

		var className8 = result8.get(0).getSignature();
		var B = result8.get(0).getPrgElementList().get(0).getSignature();
		var C = result8.get(0).getPrgElementList().get(1).getSignature();
		var forC = result8.get(0).getPrgElementList().get(1).getPrgElementList().get(0).getSignature();
		var D = result8.get(0).getPrgElementList().get(2).getSignature();
		var Dif = result8.get(0).getPrgElementList().get(2).getPrgElementList().get(0).getSignature();
		var Dfor = result8.get(0).getPrgElementList().get(2).getPrgElementList().get(1).getSignature();
		var E = result8.get(0).getPrgElementList().get(3).getSignature();
		var F = result8.get(0).getPrgElementList().get(4).getSignature();
		var Ffor = result8.get(0).getPrgElementList().get(4).getPrgElementList().get(0).getSignature();
		var Fif1 = result8.get(0).getPrgElementList().get(4).getPrgElementList().get(0).getPrgElementList().get(0)
				.getSignature();
		var Fifif = result8.get(0).getPrgElementList().get(4).getPrgElementList().get(0).getPrgElementList().get(0)
				.getPrgElementList().get(0).getSignature();
		var Fifif2 = result8.get(0).getPrgElementList().get(4).getPrgElementList().get(0).getPrgElementList().get(0)
				.getPrgElementList().get(1).getSignature();
		var Fifelse = result8.get(0).getPrgElementList().get(4).getPrgElementList().get(0).getPrgElementList().get(0)
				.getPrgElementList().get(2).getSignature();
		var Fif2 = result8.get(0).getPrgElementList().get(4).getPrgElementList().get(0).getPrgElementList().get(1)
				.getSignature();
		var Felse = result8.get(0).getPrgElementList().get(4).getPrgElementList().get(0).getPrgElementList().get(1)
				.getSignature();
		var Fswitch = result8.get(0).getPrgElementList().get(4).getPrgElementList().get(1).getSignature();
		assertNull(className8);
		assertEquals(0, B.size());
		assertEquals(0, C.size());
		assertNull(forC);
		assertEquals(0, D.size());
		assertNull(Dif);
		assertNull(Dfor);
		assertEquals(0, E.size());
		assertEquals(0, F.size());
		assertNull(Ffor);
		assertNull(Fif1);
		assertNull(Fifif);
		assertNull(Fifif2);
		assertNull(Fifelse);
		assertNull(Fif2);
		assertNull(Felse);
		assertNull(Fswitch);

		var className9 = result9.get(0).getSignature();
		var remove9 = result9.get(0).getPrgElementList().get(0).getSignature();
		var removeWhile = result9.get(0).getPrgElementList().get(0).getPrgElementList().get(0).getSignature();
		var removeWhileSwitch = result9.get(0).getPrgElementList().get(0).getPrgElementList().get(0).getPrgElementList()
				.get(0).getSignature();
		var removeWhileSwitchSwitch = result9.get(0).getPrgElementList().get(0).getPrgElementList().get(0)
				.getPrgElementList().get(0).getPrgElementList().get(0).getSignature();
		var skipBlockComment9 = result9.get(0).getPrgElementList().get(1).getSignature();
		var skipBlockCommentWhile = result9.get(0).getPrgElementList().get(1).getPrgElementList().get(0).getSignature();
		var skipBlockCommentWhileIf = result9.get(0).getPrgElementList().get(1).getPrgElementList().get(0)
				.getPrgElementList().get(0);
		var skipBlockCommentWhileIf2 = result9.get(0).getPrgElementList().get(1).getPrgElementList().get(0)
				.getPrgElementList().get(1);
		var skipLineComment9 = result9.get(0).getPrgElementList().get(2).getSignature();
		var skipLineCommentWhile = result9.get(0).getPrgElementList().get(2).getPrgElementList().get(0);
		var skipStringOrChar9 = result9.get(0).getPrgElementList().get(3).getSignature();
		var skipStringOrCharWhile = result9.get(0).getPrgElementList().get(3).getPrgElementList().get(0).getSignature();
		var skipStringOrCharWhileIf = result9.get(0).getPrgElementList().get(3).getPrgElementList().get(0)
				.getPrgElementList().get(0).getSignature();
		var skipStringOrCharWhileIf2 = result9.get(0).getPrgElementList().get(3).getPrgElementList().get(0)
				.getPrgElementList().get(1).getSignature();
		assertNull(className9);
		assertEquals(Arrays.asList("String text"), remove9);
		assertNull(removeWhile);
		assertNull(removeWhileSwitch);
		assertNull(removeWhileSwitchSwitch);
		assertEquals(Arrays.asList("CharReader r"), skipBlockComment9);
		assertNull(skipBlockCommentWhile);
		assertNull(skipBlockCommentWhileIf);
		assertNull(skipBlockCommentWhileIf2);
		assertEquals(Arrays.asList("CharReader r"), skipLineComment9);
		assertNull(skipLineCommentWhile);
		assertEquals(Arrays.asList("char start", "CharReader r"), skipStringOrChar9);
		assertNull(skipStringOrCharWhile);
		assertNull(skipStringOrCharWhileIf);
		assertNull(skipStringOrCharWhileIf2);
	}

	@Test
	public void testGenerateReturnVal() throws IOException {
		var result6 = utilsBusinessLogic
				.generatePrgElementDtoList(generatePrgElementList(generateInputWordFromFileUrlForTest(url9)));
		var result7 = utilsBusinessLogic
				.generatePrgElementDtoList(generatePrgElementList(generateInputWordFromFileUrlForTest(url10)));
		var result8 = utilsBusinessLogic
				.generatePrgElementDtoList(generatePrgElementList(generateInputWordFromFileUrlForTest(url11)));
		var result9 = utilsBusinessLogic
				.generatePrgElementDtoList(generatePrgElementList(generateInputWordFromFileUrlForTest(url12)));
		var className6 = result6.get(0).getReturnVal();
		var execute6 = result6.get(0).getPrgElementList().get(0).getReturnVal();
		var executeParameters6 = result6.get(0).getPrgElementList().get(1).getReturnVal();
		assertNull(className6);
		assertEquals("void", execute6);
		assertEquals("void", executeParameters6);

		var className7 = result7.get(0).getReturnVal();
		var executeMethodUtils7 = result7.get(0).getPrgElementList().get(0).getReturnVal();
		var try7 = result7.get(0).getPrgElementList().get(0).getPrgElementList().get(0).getReturnVal();
		var catch7 = result7.get(0).getPrgElementList().get(0).getPrgElementList().get(1).getReturnVal();
		var beforeProcess7 = result7.get(0).getPrgElementList().get(1).getReturnVal();
		var mainProcess7 = result7.get(0).getPrgElementList().get(2).getReturnVal();
		var afterProcess7 = result7.get(0).getPrgElementList().get(3).getReturnVal();
		var getFileStringFromInputUrl7 = result7.get(0).getPrgElementList().get(4).getReturnVal();
		var if74 = result7.get(0).getPrgElementList().get(4).getPrgElementList().get(0);
		var beforeMainProcess7 = result7.get(0).getPrgElementList().get(5).getReturnVal();
		var if75 = result7.get(0).getPrgElementList().get(5).getPrgElementList().get(0).getReturnVal();
		var else75 = result7.get(0).getPrgElementList().get(5).getPrgElementList().get(1).getReturnVal();
		var generatePrgElementList7 = result7.get(0).getPrgElementList().get(6).getReturnVal();
		var generatePrgElementDtoList7 = result7.get(0).getPrgElementList().get(7).getReturnVal();
		var generatePrgElementDtoListRecursion7 = result7.get(0).getPrgElementList().get(8).getReturnVal();
		var for78 = result7.get(0).getPrgElementList().get(8).getPrgElementList().get(0).getReturnVal();
		var if78 = result7.get(0).getPrgElementList().get(8).getPrgElementList().get(0).getPrgElementList().get(0)
				.getReturnVal();
		var if78of2 = result7.get(0).getPrgElementList().get(8).getPrgElementList().get(0).getPrgElementList().get(1)
				.getReturnVal();
		var if78of3 = result7.get(0).getPrgElementList().get(8).getPrgElementList().get(0).getPrgElementList().get(2)
				.getReturnVal();
		var setPrgElementInfo7 = result7.get(0).getPrgElementList().get(9).getReturnVal();
		var addIntoPrgElementInfoList7 = result7.get(0).getPrgElementList().get(10).getReturnVal();
		var lambda7 = result7.get(0).getPrgElementList().get(11).getPrgElementList().get(0);
		var initEnvAndPrintFirstLog7 = result7.get(0).getPrgElementList().get(12).getReturnVal();
		assertNull(className7);
		assertEquals("void", executeMethodUtils7);
		assertNull(try7);
		assertNull(catch7);
		assertEquals("void", beforeProcess7);
		assertEquals("void", mainProcess7);
		assertEquals("void", afterProcess7);
		assertEquals("List<String>", getFileStringFromInputUrl7);
		assertNull(if74);
		assertEquals("String", beforeMainProcess7);
		assertNull(if75);
		assertNull(else75);
		assertEquals("List<String>", generatePrgElementList7);
		assertEquals("List<PrgElementInfoDto>", generatePrgElementDtoList7);
		assertEquals("List<PrgElementInfoDto>", generatePrgElementDtoListRecursion7);
		assertNull(for78);
		assertNull(if78);
		assertNull(if78of2);
		assertNull(if78of3);
		assertEquals("List<PrgElementInfoDto>", setPrgElementInfo7);
		assertEquals("void", addIntoPrgElementInfoList7);
		assertNull(lambda7);
		assertEquals("void", initEnvAndPrintFirstLog7);

		var className8 = result8.get(0).getReturnVal();
		var B = result8.get(0).getPrgElementList().get(0).getReturnVal();
		var C = result8.get(0).getPrgElementList().get(1).getReturnVal();
		var forC = result8.get(0).getPrgElementList().get(1).getPrgElementList().get(0).getReturnVal();
		var D = result8.get(0).getPrgElementList().get(2).getReturnVal();
		var Dif = result8.get(0).getPrgElementList().get(2).getPrgElementList().get(0).getReturnVal();
		var Dfor = result8.get(0).getPrgElementList().get(2).getPrgElementList().get(1).getReturnVal();
		var E = result8.get(0).getPrgElementList().get(3).getReturnVal();
		var F = result8.get(0).getPrgElementList().get(4).getReturnVal();
		var Ffor = result8.get(0).getPrgElementList().get(4).getPrgElementList().get(0).getReturnVal();
		var Fif1 = result8.get(0).getPrgElementList().get(4).getPrgElementList().get(0).getPrgElementList().get(0)
				.getReturnVal();
		var Fifif = result8.get(0).getPrgElementList().get(4).getPrgElementList().get(0).getPrgElementList().get(0)
				.getPrgElementList().get(0).getReturnVal();
		var Fifif2 = result8.get(0).getPrgElementList().get(4).getPrgElementList().get(0).getPrgElementList().get(0)
				.getPrgElementList().get(1).getReturnVal();
		var Fifelse = result8.get(0).getPrgElementList().get(4).getPrgElementList().get(0).getPrgElementList().get(0)
				.getPrgElementList().get(2).getReturnVal();
		var Fif2 = result8.get(0).getPrgElementList().get(4).getPrgElementList().get(0).getPrgElementList().get(1)
				.getReturnVal();
		var Felse = result8.get(0).getPrgElementList().get(4).getPrgElementList().get(0).getPrgElementList().get(1)
				.getReturnVal();
		var Fswitch = result8.get(0).getPrgElementList().get(4).getPrgElementList().get(1).getReturnVal();
		assertNull(className8);
		assertEquals("void", B);
		assertEquals("String", C);
		assertNull(forC);
		assertEquals("int", D);
		assertNull(Dif);
		assertNull(Dfor);
		assertNull(E);
		assertEquals("void", F);
		assertNull(Ffor);
		assertNull(Fif1);
		assertNull(Fifif);
		assertNull(Fifif2);
		assertNull(Fifelse);
		assertNull(Fif2);
		assertNull(Felse);
		assertNull(Fswitch);

		var className9 = result9.get(0).getReturnVal();
		var remove9 = result9.get(0).getPrgElementList().get(0).getReturnVal();
		var removeWhile = result9.get(0).getPrgElementList().get(0).getPrgElementList().get(0).getReturnVal();
		var removeWhileSwitch = result9.get(0).getPrgElementList().get(0).getPrgElementList().get(0).getPrgElementList()
				.get(0).getReturnVal();
		var removeWhileSwitchSwitch = result9.get(0).getPrgElementList().get(0).getPrgElementList().get(0)
				.getPrgElementList().get(0).getPrgElementList().get(0).getReturnVal();
		var skipBlockComment9 = result9.get(0).getPrgElementList().get(1).getReturnVal();
		var skipBlockCommentWhile = result9.get(0).getPrgElementList().get(1).getPrgElementList().get(0).getReturnVal();
		var skipBlockCommentWhileIf = result9.get(0).getPrgElementList().get(1).getPrgElementList().get(0)
				.getPrgElementList().get(0);
		var skipBlockCommentWhileIf2 = result9.get(0).getPrgElementList().get(1).getPrgElementList().get(0)
				.getPrgElementList().get(1);
		var skipLineComment9 = result9.get(0).getPrgElementList().get(2).getReturnVal();
		var skipLineCommentWhile = result9.get(0).getPrgElementList().get(2).getPrgElementList().get(0);
		var skipStringOrChar9 = result9.get(0).getPrgElementList().get(3).getReturnVal();
		var skipStringOrCharWhile = result9.get(0).getPrgElementList().get(3).getPrgElementList().get(0).getReturnVal();
		var skipStringOrCharWhileIf = result9.get(0).getPrgElementList().get(3).getPrgElementList().get(0)
				.getPrgElementList().get(0).getReturnVal();
		var skipStringOrCharWhileIf2 = result9.get(0).getPrgElementList().get(3).getPrgElementList().get(0)
				.getPrgElementList().get(1).getReturnVal();
		assertEquals("CommentRemoveUtils", className9);
		assertEquals("remove", remove9);
		assertEquals("while", removeWhile);
		assertEquals("switch", removeWhileSwitch);
		assertEquals("switch", removeWhileSwitchSwitch);
		assertEquals("skipBlockComment", skipBlockComment9);
		assertEquals("while", skipBlockCommentWhile);
		assertEquals("if", skipBlockCommentWhileIf);
		assertEquals("if", skipBlockCommentWhileIf2);
		assertEquals("skipLineComment", skipLineComment9);
		assertEquals("while", skipLineCommentWhile);
		assertEquals("skipStringOrChar", skipStringOrChar9);
		assertEquals("while", skipStringOrCharWhile);
		assertEquals("if", skipStringOrCharWhileIf);
		assertEquals("if", skipStringOrCharWhileIf2);
	}

	@Test
	public void testGenerateFirstAndLastParenthesisCount() throws IOException {
		var url = "/Applications/Eclipse_2022-03.app/Contents/workspace/method-utils-v2/src/main/resources/test/SampleGenerateFirstAndLastParenthesisCount.java";
		var list = generatePrgElementList(generateInputWordFromFileUrlForTest(url));
		var result1 = parenthesisUtils.generateFirstAndLastParenthesisCount(list, 3);
		var result2 = parenthesisUtils.generateFirstAndLastParenthesisCount(list, 9);
		var result3 = parenthesisUtils.generateFirstAndLastParenthesisCount(list, 18);
		var result4 = parenthesisUtils.generateFirstAndLastParenthesisCount(list, 29);
		var result5 = parenthesisUtils.generateFirstAndLastParenthesisCount(list, 38);
		var result6 = parenthesisUtils.generateFirstAndLastParenthesisCount(list, 46);
		var result7 = parenthesisUtils.generateFirstAndLastParenthesisCount(list, 49);
		var result8 = parenthesisUtils.generateFirstAndLastParenthesisCount(list, 53);

		assertEquals(new LeftAndRightParenthesisDto(0, 0), result1);
		assertEquals(new LeftAndRightParenthesisDto(7, 8), result2);
		assertEquals(new LeftAndRightParenthesisDto(14, 17), result3);
		assertEquals(new LeftAndRightParenthesisDto(24, 28), result4);
		assertEquals(new LeftAndRightParenthesisDto(36, 37), result5);
		assertEquals(new LeftAndRightParenthesisDto(40, 45), result6);
		assertEquals(new LeftAndRightParenthesisDto(0, 0), result7);
		assertEquals(new LeftAndRightParenthesisDto(0, 0), result8);

	}

	private List<String> generatePrgElementList(String text) {
		var spaced = parenthesisUtils.replaceParenthesisIntoSpaced(text);
		return Arrays.asList(replaceWordUtils.replaceTabAndLined(spaced).split(" ")).stream()
				.filter(word -> !word.equals("")).collect(Collectors.toList());
	}

	private String generateInputWordFromFileUrlForTest(String url) throws IOException {
		return replaceWordUtils.replaceQuotedWord(CommentRemoveUtils
				.remove(Files.readAllLines(Paths.get(url)).stream().collect(Collectors.joining("\n"))));
	}

}