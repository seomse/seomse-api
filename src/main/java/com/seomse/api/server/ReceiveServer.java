


package com.seomse.api.server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seomse.commons.handler.ExceptionHandler;
import com.seomse.commons.utils.ExceptionUtil;
/**
 * <pre>
 *  파 일 명 : ReceiveServer.java
 *  설    명 : 문자열 받는 서버
 *             문자열이 대용량일경우 빠른속도로 받기위해 사용
 *
 *  작 성 자 : macle
 *  작 성 일 : 2018.04
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2018 by ㈜섬세한사람들. All right reserved.
 */
public class ReceiveServer extends Thread{
	private static final Logger logger = LoggerFactory.getLogger(ReceiveServer.class);
	
	private ServerSocket serverSocket = null;
	private boolean isService = true;
	
	private boolean isEnd = false;
	
	private int port;
	private InetAddress inetAddress = null;
	
	private int bufferSize = 10240;
	
	private ExceptionHandler exceptionHandler;
	
	/**
	 * 생성자
	 */
	public ReceiveServer(int port){
		this.port = port;
	}

	/**
	 * 예외 핸들러설정
	 * @param exceptionHandler
	 */
	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
	
	/**
	 * bufferSize 얻기
	 * @return
	 */
	public int getBufferSize() {
		return bufferSize;
	}


	/**
	 * bufferSize 설정 기본 10240
	 * @param bufferSize
	 */
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	/**
	 * inetAddress 설정
	 * @param inetAddress
	 */
	public void setInetAddress(InetAddress inetAddress) {
		this.inetAddress = inetAddress;
	}
	
	
	@Override
	public void run(){
	
		try{
			if(serverSocket == null){
				
				if(inetAddress == null){
					serverSocket = new ServerSocket(port);
				
				}else{
					serverSocket = new ServerSocket(port, 50, inetAddress );
					
				}
			}
			
			while(isService){
				
				try{
					Socket socket = serverSocket.accept();	
					ReceiveCommunication receiveCommunication = new ReceiveCommunication(socket, bufferSize);
					receiveCommunication.setExceptionHandler(exceptionHandler);
					receiveCommunication.start();
				}catch(Exception e){
					ExceptionUtil.exception(e, logger, exceptionHandler);
				}
			}
			
		}catch(Exception e){
			ExceptionUtil.exception(e, logger, exceptionHandler);
		}
		
		isEnd = true;
		
	}
	
	/**
	 * 서비스 종료 
	 */
	public void stopService(){
		isService = false;
		try{
			if(serverSocket != null){
				serverSocket.close();
				serverSocket = null;
			}
		}catch(Exception e){}
	}

	
	/**
	 * 종료여부얻기
	 * @return
	 */
	public boolean isEnd() {
		return isEnd;
	}
}
