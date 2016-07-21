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
package com.blackducksoftware.integration.suite.sdk.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.InvalidParameterException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.slf4j.Logger;

public class SdkLoggerUnitTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private Logger getMockedLogger() {
        return Mockito.mock(Logger.class);
    }

    private String concatenate(String txt, Throwable t) {
        StringWriter sw = new StringWriter();
        if (t != null) {
            t.printStackTrace(new PrintWriter(sw));
        }
        return txt + System.getProperty("line.separator") + sw;
    }

    @Test(expected = InvalidParameterException.class)
    public void testNullLoggerInt() {
        new SdkLogger((Logger) null);
    }

    @Test
    public void testLoggerMessageAllLevels() {
        Logger loggerInt = getMockedLogger();

        SdkLogger logger = new SdkLogger(loggerInt);
        logger.info("testInfo");
        logger.debug("testDebug");
        logger.error("testError");
        logger.warn("testWarn");
        logger.trace("testTrace");

        Mockito.verify(loggerInt).info("testInfo");
        Mockito.verify(loggerInt).debug("testDebug");
        Mockito.verify(loggerInt).error("testError");
        Mockito.verify(loggerInt).warn("testWarn");
        Mockito.verify(loggerInt).trace("testTrace");
    }

    @Test
    public void testLoggerExceptionAllLevels() {
        Logger loggerInt = getMockedLogger();

        SdkLogger logger = new SdkLogger(loggerInt);
        Exception testException = new Exception("testException");
        logger.error(testException);
        logger.trace(testException);

        Mockito.verify(loggerInt).error(concatenate(testException.getMessage(), testException));
        Mockito.verify(loggerInt).trace(concatenate(testException.getMessage(), testException));
    }

    @Test
    public void testLoggerMessageAndExceptionAllLevels() {
        Logger loggerInt = getMockedLogger();

        SdkLogger logger = new SdkLogger(loggerInt);
        Exception testException = new Exception("testException");
        logger.error("testErrorWithException", testException);
        logger.debug("testDebugWithException", testException);
        logger.trace("testTraceWithException", testException);

        Mockito.verify(loggerInt).error(concatenate("testErrorWithException", testException));
        Mockito.verify(loggerInt).debug(concatenate("testDebugWithException", testException));
        Mockito.verify(loggerInt).trace(concatenate("testTraceWithException", testException));
    }
}
