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

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;

public final class LicenseDecrypter {
    private static Logger logger = LoggerFactory.getLogger(LicenseDecrypter.class);

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private static final String EMBEDDED_SUN_KEY_FILE = "/License-Sun-Key.jceks";

    private static final String EMBEDDED_IBM_KEY_FILE = "/License-IBM-Key.jceks";

    private LicenseDecrypter() {

    }

    /**
     * Decrypts the licenseKey provided. Returns the decrypted version.
     * 
     * @param licenseKey
     *            String with the licenseKey to decrypt
     * 
     * @return String decrypted licenseKey.
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    protected static String decrypt(String licenseKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException
    {
        if (StringUtils.isEmpty(licenseKey)) {
            return null;
        }
        // needs to be at least 8 characters
        char[] keyPass = { 'l', 'i', 'c', 'e', 'n', 's', 'e', 'K', 'e', 'y', 'p', 'a', 's', 's', 'b', 'l', 'a', 'c', 'k', 'd', 'u', 'c', 'k', '1', '2', '3' };
        Key key;
        key = getKey(keyPass);

        if (key == null) {
            logError("The license decryption key is null", null);
            return null;
        }
        String reconstitutedString = null;
        byte[] buffer = null;
        byte[] bytes = null;
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");

        bytes = licenseKey.getBytes(UTF8);
        // // org.apache.xml.security.Init.init();

        cipher.init(Cipher.DECRYPT_MODE, key);
        bytes = Arrays.copyOf(bytes, 64);
        buffer = cipher.doFinal(Base64.decodeBase64(bytes));
        buffer = Arrays.copyOf(buffer, 64);

        reconstitutedString = new String(buffer, UTF8).trim();
        return reconstitutedString;
    }

    /**
     * Retrieves the cipher Key.
     * 
     * @param instream
     *            InputStream to get the Key from. If null it will use the default cipher keys provided.
     * @param keypass
     *            char[] with the key password that will gain access to the key
     *            (currently hard coded in)
     * @return Key
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws UnrecoverableKeyException
     * @throws CertificateException
     */
    private static Key getKey(char[] keypass) {
        Key key = null;
        InputStream is = null;
        // attempts to retrieve the Sun cipher key, if that doesnt work then it tries to get the Ibm key
        try {
            is = PasswordDecrypter.class.getResourceAsStream(EMBEDDED_SUN_KEY_FILE);
            try {
                KeyStore keystore = KeyStore.getInstance("JCEKS");
                keystore.load(is, keypass);
                key = keystore.getKey("keyStore", keypass);
                return key;
            } finally {
                if (is != null) {
                    is.close();

                }
            }
        } catch (Exception e) {
            logError("Retrieving the key from '" + EMBEDDED_SUN_KEY_FILE + "' FAILED.  Trying the '" + EMBEDDED_IBM_KEY_FILE + "'", e);
            try {

                is = PasswordDecrypter.class.getResourceAsStream(EMBEDDED_IBM_KEY_FILE);
                try {
                    KeyStore keystore = KeyStore.getInstance("JCEKS");
                    keystore.load(is, keypass);
                    key = keystore.getKey("keyStore", keypass);
                    return key;
                } finally {
                    if (is != null) {
                        is.close();

                    }
                }
            } catch (Exception e1) {
                logError("Retrieving the key from '" + EMBEDDED_IBM_KEY_FILE + "' FAILED", e);
            }
        }
        return null;
    }

    private static void logError(String txt, Throwable e) {
        StringWriter sw = new StringWriter();
        if (e != null) {
            e.printStackTrace(new PrintWriter(sw));
        }
        if (logger != null && !(logger instanceof NOPLogger)) {
            logger.error(txt);
            if (e != null) {
                logger.error(sw.toString());
            }
        } else {
            // If no logger can be found print to System error
            System.err.println(txt);
            if (e != null) {
                System.err.println(sw.toString());
            }
        }
    }
}
