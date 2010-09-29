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

import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.binding.tests.beans.TestBean;
import com.jgoodies.binding.tests.event.PropertyChangeReport;
import com.jgoodies.binding.tests.value.ToUpperCaseStringHolder;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

/**
 * A test case for class {@link PropertyConnector}.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.17 $
 */
public final class PropertyConnectorTest extends TestCase {

    private PropertyConnector createDefaultConnector(TestBean bean1, TestBean bean2) {
        return PropertyConnector.connect(
            bean1,
            "readWriteObjectProperty",
            bean2,
            "readWriteObjectProperty");
    }

    private PropertyConnector createDefaultConnector(TestBean bean1, Object initialValue1, TestBean bean2, Object initialValue2) {
        bean1.setReadWriteObjectProperty(initialValue1);
        bean2.setReadWriteObjectProperty(initialValue2);
        return createDefaultConnector(bean1, bean2);
    }


    // Constructor Tests ******************************************************

    public void testConstructorRejectsNullParameters() {
        TestBean bean1;
        TestBean bean2;
        bean1 = new TestBean();
        bean2 = new TestBean();
        try {
            PropertyConnector.connect(null, "readWriteObjectProperty", bean2, "readWriteObjectProperty");
            fail("Constructor failed to reject bean1==null.");
        } catch (NullPointerException ex) {
            // The expected behavior
        }
        bean1 = new TestBean();
        bean2 = new TestBean();
        try {
            PropertyConnector.connect(bean1, null, bean2, "readWriteObjectProperty");
            fail("Constructor failed to reject property1Name==null.");
        } catch (NullPointerException ex) {
            // The expected behavior
        }
        bean1 = new TestBean();
        bean2 = new TestBean();
        try {
            PropertyConnector.connect(bean1, "readWriteObjectProperty", null, "readWriteObjectProperty");
            fail("Constructor failed to reject bean2==null.");
        } catch (NullPointerException ex) {
            // The expected behavior
        }
        bean1 = new TestBean();
        bean2 = new TestBean();
        try {
            PropertyConnector.connect(bean1, "readWriteObjectProperty", bean2, null);
            fail("Constructor failed to reject property2Name==null.");
        } catch (NullPointerException ex) {
            // The expected behavior
        }
    }


    public void testConstructorRejectsToConnectTheSameProperty() {
        TestBean bean = new TestBean();
        try {
            PropertyConnector.connect(bean, "readWriteObjectProperty", bean, "readWriteObjectProperty");
            fail("Constructor failed to reject bean1==bean2 && property1Name.equals(property2Name).");
        } catch (IllegalArgumentException ex) {
            // The expected behavior
        }
    }


    public void testConstructorRejectsWriteOnlyProperty1() {
        TestBean bean = new TestBean();
        try {
            PropertyConnector.connect(bean, "writeOnlyObjectProperty", bean, "readWriteObjectProperty");
            fail("Constructor failed to reject the write only property1.");
        } catch (IllegalArgumentException ex) {
            // The expected behavior
        }
    }


    public void testConstructorRejectsWriteOnlyProperty2() {
        TestBean bean = new TestBean();
        try {
            PropertyConnector.connect(bean, "readWriteObjectProperty", bean, "writeOnlyObjectProperty");
            fail("Constructor failed to reject the write only property2.");
        } catch (IllegalArgumentException ex) {
            // The expected behavior
        }
    }


    public void testConstructorRejectsReadOnlyBeans() {
        TestBean bean1 = new TestBean();
        TestBean bean2 = new TestBean();
        try {
            PropertyConnector.connect(bean1, "readOnlyObjectProperty",
                                      bean2, "readOnlyObjectProperty");
            fail("Constructor failed to reject read-only bean properties.");
        } catch (IllegalArgumentException ex) {
            // The expected behavior
        }
    }

    public void testConstructorLeavesValuesUnchanged() {
        TestBean bean1 = new TestBean();
        TestBean bean2 = new TestBean();
        Object initialValue1 = "initialValue1";
        Object initialValue2 = "initialValue2";
        createDefaultConnector(bean1, initialValue1, bean2, initialValue2);
        assertEquals("The constructor must not change the property1.",
                initialValue1,
                bean1.getReadWriteObjectProperty());
        assertEquals("The constructor must not change the property2.",
                initialValue2,
                bean2.getReadWriteObjectProperty());
    }


