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

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.PropertyVetoException;

import junit.framework.TestCase;

import com.jgoodies.binding.beans.BeanUtils;
import com.jgoodies.binding.tests.beans.BeanClasses;
import com.jgoodies.binding.tests.beans.TestBean;
import com.jgoodies.binding.tests.beans.VetoableChangeRejector;

/**
 * A test case for class {@link BeanUtils}.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.15 $
 */
public final class BeanUtilsTest extends TestCase {


    /**
     * Checks that #supportsBoundProperties detects observable classes.
     */
    public void testDetectObservableClasses() {
        for (Class<?> beanClass : BeanClasses.getObservableClasses()) {
            assertTrue(
                "Could not detect that the class supports bound properties.",
                 BeanUtils.supportsBoundProperties(beanClass));
        }
    }

    /**
     * Checks that #supportsBoundProperties rejects unobservable classes.
     */
    public void testRejectUnobservableClasses() {
        for (Class<?> beanClass : BeanClasses.getUnobservableClasses()) {
            assertFalse(
                "Failed to reject a class that supports no bound properties.",
                BeanUtils.supportsBoundProperties(beanClass));
        }
    }


    public void testWriteConstrainedProperty() {
        TestBean bean = new TestBean();
        try {
            bean.setConstrainedProperty("value1");
        } catch (PropertyVetoException e1) {
            fail("Couldn't set the valid value1.");
        }
        assertEquals("Bean has the initial value1.",
                bean.getConstrainedProperty(),
                "value1");
        PropertyDescriptor descriptor = null;
        try {
            descriptor = BeanUtils.getPropertyDescriptor(bean.getClass(), "constrainedProperty");
            try {
                BeanUtils.setValue(bean, descriptor, "value2");
            } catch (PropertyVetoException e) {
                fail("No PropertyVetoException shall be thrown if there's no VetoableChangeListener.");
            }
            assertEquals("Bean now has the value2.",
                    bean.getConstrainedProperty(),
                    "value2");
            bean.addVetoableChangeListener(new VetoableChangeRejector());
            try {
                BeanUtils.setValue(bean, descriptor, "value3");
                fail("Setting a value that will be vetoed must throw an exception.");
            } catch (PropertyVetoException e) {
                // The expected behavior.
            }
            assertEquals("Bean still has the value2.",
                    bean.getConstrainedProperty(),
                    "value2");
        } catch (IntrospectionException e) {
            fail("Couldn't look up the descriptor for the constrained property.");
        }
    }


}
