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

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;

import junit.framework.TestCase;

import com.jgoodies.binding.BindingUtils;
import com.jgoodies.binding.adapter.RadioButtonAdapter;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.tests.beans.TestBean;
import com.jgoodies.binding.tests.event.ChangeReport;
import com.jgoodies.binding.tests.event.ItemChangeReport;
import com.jgoodies.binding.tests.value.RejectingValueModel;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;


/**
 * A test case for class {@link RadioButtonAdapter}.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.15 $
 */
public final class RadioButtonAdapterTest extends TestCase {

    // Parameter Tests ******************************************************

    public void testConstructorRejectsNullSubject() {
        try {
            new RadioButtonAdapter(null, "choice");
            fail("RadioButtonAdapter(ValueModel, Object) failed to reject a null subject.");
        } catch (NullPointerException ex) {
            // The expected behavior
        }
    }


    public void testRejectsButtonGroup() {
        ButtonModel model = new RadioButtonAdapter(new ValueHolder(), "wow");
        ButtonGroup group = new ButtonGroup();
        try {
            model.setGroup(group);
            fail("RadioButtonAdapter.setGroup(ButtonGroup) must reject all ButtonGroups.");
        } catch (UnsupportedOperationException ex) {
            // The expected behavior
        }
    }


    // Basic Adapter Features *************************************************

    public void testAdaptsReadWriteBooleanProperty() {
        TestBean bean = new TestBean();
        bean.setReadWriteBooleanProperty(true);
        ValueModel subject = new PropertyAdapter<TestBean>(bean, "readWriteBooleanProperty", true);
        RadioButtonAdapter adapter1 = new RadioButtonAdapter(subject, Boolean.TRUE);
        RadioButtonAdapter adapter2 = new RadioButtonAdapter(subject, Boolean.FALSE);

        // Reading
        assertTrue ("Adapter1 is selected.",   adapter1.isSelected());
        assertFalse("Adapter2 is deselected.", adapter2.isSelected());

        bean.setReadWriteBooleanProperty(false);
        assertFalse("Adapter1 is deselected.", adapter1.isSelected());
        assertTrue ("Adapter2 is selected.",   adapter2.isSelected());

        bean.setReadWriteBooleanProperty(true);
        assertTrue ("Adapter1 is selected again.",   adapter1.isSelected());
        assertFalse("Adapter2 is deselected again.", adapter2.isSelected());

        // Writing
        adapter2.setSelected(true);
        assertFalse("Adapted property is false.", bean.isReadWriteBooleanProperty());

        adapter1.setSelected(true);
        assertTrue("Adapted property is true.", bean.isReadWriteBooleanProperty());
    }


    public void testReadWriteOperations() {
        testReadWriteOperations(null,  "one", "two");
        testReadWriteOperations(null,  null,  "two");
        testReadWriteOperations(null,  "one", null);
        testReadWriteOperations("one", "one", "two");
        testReadWriteOperations("one", null,  "two");
        testReadWriteOperations("one", "one", null);
        testReadWriteOperations("two", "one", "two");
        testReadWriteOperations("two", null,  "two");
        testReadWriteOperations("two", "one", null);
    }


    private void testReadWriteOperations(
            Object initialValue,
            Object value1,
            Object value2) {
        ValueModel subject = new ValueHolder(initialValue);
        RadioButtonAdapter adapter1 = new RadioButtonAdapter(subject, value1);
        RadioButtonAdapter adapter2 = new RadioButtonAdapter(subject, value2);

        // Reading
        assertEquals("Adapter1 selection reflects the initial subject value.",
                BindingUtils.equals(initialValue, value1),
                adapter1.isSelected());
        assertEquals("Adapter2 selection reflects the initial subject value.",
                BindingUtils.equals(initialValue, value2),
                adapter2.isSelected());

        subject.setValue(value1);
        assertTrue ("Adapter1 is selected.",   adapter1.isSelected());
        assertFalse("Adapter2 is deselected.", adapter2.isSelected());

        subject.setValue(value2);
        assertFalse("Adapter1 is deselected again.", adapter1.isSelected());
        assertTrue ("Adapter2 is selected again.",   adapter2.isSelected());

        // Writing
        adapter1.setSelected(true);
        assertEquals("The subject value is value1.",
                value1,
                subject.getValue());

        adapter1.setSelected(true);
        assertEquals("The subject value is still value1.",
                value1,
                subject.getValue());

        adapter2.setSelected(false);
        assertEquals("Deselecting an adapter doesn't modify the subject value.",
                value1,
                subject.getValue());

        adapter2.setSelected(true);
        assertEquals("The subject value is value2.",
                value2,
                subject.getValue());

        adapter2.setSelected(true);
        assertEquals("The subject value is still value2.",
                value2,
                subject.getValue());
    }


