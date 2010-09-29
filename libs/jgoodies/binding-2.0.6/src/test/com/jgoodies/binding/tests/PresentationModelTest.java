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

import java.beans.PropertyVetoException;

import junit.framework.TestCase;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.tests.beans.EquityTestBean;
import com.jgoodies.binding.tests.beans.TestBean;
import com.jgoodies.binding.tests.event.PropertyChangeReport;
import com.jgoodies.binding.value.BufferedValueModel;
import com.jgoodies.binding.value.Trigger;
import com.jgoodies.binding.value.ValueHolder;

/**
 * A test case for class {@link com.jgoodies.binding.PresentationModel}.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.13 $
 */
public final class PresentationModelTest extends TestCase {


    // Life Cycle *************************************************************

    public void testAcceptsMultipleReleases() {
        PresentationModel<TestBean> pm = new PresentationModel<TestBean>(new TestBean());
        pm.release();
        pm.release();
    }




    // Null As Property Name **************************************************

    public void testRejectNullPropertyName() {
        testRejectNullPropertyName(null);
        testRejectNullPropertyName(new TestBean());
    }

    private void testRejectNullPropertyName(TestBean bean) {
        PresentationModel<TestBean> model = new PresentationModel<TestBean>(bean);
        try {
            model.getValue(null);
            fail("#getValue(null) should throw an NPE.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            model.setValue(null, null);
            fail("#setValue(null, Object) should throw an NPE.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            model.setVetoableValue(null, null);
            fail("#setVetoableValue(null, Object) should throw an NPE.");
        } catch (NullPointerException e) {
            // The expected behavior
        } catch (PropertyVetoException e) {
            fail("An NPE should be thrown.");
        }
        try {
            model.getBufferedValue(null);
            fail("#getBufferedValue(null) should throw an NPE.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            model.setBufferedValue(null, null);
            fail("#setBufferedValue(null, Object) should throw an NPE.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            model.getModel(null);
            fail("#getModel(null) should throw an NPE.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            model.getModel(null, "readA", "writeA");
            fail("#getModel(null, String, String) should throw an NPE.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            model.getComponentModel(null);
            fail("#getComponentModel(null) should throw an NPE.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            model.getBufferedModel(null);
            fail("#getBufferedModel(null) should throw an NPE.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            model.getBufferedModel(null, "readA", "writeA");
            fail("#getBufferedModel(null, String, String) should throw an NPE.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            model.getBufferedComponentModel(null);
            fail("#getBufferedComponentModel(null) should throw an NPE.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
    }


    // ************************************************************************

    /**
     * Verifies that the factory method vends the same instance of the
     * buffered model if called multiple times.
     */
    public void testVendsSameModel() {
        PresentationModel<TestBean> model = new PresentationModel<TestBean>(new TestBean());
        Object model1 = model.getModel("readWriteObjectProperty");
        Object model2 = model.getModel("readWriteObjectProperty");

        assertSame("The factory method vends the same instance.", model1, model2);
    }

    /**
     * Verifies that the factory method vends the same instance of the
     * buffered model if called multiple times.
     */
    public void testVendsSameBufferedModel() {
        PresentationModel<TestBean> model = new PresentationModel<TestBean>(new TestBean());
        Object model1 = model.getBufferedModel("readWriteObjectProperty");
        Object model2 = model.getBufferedModel("readWriteObjectProperty");

        assertSame("The factory method vends the same instance.", model1, model2);
    }


    /**
     * Verifies that the PresentationModel rejects attempts to get an adapting
     * ValueModel by means of <code>#getModel</code> with different
     * property accessor names. In other words, for each bean property
     * API users must use either {@link PresentationModel#getModel(String)} or
     * {@link PresentationModel#getModel(String, String, String)}, not both.
     * And all calls to the latter method must use the same getter and setter
     * names for the same property name.<p>
     *
     * This test invokes both methods for the same property name with different
     * getter and/or setter names and expects that the second call is rejected.
     * The PresentationModel is created without a bean set, to avoid that the
     * underlying BeanAdapter checks for a valid property.
     */
    public void testRejectsGetModelWithDifferentAccessors() {
        String failureText  =
                "The PresentationModel must reject attempts "
              + "to get a ValueModel for the same property "
              + "with different accessor names.";
        String propertyName = "property";
        String getterName1  = "getter1";
        String getterName2  = "getter2";
        String setterName1  = "setter1";
        String setterName2  = "setter2";

        PresentationModel<Object> model1 = new PresentationModel<Object>(null);
        model1.getModel(propertyName);
        try {
            model1.getModel(propertyName, getterName1, setterName1);
            fail(failureText);
        } catch (IllegalArgumentException e) {
            // The expected result.
        }

        PresentationModel<Object> model2 = new PresentationModel<Object>(null);
        model2.getModel(propertyName, getterName1, setterName1);
        try {
            model2.getModel(propertyName);
            fail(failureText);
        } catch (IllegalArgumentException e) {
            // The expected result.
        }

        PresentationModel<Object> model3 = new PresentationModel<Object>(null);
        model3.getModel(propertyName, getterName1, setterName1);
        try {
            model3.getModel(propertyName, getterName2, setterName1);
            fail(failureText);
        } catch (IllegalArgumentException e) {
            // The expected result.
        }

        PresentationModel<Object> model4 = new PresentationModel<Object>(null);
        model4.getModel(propertyName, getterName1, setterName1);
        try {
            model4.getModel(propertyName, getterName1, setterName2);
            fail(failureText);
        } catch (IllegalArgumentException e) {
            // The expected result.
        }
    }


    /**
     * Verifies that the PresentationModel rejects attempts to get a buffered
     * adapting ValueModel by means of <code>#getBufferedModel</code> with
     * different accessor names. In other words, for each bean property API
     * users must use either {@link PresentationModel#getBufferedModel(String)}
     * or {@link PresentationModel#getBufferedModel(String, String, String)},
     * not both. And all calls to the latter method must use the same getter
     * and setter names for the same property name.<p>
     *
     * This test invokes both methods for the same property name with different
     * getter and/or setter names and expects that the second call is rejected.
     * The PresentationModel is created without a bean set, to avoid that the
     * underlying BeanAdapter checks for a valid property.
     */
    public void testRejectsGetBufferedModelWithDifferentAccessors() {
        String failureText  =
              "The PresentationModel must reject attempts "
            + "to get a buffered ValueModel for the same property "
            + "with different accessor names.";
        String propertyName = "property";
        String getterName1  = "getter1";
        String getterName2  = "getter2";
        String setterName1  = "setter1";
        String setterName2  = "setter2";

        PresentationModel<Object> model1 = new PresentationModel<Object>(null);
        model1.getBufferedModel(propertyName);
        try {
            model1.getBufferedModel(propertyName, getterName1, setterName1);
            fail(failureText);
        } catch (IllegalArgumentException e) {
            // The expected result.
        }

        PresentationModel<Object> model2 = new PresentationModel<Object>(null);
        model2.getBufferedModel(propertyName, getterName1, setterName1);
        try {
            model2.getBufferedModel(propertyName);
            fail(failureText);
        } catch (IllegalArgumentException e) {
            // The expected result.
        }

        PresentationModel<Object> model3 = new PresentationModel<Object>(null);
        model3.getBufferedModel(propertyName, getterName1, setterName1);
        try {
            model3.getBufferedModel(propertyName, getterName2, setterName1);
            fail(failureText);
        } catch (IllegalArgumentException e) {
            // The expected result.
        }

        PresentationModel<Object> model4 = new PresentationModel<Object>(null);
        model4.getBufferedModel(propertyName, getterName1, setterName1);
        try {
            model4.getBufferedModel(propertyName, getterName1, setterName2);
            fail(failureText);
        } catch (IllegalArgumentException e) {
            // The expected result.
        }
    }


    public void testSetTriggerChannelUpdatesExistingBufferedValueModels() {
        Object value1 = "value1";
        Object value2 = "value2";
        Object value3 = "value3";
        Object value4 = "value4";
        TestBean bean = new TestBean();
        bean.setReadWriteObjectProperty(value1);

        Trigger trigger1 = new Trigger();
        Trigger trigger2 = new Trigger();
        PresentationModel<TestBean> model = new PresentationModel<TestBean>(bean, trigger1);
        BufferedValueModel buffer = model.getBufferedModel("readWriteObjectProperty");
        buffer.setValue(value2);

        trigger1.triggerCommit();
        assertEquals("Before the trigger change Trigger1 commits buffered values.",
                value2,
                bean.getReadWriteObjectProperty());

        buffer.setValue(value3);
        trigger2.triggerCommit();
        assertEquals("Before the trigger change Trigger2 does not affect the buffer.",
                value2,
                bean.getReadWriteObjectProperty());

        model.setTriggerChannel(trigger2);
        trigger1.triggerCommit();
        assertEquals("After the trigger change Trigger1 shall not affect the buffered value anymore.",
                value2,
                bean.getReadWriteObjectProperty());

        buffer.setValue(value4);
        trigger2.triggerCommit();
        assertEquals("After the trigger change Trigger2 shall commit buffered values.",
                value4,
                bean.getReadWriteObjectProperty());
    }


    // Testing Bean Changes ***************************************************

    public void testBeanChangeFiresThreeBeanEvents() {
        TestBean bean = new TestBean();
        PresentationModel<TestBean> model =
            new PresentationModel<TestBean>((TestBean) null);
        PropertyChangeReport changeReport = new PropertyChangeReport();
        model.addPropertyChangeListener(changeReport);

        model.setBean(bean);
        assertEquals("Changing the bean fires three events: before, changing, after.",
                3,
                changeReport.eventCount());
    }

    public void testEqualBeanChangeFiresThreeBeanEvents() {
        EquityTestBean bean1 = new EquityTestBean("bean");
        EquityTestBean bean2 = new EquityTestBean("bean");
        assertEquals("The two test beans are equal.", bean1, bean2);
        assertNotSame("The two test beans are not the same.", bean1, bean2);

        PresentationModel<EquityTestBean> model1 = new PresentationModel<EquityTestBean>(bean1);
        PropertyChangeReport beanChannelValueChangeReport =
            new PropertyChangeReport();
        model1.getBeanChannel().addValueChangeListener(beanChannelValueChangeReport);
        PropertyChangeReport changeReport1 = new PropertyChangeReport();
        model1.addPropertyChangeListener(changeReport1);

        model1.setBean(bean2);
        assertEquals("Changing the bean fires a change event in the bean channel.",
                1,
                beanChannelValueChangeReport.eventCount());
        assertEquals("Changing the bean fires three events: before, changing, after.",
                3,
                changeReport1.eventCount());

        PresentationModel<EquityTestBean> model2 = new PresentationModel<EquityTestBean>(new ValueHolder(null, true));
        model2.setBean(bean1);
        PropertyChangeReport changeReport2 = new PropertyChangeReport();
        model2.addPropertyChangeListener(changeReport2);

        model2.setBean(bean2);
        assertEquals("Changing the bean fires three events: before, changing, after.",
                3,
                changeReport2.eventCount());
    }


}
