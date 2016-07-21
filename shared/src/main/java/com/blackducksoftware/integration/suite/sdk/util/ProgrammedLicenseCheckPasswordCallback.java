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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.ws.security.WSPasswordCallback;

public class ProgrammedLicenseCheckPasswordCallback extends ProgrammedPasswordCallback {

    private String license = "";

    @SuppressWarnings("PMD.EmptyCatchBlock")
    public ProgrammedLicenseCheckPasswordCallback() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException {
        super();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = classLoader.getResourceAsStream("BDLicenseKey");
        String licenseKey = null;
        if (is != null) {
            BufferedReader br = null;
            InputStreamReader ir = null;
            try {
                ir = new InputStreamReader(is);
                br = new BufferedReader(ir);
                try {
                    if (br != null) {
                        licenseKey = br.readLine();
                    }
                } catch (IOException e) {
                    System.err.println("reading the License failed!");
                }
            } finally {
                if (br != null) {
                    try {
                        br.close();
                        if (ir != null) {
                            ir.close();
                        }
                    } catch (IOException e) {
                        // NOPMD - ignore any IO exception at this point
                    }
                }
            }
        }
        if (licenseKey != null) {
            license = LicenseDecrypter.decrypt(licenseKey);
        }
    }

    @Override
    protected void setPassword(WSPasswordCallback pc, String username) {
        // String debugLicense = license;
        // set the password for our message.
        if (StringUtils.isEmpty(license)) {
            pc.setPassword(getPasswordCache().get(username));
        } else {
            pc.setPassword(license + "#" + getPasswordCache().get(username));
            // pc.setPassword("DYNAM" + license + "#" + getPasswordCache().get(username));
        }
    }
}
