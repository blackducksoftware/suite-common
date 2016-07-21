/*******************************************************************************
 * Copyright (C) 2016 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package com.blackducksoftware.integration.suite.sdk.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

public class ProgrammedPasswordCallback implements CallbackHandler {

    private final Map<String, String> passwordCache = new HashMap<String, String>();

    protected Map<String, String> getPasswordCache() {
        return passwordCache;
    }

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

        WSPasswordCallback pc = null;
        try {
            pc = (WSPasswordCallback) callbacks[0];
        } catch (ClassCastException ce) {
            UnsupportedCallbackException exception = new UnsupportedCallbackException(pc, "callback method is not of type "
                    + WSPasswordCallback.class.getName());
            exception.setStackTrace(ce.getStackTrace()); // stack trace is preserved
            throw exception;
        }
        String username = pc.getIdentifier();
        if (passwordCache.get(username) == null) {
            throw new UnsupportedCallbackException(pc,
                    "Password not set in client. Call static method "
                            + ProgrammedPasswordCallback.class.getName()
                            + ".addUserNameAndPassword(\"<your username>\", \"<your password>\")");
        }
        setPassword(pc, username);

    }

    protected void setPassword(WSPasswordCallback pc, String username) throws IOException {
        pc.setPassword(passwordCache.get(username));
    }

    public void addUserNameAndPassword(String username, String password) {
        passwordCache.put(username, password);
    }

}
