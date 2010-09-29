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
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.tests.beans.TestBean;
import com.jgoodies.binding.tests.event.PropertyChangeReport;
import com.jgoodies.binding.tests.value.CloningValueHolder;
import com.jgoodies.binding.tests.value.ToUpperCaseStringHolder;
import com.jgoodies.binding.value.BufferedValueModel;
import com.jgoodies.binding.value.Trigger;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

/**
 * Tests class {@link BufferedValueModel}.
 * Critical are state changes to and from the state
 * where buffered value == subject value.
 *
 * @author Jeanette Winzenburg
 * @author Karsten Lentzsch
 * @version $Revision: 1.20 $
 *
 * @see     BufferedValueModel
 */
public final class BufferedValueModelTest extends TestCase {

    private static final Object INITIAL_VALUE = "initial value";
    private static final Object RESET_VALUE   = "reset value";

    /**
     * Holds a subject that can be reused by tests.
     */
    private ValueModel subject;

    /**
     * Holds a trigger channel that can be reused by tests
     * and changed by invoking #commit and #flush.
     */
    private Trigger triggerChannel;


    // Testing Proper Values **************************************************

    /**
     * Tests that the BufferedValueModel returns the subject's values
     * as long as no value has been assigned.
     */
    public void testReturnsSubjectValueIfNoValueAssigned() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        assertEquals(
            "Buffer value equals the subject value before any changes.",
            buffer.getValue(),
            subject.getValue());

        subject.setValue("change1");
        assertEquals(
            "Buffer value equals the subject value changes as long as no value has been assigned.",
            buffer.getValue(),
            subject.getValue());

        subject.setValue(null);
        assertEquals(
            "Buffer value equals the subject value changes as long as no value has been assigned.",
            buffer.getValue(),
            subject.getValue());

