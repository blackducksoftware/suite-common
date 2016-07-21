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

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.ws.security.WSPasswordCallback;

public class ProgrammedEncryptedPasswordCallback extends ProgrammedPasswordCallback {

    public ProgrammedEncryptedPasswordCallback() {
        super();

    }

    @Override
    protected void setPassword(WSPasswordCallback pc, String username) throws IOException {

        try {

            pc.setPassword(PasswordDecrypter.decrypt(getPasswordCache().get(username)));

        } catch (InvalidKeyException e) {
            throw new IOException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new IOException(e);
        } catch (NoSuchPaddingException e) {
            throw new IOException(e);
        } catch (IllegalBlockSizeException e) {
            throw new IOException(e);
        } catch (BadPaddingException e) {
            throw new IOException(e);
        }
    }
}
