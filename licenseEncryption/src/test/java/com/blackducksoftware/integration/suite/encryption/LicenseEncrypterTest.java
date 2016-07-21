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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public final class LicenseEncrypterTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private static PrintStream orgStream = null;

    private static PrintStream orgErrStream = null;

    private static ByteArrayOutputStream byteOutput = null;

    @BeforeClass
    public static void init() throws IOException {
        orgStream = System.out;
        orgErrStream = System.err;
        byteOutput = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteOutput);
        System.setOut(ps);
        System.setErr(ps);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        System.setOut(orgStream);
        System.setErr(orgErrStream);

        if (byteOutput != null) {
            byteOutput.close();
        }
        byteOutput = null;
    }

    @Before
    public void testSetup() throws IOException {
        // reset the output stream for each test
        byteOutput.reset();
        assertTrue(byteOutput.size() == 0);
    }

    @Test
    public void testMainNullArgs() throws Exception {
        LicenseEncrypter.main(null);
        String output = byteOutput.toString("UTF-8");
        String split[] = null;
        split = output.split("\\n");

        assertTrue("Expected #lines > 0 - Output: " + output, split.length > 1);
        assertEquals(output, "Please provide a licenseName.", split[0]);
    }

    @Test
    public void testMainNoArgs() throws Exception {
        LicenseEncrypter.main(new String[0]);
        String output = byteOutput.toString("UTF-8");
        String split[] = null;
        split = output.split("\\n");

        assertTrue("Expected #lines > 0 - Output: " + output, split.length > 1);
        assertEquals(output, "# of arguments = 0", split[0]);
        assertEquals(output, "Please provide a licenseName.", split[1]);
    }

    @Test
    public void testMainExtraArgs() throws Exception {
        String[] args = new String[2];
        args[0] = "TestLicense";
        args[1] = "SecondTestLicense";
        LicenseEncrypter.main(args);

        String output = byteOutput.toString("UTF-8");
        String split[] = null;
        split = output.split("\\n");

        assertTrue("Expected #lines > 0 - Output: " + output, split.length > 1);
        assertEquals(output, "# of arguments = 2", split[0]);
        assertEquals(output, "Please ONLY provide a licenseName.", split[1]);
    }

    @Test
    public void testMainEncryptLicense() throws Exception {
        String[] args = new String[1];
        args[0] = "TestLicense";
        LicenseEncrypter.main(args);
        String output = byteOutput.toString("UTF-8");
        String split[] = null;
        split = output.split("\\n");
        assertEquals("Expect single line: " + output, 1, split.length);
        assertEquals("EwNNRLEBfJAUzjtXKt1iLjmV8FXR6KwsOZXwVdHorCw5lfBV0eisLDmV8FXR6KwsOZXwVdHorCw5lfBV0eisLA==", split[0]);
    }

}
