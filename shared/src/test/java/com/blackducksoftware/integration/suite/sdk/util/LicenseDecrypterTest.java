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
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.BeforeClass;
import org.junit.Test;

public class LicenseDecrypterTest {

    private static String line = null;

    @BeforeClass
    public static void init() throws URISyntaxException, IOException {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File licenseTextFile = new File(classLoader.getResource("LicenseKey.txt").toURI());

        BufferedReader in = new BufferedReader(new FileReader(licenseTextFile));
        line = in.readLine();
        in.close();
    }

    @Test
    public void testLicenseDecryption() throws Exception {

        assertEquals("TestLicense", LicenseDecrypter.decrypt(line));
    }

    @Test
    public void testLicenseDecryptionEmptyKey() throws Exception {

        assertNull(LicenseDecrypter.decrypt(""));
    }

    @Test
    public void testLicenseDecryptionNullKey() throws Exception {

        assertNull(LicenseDecrypter.decrypt(null));
    }

}
