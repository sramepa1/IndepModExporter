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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.tests.beans.TestBean;
import com.jgoodies.binding.tests.event.PropertyChangeReport;
import com.jgoodies.binding.value.AbstractValueModel;
import com.jgoodies.binding.value.BufferedValueModel;
import com.jgoodies.binding.value.Trigger;
import com.jgoodies.binding.value.ValueModel;

/**
 * Tests old and new values when the bean, value or subject changes in
 * BeanAdapter, PropertyAdapter, PresentationModel and BufferedValueModel.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.13 $
 */
public final class ValueChangeTest extends TestCase {

    private TestBean model1;
    private TestBean model2;


    // Setup ******************************************************************

    /**
     * @throws Exception  in case of an unexpected problem
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        model1 = new TestBean();
        model2 = new TestBean();
    }

    /**
     * @throws Exception  in case of an unexpected problem
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        model1 = null;
        model2 = null;
    }


    // Public Tests ***********************************************************

    public void testBeanAdapterBeanChange() {
        Object[][] pairs = createOldAndNewValuePairs();
        for (Object[] element : pairs) {
            Object bean1Value = element[0];
            Object bean2Value = element[1];
            testBeanAdapterBeanChange(bean1Value, bean2Value);
        }
    }

    public void testBeanAdapterValueChange() {
        Object[][] pairs = createOldAndNewValuePairs();
        for (Object[] element : pairs) {
            Object value1 = element[0];
            Object value2 = element[1];
            testBeanAdapterValueChange(value1, value2);
        }
    }

    public void testPropertyAdapterBeanChange() {
        Object[][] pairs = createOldAndNewValuePairs();
        for (Object[] element : pairs) {
            Object bean1Value = element[0];
            Object bean2Value = element[1];
            testPropertyAdapterBeanChange(bean1Value, bean2Value);
        }
    }


    public void testPropertyAdapterValueChange() {
        Object[][] pairs = createOldAndNewValuePairs();
        for (Object[] element : pairs) {
            Object value1 = element[0];
            Object value2 = element[1];
            testPropertyAdapterValueChange(value1, value2);
        }
    }


    public void testBufferedValueModelSubjectChange() {
        Object[][] pairs = createOldAndNewValuePairs();
        for (Object[] element : pairs) {
            Object value1 = element[0];
            Object value2 = element[1];
            testBufferedValueModelSubjectChange(value1, value2);
        }
    }


    public void testBufferedValueModelValueChange() {
        Object[][] pairs = createOldAndNewValuePairs();
        for (Object[] element : pairs) {
            Object value1 = element[0];
            Object value2 = element[1];
            testBufferedValueModelSubjectValueChange(value1, value2);
        }
    }


    // Test Implementations ***************************************************

    private void testBeanAdapterBeanChange(
            Object value1, Object value2) {
        model1.setReadWriteObjectProperty(value1);
        model2.setReadWriteObjectProperty(value2);

        BeanAdapter<TestBean> adapter = new BeanAdapter<TestBean>((TestBean)null, true);
        AbstractValueModel valueModel = adapter.getValueModel("readWriteObjectProperty");

        PropertyChangeReport changeReport = new PropertyChangeReport();
        valueModel.addPropertyChangeListener("value", changeReport);
        Object eventsOldValue;
        Object eventsNewValue;
        int expectedEventCount = 0;

        adapter.setBean(model1);
        boolean firesEventFromNullToModel1 = requiresPropertyChangeEvent(null, value1);
        if (firesEventFromNullToModel1) {
            expectedEventCount++;
        }
        assertEquals(
                "Expected event count after null -> model1 ("
                + null + " -> " + value1 + ").",
                expectedEventCount,
                changeReport.eventCount());
        if (firesEventFromNullToModel1) {
            eventsOldValue = changeReport.lastOldValue();
            eventsNewValue = changeReport.lastNewValue();
            assertEquals("null-> model1 change fires proper old value.", null, eventsOldValue);
            assertEquals("null-> model1 change fires proper new value.", value1, eventsNewValue);
        }

        adapter.setBean(model2);
        boolean firesEventFromModel1ToModel2 = requiresPropertyChangeEvent(value1, value2);
        if (firesEventFromModel1ToModel2) {
            expectedEventCount++;
        }
        assertEquals(
                "Expected event count after model1 -> model2 ("
                + value1 + " -> " + value2 + ").",
                expectedEventCount,
                changeReport.eventCount());
        if (firesEventFromModel1ToModel2) {
            eventsOldValue = changeReport.lastOldValue();
            eventsNewValue = changeReport.lastNewValue();
            assertEquals("model1 -> model2 change fires proper old value.", value1, eventsOldValue);
            assertEquals("model1 -> model2 change fires proper new value.", value2, eventsNewValue);
        }

        adapter.setBean(null);
        boolean firesEventFromModel2ToNull = requiresPropertyChangeEvent(value2, null);
        if (firesEventFromModel2ToNull) {
            expectedEventCount++;
        }
        assertEquals(
                "Expected event count after model2 -> null ("
                + value2 + " -> " + null + ").",
                expectedEventCount,
                changeReport.eventCount());
        if (firesEventFromModel2ToNull) {
            eventsOldValue = changeReport.lastOldValue();
            eventsNewValue = changeReport.lastNewValue();
            assertEquals("model2 -> null change fires proper old value.", value2, eventsOldValue);
            assertEquals("model2 -> null change fires proper new value.", null, eventsNewValue);
        }
    }

    private void testBeanAdapterValueChange(
            Object value1, Object value2) {
        BeanAdapter<TestBean> adapter = new BeanAdapter<TestBean>(model1, true);
        testValueChange(
                value1,
                value2,
                adapter.getValueModel("readWriteObjectProperty"));
    }


    private void testPropertyAdapterBeanChange(
            Object value1,
            Object value2) {
        model1.setReadWriteObjectProperty(value1);
        model2.setReadWriteObjectProperty(value2);

        PropertyAdapter<TestBean> adapter = new PropertyAdapter<TestBean>((TestBean)null, "readWriteObjectProperty", true);
        PropertyChangeReport changeReport = new PropertyChangeReport();
        adapter.addPropertyChangeListener("value", changeReport);
        Object eventsOldValue;
        Object eventsNewValue;
        int expectedEventCount = 0;

        adapter.setBean(model1);
        boolean firesEventFromNullToModel1 = requiresPropertyChangeEvent(null, value1);
        if (firesEventFromNullToModel1) {
            expectedEventCount++;
        }
        assertEquals(
                "Expected event count after null -> model1 ("
                + null + " -> " + value2 + ").",
                expectedEventCount,
                changeReport.eventCount());
        if (firesEventFromNullToModel1) {
            eventsOldValue = changeReport.lastOldValue();
            eventsNewValue = changeReport.lastNewValue();
            assertEquals("null-> model1 change fires proper old value.", null, eventsOldValue);
            assertEquals("null-> model1 change fires proper new value.", value1, eventsNewValue);
        }

        adapter.setBean(model2);
        boolean firesEventFromModel1ToModel2 = requiresPropertyChangeEvent(value1, value2);
        if (firesEventFromModel1ToModel2) {
            expectedEventCount++;
        }
        assertEquals(
                "Expected event count after model1 -> model2 ("
                + value1 + " -> " + value2 + ").",
                expectedEventCount,
                changeReport.eventCount());
        if (firesEventFromModel1ToModel2) {
            eventsOldValue = changeReport.lastOldValue();
            eventsNewValue = changeReport.lastNewValue();
            assertEquals("model1 -> model2 change fires proper old value.", value1, eventsOldValue);
            assertEquals("model1 -> model2 change fires proper new value.", value2, eventsNewValue);
        }

        adapter.setBean(null);
        boolean firesEventFromModel2ToNull = requiresPropertyChangeEvent(value2, null);
        if (firesEventFromModel2ToNull) {
            expectedEventCount++;
        }
        assertEquals(
                "Expected event count after model2 -> null ("
                + value2 + " -> " + null + ").",
                expectedEventCount,
                changeReport.eventCount());
        if (firesEventFromModel2ToNull) {
            eventsOldValue = changeReport.lastOldValue();
            eventsNewValue = changeReport.lastNewValue();
            assertEquals("model2 -> null change fires proper old value.", value2, eventsOldValue);
            assertEquals("model2 -> null change fires proper new value.", null, eventsNewValue);
        }
    }


    private void testPropertyAdapterValueChange(
            Object value1, Object value2) {
        testValueChange(
                value1,
                value2,
                new PropertyAdapter<TestBean>(model1, "readWriteObjectProperty", true));
    }


    private void testBufferedValueModelSubjectChange(
            Object value1,
            Object value2) {
        model1.setReadWriteObjectProperty(value1);
        model2.setReadWriteObjectProperty(value2);

        PropertyAdapter<TestBean> adapter1 = new PropertyAdapter<TestBean>(model1, "readWriteObjectProperty", true);
        PropertyAdapter<TestBean> adapter2 = new PropertyAdapter<TestBean>(model2, "readWriteObjectProperty", true);

        BufferedValueModel buffer = new BufferedValueModel(null, new Trigger());
        PropertyChangeReport changeReport = new PropertyChangeReport();
        buffer.addValueChangeListener(changeReport);
        Object eventsOldValue;
        Object eventsNewValue;
        int expectedEventCount = 0;

        buffer.setSubject(adapter1);
        boolean firesEventFromNullToModel1 = requiresPropertyChangeEvent(null, value1);
        if (firesEventFromNullToModel1) {
            expectedEventCount++;
        }
        assertEquals(
                "Expected event count after null -> adapter1 ("
                + null + " -> " + value2 + ").",
                expectedEventCount,
                changeReport.eventCount());
        if (firesEventFromNullToModel1) {
            eventsOldValue = changeReport.lastOldValue();
            eventsNewValue = changeReport.lastNewValue();
            assertEquals("null-> adapter1 change fires proper old value.", null, eventsOldValue);
            assertEquals("null-> adapter1 change fires proper new value.", value1, eventsNewValue);
        }

        buffer.setSubject(adapter2);
        boolean firesEventFromModel1ToModel2 = requiresPropertyChangeEvent(value1, value2);
        if (firesEventFromModel1ToModel2) {
            expectedEventCount++;
        }
        assertEquals(
                "Expected event count after adapter1 -> adapter2 ("
                + value1 + " -> " + value2 + ").",
                expectedEventCount,
                changeReport.eventCount());
        if (firesEventFromModel1ToModel2) {
            eventsOldValue = changeReport.lastOldValue();
            eventsNewValue = changeReport.lastNewValue();
            assertEquals("adapter1 -> adapter2 change fires proper old value.", value1, eventsOldValue);
            assertEquals("adapter1 -> adapter2 change fires proper new value.", value2, eventsNewValue);
        }

        buffer.setSubject(null);
        boolean firesEventFromModel2ToNull = requiresPropertyChangeEvent(value2, null);
        if (firesEventFromModel2ToNull) {
            expectedEventCount++;
        }
        assertEquals(
                "Expected event count after adapter2 -> null ("
                + value2 + " -> " + null + ").",
                expectedEventCount,
                changeReport.eventCount());
        if (firesEventFromModel2ToNull) {
            eventsOldValue = changeReport.lastOldValue();
            eventsNewValue = changeReport.lastNewValue();
            assertEquals("adapter2 -> null change fires proper old value.", value2, eventsOldValue);
            assertEquals("adapter2 -> null change fires proper new value.", null, eventsNewValue);
        }
    }


    private void testBufferedValueModelSubjectValueChange(
            Object value1, Object value2) {
        ValueModel valueModel = new PropertyAdapter<TestBean>(model1, "readWriteObjectProperty", true);
        ValueModel triggerChannel = new Trigger();
        testValueChange(
                value1,
                value2,
                new BufferedValueModel(valueModel, triggerChannel));
    }

    private void testValueChange(
            Object value1, Object value2, ValueModel valueModel) {
        model1.setReadWriteObjectProperty(null);
        PropertyChangeReport changeReport = new PropertyChangeReport();
        valueModel.addValueChangeListener(changeReport);
        Object eventsOldValue;
        Object eventsNewValue;
        int expectedEventCount = 0;

        model1.setReadWriteObjectProperty(value1, true);
        boolean firesEventFromNullToValue1 = requiresPropertyChangeEventWithNull(null, value1);
        if (firesEventFromNullToValue1) {
            expectedEventCount++;
        }
        assertEquals(
                "Expected event count after ( null -> "
                + value2 + ").",
                expectedEventCount,
                changeReport.eventCount());
        if (firesEventFromNullToValue1) {
            eventsOldValue = changeReport.lastOldValue();
            eventsNewValue = changeReport.lastNewValue();
            assertEquals("null-> model1 change fires proper old value.", null, eventsOldValue);
            assertEquals("null-> model1 change fires proper new value.", value1, eventsNewValue);
        }

        model1.setReadWriteObjectProperty(value2, true);
        boolean firesEventFromValue1ToValue2 = requiresPropertyChangeEventWithNull(value1, value2);
        if (firesEventFromValue1ToValue2) {
            expectedEventCount++;
        }
        assertEquals(
                "Expected event count after ("
                + value1 + " -> " + value2 + ").",
                expectedEventCount,
                changeReport.eventCount());
        if (firesEventFromValue1ToValue2) {
            eventsOldValue = changeReport.lastOldValue();
            eventsNewValue = changeReport.lastNewValue();
            assertEquals("model1 -> model2 change fires proper old value.", value1, eventsOldValue);
            assertEquals("model1 -> model2 change fires proper new value.", value2, eventsNewValue);
        }

        model1.setReadWriteObjectProperty(null, true);
        boolean firesEventFromValue2ToNull = requiresPropertyChangeEventWithNull(value2, null);
        if (firesEventFromValue2ToNull) {
            expectedEventCount++;
        }
        assertEquals(
                "Expected event count after ("
                + value2 + " -> " + null + ").",
                expectedEventCount,
                changeReport.eventCount());
        if (firesEventFromValue2ToNull) {
            eventsOldValue = changeReport.lastOldValue();
            eventsNewValue = changeReport.lastNewValue();
            assertEquals("model2 -> null change fires proper old value.", value2, eventsOldValue);
            assertEquals("model2 -> null change fires proper new value.", null, eventsNewValue);
        }
    }


    // Helper Code ************************************************************

    private Object[][] createOldAndNewValuePairs() {
        String sameValue = "same value";
        List<String> list1a = Collections.emptyList();
        List<String> list1b = new LinkedList<String>();
        List<String> list2a = new ArrayList<String>(); list2a.add("one");
        List<String> list2b = new ArrayList<String>(list2a);
        List<String> list3  = new ArrayList<String>(list2a); list3.add("two");
        return new Object[][] {
                {null,          null},
                {null,          "value"},
                {"value",       null},
                {sameValue,     sameValue},
                {"equal value", new String("equal value")},
                {"value1",      "value2"},
                {new Float(1),  new Float(1)},
                {new Float(1),  new Float(2)},
                {Boolean.TRUE,  new Boolean(true)},
                {list1a,        list1a},
                {list1a,        list1b},
                {list1a,        list2a},
                {list2a,        list2a},
                {list2a,        list2b},
                {list2a,        list3},
        };
    }


    /**
     * Checks and answers whether changing a property from value1 to value2
     * requires to send a PropertyChangeEvent. A future version may be
     * placed in class Model or ExtendedPropertyChangeSupport; it may test
     * for known core types that can be compared via #equals, not ==.
     */
    private boolean requiresPropertyChangeEvent(Object value1, Object value2) {
        return value1 != value2;
    }


    /**
     * Checks and answers whether changing a property from value1 to value2
     * requires to send a PropertyChangeEvent. A future version may be
     * placed in class Model or ExtendedPropertyChangeSupport; it may test
     * for known core types that can be compared via #equals, not ==.
     */
    private boolean requiresPropertyChangeEventWithNull(Object value1, Object value2) {
        return (value1 != value2) || (value1 == null && value2 == null);
    }


}
