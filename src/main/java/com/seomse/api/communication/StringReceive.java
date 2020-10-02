/*
 * Copyright (C) 2020 Seomse Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.seomse.api.communication;


import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * 문자열 받기
 * 대용량 메시지를 전달받기 위해 개발함
 * @author macle
 */
public class StringReceive {
	private Socket socket ;
	private InputStreamReader reader ;
	private final char [] charBuffer;

	/**
	 * 생성자
	 * @param socket Socket
	 * @param bufSize int receive buffer size
	 * @throws IOException IOException
	 */
	public StringReceive(Socket socket, int bufSize) throws IOException{
		this.socket =socket;
		reader  = new InputStreamReader(socket.getInputStream(), CommunicationDefault.CHAR_SET);
		charBuffer = new char[bufSize];
	}
	

	/**
	 * 메시지 얻기
	 * @return Socket receive message
	 * @throws IOException IOException
	 */
	public String receive() throws IOException{
	
		int i = reader.read(charBuffer);
		if( i == -1){
			return null;
		}
		
		
		return new String(charBuffer,0,i);
	}
	
	/**
	 * 연결 해제
	 */
	public void disConnect(){

		//noinspection CatchMayIgnoreException
		try{
			if(reader != null ){
				reader.close();	
			}
		}catch(Exception e){}

		reader = null;
		//noinspection CatchMayIgnoreException
		try{
			if(socket != null && !socket.isClosed() ){
				socket.close();
			}
		}catch(Exception e){}
		socket = null;
	}
	
}
