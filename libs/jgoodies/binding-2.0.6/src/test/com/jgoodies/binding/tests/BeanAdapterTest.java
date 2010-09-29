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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;

import junit.framework.TestCase;

import com.jgoodies.binding.beans.*;
import com.jgoodies.binding.tests.beans.*;
import com.jgoodies.binding.tests.event.PropertyChangeReport;
import com.jgoodies.binding.tests.value.ValueHolderWithNewValueNull;
import com.jgoodies.binding.tests.value.ValueHolderWithOldAndNewValueNull;
import com.jgoodies.binding.tests.value.ValueHolderWithOldValueNull;
import com.jgoodies.binding.value.AbstractValueModel;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

/**
 * A test case for class {@link com.jgoodies.binding.beans.BeanAdapter}.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.29 $
 */
public final class BeanAdapterTest extends TestCase {

    private TestBean model1;
    private TestBean model2;

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


    // Constructor Tests ******************************************************

    /**
     * Verifies that we can adapt observable and non-observable objects
     * if we do not observe changes.
     */
    public void testConstructorsAcceptAllBeansWhenNotObserving() {
        for (Object bean : BeanClasses.getBeans()) {
            try {
                new BeanAdapter<Object>(bean, false);
                new BeanAdapter<Object>(new ValueHolder(bean, true), false);
            } catch (PropertyUnboundException ex) {
                fail("Constructor must not try to observe with observeChanges == false");
            }
        }
    }

    /**
     * Verifies that we can adapt and observe observables.
     */
    public void testConstructorsAcceptToObserveObservables() {
        for (Object bean : BeanClasses.getObservableBeans()) {
            try {
                new BeanAdapter<Object>(bean, true);
                new BeanAdapter<Object>(new ValueHolder(bean, true), true);
            } catch (PropertyUnboundException ex) {
                fail("Constructor failed to accept to observe an observable." +
                     "\nbean class=" + bean.getClass() +
                     "\nexception=" + ex);
            }
        }
    }


    public void testConstructorRejectsNonIdentityCheckingBeanChannel() {
        try {
            new BeanAdapter<Object>(new ValueHolder(null));
            fail("Constructor must reject bean channel that has the identity check feature disabled.");
        } catch (IllegalArgumentException e) {
            // The expected behavior
        }
    }

    /**
     * Verifies that we cannot observe non-observables.
     */
    public void testConstructorsRejectToObserveObservables() {
        for (Object bean : BeanClasses.getUnobservableBeans()) {
            try {
                new BeanAdapter<Object>(bean, true);
                fail("Constructor must reject to observe non-observables.");
            } catch (PropertyUnboundException ex) {
                // The expected behavior
            }
            try {
                new BeanAdapter<Object>(new ValueHolder(bean, true), true);
                fail("Constructor must reject to observe non-observables.");
            } catch (PropertyUnboundException ex) {
                // The expected behavior
            }
        }
    }

    /**
     * Verifies that we cannot observe non-observables.
     */
    public void testConstructorsAcceptsToObserveObjectsThatSupportBoundProperties() {
        for (Object bean : BeanClasses.getBeans()) {
            try {
                boolean supportsBoundProperties =
                    BeanUtils.supportsBoundProperties(bean.getClass());
                new BeanAdapter<Object>(bean, supportsBoundProperties);
                new BeanAdapter<Object>(new ValueHolder(bean, true), supportsBoundProperties);
            } catch (PropertyUnboundException ex) {
                fail("Constructor failed to accept to observe an observable." +
                     "\nbean class=" + bean.getClass() +
                     "\nexception=" + ex);
            }
        }
    }

    public void testConstructorAcceptsNullBean() {
        try {
            new BeanAdapter<Object>((Object) null, true);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            fail("Constructor failed to accept a null bean."
                + "\nexception=" + ex);
        }
    }


    public void testAcceptsMultipleReleases() {
        BeanAdapter<TestBean> adapter = new BeanAdapter<TestBean>(model1);
        adapter.release();
        adapter.release();
    }


    // Null As Property Name **************************************************

    public void testRejectNullPropertyName() {
        testRejectNullPropertyName(null);
        testRejectNullPropertyName(new TestBean());
    }

