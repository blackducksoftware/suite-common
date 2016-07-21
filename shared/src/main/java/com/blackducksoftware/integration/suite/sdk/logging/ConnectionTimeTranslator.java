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

import java.util.concurrent.TimeUnit;

public final class ConnectionTimeTranslator {

    private ConnectionTimeTranslator() {

    }

    /**
     * Translates the Connection time from a Long to a human readable String
     * 
     * @param actual
     *            long, the actual time in Milliseconds
     * 
     * @return String
     */
    public static String getLoggableConnectionTimeout(long actual) {
        if (actual == 0L) {
            return "INDEFINITE";
        } else {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(actual);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(actual) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(actual));
            long milliSeconds = actual - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(actual));
            StringBuilder time = new StringBuilder();
            if (minutes != 0L) {
                time.append(String.format("%dm", minutes));
            }
            if (seconds != 0L) {
                if (time.length() > 0) {
                    time.append(" ");
                }
                time.append(String.format("%ds", seconds));
            }
            if (milliSeconds != 0L) {
                if (time.length() > 0) {
                    time.append(" ");
                }
                time.append(String.format("%dms", milliSeconds));
            }
            return time.toString();
        }
    }
}
