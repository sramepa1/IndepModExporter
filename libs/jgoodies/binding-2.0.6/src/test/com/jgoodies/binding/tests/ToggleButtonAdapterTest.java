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

import junit.framework.TestCase;

import com.jgoodies.binding.BindingUtils;
import com.jgoodies.binding.adapter.ToggleButtonAdapter;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.tests.beans.TestBean;
import com.jgoodies.binding.tests.event.ChangeReport;
import com.jgoodies.binding.tests.event.ItemChangeReport;
import com.jgoodies.binding.tests.value.RejectingValueModel;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

/**
 * A test case for class {@link ToggleButtonAdapter}.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.11 $
 */
public final class ToggleButtonAdapterTest extends TestCase {


    // Constructor Tests ******************************************************

    public void testConstructorRejectsNullSubject() {
        try {
            new ToggleButtonAdapter(null);
            fail("ToggleButtonAdapter(ValueModel) failed to reject a null subject.");
        } catch (NullPointerException ex) {
            // The expected behavior
        }
        try {
            new ToggleButtonAdapter(null, "one", "two");
            fail("ToggleButtonAdapter(ValueModel, Object, Object) failed to reject a null subject.");
        } catch (NullPointerException ex) {
            // The expected behavior
        }
    }

    public void testConstructorRejectsIdenticalSelectionAndDeselectionValues() {
        Object value = "selected";
        try {
            new ToggleButtonAdapter(new ValueHolder(), value, value);
            fail("ToggleButtonAdapter(ValueModel, Object, Object) must reject identical values for selected and deselected.");
        } catch (IllegalArgumentException ex) {
            // The expected behavior
        }
        try {
            new ToggleButtonAdapter(new ValueHolder(), new Integer(1), new Integer(1));
            fail("ToggleButtonAdapter(ValueModel, Object, Object) must reject equal values for selected and deselected.");
        } catch (IllegalArgumentException ex) {
            // The expected behavior
        }
        try {
            new ToggleButtonAdapter(new ValueHolder(), null, null);
            fail("ToggleButtonAdapter(ValueModel, Object, Object) must reject null for both the selected and deselected value.");
        } catch (IllegalArgumentException ex) {
            // The expected behavior
        }
    }


    // Basic Adapter Features *************************************************

    public void testAdaptsReadWriteBooleanProperty() {
        TestBean bean = new TestBean();
        bean.setReadWriteBooleanProperty(true);
        ValueModel subject = new PropertyAdapter<TestBean>(bean, "readWriteBooleanProperty", true);
        ToggleButtonAdapter adapter = new ToggleButtonAdapter(subject);

        // Reading
        assertTrue("Adapter is selected.", adapter.isSelected());

        bean.setReadWriteBooleanProperty(false);
        assertFalse("Adapter is deselected.", adapter.isSelected());

        bean.setReadWriteBooleanProperty(true);
        assertTrue("Adapter is selected again.", adapter.isSelected());

        // Writing
        adapter.setSelected(false);
        assertFalse("Adapted property is false.", bean.isReadWriteBooleanProperty());

        adapter.setSelected(true);
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
            Object selectedValue,
            Object deselectedValue) {
        ValueModel subject = new ValueHolder(initialValue);
        ToggleButtonAdapter adapter = new ToggleButtonAdapter(
                subject, selectedValue, deselectedValue);

        // Reading
        assertEquals("Adapter selection reflects the initial subject value.",
                BindingUtils.equals(initialValue, selectedValue),
                adapter.isSelected());

        subject.setValue(selectedValue);
        assertTrue("Adapter is selected again.", adapter.isSelected());

        subject.setValue(deselectedValue);
        assertFalse("Adapter is deselected.", adapter.isSelected());

        // Writing
        adapter.setSelected(true);
        assertEquals("The subject value is the selected value.",
                selectedValue,
                subject.getValue());

        adapter.setSelected(true);
        assertEquals("The subject value is still the selected value.",
                selectedValue,
                subject.getValue());

        adapter.setSelected(false);
        assertEquals("The subject value is the deselected value.",
                deselectedValue,
                subject.getValue());

        adapter.setSelected(false);
        assertEquals("The subject value is still the deselected value.",
                deselectedValue,
                subject.getValue());
    }


    // Change and Item Events *************************************************

    public void testFiresChangeAndItemEvents() {
        Object selectedValue   = "selected";
        Object deselectedValue = "deselected";
        TestBean bean = new TestBean();
        bean.setReadWriteObjectProperty(selectedValue);
        ValueModel subject = new PropertyAdapter<TestBean>(bean, "readWriteObjectProperty", true);
        ToggleButtonAdapter adapter = new ToggleButtonAdapter(subject, selectedValue, deselectedValue);

        ChangeReport changeReport = new ChangeReport();
        ItemChangeReport itemChangeReport = new ItemChangeReport();
        adapter.addChangeListener(changeReport);
        adapter.addItemListener(itemChangeReport);

        bean.setReadWriteObjectProperty(deselectedValue);
        assertEquals("Deselecting the property fires a ChangeEvent.",
                1,
                changeReport.eventCount());
        assertEquals("Deselecting the property fires an ItemChangeEvent.",
                1,
                itemChangeReport.eventCount());
        assertTrue("The last ItemChangeEvent indicates deselected.",
                itemChangeReport.isLastStateChangeDeselected());

        bean.setReadWriteObjectProperty(selectedValue);
        assertEquals("Selecting the property fires another ChangeEvent.",
                2,
                changeReport.eventCount());
        assertEquals("Selecting the property fires another ItemChangeEvent.",
                2,
                itemChangeReport.eventCount());
        assertTrue("The last ItemChangeEvent indicates selected.",
                itemChangeReport.isLastStateChangeSelected());

        adapter.setSelected(false);
        assertEquals("Deselecting the adapter fires a single ChangeEvent.",
                3,
                changeReport.eventCount());
        assertEquals("Deselecting the adapter fires a single ItemChangeEvent.",
                3,
                itemChangeReport.eventCount());
        assertTrue("The last ItemChangeEvent indicates deselected.",
                itemChangeReport.isLastStateChangeDeselected());

        adapter.setSelected(true);
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
        Object selectedValue   = "selected";
        Object deselectedValue = "deselected";
        TestBean bean = new TestBean();
        bean.fireChangeOnReadOnlyObjectProperty(selectedValue);
        ValueModel subject = new PropertyAdapter<TestBean>(bean, "readOnlyObjectProperty", true);
        ToggleButtonAdapter adapter = new ToggleButtonAdapter(subject, selectedValue, deselectedValue);

        assertTrue("Adapter is selected.", adapter.isSelected());

        bean.fireChangeOnReadOnlyObjectProperty(deselectedValue);
        assertFalse("Adapter is deselected.", adapter.isSelected());

        bean.fireChangeOnReadOnlyObjectProperty(selectedValue);
        assertTrue("Adapter is selected again.", adapter.isSelected());
    }


    // Re-Synchronizes if the Subject Change is Rejected **********************

    public void testResynchronizesAfterRejectedSubjectChange() {
        ValueModel selectionHolder      = new ValueHolder(Boolean.TRUE);
        ValueModel rejectingValueHolder = new RejectingValueModel(selectionHolder);
        ToggleButtonAdapter adapter =
            new ToggleButtonAdapter(rejectingValueHolder);

        assertTrue("Adapter is selected.", adapter.isSelected());
        adapter.setSelected(false);
        assertTrue("Adapter is still selected.", adapter.isSelected());
    }


}
