package com.blackducksoftware.integration.suite.encryption;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PasswordEncrypterTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private static PrintStream orgStream = null;

    private static PrintStream orgErrStream = null;

    private ByteArrayOutputStream byteOutput = null;

    @BeforeClass
    public static void init() throws IOException {

        orgStream = System.out;
        orgErrStream = System.err;
    }

    @AfterClass
    public static void tearDown() throws Exception {
        System.setOut(orgStream);
        System.setErr(orgErrStream);
    }

    @Before
    public void testSetup() throws IOException {
        byteOutput = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteOutput);
        System.setOut(ps);
        System.setErr(ps);
    }

    @After
    public void testTearDown() throws Exception {
        byteOutput = null;
    }

    @Test
    public void testMainNullArgsSL4jOutput() throws Exception {
        // SLF4j messages are output complaining about the lack of a binding
        // Run the main method with null to print them out and get rid of them
        PasswordEncrypter.main(null);
    }

    @Test
    public void testMainNullArgs() throws Exception {
        PasswordEncrypter.main(null);
        String output = byteOutput.toString("UTF-8");
        String split[] = null;
        split = output.split("\\n");

        assertEquals("Please provide a UserName and Password, UserName should be provided first.", split[1]);
    }

    @Test
    public void testMainNoArgs() throws Exception {
        PasswordEncrypter.main(new String[0]);
        String output = byteOutput.toString("UTF-8");
        String split[] = null;
        split = output.split("\\n");
        assertEquals("# of arguments = 0", split[0]);
        assertEquals("Please provide a UserName and Password, UserName should be provided first.", split[2]);
    }

    @Test
    public void testMainUserNameOnly() throws Exception {
        String[] args = new String[1];
        args[0] = "UserName";
        PasswordEncrypter.main(args);
        String output = byteOutput.toString("UTF-8");
        String split[] = null;
        split = output.split("\\n");
        assertEquals("# of arguments = 1", split[0]);
        assertEquals("Example input: UserName Password", split[1]);
        assertEquals("Please provide both a UserName and Password, UserName should be provided first.", split[2]);
    }

    @Test
    public void testMainPasswordOnly() throws Exception {
        String[] args = new String[1];
        args[0] = "Password";
        PasswordEncrypter.main(args);
        String output = byteOutput.toString("UTF-8");
        String split[] = null;
        split = output.split("\\n");
        assertEquals("# of arguments = 1", split[0]);
        assertEquals("Example input: UserName Password", split[1]);
        assertEquals("Please provide both a UserName and Password, UserName should be provided first.", split[2]);
    }

    @Test
    public void testMainExtraArgs() throws Exception {
        String[] args = new String[4];
        args[0] = "UserName";
        args[1] = "Password";
        args[2] = "SecondUserName";
        args[3] = "SecondPassword";
        PasswordEncrypter.main(args);
        String output = byteOutput.toString("UTF-8");
        String split[] = null;
        split = output.split("\\n");
        assertEquals("# of arguments = 4", split[0]);
        assertEquals("Example input: UserName Password", split[1]);
        assertEquals("Please ONLY provide a UserName and Password, UserName should be provided first.", split[2]);
    }

    @Test
    public void testMainEncryptPassword() throws Exception {
        String[] args = new String[2];
        args[0] = "UserName";
        args[1] = "Password";
        PasswordEncrypter.main(args);
        String output = byteOutput.toString("UTF-8");
        String split[] = null;
        split = output.split("\\n");
        assertEquals(1, split.length);
        assertEquals("UserName = SaTaqurAqc7q0nf0n6IL4erSd/Sfogvh6tJ39J+iC+Hq0nf0n6IL4erSd/Sfogvh6tJ39J+iC+Hq0nf0n6IL4Q==", split[0]);
    }

}
