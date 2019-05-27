

package com.seomse.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seomse.commons.communication.SendToReceive;
import com.seomse.commons.handler.EndHandler;
import com.seomse.commons.handler.ExceptionHandler;
import com.seomse.commons.utils.ExceptionUtil;
/**
 * <pre>
 *  파 일 명 : ApiCommunication.java
 *  설    명 : ApiCommunication
 *
 *  작 성 자 : macle
 *  작 성 일 : 2018.04
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2018 by ㈜섬세한사람들. All right reserved.
 */
public class ApiCommunication extends Thread{

	
	
	private static final Logger logger = LoggerFactory.getLogger(ApiCommunication.class);
	public static final char DEFAULT_PACKAGE = 'D';
	public static final char CUSTOM_PACKAGE = 'C';
	
	
	
	
	private SendToReceive sendToReceive;
	private boolean flag = true;
	
	private String defaultPackageName;
	
	
	private long createTime;
	
	private EndHandler endHandler = null;
	
	private ExceptionHandler exceptionHandler = null;

		
	private int maxLogLength = 150;
	
	
	private boolean isLog= true;
	
	public void setNotLog() {
		isLog = false;
	}
	
	/**
	 * 생성자
	 * @param defaultPackageName 기본패키지
	 * @param socket 소캣
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public ApiCommunication(String defaultPackageName, Socket socket) throws UnsupportedEncodingException, IOException {
		sendToReceive = new SendToReceive(socket);
		createTime = System.currentTimeMillis();
		
		this.defaultPackageName = defaultPackageName;
	
	}
	
	/**
	 * 로그 최대문자 길이 설정
	 * @param maxLogLength
	 */
	public void setMaxLogLength(int maxLogLength) {
		this.maxLogLength = maxLogLength;
	}



	/**
	 * 생성 time 얻기
	 * @return
	 */
	public long getCreateTime() {
		return createTime;
	}
	
	
	/**
	 * 종료 핸들러
	 * @param endHandler
	 */
	public void setEndHandler(EndHandler endHandler) {
		this.endHandler = endHandler;
	}

	/**
	 * 예외 핸들러설정
	 * @param exceptionHandler
	 */
	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
	
	@Override
	public void run() {
		
		try {
			while(flag){
				
				String message  = sendToReceive.receive();
				if(message == null){
					flag = false;
					disConnect();
					break;
				}
				
				readMessage(message);
				
			}
		}catch(Exception e) {
			ExceptionUtil.exception(e, logger, exceptionHandler);
		}
		try{
			if(endHandler != null){
				endHandler.end(this);
			}
		}catch(Exception e){
			ExceptionUtil.exception(e, logger, exceptionHandler);
		}
		
	}
	
	private void readMessage(String message) {
		
		if(isLog) {
			if(message.length() > maxLogLength){
				logger.debug("readMessage: " + message.substring(0 , maxLogLength) + ".. +" + message.length() + "characters.");
			} else {
				logger.debug("readMessage: " + message);
			}
		}
	
		
		char packageType = message.charAt(0);
		message = message.substring(1);
		
		String className ;
		String messageCode ;
		
		if(packageType == DEFAULT_PACKAGE){
			
			int idx = message.indexOf(",");
			messageCode = message.substring(0, idx);
			className =  defaultPackageName +"."+messageCode;
			message = message.substring(idx+1);
		}else{
			
			int idx = message.indexOf(",");
			String packageName = message.substring(0, idx);
			
			int next = idx+1;
			
			idx = message.indexOf(",", next);
			messageCode = message.substring(next, idx);
			className =  packageName +"."+messageCode;
			message = message.substring(idx+1);
		}	
			
		try {
			Class<?> apiMessageClass = Class.forName(className);
			//noinspection deprecation
			ApiMessage apiMessage = (ApiMessage)apiMessageClass.newInstance();
			apiMessage.setCommunication(this);
			apiMessage.receive(message);
		} catch (Exception e1) {
			sendMessage(ExceptionUtil.getStackTrace(e1));
			ExceptionUtil.exception(e1, logger, exceptionHandler);

		}
			
			
				
	}
	
	/**
	 * 메시지전달
	 * null이나 빈값이 들어오면 전달하지 않는다.
	 * @param message 메시지를 전달한 
	 * @throws IOException 
	 */
	public boolean sendMessage(String message){
		if(!sendToReceive.isConnect()){
			logger.error("message send Fail(Not Connected) : " + message);
			return false;
		}
		if(isLog) {
			if(message.length() > maxLogLength){
				logger.debug("sendMessage: " + message.substring(0 , maxLogLength) + ".. +" + message.length() + "characters.");
			} else {
				logger.debug("sendMessage: " + message);
				
			}
		}
		return sendToReceive.send(message);
	}
	
	
	
	
	/**
	 * 연결종료
	 */
	public void disConnect() {
		flag = false;
		sendToReceive.disConnect();
		
		
	}

}