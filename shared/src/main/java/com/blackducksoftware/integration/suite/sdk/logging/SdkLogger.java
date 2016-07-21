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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This logger class is meant to simplify the logging the Suite Sdk Integration project. This logger should only be used
 * in this project.
 * 
 * @author jrichard
 * 
 */
public class SdkLogger implements IntLogger {

    private static final String NL = System.getProperty("line.separator");

    private final Logger logger;

    private LogLevel level = LogLevel.INFO; // default is INFO

    /**
     * Creates the SdkLogger with either a implementation of IntLogger or with an Slf4j logger. If both are provided the
     * implementation of IntLogger will be used when logging messages.
     * 
     * @param intLogger
     *            IntLogger, Integration logging interface.
     * 
     * @param logger
     *            Logger, slf4j Logger
     */
    public SdkLogger(Logger logger) throws InvalidParameterException {
        if (logger == null) {
            throw new InvalidParameterException("Paramter 'logger' can't be null");
        }
        this.logger = logger;
    }

    public SdkLogger(Class<?> clazz) {
        logger = LoggerFactory.getLogger(clazz);
    }

    // public void setSlf4jLogger(Logger logger) {
    // if (logger != null) {
    // this.logger = logger;
    // }
    // }
    //
    // public void setIntLogger(IntLogger intLogger) {
    // if (intLogger != null) {
    // this.intLogger = intLogger;
    // }
    // }

    public void setLogLevel(LogLevel level) {
        this.level = level;
    }

    public LogLevel getLogLevel() {
        return level;
    }

    /**
     * Logs the message provided to the Info level. Will use the IntLogger if one is provided, else it will use
     * the slf4j logger
     * 
     * @param txt
     *            String
     */
    public void info(String txt) {
        logger.info(txt);
    }

    /**
     * Logs the message provided to the Debug level. Will use the IntLogger if one is provided, else it will
     * use the slf4j logger
     * 
     * @param txt
     *            String
     */
    public void debug(String txt) {
        logger.debug(txt);
    }

    /**
     * Logs the message and the throwable provided to the Trace level. Will use the IntLogger if one is provided, else
     * it will use the slf4j logger
     * 
     * @param txt
     *            String
     * @param t
     *            Throwable
     */
    public void debug(String txt, Throwable t) {
        logger.debug(logException(txt, t));
    }

    /**
     * Logs the message provided to the Error level and prints out the stack trace. Will use the IntLogger if
     * one is provided, else it will use the slf4j logger
     * 
     * @param txt
     *            String
     * @param t
     *            Throwable
     */
    public void error(String txt, Throwable t) {
        logger.error(logException(txt, t));
    }

    /**
     * Logs the message provided to the Error level. Will use the IntLogger if
     * one is provided, else it will use the slf4j logger
     * 
     * @param txt
     *            String
     * 
     */
    public void error(String txt) {
        logger.error(txt);
    }

    /**
     * Logs the message provided to the Warn level. Will use the IntLogger if one is provided, else it will use
     * the slf4j logger
     * 
     * @param txt
     *            String
     */
    public void warn(String txt) {
        logger.warn(txt);
    }

    /**
     * Logs the message provided to the Trace level. Will use the IntLogger if one is provided, else it will
     * use the slf4j logger
     * 
     * @param txt
     *            String
     */
    public void trace(String txt) {
        logger.trace(txt);
    }

    /**
     * Prints out the stack trace. Will use the IntLogger if
     * one is provided, else it will use the slf4j logger
     * 
     * @param t
     *            Throwable
     */
    public void error(Throwable t) {
        logger.error(logException(t.getMessage(), t));
    }

    /**
     * Logs the message and the throwable provided to the Trace level. Will use the IntLogger if one is provided, else
     * it will use the slf4j logger
     * 
     * @param txt
     *            String
     * @param t
     *            Throwable
     */
    public void trace(String txt, Throwable t) {
        logger.trace(logException(txt, t));
    }

    /**
     * Prints out the stack trace. Will use the IntLogger if
     * one is provided, else it will use the slf4j logger
     * 
     * @param t
     *            Throwable
     */
    public void trace(Throwable t) {
        logger.trace(logException(t.getMessage(), t));
    }

    private String logException(String txt, Throwable t) {
        StringWriter sw = new StringWriter();
        sw.append(txt);
        sw.append(NL);
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

}