    public void testAcceptsMultipleReleases() {
        TestBean bean1 = new TestBean();
        TestBean bean2 = new TestBean();
        Object initialValue1 = "initialValue1";
        Object initialValue2 = "initialValue2";
        PropertyConnector connector =
            createDefaultConnector(bean1, initialValue1, bean2, initialValue2);
        connector.release();
        connector.release();
    }


    // Synchronization ********************************************************

    public void testUpdateProperty1() {
        TestBean bean1 = new TestBean();
        TestBean bean2 = new TestBean();
        Object initialValue2 = "newValue2";
        PropertyConnector connector =
            createDefaultConnector(bean1, "value1", bean2, initialValue2);
        connector.updateProperty1();
        assertEquals(
            "#updateProperty1 failed to update property1.",
            bean1.getReadWriteObjectProperty(),
            bean2.getReadWriteObjectProperty());
    }

    public void testUpdateProperty2() {
        TestBean bean1 = new TestBean();
        TestBean bean2 = new TestBean();
        Object initialValue1 = "newValue1";
        PropertyConnector connector =
            createDefaultConnector(bean1, initialValue1, bean2, "value2");
        connector.updateProperty2();
        assertEquals(
            "#updateProperty2 failed to update property2.",
            bean1.getReadWriteObjectProperty(),
            bean2.getReadWriteObjectProperty());
    }

    public void testProperty1ChangeUpdatesProperty2() {
        TestBean bean1 = new TestBean();
        TestBean bean2 = new TestBean();
        createDefaultConnector(bean1, "value1", bean2, "value2");
        bean1.setReadWriteObjectProperty("newValue1");
        assertEquals(
            "Failed to update property2 after a named change.",
            bean1.getReadWriteObjectProperty(),
            bean2.getReadWriteObjectProperty());
    }

    public void testProperty2ChangeUpdatesProperty1() {
        TestBean bean1 = new TestBean();
        TestBean bean2 = new TestBean();
        createDefaultConnector(bean1, "value1", bean2, "value2");
        bean2.setReadWriteObjectProperty("newValue1");
        assertEquals(
            "Failed to update property1 after a named change.",
            bean1.getReadWriteObjectProperty(),
            bean2.getReadWriteObjectProperty());
    }

    public void testProperty1AnonymousChangeUpdatesProperty2() {
        TestBean bean1 = new TestBean();
        TestBean bean2 = new TestBean();
        createDefaultConnector(bean1, "value1", bean2, "value2");
        bean1.setReadWriteObjectProperties("newValue1", true, 42);
        assertEquals(
            "Failed to update property2 after an unnamed change.",
            bean1.getReadWriteObjectProperty(),
            bean2.getReadWriteObjectProperty());
    }

    public void testProperty2AnonymousChangeUpdatesProperty1() {
        TestBean bean1 = new TestBean();
        TestBean bean2 = new TestBean();
        createDefaultConnector(bean1, "value1", bean2, "value2");
        bean2.setReadWriteObjectProperties("newValue1", true, 42);
        assertEquals(
            "Failed to update property1 after an unnamed change.",
            bean1.getReadWriteObjectProperty(),
            bean2.getReadWriteObjectProperty());
    }


    // Avoid Unnecessary Events ***********************************************

    public void testAvoidUnnecessaryChangeEvents() {
        TestBean bean1 = new TestBean();
        TestBean bean2 = new TestBean();
        createDefaultConnector(bean1, "value1", bean2, null);
        PropertyChangeReport report = new PropertyChangeReport();
        bean2.addPropertyChangeListener("readWriteObjectProperty", report);
        assertEquals(
                "No changes.",
                0,
                report.eventCount());
        bean1.setReadWriteObjectProperty(null);
        assertEquals(
                "bean2 remains the same",
                0,
                report.eventCount());
        bean1.setReadWriteObjectProperty("newValue1");
        assertEquals(
                "bean2 updated",
                1,
                report.eventCount());
    }


    // Events that lack the new value *****************************************

    public void testProperty1ChangeWithNullEventNewValueUpdatesProperty2() {
        TestBean bean1 = new TestBean();
        TestBean bean2 = new TestBean();
        PropertyConnector.connect(
                bean1,
                "nullNewValueProperty",
                bean2,
                "readWriteObjectProperty");
        bean1.setNullNewValueProperty("newValue1");
        assertEquals(
            "Failed to update property2 after a change with event that has a null new value.",
            bean1.getNullNewValueProperty(),
            bean2.getReadWriteObjectProperty());
    }

