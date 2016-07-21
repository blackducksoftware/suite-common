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
package com.blackducksoftware.integration.suite.sdk.aspects.util;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.junit.Test;

public class AspectUtilityTest {

    @Test
    public void testArgumentsToStringEmpty() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, InstantiationException {

        ArrayList<String> testList = new ArrayList<String>();
        Object[] test = new Object[1];
        test[0] = testList;

        TestLogger logger = new TestLogger(AspectUtilityTest.class);
        String listIntrospection = AspectUtility.inputArgumentsToString(test, logger);

        System.out.println(listIntrospection);
        assertEquals("{ArrayList->{EMPTY}}", listIntrospection);
    }

    @Test
    public void testArgumentsToStringMultipleEmptyLists() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, InstantiationException {

        ArrayList<String> testList = new ArrayList<String>();
        ArrayList<String> testList2 = new ArrayList<String>();
        Object[] test = new Object[2];
        test[0] = testList;
        test[1] = testList2;

        TestLogger logger = new TestLogger(AspectUtilityTest.class);
        String listIntrospection = AspectUtility.inputArgumentsToString(test, logger);

        System.out.println(listIntrospection);
        assertEquals("{ArrayList->{EMPTY}::ArrayList->{EMPTY}}", listIntrospection);
    }

    @Test
    public void testArgumentsToStringNull() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, InstantiationException {

        Object[] test = new Object[1];
        test[0] = null;

        TestLogger logger = new TestLogger(AspectUtilityTest.class);
        String listIntrospection = AspectUtility.inputArgumentsToString(test, logger);

        System.out.println(listIntrospection);
        assertEquals("{null}", listIntrospection);
    }

    @Test
    public void testArgumentsToStringList() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        ArrayList<String> testList = new ArrayList<String>();
        testList.add("testArg1");
        testList.add("testArg2");
        testList.add("testArg3");

        Object[] test = new Object[1];
        test[0] = testList;

        TestLogger logger = new TestLogger(AspectUtilityTest.class);
        String listIntrospection = AspectUtility.inputArgumentsToString(test, logger);

