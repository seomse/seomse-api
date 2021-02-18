/*
 * Copyright (C) 2021 Seomse Inc.
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

package com.seomse.api.example;

import com.seomse.api.ApiMessage;
import com.seomse.api.Messages;
import com.seomse.commons.utils.ExceptionUtil;

/**
 * 대기사간 기능 테스트 용 api
 * @author macle
 */
public class WaitingTestApi extends ApiMessage {

    @Override
    public void receive(String message) {
        try {

            //waiting test 이므로 10초간 sleep
            //호출하는족에서는 3초만 기다렸다 오류표시
            Thread.sleep(10000L);
            sendMessage(Messages.SUCCESS);
        }catch(Exception e) {
            sendMessage(Messages.FAIL + ExceptionUtil.getStackTrace(e));
        }
    }

}