    public void testProperty2ChangeWithNullEventNewValueUpdatesProperty1() {
        TestBean bean1 = new TestBean();
        TestBean bean2 = new TestBean();
        PropertyConnector.connect(
                bean1,
                "readWriteObjectProperty",
                bean2,
                "nullNewValueProperty");
        bean2.setNullNewValueProperty("newValue1");
        assertEquals(
            "Failed to update property1 after a change with event that has a null new value.",
            bean1.getReadWriteObjectProperty(),
            bean2.getNullNewValueProperty());
    }




    // One-Way Synchronization ************************************************

    public void testConnectReadWriteProperty1WithReadOnlyProperty2() {
        TestBean bean1 = new TestBean();
        TestBean bean2 = new TestBean();
        bean1.setReadWriteObjectProperty("initalValue1");
        PropertyConnector.connect(
                bean1,
                "readWriteObjectProperty",
                bean2,
                "readOnlyObjectProperty");
        testConnectReadWriteWithReadOnlyProperty(bean1, bean2);
    }

    public void testConnectReadOnlyProperty1WithReadWriteProperty2() {
        TestBean bean1 = new TestBean();
        TestBean bean2 = new TestBean();
        bean1.setReadWriteObjectProperty("initalValue1");
        PropertyConnector.connect(
                bean1,
                "readOnlyObjectProperty",
                bean2,
                "readWriteObjectProperty");
        testConnectReadWriteWithReadOnlyProperty(bean2, bean1);
    }


    public void testUpdateHonorsTargetModifications() {
        ValueModel bean1 = new ValueHolder("initalValue");
        ValueModel bean2 = new ToUpperCaseStringHolder();
        PropertyConnector.connect(
                bean1,
                "value",
                bean2,
                "value");
        String newValue = "newValue";
        String newValueUpperCase = newValue.toUpperCase();
        bean1.setValue("newValue");
        assertEquals("Target value is uppercase",
                newValueUpperCase,
                bean2.getValue());
        assertEquals("Source value is uppercase too",
                newValueUpperCase,
                bean1.getValue());
    }


    public void testConnectReadOnlyWithModifyingTarget() {
        TestBean bean1 = new TestBean();
        ValueModel bean2 = new ToUpperCaseStringHolder();
        bean1.readOnlyObjectProperty = "initialValue";
        PropertyConnector.connect(
                bean1,
                "readOnlyObjectProperty",
                bean2,
                "value");
        // Update property1 if the read-only property2 changes.
        String newValue = "newValue";
        bean1.fireChangeOnReadOnlyObjectProperty(newValue);
        assertEquals(
                "Bean2 has the read-only in upper case.",
                newValue.toUpperCase(),
                bean2.getValue());
    }


    // Helper Code ************************************************************

    private void testConnectReadWriteWithReadOnlyProperty(
            TestBean beanWithReadWriteProperty,
            TestBean beanWithReadOnlyProperty) {
        Object initialValue2 = "initialValue2";
        beanWithReadOnlyProperty.readOnlyObjectProperty = initialValue2;

        // Ignore updates of property1.
        beanWithReadWriteProperty.setReadWriteObjectProperty("newValue1");
        assertEquals(
            "The connector must not update property2.",
            initialValue2,
            beanWithReadOnlyProperty.getReadOnlyObjectProperty());

        // Update property1 if the read-only property2 changes.
        Object newValue2 = "newValue2";
        beanWithReadOnlyProperty.fireChangeOnReadOnlyObjectProperty(newValue2);
        assertEquals(
                "Bean2 has a new value for the read-only property2.",
                newValue2,
                beanWithReadOnlyProperty.getReadOnlyObjectProperty());
        assertEquals(
            "The connector must update property1.",
            newValue2,
            beanWithReadWriteProperty.getReadWriteObjectProperty());

        // Ignore subsequent updates of property1.
        beanWithReadWriteProperty.setReadWriteObjectProperty("newValue1b");
        assertEquals(
            "The connector must not update property2.",
            newValue2,
            beanWithReadOnlyProperty.getReadOnlyObjectProperty());
    }


}
