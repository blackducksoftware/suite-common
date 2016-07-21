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

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

public class PasswordDecrypterTest {

    private static Properties encryptedUserPassword = null;

    @BeforeClass
    public static void init() throws URISyntaxException, IOException {

        encryptedUserPassword = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = classLoader.getResourceAsStream("encryptedPasswordFile.txt");
        try {
            encryptedUserPassword.load(is);
        } catch (IOException e) {
            System.err.println("reading encryptedPasswordFile failed!");
        }
    }

    @Test
    public void testPasswordDecryption() throws Exception {

        assertEquals("super", PasswordDecrypter.decrypt(encryptedUserPassword.getProperty("super")));
    }

    @Test
    public void testPasswordDecryptionAgain() throws Exception {

        assertEquals("testing", PasswordDecrypter.decrypt(encryptedUserPassword.getProperty("test@blackducksoftware.com")));
    }

    @Test
    public void testPasswordDecryptionEmptyKey() throws Exception {

        assertNull(PasswordDecrypter.decrypt(""));
    }

    @Test
    public void testPasswordDecryptionNullKey() throws Exception {

        assertNull(PasswordDecrypter.decrypt(null));
    }

}