    // Change and Item Events *************************************************

    public void testFiresChangeAndItemEvents() {
        Object value1 = "value1";
        Object value2 = "value2";
        TestBean bean = new TestBean();
        bean.setReadWriteObjectProperty(value1);
        ValueModel subject = new PropertyAdapter<TestBean>(bean, "readWriteObjectProperty", true);
        RadioButtonAdapter adapter1 = new RadioButtonAdapter(subject, value1);
        RadioButtonAdapter adapter2 = new RadioButtonAdapter(subject, value2);

        ChangeReport changeReport = new ChangeReport();
        ItemChangeReport itemChangeReport = new ItemChangeReport();
        adapter1.addChangeListener(changeReport);
        adapter1.addItemListener(itemChangeReport);

        bean.setReadWriteObjectProperty(value2);
        assertEquals("Deselecting the property fires a ChangeEvent.",
                1,
                changeReport.eventCount());
        assertEquals("Deselecting the property fires an ItemChangeEvent.",
                1,
                itemChangeReport.eventCount());
        assertTrue("The last ItemChangeEvent indicates deselected.",
                itemChangeReport.isLastStateChangeDeselected());

        bean.setReadWriteObjectProperty(value1);
        assertEquals("Selecting the property fires another ChangeEvent.",
                2,
                changeReport.eventCount());
        assertEquals("Selecting the property fires another ItemChangeEvent.",
                2,
                itemChangeReport.eventCount());
        assertTrue("The last ItemChangeEvent indicates selected.",
                itemChangeReport.isLastStateChangeSelected());

        adapter1.setSelected(false);
        assertEquals("Deselecting the adapter fires no ChangeEvent.",
                2,
                changeReport.eventCount());
        assertEquals("Deselecting the adapter fires no ItemChangeEvent.",
                2,
                itemChangeReport.eventCount());
        assertTrue("The last ItemChangeEvent indicates selected.",
                itemChangeReport.isLastStateChangeSelected());

        adapter2.setSelected(true);
        assertEquals("Selecting a grouped adapter fires a single ChangeEvent.",
                3,
                changeReport.eventCount());
        assertEquals("Selecting a grouped adapter fires a single ItemChangeEvent.",
                3,
                itemChangeReport.eventCount());
        assertTrue("The last ItemChangeEvent indicates deselected.",
                itemChangeReport.isLastStateChangeDeselected());

        adapter1.setSelected(true);
        assertEquals("Selecting the adapter fires another ChangeEvent.",
                4,
                changeReport.eventCount());
        assertEquals("Selecting the adapter fires another ItemChangeEvent.",
                4,
                itemChangeReport.eventCount());
        assertTrue("The last ItemChangeEvent indicates selected.",
                itemChangeReport.isLastStateChangeSelected());
    }


    // Read-Only Model ********************************************************

    public void testAdaptsReadOnlyObjectProperty() {
        Object value1 = "value1";
        Object value2 = "value2";
        TestBean bean = new TestBean();
        bean.fireChangeOnReadOnlyObjectProperty(value1);
        ValueModel subject = new PropertyAdapter<TestBean>(bean, "readOnlyObjectProperty", true);
        RadioButtonAdapter adapter = new RadioButtonAdapter(subject, value1);

        assertTrue("Adapter is selected.", adapter.isSelected());

        bean.fireChangeOnReadOnlyObjectProperty(value2);
        assertFalse("Adapter is deselected.", adapter.isSelected());

        bean.fireChangeOnReadOnlyObjectProperty(value1);
        assertTrue("Adapter is selected again.", adapter.isSelected());
    }


    // Re-Synchronizes if the Subject Change is Rejected **********************

    public void testResynchronizesAfterRejectedSubjectChange() {
        ValueModel selectionHolder = new ValueHolder(Boolean.FALSE);
        ValueModel rejectingSelectionHolder = new RejectingValueModel(selectionHolder);
        RadioButtonAdapter adapter =
            new RadioButtonAdapter(rejectingSelectionHolder, Boolean.TRUE);

        assertFalse("Adapter is deselected.", adapter.isSelected());
        adapter.setSelected(true);
        assertFalse("Adapter is still deselected.", adapter.isSelected());
    }


}
