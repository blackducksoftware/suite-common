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
package com.blackducksoftware.integration.suite.encryption;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;

public final class LicenseEncrypter {

    private static Logger logger = LoggerFactory.getLogger(LicenseEncrypter.class);

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private static final String EMBEDDED_SUN_KEY_FILE = "/License-Sun-Key.jceks";

    private static final String EMBEDDED_IBM_KEY_FILE = "/License-IBM-Key.jceks";

    private LicenseEncrypter() {

    }

    public static void main(String[] args)
    {
        try {
            if (args == null || args.length == 0) {
                throw new IllegalArgumentException("Please provide a licenseName.");
            } else if (args.length > 1) {
                throw new IllegalArgumentException("Please ONLY provide a licenseName.");
            }

            // needs to be at least 8 characters
            char[] keyPass = { 'l', 'i', 'c', 'e', 'n', 's', 'e', 'K', 'e', 'y', 'p', 'a', 's', 's', 'b', 'l', 'a', 'c', 'k', 'd', 'u', 'c', 'k', '1', '2', '3' };
            Key key;
            key = getKey(null, keyPass);
            String encryptedLicense = null;
            if (key == null) {
                logError("The encryption key is null", null);
                return;
            } else {
                encryptedLicense = encrypt(key, args[0]);
            }
            if (encryptedLicense == null) {
                logError("The encrypted License is null", null);
                return;
            } else {
                logMessage(encryptedLicense);
            }

        } catch (IllegalArgumentException e) {
            if (args != null) {
                logMessage("# of arguments = " + args.length);
            }
            logError(e.getMessage(), e);
        }
    }

    /**
     * Encrypts the licenseKey provided. Returns the encrypted or
     * version.
     * 
     * @param keyToUse
     *            Key to use for the encryption
     * @param licenseKey
     *            String to be encrypted
     * 
     * @return String encrypted licenseKey.
     */
    private static String encrypt(Key keyToUse, String licenseKey)
    {
        String reconstitutedString = null;
        try {
            byte[] buffer = null;
            byte[] bytes = null;
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            bytes = licenseKey.getBytes(UTF8);
            // // org.apache.xml.security.Init.init();

            cipher.init(Cipher.ENCRYPT_MODE, keyToUse);
            bytes = Arrays.copyOf(bytes, 64);
            buffer = cipher.doFinal(bytes);
            buffer = Arrays.copyOf(buffer, 64);

            reconstitutedString = new String(Base64.encodeBase64(buffer), UTF8).trim();

        } catch (Exception e) {
            logError(e.getMessage(), e);
        }
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
    private static Key getKey(InputStream is, char[] keypass) {
        Key key = null;
        if (is == null) { // Get default cipher keys that are provided
            // attempts to retrieve the Sun cipher key, if that doesnt work then it tries to get the Ibm key
            InputStream defaultIs = null;
            try {
                defaultIs = LicenseEncrypter.class.getResourceAsStream(EMBEDDED_SUN_KEY_FILE);
                try {
                    KeyStore keystore = KeyStore.getInstance("JCEKS");
                    keystore.load(defaultIs, keypass);
                    key = keystore.getKey("keyStore", keypass);
                    return key;
                } finally {
                    if (defaultIs != null) {
                        defaultIs.close();

                    }
                }
            } catch (Exception e) {
                try {
                    defaultIs = LicenseEncrypter.class.getResourceAsStream(EMBEDDED_IBM_KEY_FILE);
                    try {
                        KeyStore keystore = KeyStore.getInstance("JCEKS");
                        keystore.load(defaultIs, keypass);
                        key = keystore.getKey("keyStore", keypass);
                        return key;
                    } finally {
                        if (defaultIs != null) {
                            defaultIs.close();

                        }
                    }
                } catch (Exception e1) {
                    logError("Failed to retrieve the encryption Key.", e);
                }
            }
        } // else {
          // TODO get the key specified
          // }
        return null;
    }

    /**
     * Generates a new key. Should be used manually and only when creating a new key is necessarry.
     * 
     * 
     * @param keypass
     *            char[] with the keypass that will gain access to the key
     *            (currently hard coded in)
     * @throws IOException
     */
    @SuppressWarnings("unused")
    private static Key setKey(char[] keypass, File keyFile) {
        // If the current keys are replaced then we will not be able to decrypt passwords that were encrypted with the
        // old keys
        Key key = null;
        FileOutputStream output = null;
        try {

            output = new FileOutputStream(keyFile.getCanonicalPath());
            key = KeyGenerator.getInstance("DES").generateKey();
            KeyStore keystore = KeyStore.getInstance("JCEKS");
            keystore.load(null, null);
            keystore.setKeyEntry("keyStore", key, keypass, null);
            keystore.store(output, keypass);

        } catch (Exception e) {
            logError("Problem setting the encryption Key. ", e);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    logError("Problem closing the OutputStream for file at : " + keyFile.getPath(), e);
                }
            }
        }
        return key;
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
            System.err.println(txt);
            if (e != null) {
                // If no logger can be found print to System error
                System.err.println(sw.toString());
            }
        }
    }

    private static void logMessage(String txt) {
        if (logger != null && !(logger instanceof NOPLogger)) {
            logger.info(txt);
        } else {
            // If no logger can be found print to System out
            System.out.println(txt);
        }
    }

}
