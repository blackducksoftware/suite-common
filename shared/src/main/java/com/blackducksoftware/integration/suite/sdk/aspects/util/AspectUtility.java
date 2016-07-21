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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.blackducksoftware.integration.suite.sdk.logging.IntLogger;

public final class AspectUtility {

    private AspectUtility() {

    }

    public static String inputArgumentsToString(Object[] arguments, IntLogger logger) {
        StringBuilder inputBuilder = new StringBuilder();
        int i = 0;
        if (arguments.length > 0) {
            for (Object argument : arguments) {
                if (argument != null) {
                    if (!(isPrintable(argument))) {
                        try {

                            inputBuilder.append(AspectUtility.argumentToString(argument));
                            i++;
                            if (i < arguments.length) {
                                inputBuilder.append("::");
                            }
                        } catch (IllegalArgumentException e) {
                            logger.trace("Error getting the method inputs : " + e.getMessage());
                        } catch (IllegalAccessException e) {
                            logger.trace("Error getting the method inputs : " + e.getMessage());
                        } catch (InvocationTargetException e) {
                            logger.trace("Error getting the method inputs : " + e.getMessage());
                        }
                    } else {
                        inputBuilder.append(argument.getClass().getSimpleName() + "->[" + argument.toString() + "]");
                        i++;
                        if (i < arguments.length) {
                            inputBuilder.append("::");
                        }
                    }
                } else {
                    inputBuilder.append("null");
                }
            }
        }

        return "{" + inputBuilder.toString() + "}";
    }

    private static String argumentToString(Object object) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

        if (object != null) {
            StringBuilder argumentString = new StringBuilder();

            Class<?> c = object.getClass();
            Method[] methods = c.getMethods();
            if (isPrintable(object) || !((object instanceof List) || (c.isArray()))) {
                argumentString.append(c.getSimpleName());
                argumentString.append("->[");
            }
            int i = 0;
            if (object instanceof List) {
                List<Object> list = (List<Object>) object;
                if (!list.isEmpty()) {
                    argumentString.append(c.getSimpleName());
                    argumentString.append("<");
                    argumentString.append(list.get(0).getClass().getSimpleName());
                    argumentString.append(">->{");
                    int l = 0;
                    for (Object o : list) {
                        if (l > 0) {
                            argumentString.append(",");
                        }
                        argumentString.append(argumentToString(o));
                        l++;
                    }
                    argumentString.append("}");
                } else {
                    argumentString.append(c.getSimpleName());
                    argumentString.append("->{EMPTY}");
                }
            } else if (c.isArray()) {
                argumentString.append(c.getSimpleName());
                argumentString.append("->{");
                int a = 0;
                Object[] array = (Object[]) object;
                for (Object o : array) {
                    if (a > 0) {
                        argumentString.append(",");
                    }
                    argumentString.append(argumentToString(o));
                    a++;
                }
                argumentString.append("}");
            } else {
                if (!(isPrintable(object) || (object instanceof Enum))) {
                    for (Method method : methods) {
                        if (method.getName().startsWith("get") || method.getName().startsWith("value")) {
                            // Only care about getters. Do nothing with methods that take parameters
                            // and do not have a return
                            if (method.getParameterTypes().length == 0 && !void.class.equals(method.getReturnType()) && !method.getName().endsWith("Class")
                                    && !method.getName().startsWith("values")) {
                                if (i > 0) {
                                    argumentString.append(" , ");
                                }

                                Object innerArgument = method.invoke(object, (Object[]) null);
                                argumentString.append(methodParameterString(method, argumentToString(innerArgument)));
                                i++;
                            }
                        }
                    }
                } else {
                    if (object instanceof String) {
                        argumentString.append((String) object);
                    } else {
                        argumentString.append(object.toString());
                    }

                    i++;
                }
            }
            if (isPrintable(object)) {
                argumentString.append("]");
            }
            return argumentString.toString();
        }

        return "null";
    }

    private static String methodParameterString(Method method, Object parameter) {
        StringBuilder output = new StringBuilder();
        if (parameter != null) {
            if (parameter instanceof String) {
                String parameterAsString = (String) parameter;
                addStringParameter(output, method.getName(), parameterAsString);
            } else {
                String parameterAsString = parameter.toString();
                addStringParameter(output, method.getName(), parameterAsString);
            }
        } else {
            output.append(method.getName().substring(3));
            output.append(" -> null");
        }

        return output.toString();
    }

    private static void addStringParameter(StringBuilder output, String methodName, String parameterAsString) {
        if (methodName.startsWith("value")) {
            if (StringUtils.isEmpty(parameterAsString)) {
                output.append("''");
            } else {
                output.append(parameterAsString);
            }
        } else {
            output.append(methodName.substring(3));
            if (StringUtils.isEmpty(parameterAsString)) {
                output.append(" -> ''");
            } else {
                output.append(" -> ");
                output.append(parameterAsString);
            }
        }
    }

    // private String getPasswordMask(String password) {
    // char[] mask = new char[password.length()];
    // Arrays.fill(mask, '*');
    // return new String(mask);
    // }

    private static Boolean isPrintable(Object o) {
        if (o != null) {
            if (o instanceof String) {
                return true;
            }
            if (o instanceof Number) {
                return true;
            }
            if (o instanceof Boolean) {
                return true;
            }
            if (o instanceof Character) {
                return true;
            }
            return false;
        }
        return false;
    }

}
