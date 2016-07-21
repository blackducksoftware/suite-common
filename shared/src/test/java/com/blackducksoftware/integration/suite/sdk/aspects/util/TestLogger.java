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
package com.blackducksoftware.integration.suite.sdk.aspects.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.blackducksoftware.integration.suite.sdk.logging.SdkLogger;

public class TestLogger extends SdkLogger {

    public TestLogger(Class<?> clazz) {
        super(clazz);
    }

    public TestLogger(Logger logger) {
        super(logger);
    }

    private List<String> testOutput = new ArrayList<String>();

    private List<Throwable> testExceptions = new ArrayList<Throwable>();

    public void clear() {
        testOutput.clear();
        testExceptions.clear();
    }

    @Override
    public void info(String txt) {
        System.out.println(txt);
        testOutput.add(txt);
    }

    @Override
    public void error(String txt, Throwable e) {
        System.out.println(txt);
        System.out.println("EXCEPTION : " + e.toString());
        testOutput.add(txt);
        testExceptions.add(e);
    }

    @Override
    public void error(String txt) {
        System.out.println(txt);
        testOutput.add(txt);
    }

    @Override
    public void warn(String txt) {
        System.out.println(txt);
        testOutput.add(txt);
    }

    @Override
    public void trace(String txt) {
        System.out.println(txt);
        testOutput.add(txt);
    }

    @Override
    public void debug(String txt) {
        System.out.println(txt);
        testOutput.add(txt);
    }

    public List<String> getTestOutput() {
        return testOutput;
    }

    public List<Throwable> getTestExceptions() {

        return testExceptions;
    }

    @Override
    public void error(Throwable e) {
        if (e.getCause() != null) {
            System.out.println("EXCEPTION : " + e.getCause().toString());
        } else {
            System.out.println("EXCEPTION : " + e.toString());
        }
        testExceptions.add(e);
    }

    @Override
    public void trace(String txt, Throwable e) {
        System.out.println(txt);
        System.out.println("EXCEPTION : " + e.toString());
        testOutput.add(txt);
        testExceptions.add(e);

    }
}
