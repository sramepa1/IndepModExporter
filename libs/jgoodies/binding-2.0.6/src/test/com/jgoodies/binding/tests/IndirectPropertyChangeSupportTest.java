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

import java.beans.PropertyChangeListener;

import junit.framework.TestCase;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.beans.IndirectPropertyChangeSupport;
import com.jgoodies.binding.tests.beans.EquityTestBean;
import com.jgoodies.binding.tests.beans.TestBean;
import com.jgoodies.binding.tests.event.PropertyChangeReport;
import com.jgoodies.binding.value.Trigger;

/**
 * A test case for class {@link IndirectPropertyChangeSupport}
 * that checks whether listeners are added, removed and re-registered properly.
 * Also tests the classes BeanAdapter and PresentationModel that use
 * the IndirectPropertyChangeSupport directly or indirectly.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.14 $
 */
public final class IndirectPropertyChangeSupportTest extends TestCase {


    // Re-Registering Listeners on Different Bean Types ***********************

    public void testReregisterListenerOnDifferentBeans() {
        testReregisterListener(
                new EquityTestBean("key1"),
                new EquityTestBean("key2"),
                new IndirectPropertyChangeSupport());

        TestBean bean1 = new EquityTestBean("key1");
        testReregisterListener(
                bean1,
                new EquityTestBean("key2"),
                new IndirectPropertyChangeSupport(bean1));
    }


    public void testReregisterListenerOnEqualBeans() {
        testReregisterListener(
                new EquityTestBean("key1"),
                new EquityTestBean("key1"),
                new IndirectPropertyChangeSupport());

        TestBean bean1 = new EquityTestBean("key1");
        testReregisterListener(
                bean1,
                new EquityTestBean("key1"),
                new IndirectPropertyChangeSupport(bean1));
    }


    public void testBeanAdapterReregisterListenerOnEqualBeans() {
        TestBean bean1 = new EquityTestBean("key1");
        testReregisterListener(
                bean1,
                new EquityTestBean("key1"),
                new BeanAdapter<TestBean>(bean1));

        bean1 = new EquityTestBean("key1");
        testReregisterListener(
                bean1,
                new EquityTestBean("key1"),
                new BeanAdapter<TestBean>(bean1, false));
    }


    public void testPresentationModelReregisterListenerOnEqualBeans() {
        TestBean bean1 = new EquityTestBean("key1");
        testReregisterListener(
                bean1,
                new EquityTestBean("key1"),
                new PresentationModel<TestBean>(bean1));

        bean1 = new EquityTestBean("key1");
        testReregisterListener(
                bean1,
                new EquityTestBean("key1"),
                new PresentationModel<TestBean>(bean1, new Trigger()));
    }


    // Reusable Basic Test ****************************************************

    private void testReregisterListener(
            TestBean bean1,
            TestBean bean2,
            Object beanHolder) {
        String propertyName = "readWriteObjectProperty";
        setBean(beanHolder, bean1);
        PropertyChangeReport report1a = new PropertyChangeReport();
        PropertyChangeReport report1b = new PropertyChangeReport();
        PropertyChangeReport report2a = new PropertyChangeReport();
        PropertyChangeReport report2b = new PropertyChangeReport();
        addIndirectListener(beanHolder, report1a);
        addIndirectListener(beanHolder, report1b);
        addIndirectListener(beanHolder, propertyName, report2a);
        addIndirectListener(beanHolder, propertyName, report2b);
        bean1.setReadWriteObjectProperty("bean1value1");
        assertEquals("Changing the observed bean1 fires a change.",
                1,
                report1a.eventCount());
        assertEquals("All unnamed listeners receive the same number of events.",
                report1a.eventCount(),
                report1b.eventCount());
        assertEquals("Changing the observed bean1 fires a named change.",
                1,
                report2a.eventCount());
        assertEquals("All named listeners receive the same number of events.",
                report2a.eventCount(),
                report2b.eventCount());

        bean2.setReadWriteObjectProperty("bean2value1");
        assertEquals("Changing the unobserved bean2 fires no change.",
                1,
                report1a.eventCount());
        assertEquals("Changing the unobserved bean2 fires no named change.",
                1,
                report1a.eventCount());

        // Change the bean
        setBean(beanHolder, bean2);
        bean1.setReadWriteObjectProperty("bean1value2");
        assertEquals("Changing the unobserved bean1 fires no change.",
                1,
                report1a.eventCount());
        assertEquals("Changing the unobserved bean1 fires no named change.",
                1,
                report2a.eventCount());

        bean2.setReadWriteObjectProperty("bean2value2");
        assertEquals("Changing the observed bean2 fires a change.",
                2,
                report1a.eventCount());
        assertEquals("2) All unnamed listeners receive the same number of events.",
                report1a.eventCount(),
                report1b.eventCount());
        assertEquals("Changing the observed bean2 fires a change.",
                2,
                report2a.eventCount());
        assertEquals("2) All named listeners receive the same number of events.",
                report2a.eventCount(),
                report2b.eventCount());

        removeIndirectListener(beanHolder, report1a);
        removeIndirectListener(beanHolder, report1b);
        removeIndirectListener(beanHolder, propertyName, report2a);
        removeIndirectListener(beanHolder, propertyName, report2b);

        // Change the bean back to bean1
        setBean(beanHolder, bean1);
        bean1.setReadWriteObjectProperty("bean1value3");
        assertEquals("Unregistered listeners receive no more events.",
                2,
                report1a.eventCount());
        assertEquals("All unregistered listeners receive no more events.",
                report1a.eventCount(),
                report1b.eventCount());
        assertEquals("Deregistered named listeners receive no more events.",
                2,
                report2a.eventCount());
        assertEquals("All deregistered named listeners receive no more events.",
                report2a.eventCount(),
                report2b.eventCount());

    }


