/*
 * Copyright (c) 2002-2008 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jgoodies.binding.tests;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.NumberFormat;

import junit.framework.TestCase;

import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.tests.beans.TestBean;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

/**
 * Checks reflection access to a bunch of ValueModel implementations.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.8 $
 */
public final class ReflectionTest extends TestCase {


    /**
     * Checks reflection access to ValueModels returned by the BeanAdapter.
     */
    public void testAccessToBeanAdapterModels() {
        BeanAdapter<TestBean> adapter = new BeanAdapter<TestBean>(new TestBean());
        ValueModel valueModel = adapter.getValueModel("readWriteObjectProperty");
        checkAccess(valueModel, null, "BeanAdapter's ValueModel");
    }


    // Checking Access to ConverterFactory Converters *************************

    /**
     * Checks reflection access to the converting ValueModel
     * returned by <code>ConverterFactory.createBooleanNegator</code>.
     */
    public void testAccessToBooleanNegator() {
        ValueModel booleanNegator = ConverterFactory.createBooleanNegator(
                new ValueHolder(true));
        checkAccess(booleanNegator, null, "ConverterFactory#createBooleanNegator");
    }


    /**
     * Checks reflection access to the converting ValueModel
     * returned by <code>ConverterFactory.createBooleanToStringConverter</code>.
     */
    public void testAccessToBooleanToStringConverter() {
        ValueModel stringConverter = ConverterFactory.createBooleanToStringConverter(
                new ValueHolder(true), "true", "false");
        checkAccess(stringConverter, "true", "ConverterFactory#createBooleanToStringConverter");
    }


    /**
     * Checks reflection access to the converting ValueModel
     * returned by <code>ConverterFactory.createDoubleToIntegerConverter</code>.
     */
    public void testAccessToDoubleToIntegerConverter() {
        ValueModel doubleConverter = ConverterFactory.createDoubleToIntegerConverter(
                new ValueHolder(1d));
        checkAccess(doubleConverter, new Integer(2), "ConverterFactory#createDoubleToIntegerConverter");
    }


    /**
     * Checks reflection access to the converting ValueModel
     * returned by <code>ConverterFactory.createFloatToIntegerConverter</code>.
     */
    public void testAccessToFloatToIntegerConverter() {
        ValueModel floatConverter = ConverterFactory.createFloatToIntegerConverter(
                new ValueHolder(1f));
        checkAccess(floatConverter, new Integer(2), "ConverterFactory#createFloatToIntegerConverter");
    }


    /**
     * Checks reflection access to the converting ValueModel
     * returned by <code>ConverterFactory.createLongToIntegerConverter</code>.
     */
    public void testAccessToLongToIntegerConverter() {
        ValueModel longConverter = ConverterFactory.createLongToIntegerConverter(
                new ValueHolder(new Long(42)));
        checkAccess(longConverter, new Integer(2), "ConverterFactory#createLongToIntegerConverter");
    }


    /**
     * Checks reflection access to the converting ValueModel
     * returned by <code>ConverterFactory.createStringConverter</code>.
     */
    public void testAccessToStringConverter() {
        ValueModel stringConverter = ConverterFactory.createStringConverter(
                new ValueHolder(1967),
                NumberFormat.getIntegerInstance());
        checkAccess(stringConverter, "512", "ConverterFactory#createStringConverter");
    }


    // Helper Code ************************************************************

    /**
     * Checks read-write-access to the given ValueModel using reflection.
     *
     * @param valueModel   the ValueModel that implements the setter
     * @param newValue     the test value for the setter
     * @param description  used in failure message to describe the model
     */
    private static void checkAccess(
            ValueModel valueModel,
            Object newValue,
            String description) {
        checkReadAccess (valueModel,           description + "#getValue()");
        checkWriteAccess(valueModel, newValue, description + "#setValue(Object)");
    }


    /**
     * Checks read-access to the given ValueModel using reflection.
     *
     * @param valueModel   the ValueModel that implements the getter
     * @param description  used in failure message to describe the model
     */
    private static void checkReadAccess(ValueModel valueModel, String description) {
        try {
            Method getter = valueModel.getClass().getMethod("getValue", new Class[]{});
            getter.invoke(valueModel, new Object[]{});
        } catch (NoSuchMethodException e) {
            fail("Inaccessible " + description);
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException in " + description);
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException in " + description);
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException in " + description);
        }
    }

    /**
     * Checks write-access to the given ValueModel using reflection.
     *
     * @param valueModel   the ValueModel that implements the setter
     * @param newValue     the value to be set
     * @param description  used in failure message to describe the model
     */
    private static void checkWriteAccess(
            ValueModel valueModel,
            Object newValue,
            String description) {
        try {
            Method setter = valueModel.getClass().getMethod("setValue", new Class[]{Object.class});
            setter.invoke(valueModel, new Object[]{newValue});
        } catch (NoSuchMethodException e) {
            fail("Inaccessible " + description);
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException in " + description);
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException in " + description);
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException in " + description);
        }
    }



}