        System.out.println(listIntrospection);
        assertEquals("{ArrayList<String>->{String->[testArg1],String->[testArg2],String->[testArg3]}}", listIntrospection);
    }

    @Test
    public void testArgumentsToStringArray() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        String[] testArray = new String[3];
        testArray[0] = "testArg1";
        testArray[1] = "testArg2";
        testArray[2] = "testArg3";

        Object[] test = new Object[1];
        test[0] = testArray;

        TestLogger logger = new TestLogger(AspectUtilityTest.class);
        String arrayIntrospection = AspectUtility.inputArgumentsToString(test, logger);

        System.out.println(arrayIntrospection);
        assertEquals("{String[]->{String->[testArg1],String->[testArg2],String->[testArg3]}}", arrayIntrospection);
    }

    @Test
    public void testArgumentsToStringString() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Object[] test = new Object[1];
        test[0] = "testArg";

        TestLogger logger = new TestLogger(AspectUtilityTest.class);
        String stringIntrospection = AspectUtility.inputArgumentsToString(test, logger);

        System.out.println(stringIntrospection);
        assertEquals("{String->[testArg]}", stringIntrospection);
    }

    @Test
    public void testArgumentsToStringCharacter() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Object[] test = new Object[2];
        test[0] = 'c';
        test[1] = 'h';

        TestLogger logger = new TestLogger(AspectUtilityTest.class);
        String charIntrospection = AspectUtility.inputArgumentsToString(test, logger);

        System.out.println(charIntrospection);
        assertEquals("{Character->[c]::Character->[h]}", charIntrospection);
    }

    @Test
    public void testArgumentsToStringBoolean() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Object[] test = new Object[2];
        test[0] = false;
        test[1] = true;

        TestLogger logger = new TestLogger(AspectUtilityTest.class);
        String booleanIntrospection = AspectUtility.inputArgumentsToString(test, logger);

        System.out.println(booleanIntrospection);
        assertEquals("{Boolean->[false]::Boolean->[true]}", booleanIntrospection);
    }

    @Test
    public void testArgumentsToStringBooleanAndString() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Object[] test = new Object[4];
        test[0] = false;
        test[1] = true;
        test[2] = "arg1";
        test[3] = "arg2";

        TestLogger logger = new TestLogger(AspectUtilityTest.class);
        String booleanIntrospection = AspectUtility.inputArgumentsToString(test, logger);

        System.out.println(booleanIntrospection);
        assertEquals("{Boolean->[false]::Boolean->[true]::String->[arg1]::String->[arg2]}", booleanIntrospection);
    }

    enum TestEnum {
        TEST;
    }

    @Test
    public void testArgumentsToStringEnum() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Object[] test = new Object[1];
        test[0] = TestEnum.TEST;

        TestLogger logger = new TestLogger(AspectUtilityTest.class);
        String enumIntrospection = AspectUtility.inputArgumentsToString(test, logger);

        System.out.println(enumIntrospection);
        assertEquals("{TestEnum->[TEST}", enumIntrospection);
    }

    @Test
    public void testArgumentsToStringNumber() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, InstantiationException {

        Object[] test = new Object[1];
        test[0] = 2014;

        TestLogger logger = new TestLogger(AspectUtilityTest.class);
        String intIntrospection = AspectUtility.inputArgumentsToString(test, logger);

        System.out.println(intIntrospection);
        assertEquals("{Integer->[2014]}", intIntrospection);
    }

    @Test
    public void testArgumentsToStringPrimitive() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        int i = 2014;
        char c = 'c';
        Object[] test = new Object[2];
        test[0] = i;
        test[1] = c;

        TestLogger logger = new TestLogger(AspectUtilityTest.class);
        String intIntrospection = AspectUtility.inputArgumentsToString(test, logger);

        System.out.println(intIntrospection);
        assertEquals("{Integer->[2014]::Character->[c]}", intIntrospection);
    }

    // FIXME
    // @Test
    // public void testArgumentsToStringObject() throws SecurityException, NoSuchMethodException,
    // IllegalArgumentException, IllegalAccessException,
    // InvocationTargetException, InstantiationException {
    //
    // ArrayList<ComponentApprovalNameOrIdToken> testList = new ArrayList<ComponentApprovalNameOrIdToken>();
    //
    // ApprovalNameToken approval1 = new ApprovalNameToken();
    // approval1.setName("approvalName");
    // ComponentNameVersionToken component1 = new ComponentNameVersionToken();
    // component1.setName("componentName");
    // component1.setVersion("componentVersion");
    // ComponentApprovalNameOrIdToken compApproval1 = new ComponentApprovalNameOrIdToken();
    // compApproval1.setApprovalId(approval1);
    // compApproval1.setComponentId(component1);
    //
    // testList.add(compApproval1);
    //
    // ApprovalNameToken approval2 = new ApprovalNameToken();
    // approval2.setName("");
    // ComponentNameVersionToken component2 = new ComponentNameVersionToken();
    // component2.setName("");
    // component2.setVersion("");
    // ComponentApprovalNameOrIdToken compApproval2 = new ComponentApprovalNameOrIdToken();
    // compApproval2.setApprovalId(approval2);
    // compApproval2.setComponentId(component2);
    //
    // testList.add(compApproval2);
    //
    // ApprovalNameToken approval3 = new ApprovalNameToken();
    // approval3.setName(null);
    // ComponentNameVersionToken component3 = new ComponentNameVersionToken();
    // component3.setName(null);
    // component3.setVersion(null);
    // ComponentApprovalNameOrIdToken compApproval3 = new ComponentApprovalNameOrIdToken();
    // compApproval3.setApprovalId(approval3);
    // compApproval3.setComponentId(component3);
    //
    // testList.add(compApproval3);
    //
    // ComponentNameVersionToken component4 = new ComponentNameVersionToken();
    // component4.setName("componentName");
    // component4.setVersion("componentVersion");
    // ComponentApprovalNameOrIdToken compApproval4 = new ComponentApprovalNameOrIdToken();
    // compApproval4.setApprovalId(null);
    // compApproval4.setComponentId(component4);
    //
    // testList.add(compApproval4);
    //
    // ApprovalNameToken approval5 = new ApprovalNameToken();
    // approval5.setName("approvalName");
    // ComponentApprovalNameOrIdToken compApproval5 = new ComponentApprovalNameOrIdToken();
    // compApproval5.setApprovalId(approval5);
    // compApproval5.setComponentId(null);
    //
    // testList.add(compApproval5);
    //
    // ComponentApprovalNameOrIdToken compApproval6 = new ComponentApprovalNameOrIdToken();
    // compApproval6.setApprovalId(null);
    // compApproval6.setComponentId(null);
    //
    // testList.add(compApproval6);
    //
    // Object[] test = new Object[1];
    // test[0] = testList;
    //
    // TestLogger logger = new TestLogger(AspectUtilityTest.class);
    // String objectIntrospection = AspectUtility.inputArgumentsToString(test, logger);
    //
    // System.out.println(objectIntrospection);
    // assertEquals(
    // "{ArrayList<ComponentApprovalNameOrIdToken>->{ComponentApprovalNameOrIdToken->[ApprovalId -> ApprovalNameToken->[Name -> String->[approvalName] , ComponentId -> ComponentNameVersionToken->[Name -> String->[componentName] , Version -> String->[componentVersion],ComponentApprovalNameOrIdToken->[ApprovalId -> ApprovalNameToken->[Name -> String->[] , ComponentId -> ComponentNameVersionToken->[Name -> String->[] , Version -> String->[],ComponentApprovalNameOrIdToken->[ApprovalId -> ApprovalNameToken->[Name -> null , ComponentId -> ComponentNameVersionToken->[Name -> null , Version -> null,ComponentApprovalNameOrIdToken->[ApprovalId -> null , ComponentId -> ComponentNameVersionToken->[Name -> String->[componentName] , Version -> String->[componentVersion],ComponentApprovalNameOrIdToken->[ApprovalId -> ApprovalNameToken->[Name -> String->[approvalName] , ComponentId -> null,ComponentApprovalNameOrIdToken->[ApprovalId -> null , ComponentId -> null}}",
    // objectIntrospection);
    //
    // }

}
