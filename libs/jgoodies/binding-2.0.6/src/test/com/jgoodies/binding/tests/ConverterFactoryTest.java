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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import junit.framework.TestCase;

import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;


/**
 * A test case for converters created by the ConverterFactory.
 *
 * TODO: add tests for null values in subject.
 *
 * @author  Andrej Golovnin
 * @author  Karsten Lentzsch
 * @version $Revision: 1.9 $
 */
public final class ConverterFactoryTest extends TestCase {


    // Constructor Tests ******************************************************

    public void testConstructorsRejectNullSubjects() {
        try {
            ConverterFactory.createBooleanNegator(null);
            fail("BooleanNegator should reject null subject.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            ConverterFactory.createBooleanToStringConverter(null, "yes", "no");
            fail("BooleanToStringConverter should reject null subject.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            ConverterFactory.createBooleanToStringConverter(null, "yes", "no", "none");
            fail("BooleanToStringConverter should reject null subject.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            ConverterFactory.createDoubleToIntegerConverter(null);
            fail("DoubleToIntegerConverter should reject null subject.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            ConverterFactory.createDoubleToIntegerConverter(null, 100);
            fail("DoubleToIntegerConverter should reject null subject.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            ConverterFactory.createFloatToIntegerConverter(null);
            fail("FloatToIntegerConverter should reject null subject.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            ConverterFactory.createFloatToIntegerConverter(null, 100);
            fail("FloatToIntegerConverter should reject null subject.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            ConverterFactory.createLongToIntegerConverter(null);
            fail("LongToIntegerConverter should reject null subject.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            ConverterFactory.createLongToIntegerConverter(null, 100);
            fail("LongToIntegerConverter should reject null subject.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            ConverterFactory.createStringConverter(null, DateFormat.getDateInstance());
            fail("StringConverter should reject null subject.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
    }


    public void testBooleanToStringConverterRejectsNullTexts() {
        try {
            ConverterFactory.createBooleanToStringConverter(new ValueHolder(), null, "no", "none");
            fail("BooleanToStringConverter should reject null trueText.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            ConverterFactory.createBooleanToStringConverter(new ValueHolder(), "yes", null, "none");
            fail("BooleanToStringConverter should reject null falseText.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            ConverterFactory.createBooleanToStringConverter(new ValueHolder(), "yes", "no", null);
            fail("BooleanToStringConverter should reject null falseText.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
    }


    public void testBooleanToStringConverterRejectsEqualTexts() {
        try {
            ConverterFactory.createBooleanToStringConverter(new ValueHolder(), "yes", "yes", "none");
            fail("BooleanToStringConverter should reject equal trueText and falseText.");
        } catch (IllegalArgumentException e) {
            // The expected behavior
        }
        try {
            ConverterFactory.createBooleanToStringConverter(new ValueHolder(), "none", "no", "none");
        } catch (IllegalArgumentException e) {
            fail("BooleanToStringConverter should accept equal trueText and nullText.");
        }
        try {
            ConverterFactory.createBooleanToStringConverter(new ValueHolder(), "yes", "none", "none");
        } catch (IllegalArgumentException e) {
            fail("BooleanToStringConverter should accept equal falseText and nullText.");
        }
    }


    // Conversion Tests *******************************************************

    public void testBooleanNegator() {
        ValueModel subject = new ValueHolder(Boolean.TRUE);
        AbstractConverter c = (AbstractConverter)
            ConverterFactory.createBooleanNegator(subject);
        assertInvertable(c, null);
        assertInvertable(c, Boolean.TRUE);
        assertInvertable(c, Boolean.FALSE);
        assertProperConversions(subject, Boolean.TRUE,  c, Boolean.FALSE);
        assertProperConversions(subject, Boolean.FALSE, c, Boolean.TRUE);
        assertProperConversions(subject, null,          c, null);
    }

    public void testBooleanToStringConverter() {
        String trueText = "true";
        String falseText = "false";
        String nullText = "unknown";
        ValueModel subject = new ValueHolder(Boolean.TRUE);
        AbstractConverter c = (AbstractConverter)
            ConverterFactory.createBooleanToStringConverter(subject, trueText,
                    falseText, nullText);
        assertInvertable(c, null);
        assertInvertable(c, Boolean.TRUE);
        assertInvertable(c, Boolean.FALSE);
        assertProperConversions(subject, Boolean.TRUE,  c, trueText);
        assertProperConversions(subject, Boolean.FALSE, c, falseText);
        assertProperConversions(subject, null,          c, nullText);
    }

    public void testDoubleConverter() {
        Double value = new Double(1.0);
        Double convertedValue = new Double(100);
        ValueModel subject = new ValueHolder(value);
        AbstractConverter c = (AbstractConverter)
            ConverterFactory.createDoubleConverter(subject, 100);
        assertInvertable(c, value);
        assertProperConversions(subject, value, c, convertedValue);
    }

    public void testDoubleToIntegerConverter() {
        Double value = new Double(100.0);
        Integer convertedValue = new Integer(100);
        ValueModel subject = new ValueHolder(value);
        AbstractConverter c = (AbstractConverter)
            ConverterFactory.createDoubleToIntegerConverter(subject, 1);
        assertInvertable(c, value);
        assertProperConversions(subject, value, c, convertedValue);
    }

    public void testFloatConverter() {
        Float value = new Float(1.0);
        Float convertedValue = new Float(100);
        ValueModel subject = new ValueHolder(value);
        AbstractConverter c = (AbstractConverter)
            ConverterFactory.createFloatConverter(subject, 100);
        assertInvertable(c, value);
        assertProperConversions(subject, value, c, convertedValue);
    }

    public void testFloatToIntegerConverter() {
        Float value = new Float(100.0f);
        Integer convertedValue = new Integer(100);
        ValueModel subject = new ValueHolder(value);
        AbstractConverter c = (AbstractConverter)
            ConverterFactory.createFloatToIntegerConverter(subject, 1);
        assertInvertable(c, value);
        assertProperConversions(subject, value, c, convertedValue);
    }

    public void testIntegerConverter() {
        Integer value = new Integer(1);
        Integer convertedValue = new Integer(100);
        ValueModel subject = new ValueHolder(value);
        AbstractConverter c = (AbstractConverter)
            ConverterFactory.createIntegerConverter(subject, 100);
        assertInvertable(c, value);
        assertProperConversions(subject, value, c, convertedValue);
    }

    public void testLongConverter() {
        Long value = new Long(1L);
        Long convertedValue = new Long(100L);
        ValueModel subject = new ValueHolder(value);
        AbstractConverter c = (AbstractConverter)
            ConverterFactory.createLongConverter(subject, 100);
        assertInvertable(c, value);
        assertProperConversions(subject, value, c, convertedValue);
    }

    public void testLongToIntegerConverter() {
        Long value = new Long(100);
        Integer convertedValue = new Integer(100);
        ValueModel subject = new ValueHolder(value);
        AbstractConverter c = (AbstractConverter)
            ConverterFactory.createLongToIntegerConverter(subject, 1);
        assertInvertable(c, value);
        assertProperConversions(subject, value, c, convertedValue);
    }

    public void testStringConverter() {
        Long value = new Long(100);
        String convertedValue = "100";
        DecimalFormat format = new DecimalFormat("#",
                new DecimalFormatSymbols(Locale.US));
        format.setParseIntegerOnly(true);
        ValueModel subject = new ValueHolder(value);
        AbstractConverter c = (AbstractConverter)
            ConverterFactory.createStringConverter(subject, format);
        assertInvertable(c, value);
        assertProperConversions(subject, value, c, convertedValue);
    }


    // Helper Code ************************************************************

    private void assertInvertable(AbstractConverter converter, Object value) {
        try {
            converter.setValue(converter.convertFromSubject(value));
        } catch (Exception e) {
            fail("Unexpected exception is thrown: " + e);
        }
    }

    private void assertProperConversions(ValueModel subject, Object value,
            ValueModel converter, Object convertedValue) {
        subject.setValue(value);
        assertEquals("The value returned by converter is not the expected one:",
                convertedValue, converter.getValue());
        converter.setValue(convertedValue);
        assertEquals("The value returned by subject is not the expected one:",
                value, subject.getValue());
    }

}

