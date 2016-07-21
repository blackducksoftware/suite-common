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

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ProgrammedPasswordCallbackTest {

    private String passwordReturn = null;

    private WSPasswordCallback getMockedWSPasswordCallback() {
        WSPasswordCallback mockWSPasswordCallback = Mockito.mock(WSPasswordCallback.class);

        Mockito.doReturn("testUser").when(mockWSPasswordCallback).getIdentifier();

        Mockito.doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                passwordReturn = (String) args[0];
                return null;
            }
        }).when(mockWSPasswordCallback).setPassword(Mockito.anyString());

        return mockWSPasswordCallback;
    }

    @Test
    public void testPasswordCallback() throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException, IOException,
            UnsupportedCallbackException {
        ProgrammedPasswordCallback programmedCallback = new ProgrammedPasswordCallback();
        programmedCallback.addUserNameAndPassword("testUser", "testPassword");

        Field passwordCacheField = ProgrammedPasswordCallback.class.getDeclaredField("passwordCache");
        passwordCacheField.setAccessible(true);
        Map<String, String> passwordCache = (Map<String, String>) passwordCacheField.get(programmedCallback);

        assertEquals("testPassword", passwordCache.get("testUser"));

        WSPasswordCallback callback = getMockedWSPasswordCallback();
        Callback[] callbacks = new Callback[1];
        callbacks[0] = callback;

        programmedCallback.handle(callbacks);

        assertEquals("testPassword", passwordReturn);

        Mockito.verify(callback, Mockito.times(1)).getIdentifier();
        Mockito.verify(callback, Mockito.times(1)).setPassword(Mockito.anyString());
    }
}
