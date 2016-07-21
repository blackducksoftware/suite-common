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

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ConnectionTimeTranslatorTest {

    private static final Long CONNECTION_MINUTE = 1L * 1000 * 60;

    private static final Long CONNECTION_SECOND = 1L * 1000;

    private static final Long CONNECTION_MILLI = 1L;

    private static final Long LONG_CONNECTION = CONNECTION_MINUTE * 60 + CONNECTION_MINUTE + CONNECTION_SECOND + CONNECTION_MILLI;

    // private static final Long EXECUTION_MINUTE = 1L * 1000 * 1000 * 60;
    //
    // private static final Long EXECUTION_SECOND = 1L * 1000 * 1000;
    //
    // private static final Long EXECUTION_MILLI = 1L * 1000;
    //
    // private static final Long EXECUTION_MICRO = 1L;

    // private static final Long LONG_EXECUTION = EXECUTION_MINUTE * 60 + EXECUTION_MINUTE + EXECUTION_SECOND +
    // EXECUTION_MILLI + EXECUTION_MICRO;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testGetLoggableConnectionTimeoutIndefinite() {
        assertEquals("INDEFINITE", ConnectionTimeTranslator.getLoggableConnectionTimeout(0L));
    }

    @Test
    public void testGetLoggableConnectionTimeoutMillis() {
        assertEquals("1ms", ConnectionTimeTranslator.getLoggableConnectionTimeout(CONNECTION_MILLI));
    }

    @Test
    public void testGetLoggableConnectionTimeoutSeconds() {
        assertEquals("1s", ConnectionTimeTranslator.getLoggableConnectionTimeout(CONNECTION_SECOND));
    }

    @Test
    public void testGetLoggableConnectionTimeoutMinutes() {
        assertEquals("1m", ConnectionTimeTranslator.getLoggableConnectionTimeout(CONNECTION_MINUTE));
    }

    @Test
    public void testGetLoggableConnectionTimeoutSecondsAndMillis() {
        assertEquals("1s 1ms", ConnectionTimeTranslator.getLoggableConnectionTimeout(CONNECTION_SECOND + CONNECTION_MILLI));
    }

    @Test
    public void testGetLoggableConnectionTimeoutMinutesAndMillis() {
        assertEquals("1m 1ms", ConnectionTimeTranslator.getLoggableConnectionTimeout(CONNECTION_MINUTE + CONNECTION_MILLI));
    }

    @Test
    public void testGetLoggableConnectionTimeoutMinutesAndSeconds() {
        assertEquals("1m 1s", ConnectionTimeTranslator.getLoggableConnectionTimeout(CONNECTION_MINUTE + CONNECTION_SECOND));
    }

    @Test
    public void testGetLoggableConnectionTimeoutMinutesAndSecondsAndMillis() {
        assertEquals("1m 1s 1ms", ConnectionTimeTranslator.getLoggableConnectionTimeout(CONNECTION_MINUTE + CONNECTION_SECOND + CONNECTION_MILLI));
    }

    @Test
    public void testGetLoggableConnectionTimeoutLongConnection() {
        assertEquals("61m 1s 1ms", ConnectionTimeTranslator.getLoggableConnectionTimeout(LONG_CONNECTION));
    }

}