    private void testRejectNullPropertyName(Object bean) {
        BeanAdapter<Object> adapter = new BeanAdapter<Object>(bean);
        try {
            adapter.getValue(null);
            fail("#getValue(null) should throw an NPE.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            adapter.setValue(null, null);
            fail("#setValue(null, Object) should throw an NPE.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            adapter.setVetoableValue(null, null);
            fail("#setVetoableValue(null, Object) should throw an NPE.");
        } catch (NullPointerException e) {
            // The expected behavior
        } catch (PropertyVetoException e) {
            fail("An NPE should be thrown.");
        }
        try {
            adapter.getValueModel(null);
            fail("#getValueModel(null) should throw an NPE.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            adapter.getValueModel(null, "readA", "writeA");
            fail("#getValueModel(null, String, String) should throw an NPE.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
    }


    // Testing Property Access ************************************************

    /**
     */
    public void testReadWriteProperties() {
        TestBean bean = new TestBean();
        bean.setReadWriteObjectProperty("initialValue");
        bean.setReadWriteBooleanProperty(false);
        bean.setReadWriteIntProperty(42);
        ValueModel objectAdapter  = new BeanAdapter<TestBean>(bean).getValueModel("readWriteObjectProperty");
        ValueModel booleanAdapter = new BeanAdapter<TestBean>(bean).getValueModel("readWriteBooleanProperty");
        ValueModel intAdapter     = new BeanAdapter<TestBean>(bean).getValueModel("readWriteIntProperty");
        ValueModel integerAdapter = new BeanAdapter<TestBean>(bean).getValueModel("readWriteIntegerProperty");

        // Adapter values equal the bean property values.
        assertEquals(objectAdapter.getValue(),  bean.getReadWriteObjectProperty());
        assertEquals(booleanAdapter.getValue(), Boolean.valueOf(bean.isReadWriteBooleanProperty()));
        assertEquals(intAdapter.getValue(),     new Integer(bean.getReadWriteIntProperty()));
        assertEquals(integerAdapter.getValue(), bean.getReadWriteIntegerProperty());

        Object objectValue   = "testString";
        Boolean booleanValue = Boolean.TRUE;
        Integer integerValue = new Integer(43);

        objectAdapter.setValue(objectValue);
        booleanAdapter.setValue(booleanValue);
        intAdapter.setValue(integerValue);
        integerAdapter.setValue(integerValue);
        assertEquals(objectValue,  bean.getReadWriteObjectProperty());
        assertEquals(booleanValue, Boolean.valueOf(bean.isReadWriteBooleanProperty()));
        assertEquals(integerValue, new Integer(bean.getReadWriteIntProperty()));
        assertEquals(integerValue, bean.getReadWriteIntegerProperty());
    }

    public void testReadWriteCustomProperties() {
        CustomAccessBean bean = new CustomAccessBean();
        bean.writeRWObjectProperty("initialValue");
        bean.writeRWBooleanProperty(false);
        bean.writeRWIntProperty(42);
        ValueModel objectAdapter  = new BeanAdapter<CustomAccessBean>(bean).getValueModel("readWriteObjectProperty",  "readRWObjectProperty",  "writeRWObjectProperty");
        ValueModel booleanAdapter = new BeanAdapter<CustomAccessBean>(bean).getValueModel("readWriteBooleanProperty", "readRWBooleanProperty", "writeRWBooleanProperty");
        ValueModel intAdapter     = new BeanAdapter<CustomAccessBean>(bean).getValueModel("readWriteIntProperty",     "readRWIntProperty",     "writeRWIntProperty");

        // Adapter values equal the bean property values.
        assertEquals(objectAdapter.getValue(),  bean.readRWObjectProperty());
        assertEquals(booleanAdapter.getValue(), Boolean.valueOf(bean.readRWBooleanProperty()));
        assertEquals(intAdapter.getValue(),     new Integer(bean.readRWIntProperty()));

        Object objectValue   = "testString";
        Boolean booleanValue = Boolean.TRUE;
        Integer integerValue = new Integer(43);

        objectAdapter.setValue(objectValue);
        booleanAdapter.setValue(booleanValue);
        intAdapter.setValue(integerValue);
        assertEquals(objectValue,  bean.readRWObjectProperty());
        assertEquals(booleanValue, Boolean.valueOf(bean.readRWBooleanProperty()));
        assertEquals(integerValue, new Integer(bean.readRWIntProperty()));
    }

    public void testReadOnlyProperties() {
        TestBean bean = new TestBean();
        bean.readOnlyObjectProperty  = "testString";
        bean.readOnlyBooleanProperty = true;
        bean.readOnlyIntProperty     = 42;
        ValueModel objectAdapter  = new BeanAdapter<TestBean>(bean).getValueModel("readOnlyObjectProperty");
        ValueModel booleanAdapter = new BeanAdapter<TestBean>(bean).getValueModel("readOnlyBooleanProperty");
        ValueModel intAdapter     = new BeanAdapter<TestBean>(bean).getValueModel("readOnlyIntProperty");

        // Adapter values equal the bean property values.
        assertEquals(objectAdapter.getValue(),  bean.getReadOnlyObjectProperty());
        assertEquals(booleanAdapter.getValue(), Boolean.valueOf(bean.isReadOnlyBooleanProperty()));
        assertEquals(intAdapter.getValue(),     new Integer(bean.getReadOnlyIntProperty()));
        try {
            objectAdapter.setValue("some");
            fail("Adapter must reject writing of read-only properties.");
        } catch (UnsupportedOperationException ex) {
            // The expected behavior
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }


    public void testReadOnlyCustomProperties() {
        CustomAccessBean bean = new CustomAccessBean();
        bean.readOnlyObjectProperty  = "testString";
        bean.readOnlyBooleanProperty = true;
        bean.readOnlyIntProperty     = 42;
        ValueModel objectAdapter  = new BeanAdapter<CustomAccessBean>(bean).getValueModel("readOnlyObjectProperty",  "readROObjectProperty",  null);
        ValueModel booleanAdapter = new BeanAdapter<CustomAccessBean>(bean).getValueModel("readOnlyBooleanProperty", "readROBooleanProperty", null);
        ValueModel intAdapter     = new BeanAdapter<CustomAccessBean>(bean).getValueModel("readOnlyIntProperty",     "readROIntProperty",     null);

        // Adapter values equal the bean property values.
        assertEquals(objectAdapter.getValue(),  bean.readROObjectProperty());
        assertEquals(booleanAdapter.getValue(), Boolean.valueOf(bean.readROBooleanProperty()));
        assertEquals(intAdapter.getValue(),     new Integer(bean.readROIntProperty()));
        try {
            objectAdapter.setValue("some");
            fail("Adapter must reject writing of read-only properties.");
        } catch (UnsupportedOperationException ex) {
            // The expected behavior
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }

    /**
     */
    public void testWriteOnlyProperties() {
        TestBean bean = new TestBean();
        ValueModel objectAdapter  = new BeanAdapter<TestBean>(bean).getValueModel("writeOnlyObjectProperty");
        ValueModel booleanAdapter = new BeanAdapter<TestBean>(bean).getValueModel("writeOnlyBooleanProperty");
        ValueModel intAdapter     = new BeanAdapter<TestBean>(bean).getValueModel("writeOnlyIntProperty");
        Object objectValue   = "testString";
        Boolean booleanValue = Boolean.TRUE;
        Integer integerValue = new Integer(42);
        objectAdapter.setValue(objectValue);
        booleanAdapter.setValue(booleanValue);
        intAdapter.setValue(integerValue);
        assertEquals(objectValue,  bean.writeOnlyObjectProperty);
        assertEquals(booleanValue, Boolean.valueOf(bean.writeOnlyBooleanProperty));
        assertEquals(integerValue, new Integer(bean.writeOnlyIntProperty));
        try {
            objectAdapter.getValue();
            fail("Adapter must reject to read a write-only property.");
        } catch (UnsupportedOperationException ex) {
            // The expected behavior
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }

    public void testWriteOnlyCustomProperties() {
        CustomAccessBean bean = new CustomAccessBean();
        ValueModel objectAdapter  = new BeanAdapter<CustomAccessBean>(bean).getValueModel("writeOnlyObjectProperty",  null, "writeWOObjectProperty");
        ValueModel booleanAdapter = new BeanAdapter<CustomAccessBean>(bean).getValueModel("writeOnlyBooleanProperty", null, "writeWOBooleanProperty");
        ValueModel intAdapter     = new BeanAdapter<CustomAccessBean>(bean).getValueModel("writeOnlyIntProperty",     null, "writeWOIntProperty");

        Object objectValue   = "testString";
        Boolean booleanValue = Boolean.TRUE;
        Integer integerValue = new Integer(42);
        objectAdapter.setValue(objectValue);
        booleanAdapter.setValue(booleanValue);
        intAdapter.setValue(integerValue);
        assertEquals(objectValue,  bean.writeOnlyObjectProperty);
        assertEquals(booleanValue, Boolean.valueOf(bean.writeOnlyBooleanProperty));
        assertEquals(integerValue, new Integer(bean.writeOnlyIntProperty));
        try {
            objectAdapter.getValue();
            fail("Adapter must reject to read a write-only property.");
        } catch (UnsupportedOperationException ex) {
            // The expected behavior
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }


    /**
     * Checks the write access to a constrained property without veto.
     */
    public void testWriteConstrainedPropertyWithoutVeto() {
        TestBean bean = new TestBean();
        BeanAdapter<TestBean>.SimplePropertyAdapter adapter =
            new BeanAdapter<TestBean>(bean).getValueModel("constrainedProperty");
        try {
            bean.setConstrainedProperty("value1");
        } catch (PropertyVetoException e1) {
            fail("Unexpected veto for value1.");
        }
        assertEquals("Bean has the initial value1.",
                bean.getConstrainedProperty(),
                "value1");

        adapter.setValue("value2a");
        assertEquals("Bean now has the value2a.",
                bean.getConstrainedProperty(),
                "value2a");
        try {
            adapter.setVetoableValue("value2b");
        } catch (PropertyVetoException e) {
            fail("Unexpected veto for value2b.");
        }
        assertEquals("Bean now has the value2b.",
                bean.getConstrainedProperty(),
                "value2b");
    }


    /**
     * Checks the write access to a constrained property with veto.
     */
    public void testWriteConstrainedPropertyWithVeto() {
        TestBean bean = new TestBean();
        PropertyAdapter<TestBean> adapter  =
            new PropertyAdapter<TestBean>(bean, "constrainedProperty");
        try {
            bean.setConstrainedProperty("value1");
        } catch (PropertyVetoException e1) {
            fail("Unexpected veto for  value1.");
        }
        assertEquals("Bean has the initial value1.",
                bean.getConstrainedProperty(),
                "value1");

        // Writing with a veto
        bean.addVetoableChangeListener(new VetoableChangeRejector());

        adapter.setValue("value2a");
        assertEquals("Bean still has the value1.",
                bean.getConstrainedProperty(),
                "value1");
        try {
            adapter.setVetoableValue("value2b");
            fail("Couldn't set the valid value1");
        } catch (PropertyVetoException e) {
            PropertyChangeEvent pce = e.getPropertyChangeEvent();
            assertEquals("The veto's old value is value1.",
                    pce.getOldValue(),
                    "value1");
            assertEquals("The veto's new value is value2b.",
                    pce.getNewValue(),
                    "value2b");
        }
        assertEquals("After setting value2b, the bean still has the value1.",
                bean.getConstrainedProperty(),
                "value1");
    }


    /**
     * Verifies that the reader and writer can be located in different
     * levels of a class hierarchy.
     */
    public void testReaderWriterSplittedInHierarchy() {
        ReadWriteHierarchyBean bean = new ReadWriteHierarchyBean();
        ValueModel adapter = new BeanAdapter<ReadWriteHierarchyBean>(bean).getValueModel("property");
        Object value1 = "Ewa";
        String value2 = "Karsten";
        adapter.setValue(value1);
        assertEquals(value1, bean.getProperty());
        bean.setProperty(value2);
        assertEquals(value2, adapter.getValue());
    }

    /**
     * Tests access to properties that are described by a BeanInfo class.
     */
    public void testBeanInfoProperties() {
        CustomBean bean = new CustomBean();
        ValueModel adapter = new BeanAdapter<CustomBean>(bean).getValueModel("readWriteObjectProperty");
        Object value1 = "Ewa";
        String value2 = "Karsten";
        adapter.setValue(value1);
        assertEquals(value1, bean.getReadWriteObjectProperty());
        bean.setReadWriteObjectProperty(value2);
        assertEquals(value2, adapter.getValue());

        try {
            new BeanAdapter<CustomBean>(bean).getValueModel("readWriteIntProperty");
            fail("Adapter must not find properties that " +
                 "have been excluded by a custom BeanInfo.");
        } catch (PropertyNotFoundException e) {
            // The expected behavior
        }
    }


    public void testAbsentProperties() {
        TestBean bean = new TestBean();
        try {
            new BeanAdapter<TestBean>(bean).getValueModel("absentObjectProperty");
            fail("Adapter must reject an absent object property.");
        } catch (PropertyNotFoundException ex) {
            // The expected behavior
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
        try {
            new BeanAdapter<TestBean>(bean).getValueModel("absentBooleanProperty");
            fail("Adapter must reject an absent boolean property.");
        } catch (PropertyNotFoundException ex) {
            // The expected behavior
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
        try {
            new BeanAdapter<TestBean>(bean).getValueModel("absentIntProperty");
            fail("Adapter must reject an absent int property.");
        } catch (PropertyNotFoundException ex) {
            // The expected behavior
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }


    public void testIllegalPropertyAccess() {
        TestBean bean = new TestBean();
        try {
            new BeanAdapter<TestBean>(bean).getValueModel("noAccess").getValue();
            fail("Adapter must report read-access problems.");
        } catch (PropertyAccessException ex) {
            // The expected behavior
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
        try {
            new BeanAdapter<TestBean>(bean).getValueModel("noAccess").setValue("Fails");
            fail("Adapter must report write-access problems.");
        } catch (PropertyAccessException ex) {
            // The expected behavior
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
        try {
            new BeanAdapter<TestBean>(bean).getValueModel("readWriteIntProperty").setValue(1967L);
            fail("Adapter must report IllegalArgumentExceptions.");
        } catch (PropertyAccessException ex) {
            // The expected behavior
        } catch (Exception e) {
            fail("Unexpected exception=" + e);
        }
    }


    // Testing Update Notifications *******************************************

    public void testSetPropertySendsUpdates() {
        BeanAdapter<TestBean> adapter = new BeanAdapter<TestBean>(model1, true);
        String propertyName = "readWriteObjectProperty";
        AbstractValueModel valueModel = adapter.getValueModel(propertyName);

        PropertyChangeReport changeReport = new PropertyChangeReport();
        valueModel.addPropertyChangeListener("value", changeReport);

        model1.setReadWriteObjectProperty("Karsten");
        assertEquals("First property change.", 1, changeReport.eventCount());
        model1.setReadWriteObjectProperty("Ewa");
        assertEquals("Second property change.", 2, changeReport.eventCount());
        model1.setReadWriteObjectProperty(model1.getReadWriteObjectProperty());
        assertEquals("Property change repeated.", 2, changeReport.eventCount());
    }

    public void testSetValueModelSendsUpdates() {
        BeanAdapter<TestBean> adapter = new BeanAdapter<TestBean>(model1, true);
        String propertyName = "readWriteObjectProperty";
        AbstractValueModel valueModel = adapter.getValueModel(propertyName);

        PropertyChangeReport changeReport = new PropertyChangeReport();
        valueModel.addPropertyChangeListener("value", changeReport);

        valueModel.setValue("Johannes");
        assertEquals("Value change.", 1, changeReport.eventCount());
        valueModel.setValue(valueModel.getValue());
        assertEquals("Value set again.", 1, changeReport.eventCount());
    }

    public void testSetAdapterValueSendsUpdates() {
        BeanAdapter<TestBean> adapter = new BeanAdapter<TestBean>(model1, true);
        String propertyName = "readWriteObjectProperty";
        AbstractValueModel valueModel = adapter.getValueModel(propertyName);

        PropertyChangeReport changeReport = new PropertyChangeReport();
        valueModel.addPropertyChangeListener("value", changeReport);

        adapter.setValue(propertyName, "Johannes");
        assertEquals("Value change.", 1, changeReport.eventCount());
        adapter.setValue(propertyName, valueModel.getValue());
        assertEquals("Value set again.", 1, changeReport.eventCount());
    }

    public void testBeanChangeSendsUpdates() {
        BeanAdapter<TestBean> adapter = new BeanAdapter<TestBean>(model1, true);
        String propertyName = "readWriteObjectProperty";
        model1.setReadWriteObjectProperty("initialValue");
        AbstractValueModel valueModel = adapter.getValueModel(propertyName);

        PropertyChangeReport changeReport = new PropertyChangeReport();
        valueModel.addPropertyChangeListener("value", changeReport);

        adapter.setBean(model1);
        assertEquals("First bean set.", 0, changeReport.eventCount());
        adapter.setBean(new TestBean());
        assertEquals("Second bean set.", 1, changeReport.eventCount());
    }

    public void testMulticastAndNamedPropertyChangeEvents() {
        BeanAdapter<TestBean> adapter = new BeanAdapter<TestBean>(model1, true);
        String propertyName = "readWriteObjectProperty";
        AbstractValueModel valueModel = adapter.getValueModel(propertyName);

        PropertyChangeReport changeReport = new PropertyChangeReport();
        valueModel.addPropertyChangeListener("value", changeReport);

        model1.setReadWriteObjectProperty("Karsten");
        assertEquals("Adapted property changed.", 1, changeReport.eventCount());
        model1.setReadWriteBooleanProperty(false);
        assertEquals("Another property changed.", 1, changeReport.eventCount());
        model1.setReadWriteObjectProperties(null, false, 3);
        assertEquals("Multiple properties changed.", 2, changeReport.eventCount());
    }

    public void testMulticastFiresProperNewValue() {
        BeanAdapter<TestBean> adapter = new BeanAdapter<TestBean>(model1, true);
        String propertyName = "readWriteObjectProperty";
        AbstractValueModel valueModel = adapter.getValueModel(propertyName);

        PropertyChangeReport changeReport = new PropertyChangeReport();
        valueModel.addPropertyChangeListener("value", changeReport);

        Object theNewObjectValue = "The new value";
        model1.setReadWriteObjectProperties(theNewObjectValue, false, 3);

        Object eventsNewValue = changeReport.lastNewValue();
        assertEquals("Multicast fires proper new value .", theNewObjectValue, eventsNewValue);
    }


    // Misc *******************************************************************

    /**
     * Checks that #setBean changes the bean and moves the
     * PropertyChangeListeners to the new bean.
     */
    public void testSetBean() {
        Object value1_1 = "value1.1";
        Object value1_2 = "value1.2";
        Object value2_1 = "value2.1";
        Object value2_2 = "value2.2";
        Object value2_3 = "value2.3";

        model1.setReadWriteObjectProperty(value1_1);
        model2.setReadWriteObjectProperty(value2_1);
        BeanAdapter<TestBean> adapter = new BeanAdapter<TestBean>(model1, true);
        String propertyName = "readWriteObjectProperty";
        AbstractValueModel valueModel = adapter.getValueModel(propertyName);
        adapter.setBean(model2);

        assertSame(
            "Bean has not been changed.",
            adapter.getBean(),
            model2);

        assertSame(
            "Bean change does not answer the new beans's value.",
            valueModel.getValue(),
            value2_1);

        valueModel.setValue(value2_2);
        assertSame(
            "Bean change does not set the new bean's property.",
            model2.getReadWriteObjectProperty(),
            value2_2);

        model1.setReadWriteObjectProperty(value1_2);
        assertSame("Adapter listens to old bean after bean change.",
            valueModel.getValue(),
            value2_2);

        model2.setReadWriteObjectProperty(value2_3);
        assertSame("Adapter does not listen to new bean after bean change.",
            valueModel.getValue(),
            value2_3);
    }

    /**
     * Tests that we can change the bean if we adapt a write-only property.
     * Changing the bean normally calls the property's getter to request
     * the old value that is used in the fired PropertyChangeEvent.
     */
    public void testSetBeanOnWriteOnlyProperty() {
        BeanAdapter<TestBean> adapter = new BeanAdapter<TestBean>(model1, true);
        adapter.getValueModel("writeOnlyObjectProperty");
        adapter.setBean(model2);
    }


    /**
     * Tests that we can change the read/write state of the bean.
     */
    public void testSetBeanChangesReadWriteState() {
        ReadWriteBean readWriteBean = new ReadWriteBean();
        ReadOnlyBean  readOnlyBean  = new ReadOnlyBean();
        WriteOnlyBean writeOnlyBean = new WriteOnlyBean();

        // From/to readWriteBean to all other read/write states
        BeanAdapter<Model> adapter = new BeanAdapter<Model>(readWriteBean);
        adapter.setBean(null);
        adapter.setBean(readWriteBean);
        adapter.setBean(readOnlyBean);
        adapter.setBean(readWriteBean);
        adapter.setBean(writeOnlyBean);
        adapter.setBean(readWriteBean);

        // From/to writeOnlyBean to all other states
        adapter.setBean(writeOnlyBean);
        adapter.setBean(null);
        adapter.setBean(writeOnlyBean);
        adapter.setBean(readOnlyBean);
        adapter.setBean(writeOnlyBean);

        // From/to readOnlyBean to all other states
        adapter.setBean(readOnlyBean);
        adapter.setBean(null);
        adapter.setBean(readOnlyBean);
    }

    /**
     * Checks that bean changes are reported.
     */
    public void testBeanIsBoundProperty() {
        BeanAdapter<TestBean> adapter = new BeanAdapter<TestBean>(model1);
        PropertyChangeReport changeReport = new PropertyChangeReport();
        adapter.addPropertyChangeListener("bean", changeReport);

        adapter.setBean(model2);
        assertEquals("Bean changed.", 1, changeReport.eventCount());
        adapter.setBean(model2);
        assertEquals("Bean unchanged.", 1, changeReport.eventCount());
        adapter.setBean(null);
        assertEquals("Bean set to null.", 2, changeReport.eventCount());
        adapter.setBean(model1);
        assertEquals("Bean changed from null.", 3, changeReport.eventCount());
    }


    public void testBeanChangeFiresThreeBeanEvents() {
        BeanAdapter<TestBean> adapter = new BeanAdapter<TestBean>((TestBean)null, true);
        PropertyChangeReport changeReport = new PropertyChangeReport();
        adapter.addPropertyChangeListener(changeReport);

        adapter.setBean(model1);
        assertEquals("Changing the bean fires three events: before, changing, after.",
                3,
                changeReport.eventCount());
    }

    public void testEqualBeanChangeFiresThreeBeanEvents() {
        Object bean1 = new EquityTestBean("bean");
        Object bean2 = new EquityTestBean("bean");
        assertEquals("The two test beans are equal.", bean1, bean2);
        assertNotSame("The two test beans are not the same.", bean1, bean2);

        BeanAdapter<Object> adapter1 = new BeanAdapter<Object>(bean1, true);
        PropertyChangeReport changeReport1 = new PropertyChangeReport();
        adapter1.addPropertyChangeListener(changeReport1);

        adapter1.setBean(bean2);
        assertEquals("Changing the bean fires three events: before, changing, after.",
                3,
                changeReport1.eventCount());

        BeanAdapter<Object> adapter2 = new BeanAdapter<Object>(null, true);
        adapter2.setBean(bean1);
        PropertyChangeReport changeReport2 = new PropertyChangeReport();
        adapter2.addPropertyChangeListener(changeReport2);

        adapter2.setBean(bean2);
        assertEquals("Changing the bean fires three events: before, changing, after.",
                3,
                changeReport2.eventCount());
    }

    public void testBeanChangeIgnoresOldBeanNull() {
        testBeanChangeIgnoresMissingOldOrNullValues(new ValueHolderWithOldValueNull(null));
    }

    public void testBeanChangeIgnoresNewBeanNull() {
        testBeanChangeIgnoresMissingOldOrNullValues(new ValueHolderWithNewValueNull(null));
    }

    public void testBeanChangeIgnoresOldAndNewBeanNull() {
        testBeanChangeIgnoresMissingOldOrNullValues(new ValueHolderWithOldAndNewValueNull(null));
    }

    private void testBeanChangeIgnoresMissingOldOrNullValues(ValueModel beanChannel) {
        TestBean bean1 = new TestBean();
        TestBean bean2 = new TestBean();
        beanChannel.setValue(bean1);
        BeanAdapter<TestBean> adapter = new BeanAdapter<TestBean>(beanChannel, true);
        ValueModel model = adapter.getValueModel("readWriteObjectProperty");
        PropertyChangeReport changeReport = new PropertyChangeReport();
        model.addValueChangeListener(changeReport);
        beanChannel.setValue(bean2);
        bean1.setReadWriteObjectProperty("bean1value");
        assertEquals("No event if modifying the old bean",
                0,
                changeReport.eventCount());
        bean2.setReadWriteObjectProperty("bean2value");
        assertEquals("Fires event if modifying the new bean",
                1,
                changeReport.eventCount());
    }

    public void testChangedState() {
        BeanAdapter<TestBean> adapter = new BeanAdapter<TestBean>(model1, true);

        assertEquals("The initial changed state is false.", false, adapter.isChanged());
        model1.setReadWriteObjectProperty("aBrandNewValue");
        assertEquals("Changing the bean turns the changed state to true.", true, adapter.isChanged());
        adapter.resetChanged();
        assertEquals("Resetting changes turns the changed state to false.", false, adapter.isChanged());
        model1.setReadWriteObjectProperty("anotherValue");
        assertEquals("Changing the bean turns the changed state to true again.", true, adapter.isChanged());
        adapter.setBean(model2);
        assertEquals("Changing the bean resets the changed state.", false, adapter.isChanged());
    }

    public void testChangedStateFiresEvents() {
        BeanAdapter<TestBean> adapter = new BeanAdapter<TestBean>(model1, true);
        PropertyChangeReport changeReport = new PropertyChangeReport();
        adapter.addPropertyChangeListener("changed", changeReport);

        model1.setReadWriteObjectProperty("aBrandNewValue");
        adapter.resetChanged();
        model1.setReadWriteObjectProperty("anotherValue");
        adapter.setBean(model2);
        assertEquals("The changed state changed four times.", 4, changeReport.eventCount());
    }


    // Testing Bean Property Changes ******************************************

    public void testUnnamedBeanPropertyChange() {
        BeanAdapter<TestBean> adapter = new BeanAdapter<TestBean>(model1, true);
        PropertyChangeReport changeReport = new PropertyChangeReport();
        adapter.addBeanPropertyChangeListener(changeReport);

        model1.setReadWriteObjectProperty("value1");
        assertEquals("Setting a bound property forwards the PropertyChangeEvent.", 1, changeReport.eventCount());
        model1.setReadWriteObjectProperties("value2", false, 42);
        assertEquals("Firing a multicast event forwards a PropertyChangeEvent too.", 2, changeReport.eventCount());
        adapter.setBean(model2);
        assertEquals("Changing the bean fires no bean event.", 2, changeReport.eventCount());
        model1.setReadWriteObjectProperty("ignoredValue1");
        assertEquals("Setting a bound property in the old bean fires no event.", 2, changeReport.eventCount());
        model2.setReadWriteObjectProperty("value2");
        assertEquals("Setting a bound property forwards another PropertyChangeEvent.", 3, changeReport.eventCount());
    }

    public void testNamedBeanPropertyChange() {
        BeanAdapter<TestBean> adapter = new BeanAdapter<TestBean>(model1, true);
        PropertyChangeReport changeReport = new PropertyChangeReport();
        adapter.addBeanPropertyChangeListener("readWriteObjectProperty", changeReport);

        model1.setReadWriteObjectProperty("value1");
        assertEquals("Setting a bound property forwards the PropertyChangeEvent.", 1, changeReport.eventCount());
        model1.setReadWriteObjectProperties("value2", false, 42);
        assertEquals("Firing a multicast event forwards no event to named listeners.", 1, changeReport.eventCount());
        adapter.setBean(model2);
        assertEquals("Changing the bean fires no bean event.", 1, changeReport.eventCount());
        model1.setReadWriteObjectProperty("ignoredValue1");
        assertEquals("Setting a bound property in the old bean fires no event.", 1, changeReport.eventCount());
        model2.setReadWriteObjectProperty("value2");
        assertEquals("Setting a bound property forwards another PropertyChangeEvent.", 2, changeReport.eventCount());
    }


    // Misc *******************************************************************

    /**
     * Checks that the cached PropertyDescriptor is available when needed.
     */
    public void testPropertyDescriptorCache() {
        ValueModel beanChannel = new ValueHolder(null, true);
        BeanAdapter<TestBean> adapter = new BeanAdapter<TestBean>(beanChannel, true);
        ValueModel valueModel = adapter.getValueModel("readWriteObjectProperty");

        beanChannel.setValue(new TestBean());
        valueModel.setValue("Test");
    }

    /**
     * Verifies that the factory method vends the same instance of the
     * adapting model if called multiple times.
     */
    public void testVendsSameModel() {
        BeanAdapter<TestBean> adapter = new BeanAdapter<TestBean>(model1, true);
        Object adapter1 = adapter.getValueModel("readWriteObjectProperty");
        Object adapter2 = adapter.getValueModel("readWriteObjectProperty");

        assertSame("The adapter factory method vends the same instance.", adapter1, adapter2);
    }

    /**
     * Verifies that the BeanAdapter rejects attempts to get an adapting
     * ValueModel by means of <code>#getValueModel</code> with different
     * property accessor names. In other words, for each bean property
     * API users must use either {@link BeanAdapter#getValueModel(String)} or
     * {@link BeanAdapter#getValueModel(String, String, String)}, not both.
     * And all calls to the latter method must use the same getter and setter
     * names for the same property name.<p>
     *
     * This test invokes both methods for the same property name with different
     * getter and/or setter names and expects that the second call is rejected.
     * The BeanAdapter is created without a bean set, to avoid that the
     * BeanAdapter checks for a valid property.
     */
    public void testRejectsGetValueModelWithDifferentAccessors() {
        String failureText  =
            "The BeanAdapter must reject attempts " +
            "to get a ValueModel for the same property " +
            "with different accessor names.";
        String propertyName = "property";
        String getterName1  = "getter1";
        String getterName2  = "getter2";
        String setterName1  = "setter1";
        String setterName2  = "setter2";

        BeanAdapter<Object> adapter1 = new BeanAdapter<Object>(null);
        adapter1.getValueModel(propertyName);
        try {
            adapter1.getValueModel(propertyName, getterName1, setterName1);
            fail(failureText);
        } catch (IllegalArgumentException e) {
            // The expected result.
        }

        BeanAdapter<Object> adapter2 = new BeanAdapter<Object>(null);
        adapter2.getValueModel(propertyName, getterName1, setterName1);
        try {
            adapter2.getValueModel(propertyName);
            fail(failureText);
        } catch (IllegalArgumentException e) {
            // The expected result.
        }

        BeanAdapter<Object> adapter3 = new BeanAdapter<Object>(null);
        adapter3.getValueModel(propertyName, getterName1, setterName1);
        try {
            adapter3.getValueModel(propertyName, getterName2, setterName1);
            fail(failureText);
        } catch (IllegalArgumentException e) {
            // The expected result.
        }

        BeanAdapter<Object> adapter4 = new BeanAdapter<Object>(null);
        adapter4.getValueModel(propertyName, getterName1, setterName1);
        try {
            adapter4.getValueModel(propertyName, getterName1, setterName2);
            fail(failureText);
        } catch (IllegalArgumentException e) {
            // The expected result.
        }
    }


    // Checking for ConcurrentModificationExceptions **************************

    /**
     * Sets up an environment that has thrown a ConcurrentModificationException
     * in older releases. Basically, in a situation where the BeanAdapter
     * iterates over all its internal property adapters, we add a new adapter
     * and check if this fails.
     */
    public void testCanGetModelWhileListeningToBeanChange() {
        BeanAdapter<TestBean> adapter = createConcurrentModificationEnvironment();
        adapter.setBean(model2);
    }

    public void testCanGetModelWhileListeningToMulticastChange() {
        createConcurrentModificationEnvironment();
        model1.setReadWriteObjectProperties("value3", true, 3);
    }

    private BeanAdapter<TestBean> createConcurrentModificationEnvironment() {
        model1.setReadWriteObjectProperty("value1");
        model2.setReadWriteObjectProperty("value2");
        final BeanAdapter<TestBean> adapter = new BeanAdapter<TestBean>(model1, true);
        ValueModel valueModel1 = adapter.getValueModel("readWriteObjectProperty");
        // Add a second listener so that adding a new SimplePropertyAdapter
        // may cause a ConcurrentModificationException while adding a new adapter.
        adapter.getValueModel("readWriteBooleanProperty");
        PropertyChangeListener listener = new PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                adapter.getValueModel("readWriteIntProperty");
            }
        };
        valueModel1.addValueChangeListener(listener);
        return adapter;
    }


    // Null Bean **************************************************************

    public void testNullBean() {
        BeanAdapter<TestBean> adapter = new BeanAdapter<TestBean>((TestBean) null, true);
        ValueModel model = adapter.getValueModel("readWriteObjectProperty");
        model.toString();
    }


}