    // Helper Code ***********************************************************

    private void setBean(Object beanHolder, TestBean newBean) {
        if (beanHolder instanceof IndirectPropertyChangeSupport)
            ((IndirectPropertyChangeSupport) beanHolder).setBean(newBean);
        else if (beanHolder instanceof BeanAdapter)
            ((BeanAdapter<TestBean>) beanHolder).setBean(newBean);
        else if (beanHolder instanceof PresentationModel)
            ((PresentationModel<TestBean>) beanHolder).setBean(newBean);
        else
            throw new IllegalArgumentException("Unknown bean holder type. " +
                    "Must be one of: IndirectPropertyChangeSupport, BeanAdapter, PresentationModel");
    }


    private void addIndirectListener(Object beanHolder, PropertyChangeListener listener) {
        if (beanHolder instanceof IndirectPropertyChangeSupport)
            ((IndirectPropertyChangeSupport) beanHolder).addPropertyChangeListener(listener);
        else if (beanHolder instanceof BeanAdapter)
            ((BeanAdapter<?>) beanHolder).addBeanPropertyChangeListener(listener);
        else if (beanHolder instanceof PresentationModel)
            ((PresentationModel<?>) beanHolder).addBeanPropertyChangeListener(listener);
        else
            throw new IllegalArgumentException("Unknown bean holder type. " +
                    "Must be one of: IndirectPropertyChangeSupport, BeanAdapter, PresentationModel");
    }


    private void addIndirectListener(Object beanHolder, String propertyName, PropertyChangeListener listener) {
        if (beanHolder instanceof IndirectPropertyChangeSupport)
            ((IndirectPropertyChangeSupport) beanHolder).addPropertyChangeListener(propertyName, listener);
        else if (beanHolder instanceof BeanAdapter)
            ((BeanAdapter<?>) beanHolder).addBeanPropertyChangeListener(propertyName, listener);
        else if (beanHolder instanceof PresentationModel)
            ((PresentationModel<?>) beanHolder).addBeanPropertyChangeListener(propertyName, listener);
        else
            throw new IllegalArgumentException("Unknown bean holder type. " +
                    "Must be one of: IndirectPropertyChangeSupport, BeanAdapter, PresentationModel");
    }


    private void removeIndirectListener(Object beanHolder, PropertyChangeListener listener) {
        if (beanHolder instanceof IndirectPropertyChangeSupport)
            ((IndirectPropertyChangeSupport) beanHolder).removePropertyChangeListener(listener);
        else if (beanHolder instanceof BeanAdapter)
            ((BeanAdapter<?>) beanHolder).removeBeanPropertyChangeListener(listener);
        else if (beanHolder instanceof PresentationModel)
            ((PresentationModel<?>) beanHolder).removeBeanPropertyChangeListener(listener);
        else
            throw new IllegalArgumentException("Unknown bean holder type. " +
                    "Must be one of: IndirectPropertyChangeSupport, BeanAdapter, PresentationModel");
    }


    private void removeIndirectListener(Object beanHolder, String propertyName, PropertyChangeListener listener) {
        if (beanHolder instanceof IndirectPropertyChangeSupport)
            ((IndirectPropertyChangeSupport) beanHolder).removePropertyChangeListener(propertyName, listener);
        else if (beanHolder instanceof BeanAdapter)
            ((BeanAdapter<?>) beanHolder).removeBeanPropertyChangeListener(propertyName, listener);
        else if (beanHolder instanceof PresentationModel)
            ((PresentationModel<?>) beanHolder).removeBeanPropertyChangeListener(propertyName, listener);
        else
            throw new IllegalArgumentException("Unknown bean holder type. " +
                    "Must be one of: IndirectPropertyChangeSupport, BeanAdapter, PresentationModel");
    }


}