        subject.setValue("change2");
        assertEquals(
            "Buffer value equals the subject value changes as long as no value has been assigned.",
            buffer.getValue(),
            subject.getValue());
    }

    /**
     * Tests that the BufferedValueModel returns the buffered values
     * once a value has been assigned.
     */
    public void testReturnsBufferedValueIfValueAssigned() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();

        Object newValue1 = subject.getValue();
        buffer.setValue(newValue1);
        subject.setValue("subject1");
        assertSame(
            "Buffer value == new value once a value has been assigned.",
            buffer.getValue(),
            newValue1);

        Object newValue2 = "change1";
        buffer.setValue(newValue2);
        subject.setValue("subject2");
        assertSame(
            "Buffer value == new value once a value has been assigned.",
            buffer.getValue(),
            newValue2);

        Object newValue3 = null;
        buffer.setValue(newValue3);
        subject.setValue(null);
        assertSame(
            "Buffer value == new value once a value has been assigned.",
            buffer.getValue(),
            newValue3);

        Object newValue4 = "change2";
        buffer.setValue(newValue4);
        assertSame(
            "Buffer value == new value once a value has been assigned.",
            buffer.getValue(),
            newValue4);
    }

    /**
     * Tests that the BufferedValueModel returns the buffered values
     * once a value has been assigned and ignores subject value changes.
     */
    public void testIgnoresSubjectValuesIfValueAssigned() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();

        Object newValue1 = "change1";
        buffer.setValue(newValue1);
        subject.setValue("change3");
        assertSame(
            "Buffer value == new value once a value has been assigned.",
            buffer.getValue(),
            newValue1);
        subject.setValue(newValue1);
        assertSame(
            "Buffer value == new value once a value has been assigned.",
            buffer.getValue(),
            newValue1);
        subject.setValue(null);
        assertSame(
            "Buffer value == new value once a value has been assigned.",
            buffer.getValue(),
            newValue1);
    }

    /**
     * Tests that the BufferedValueModel returns the subject's values
     * after a commit.
     */
    public void testReturnsSubjectValueAfterCommit() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        buffer.setValue("change1");  // shall buffer now
        commit();
        assertEquals(
            "Buffer value equals the subject value after a commit.",
            buffer.getValue(),
            subject.getValue());

        subject.setValue("change2");
        assertEquals(
            "Buffer value equals the subject value after a commit.",
            buffer.getValue(),
            subject.getValue());

        subject.setValue(buffer.getValue());
        assertEquals(
            "Buffer value equals the subject value after a commit.",
            buffer.getValue(),
            subject.getValue());
    }

    /**
     * Tests that the BufferedValueModel returns the subject's values
     * after a flush.
     */
    public void testReturnsSubjectValueAfterFlush() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        buffer.setValue("change1");  // shall buffer now
        flush();
        assertEquals(
            "Buffer value equals the subject value after a flush.",
            subject.getValue(),
            buffer.getValue());

        subject.setValue("change2");
        assertEquals(
            "Buffer value equals the subject value after a flush.",
            subject.getValue(),
            buffer.getValue());
    }


    // Testing Proper Value Commit and Flush **********************************

    /**
     * Tests the core of the buffering feature: buffer modifications
     * do not affect the subject before a commit.
     */
    public void testSubjectValuesUnchangedBeforeCommit() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        Object oldSubjectValue = subject.getValue();
        buffer.setValue("changedBuffer1");
        assertEquals(
            "Buffer changes do not change the subject value before a commit.",
            subject.getValue(),
            oldSubjectValue
        );
        buffer.setValue(null);
        assertEquals(
            "Buffer changes do not change the subject value before a commit.",
            subject.getValue(),
            oldSubjectValue
        );
        buffer.setValue(oldSubjectValue);
        assertEquals(
            "Buffer changes do not change the subject value before a commit.",
            subject.getValue(),
            oldSubjectValue
        );
        buffer.setValue("changedBuffer2");
        assertEquals(
            "Buffer changes do not change the subject value before a commit.",
            subject.getValue(),
            oldSubjectValue
        );
    }

    /**
     * Tests the core of a commit: buffer changes are written through on commit
     * and change the subject value.
     */
    public void testCommitChangesSubjectValue() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        Object oldSubjectValue = subject.getValue();
        Object newValue1 = "change1";
        buffer.setValue(newValue1);
        assertEquals(
            "Subject value is unchanged before the first commit.",
            subject.getValue(),
            oldSubjectValue);
        commit();
        assertEquals(
            "Subject value is the new value after the first commit.",
            subject.getValue(),
            newValue1);

        // Set the buffer to the current subject value to check whether
        // the starts buffering, even if there's no value difference.
        Object newValue2 = subject.getValue();
        buffer.setValue(newValue2);
        commit();
        assertEquals(
            "Subject value is the new value after the second commit.",
            subject.getValue(),
            newValue2);
    }

    /**
     * Tests the core of a flush action: buffer changes are overridden
     * by subject changes after a flush.
     */
    public void testFlushResetsTheBufferedValue() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        Object newValue1 = "new value1";
        buffer.setValue(newValue1);
        assertSame(
            "Buffer value reflects changes before the first flush.",
            buffer.getValue(),
            newValue1);
        flush();
        assertEquals(
            "Buffer value is the subject value after the first flush.",
            buffer.getValue(),
            subject.getValue());

        // Set the buffer to the current subject value to check whether
        // the starts buffering, even if there's no value difference.
        Object newValue2 = subject.getValue();
        buffer.setValue(newValue2);
        assertSame(
            "Buffer value reflects changes before the flush.",
            buffer.getValue(),
            newValue2);
        flush();
        assertEquals(
            "Buffer value is the subject value after the second flush.",
            buffer.getValue(),
            subject.getValue());
    }

    // Tests a Proper Buffering State *****************************************

    /**
     * Tests that a buffer isn't buffering as long as no value has been assigned.
     */
    public void testIsNotBufferingIfNoValueAssigned() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        assertFalse(
            "Initially the buffer does not buffer.",
            buffer.isBuffering());

        Object newValue = "change1";
        subject.setValue(newValue);
        assertFalse(
            "Subject changes do not affect the buffering state.",
            buffer.isBuffering());

        subject.setValue(null);
        assertFalse(
            "Subject change to null does not affect the buffering state.",
            buffer.isBuffering());
    }

    /**
     * Tests that the buffer is buffering once a value has been assigned,
     * even if the buffered value is equal to the subject's value.
     */
    public void testIsBufferingIfValueAssigned() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        buffer.setValue("change1");
        assertTrue(
            "Setting a value (even the subject's value) turns on buffering.",
            buffer.isBuffering());

        buffer.setValue("change2");
        assertTrue(
            "Changing the value doesn't affect the buffering state.",
            buffer.isBuffering());

        buffer.setValue(subject.getValue());
        assertTrue(
            "Resetting the value to the subject's value doesn't affect buffering.",
            buffer.isBuffering());
    }

    /**
     * Tests that the buffer is not buffering after a commit.
     */
    public void testIsNotBufferingAfterCommit() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        buffer.setValue("change1");
        commit();
        assertFalse(
            "The buffer does not buffer after a commit.",
            buffer.isBuffering());

        Object newValue = "change1";
        subject.setValue(newValue);
        assertFalse(
        "The buffer does not buffer after a commit and subject change1.",
            buffer.isBuffering());

        subject.setValue(null);
        assertFalse(
        "The buffer does not buffer after a commit and subject change2.",
            buffer.isBuffering());
    }

    /**
     * Tests that the buffer is not buffering after a flush.
     */
    public void testIsNotBufferingAfterFlush() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        buffer.setValue("change1");
        flush();
        assertFalse(
            "The buffer does not buffer after a flush.",
            buffer.isBuffering());

        Object newValue = "change1";
        subject.setValue(newValue);
        assertFalse(
        "The buffer does not buffer after a flush and subject change1.",
            buffer.isBuffering());

        subject.setValue(null);
        assertFalse(
        "The buffer does not buffer after a flush and subject change2.",
            buffer.isBuffering());
    }

    /**
     * Tests that changing the buffering state fires changes of
     * the <i>buffering</i> property.
     */
    public void testFiresBufferingChanges() {
        Trigger trigger2 = new Trigger();
        BufferedValueModel buffer = new BufferedValueModel(subject, trigger2);
        PropertyChangeReport changeReport = new PropertyChangeReport();
        buffer.addPropertyChangeListener("buffering", changeReport);

        assertEquals("Initial state.", 0, changeReport.eventCount());
        buffer.getValue();
        assertEquals("Reading initial value.", 0, changeReport.eventCount());
        buffer.setSubject(new ValueHolder());
        assertEquals("After subject change.", 0, changeReport.eventCount());
        buffer.setTriggerChannel(triggerChannel);
        assertEquals("After trigger channel change.", 0, changeReport.eventCount());

        buffer.setValue("now buffering");
        assertEquals("After setting the first value.", 1, changeReport.eventCount());
        buffer.setValue("still buffering");
        assertEquals("After setting the second value.", 1, changeReport.eventCount());
        buffer.getValue();
        assertEquals("Reading buffered value.", 1, changeReport.eventCount());

        commit();
        assertEquals("After committing.", 2, changeReport.eventCount());
        buffer.getValue();
        assertEquals("Reading unbuffered value.", 2, changeReport.eventCount());

        buffer.setValue("buffering again");
        assertEquals("After second buffering switch.", 3, changeReport.eventCount());
        flush();
        assertEquals("After flushing.", 4, changeReport.eventCount());
        buffer.getValue();
        assertEquals("Reading unbuffered value.", 4, changeReport.eventCount());
    }

    // Tests Subject Changes **************************************************

    /**
     * Checks that #setSubject changes the subject.
     */
    public void testSubjectChange() {
        ValueHolder subject1 = new ValueHolder();
        ValueHolder subject2 = new ValueHolder();

        BufferedValueModel buffer = new BufferedValueModel(null, triggerChannel);
        assertNull(
            "Subject is null if not set in constructor.",
            buffer.getSubject());

        buffer.setSubject(subject1);
        assertSame(
            "Subject has been changed.",
            buffer.getSubject(),
            subject1);

        buffer.setSubject(subject2);
        assertSame(
            "Subject has been changed.",
            buffer.getSubject(),
            subject2);

        buffer.setSubject(null);
        assertNull(
            "Subject has been changed to null.",
            buffer.getSubject());
    }

    public void testSetValueSendsProperValueChangeEvents() {
        Object obj1  = new Integer(1);
        Object obj2a = new Integer(2);
        Object obj2b = new Integer(2);
        testSetValueFiresProperEvents(null, obj1,   true);
        testSetValueFiresProperEvents(obj1, null,   true);
        testSetValueFiresProperEvents(obj1, obj1,   false);
        testSetValueFiresProperEvents(obj1, obj2a,  true);
        testSetValueFiresProperEvents(obj2a, obj2b, true); // identity test
        testSetValueFiresProperEvents(null, null,   false);
    }


    public void testSetValueFiresProperValueChangeEvents() {
        Object obj1  = new Integer(1);
        Object obj2a = new Integer(2);
        Object obj2b = new Integer(2);
        testValueChangeSendsProperEvents(null, obj1,   true);
        testValueChangeSendsProperEvents(obj1, null,   true);
        testValueChangeSendsProperEvents(obj1, obj1,   false);
        testValueChangeSendsProperEvents(obj1, obj2a,  true);
        testValueChangeSendsProperEvents(obj2a, obj2b, true); // identity test
        testValueChangeSendsProperEvents(null, null,   false);
    }


    public void testSetValueFiresIfNewValueAndSubjectChange() {
        Object initialValue = new Integer(0);
        Object oldValue = new Integer(1);
        Object newValue = new Integer(2);
        subject.setValue(initialValue);
        BufferedValueModel buffer = new BufferedValueModel(subject, new Trigger());
        PropertyChangeReport changeReport = new PropertyChangeReport();
        buffer.addValueChangeListener(changeReport);
        buffer.setValue(oldValue);
        assertTrue("Buffer is now buffering", buffer.isBuffering());
        assertEquals("Buffer fired a value change.",
                1,
                changeReport.eventCount());
        subject.setValue(newValue);
        assertEquals("Setting the subject in buffered state fires no value change.",
                1,
                changeReport.eventCount());

        assertEquals("The buffered value is 1.",
                oldValue,
                buffer.getValue());
        // Setting the buffer from 1 to 2 should fire a value change.
        buffer.setValue(newValue);
        assertEquals("The value is now 2.",
                newValue,
                buffer.getValue());
        assertTrue("The buffer is still buffering", buffer.isBuffering());
        assertEquals("Setting the buffer from 1 to 2 fires a value change.",
                2,
                changeReport.eventCount());
    }




    /**
     * Checks that the buffer reports the current subject's value if unbuffered.
     */
    public void testReturnsCurrentSubjectValue() {
        Object value1_1 = "value1.1";
        Object value1_2 = "value1.2";
        Object value1_3 = "value1.3";
        Object value2_1 = "value2.1";
        Object value2_2 = "value2.2";

        ValueHolder subject1 = new ValueHolder(value1_1);
        ValueHolder subject2 = new ValueHolder(value2_1);

        BufferedValueModel buffer = new BufferedValueModel(subject1, triggerChannel);

        assertSame(
            "Buffer returns the subject value of the current subject1.",
            buffer.getValue(),
            subject1.getValue());

        subject1.setValue(value1_2);
        assertSame(
            "Buffer returns the new subject value of the current subject1.",
            buffer.getValue(),
            subject1.getValue());

        buffer.setSubject(subject2);
        assertSame(
            "Buffer returns the subject value of the current subject2.",
            buffer.getValue(),
            subject2.getValue());

        subject1.setValue(value1_3);
        subject2.setValue(value2_2);
        assertSame(
            "Buffer returns the new subject value of the current subject2.",
            buffer.getValue(),
            subject2.getValue());
    }

    /**
     * Checks that the buffer listens to changes of the current subject
     * and moves the value change handler if the subject changes.
     */
    public void testListensToCurrentSubject() {
        Object value1_1 = "value1.1";
        Object value1_2 = "value1.2";
        Object value2_1 = "value2.1";
        Object value2_2 = "value2.2";

        ValueHolder subject1 = new ValueHolder(null);
        ValueHolder subject2 = new ValueHolder(null);

        BufferedValueModel buffer = new BufferedValueModel(subject1, triggerChannel);
        PropertyChangeReport changeReport = new PropertyChangeReport();
        buffer.addValueChangeListener(changeReport);

        subject1.setValue(value1_1);
        assertEquals("Value change.", 1, changeReport.eventCount());

        subject2.setValue(value2_1);
        assertEquals("No value change.", 1, changeReport.eventCount());

        buffer.setSubject(subject2);
        assertEquals("Value changed because of subject change.", 2, changeReport.eventCount());

        subject1.setValue(value1_2);
        assertEquals("No value change.", 2, changeReport.eventCount());

        subject2.setValue(value2_2);
        assertEquals("Value change.", 3, changeReport.eventCount());
    }


    // Trigger Channel Tests *************************************************

    /**
     * Checks that the trigger channel is non-null.
     */
    public void testRejectNullTriggerChannel() {
        try {
            new BufferedValueModel(subject, null);
            fail("The constructor must reject a null trigger channel.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        try {
            buffer.setTriggerChannel(null);
            fail("The trigger channel setter must reject null values.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
    }

    /**
     * Checks that #setTriggerChannel changes the trigger channel.
     */
    public void testTriggerChannelChange() {
        ValueHolder trigger1 = new ValueHolder();
        ValueHolder trigger2 = new ValueHolder();

        BufferedValueModel buffer = new BufferedValueModel(subject, trigger1);
        assertSame(
            "Trigger channel has been changed.",
            buffer.getTriggerChannel(),
            trigger1);

        buffer.setTriggerChannel(trigger2);
        assertSame(
            "Trigger channel has been changed.",
            buffer.getTriggerChannel(),
            trigger2);
    }

    /**
     * Checks and verifies that commit and flush events are driven
     * by the current trigger channel.
     */
    public void testListensToCurrentTriggerChannel() {
        ValueHolder trigger1 = new ValueHolder();
        ValueHolder trigger2 = new ValueHolder();

        BufferedValueModel buffer = new BufferedValueModel(subject, trigger1);
        buffer.setValue("change1");
        Object subjectValue = subject.getValue();
        Object bufferedValue = buffer.getValue();
        trigger2.setValue(true);
        assertEquals(
            "Changing the unrelated trigger2 to true has no effect on the subject.",
            subject.getValue(),
            subjectValue);
        assertSame(
            "Changing the unrelated trigger2 to true has no effect on the buffer.",
            buffer.getValue(),
            bufferedValue);

        trigger2.setValue(false);
        assertEquals(
            "Changing the unrelated trigger2 to false has no effect on the subject.",
            subject.getValue(),
            subjectValue);
        assertSame(
            "Changing the unrelated trigger2 to false has no effect on the buffer.",
            buffer.getValue(),
            bufferedValue);

        // Change the trigger channel to trigger2.
        buffer.setTriggerChannel(trigger2);
        assertSame(
            "Trigger channel has been changed.",
            buffer.getTriggerChannel(),
            trigger2);

        trigger1.setValue(true);
        assertEquals(
            "Changing the unrelated trigger1 to true has no effect on the subject.",
            subject.getValue(),
            subjectValue);
        assertSame(
            "Changing the unrelated trigger1 to true has no effect on the buffer.",
            buffer.getValue(),
            bufferedValue);

        trigger1.setValue(false);
        assertEquals(
            "Changing the unrelated trigger1 to false has no effect on the subject.",
            subject.getValue(),
            subjectValue);
        assertSame(
            "Changing the unrelated trigger1 to false has no effect on the buffer.",
            buffer.getValue(),
            bufferedValue);

        // Commit using trigger2.
        trigger2.setValue(true);
        assertEquals(
            "Changing the current trigger2 to true commits the buffered value.",
            buffer.getValue(),
            subject.getValue());

        buffer.setValue("change2");
        subjectValue = subject.getValue();
        trigger2.setValue(false);
        assertEquals(
            "Changing the current trigger2 to false flushes the buffered value.",
            buffer.getValue(),
            subject.getValue());
        assertEquals(
            "Changing the current trigger2 to false flushes the buffered value.",
            buffer.getValue(),
            subjectValue);
    }


    // Tests Proper Update Notifications **************************************

    /**
     * Checks that subject changes fire value changes
     * if no value has been assigned.
     */
    public void testPropagatesSubjectChangesIfNoValueAssigned() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        PropertyChangeReport changeReport = new PropertyChangeReport();
        buffer.addValueChangeListener(changeReport);

        subject.setValue("change1");
        assertEquals("Value change.", 1, changeReport.eventCount());

        subject.setValue(null);
        assertEquals("Value change.", 2, changeReport.eventCount());

        subject.setValue("change2");
        assertEquals("Value change.", 3, changeReport.eventCount());

        subject.setValue(buffer.getValue());
        assertEquals("No value change.", 3, changeReport.eventCount());
    }

    /**
     * Tests that subject changes are not propagated once a value has
     * been assigned, i.e. the buffer is buffering.
     */
    public void testIgnoresSubjectChangesIfValueAssigned() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        PropertyChangeReport changeReport = new PropertyChangeReport();
        buffer.setValue("new buffer");
        buffer.addValueChangeListener(changeReport);

        subject.setValue("change1");
        assertEquals("Value change.", 0, changeReport.eventCount());

        subject.setValue(null);
        assertEquals("Value change.", 0, changeReport.eventCount());

        subject.setValue("change2");
        assertEquals("Value change.", 0, changeReport.eventCount());

        subject.setValue(buffer.getValue());
        assertEquals("No value change.", 0, changeReport.eventCount());
    }

    /**
     * Checks and verifies that a commit fires no value change.
     */
    public void testCommitFiresNoChangeOnSameOldAndNewValues() {
        BufferedValueModel buffer = createDefaultBufferedValueModel(
                new ValueHolder());
        buffer.setValue("value1");
        PropertyChangeReport changeReport = new PropertyChangeReport();
        buffer.addValueChangeListener(changeReport);

        assertEquals("No initial change.", 0, changeReport.eventCount());
        commit();
        assertEquals("First commit: no change.", 0, changeReport.eventCount());

        buffer.setValue("value2");
        assertEquals("Setting a value: a change.", 1, changeReport.eventCount());
        commit();
        assertEquals("Second commit: no change.", 1, changeReport.eventCount());
    }

    public void testCommitFiresChangeOnDifferentOldAndNewValues() {
        BufferedValueModel buffer = createDefaultBufferedValueModel(
                new ToUpperCaseStringHolder());
        buffer.setValue("initialValue");
        PropertyChangeReport changeReport = new PropertyChangeReport();
        buffer.addValueChangeListener(changeReport);
        buffer.setValue("value1");
        assertEquals("One event fired",
                1,
                changeReport.eventCount());
        assertEquals("First value set.",
                "value1",
                changeReport.lastNewValue());
        commit();
        assertEquals("Commit fires if the subject modifies the value.",
                2,
                changeReport.eventCount());
        assertEquals("Old value is the buffered value.",
                "value1",
                changeReport.lastOldValue());
        assertEquals("New value is the modified value.",
                "VALUE1",
                changeReport.lastNewValue());
    }

    /**
     * Tests that a flush event fires a value change if and only if
     * the flushed value does not equal the buffered value.
     */
    public void testFlushFiresTrueValueChanges() {
        BufferedValueModel buffer = createDefaultBufferedValueModel(new ValueHolder());
        PropertyChangeReport changeReport = new PropertyChangeReport();

        buffer.setValue("new buffer");
        subject.setValue("new subject");
        buffer.addValueChangeListener(changeReport);
        flush();
        assertEquals("First flush changes value.", 1, changeReport.eventCount());

        buffer.setValue(subject.getValue());
        assertEquals("Resetting value: no change.", 1, changeReport.eventCount());
        flush();
        assertEquals("Second flush: no change.", 1, changeReport.eventCount());

        buffer.setValue("new buffer2");
        assertEquals("Second value change.", 2, changeReport.eventCount());
        subject.setValue("new subject2");
        assertEquals("Setting new subject value: no change.", 2, changeReport.eventCount());
        buffer.setValue(subject.getValue());
        assertEquals("Third value change.", 3, changeReport.eventCount());
        flush();
        assertEquals("Third flush: no change.", 3, changeReport.eventCount());
    }

    public void testFlushChecksIdentity() {
        List<Object> list1 = new ArrayList<Object>();
        List<Object> list2 = new LinkedList<Object>();
        BufferedValueModel buffer = createDefaultBufferedValueModel(
                new ValueHolder());
        subject.setValue(list1);
        buffer.setValue(list2);
        assertSame("Buffered value is list2.",
                list2,
                buffer.getValue());
        PropertyChangeReport changeReport = new PropertyChangeReport();
        buffer.addValueChangeListener(changeReport);
        flush();
        assertSame("After flush, the value is list1.",
                list1,
                buffer.getValue());
        assertEquals("Flush fires an event for different subject value and buffer.",
                1,
                changeReport.eventCount());
        assertSame("The event's old value is list2.",
                list2,
                changeReport.lastOldValue());
        assertSame("The event's new value is list1.",
                list1,
                changeReport.lastNewValue());
    }

    public void testValueNoficationAfterSubjectChanged() {
        ValueModel initialSubject = new ValueHolder("initial value");
        BufferedValueModel buffer = new BufferedValueModel(initialSubject, new Trigger());
        PropertyChangeReport changeReport = new PropertyChangeReport();
        buffer.addValueChangeListener(changeReport);
        int eventCount = 0;
        Object newValue = "changed";
        initialSubject.setValue(newValue);
        assertEquals(
            "Value changed on original subject",
            ++eventCount,
            changeReport.eventCount());
        ValueModel subjectWithSame = new ValueHolder(newValue);
        buffer.setSubject(subjectWithSame);
        assertEquals(
            "Subject changed but same value as old",
            eventCount,
            changeReport.eventCount());
        ValueModel subject2 =
            new ValueHolder("changedSubjectWithDifferentValue");
        buffer.setSubject(subject2);
        assertEquals(
            "Value changed by changing the subject with different value",
            ++eventCount,
            changeReport.eventCount());
    }


    // Rejecting Access If Subject == null ************************************

    public void testRejectGetValueWhenSubjectIsNull() {
        BufferedValueModel buffer = new BufferedValueModel(null, triggerChannel);
        try {
            buffer.getValue();
            fail("The Buffer must reject attempts to read unbuffered values when the subject is null.");
        } catch (NullPointerException ex) {
            // The expected behavior
        }
        buffer.setSubject(subject);
        buffer.setValue("now buffering");
        buffer.setSubject(null);
        try {
            buffer.getValue();
            fail("The Buffer must reject attempts to read buffered values when the subject is null.");
        } catch (NullPointerException ex) {
            // The expected behavior
        }
    }

    public void testRejectSetValueWhenSubjectIsNull() {
        BufferedValueModel buffer = new BufferedValueModel(null, triggerChannel);
        try {
            Object value = "valuewithoutsubject??";
            buffer.setValue(value);
            fail("The Buffer must reject attempts to set values when the subject is null.");
        } catch (NullPointerException ex) {
            // The expected behavior
        }
        buffer.setSubject(subject);
        buffer.setValue("now buffering");
        buffer.setSubject(null);
        try {
            buffer.setValue("a new value");
            fail("The Buffer must reject attempts to set values when the subject is null - even if buffering.");
        } catch (NullPointerException ex) {
            // The expected behavior
        }
    }

    public void testRejectCommitWhenSubjectIsNull() {
        BufferedValueModel buffer = new BufferedValueModel(null, triggerChannel);
        try {
            commit();
            fail("The Buffer must reject attempts to commit when the subject is null.");
        } catch (NullPointerException ex) {
            // The expected behavior
        }
        buffer.setSubject(subject);
        buffer.setValue("now buffering");
        buffer.setSubject(null);
        try {
            commit();
            fail("The Buffer must reject attempts to commit when the subject is null - even if buffering.");
        } catch (NullPointerException ex) {
            // The expected behavior
        }
    }

    public void testRejectFlushWhenSubjectIsNull() {
        BufferedValueModel buffer = new BufferedValueModel(null, triggerChannel);
        try {
            flush();
            fail("The Buffer must reject attempts to commit when the subject is null.");
        } catch (NullPointerException ex) {
            // The expected behavior
        }
        buffer.setSubject(subject);
        buffer.setValue("now buffering");
        buffer.setSubject(null);
        try {
            flush();
            fail("The Buffer must reject attempts to commit when the subject is null - even if buffering.");
        } catch (NullPointerException ex) {
            // The expected behavior
        }
    }


    // Misc Tests *************************************************************

    /**
     * Tests read actions on a read-only model.
     */
    public void testReadOnly() {
        TestBean bean = new TestBean();
        bean.readOnlyObjectProperty = "testString";
        PropertyAdapter<TestBean> readOnlyModel =
            new PropertyAdapter<TestBean>(bean, "readOnlyObjectProperty");
        BufferedValueModel buffer = new BufferedValueModel(readOnlyModel, triggerChannel);

        assertSame(
            "Can read values from a read-only model.",
            buffer.getValue(),
            readOnlyModel.getValue());

        Object newValue1 = "new value";
        buffer.setValue(newValue1);
        assertSame(
            "Can read values from a read-only model when buffering.",
            buffer.getValue(),
            newValue1);

        flush();
        assertSame(
            "Can read values from a read-only model after a flush.",
            buffer.getValue(),
            bean.readOnlyObjectProperty);

        buffer.setValue("new value2");
        try {
            commit();
            fail("Cannot commit to a read-only model.");
        } catch (Exception e) {
            // The expected behavior
        }
    }

    /**
     * Tests write actions on a write-only model.
     */
    public void testWriteOnly() {
        TestBean bean = new TestBean();
        bean.writeOnlyObjectProperty = "testString";
        PropertyAdapter<TestBean> writeOnlyModel =
            new PropertyAdapter<TestBean>(bean, "writeOnlyObjectProperty");
        BufferedValueModel buffer = new BufferedValueModel(writeOnlyModel, triggerChannel);

        Object newValue1 = "new value";
        buffer.setValue(newValue1);
        assertSame(
            "Can buffer a value on a write-only model.",
            buffer.getValue(),
            newValue1);

        commit();
        writeOnlyModel.setValue("new value2");
        try {
            flush();
            fail("Cannot flush a value from a write-only model.");
        } catch (Exception e) {
            // The expected behavior
        }
    }


    // Test Implementations ***************************************************

    private void testSetValueFiresProperEvents(Object oldValue, Object newValue, boolean eventExpected) {
        BufferedValueModel valueModel =
            new BufferedValueModel(new ValueHolder(oldValue), new Trigger());
        testFiresProperEvents(valueModel, oldValue, newValue, eventExpected);
    }

    private void testValueChangeSendsProperEvents(Object oldValue, Object newValue, boolean eventExpected) {
        BufferedValueModel defaultModel = createDefaultBufferedValueModel();
        defaultModel.setValue(oldValue);
        testFiresProperEvents(defaultModel, oldValue, newValue, eventExpected);
    }

    private void testFiresProperEvents(BufferedValueModel valueModel, Object oldValue, Object newValue, boolean eventExpected) {
        PropertyChangeReport changeReport = new PropertyChangeReport();
        valueModel.addValueChangeListener(changeReport);
        int expectedEventCount = eventExpected ? 1 : 0;

        valueModel.setValue(newValue);
        assertEquals(
                "Expected event count after ( " + oldValue + " -> " + newValue + ").",
                expectedEventCount,
                changeReport.eventCount());
        if (eventExpected) {
            assertEquals("Event's old value.", oldValue, changeReport.lastOldValue());
            assertEquals("Event's new value.", newValue, changeReport.lastNewValue());
        }
    }


    // Helper Code ************************************************************

    private void commit() {
        triggerChannel.triggerCommit();
    }

    private void flush() {
        triggerChannel.triggerFlush();
    }

    private BufferedValueModel createDefaultBufferedValueModel() {
        subject.setValue(RESET_VALUE);
        return new BufferedValueModel(subject, triggerChannel);
    }

    private BufferedValueModel createDefaultBufferedValueModel(ValueModel aSubject) {
        this.subject = aSubject;
        subject.setValue(RESET_VALUE);
        return new BufferedValueModel(subject, triggerChannel);
    }


    // Setup / Teardown *******************************************************

    /**
     * @throws Exception  in case of an unexpected problem
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        subject = new CloningValueHolder(INITIAL_VALUE);
        triggerChannel = new Trigger();
    }

    /**
     * @throws Exception  in case of an unexpected problem
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        subject = null;
        triggerChannel = null;
    }


}
