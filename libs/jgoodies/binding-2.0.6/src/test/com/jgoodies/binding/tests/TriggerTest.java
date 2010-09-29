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

import com.jgoodies.binding.tests.event.PropertyChangeReport;
import com.jgoodies.binding.value.Trigger;

/**
 * A test case for class {@link Trigger}.
 *
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.11 $
 */
public final class TriggerTest extends TestCase {


    /**
     * Verifies that the trigger accepts Boolean values and null.
     */
    public void testAcceptsBoolean() {
        Trigger trigger = new Trigger();
        trigger.setValue(true);
        trigger.setValue(false);
        trigger.setValue(null);
        trigger.setValue(Boolean.TRUE);
        trigger.setValue(Boolean.FALSE);
    }


    /**
     * Checks that Trigger.setValue rejects non-Boolean values with an
     * IllegalArgumentException.
     */
    public void testRejectsNonBooleans() {
        Trigger trigger = new Trigger();
        try {
            trigger.setValue("Wow");
            fail("The Trigger must reject non-Boolean values, here: String.");
        } catch (IllegalArgumentException e) {
            // The expected behavior
        }
        try {
            trigger.setValue(1967);
            fail("The Trigger must reject non-Boolean values, here: Integer.");
        } catch (IllegalArgumentException e) {
            // The expected behavior
        }
    }


    /**
     * Checks that <code>#triggerCommit</code> fires a change event with new
     * value = Boolean.TRUE.
     */
    public void testTriggerCommit() {
        Trigger trigger = new Trigger();
        PropertyChangeReport changeReport = new PropertyChangeReport();

        trigger.setValue(null);
        trigger.addValueChangeListener(changeReport);
        trigger.triggerCommit();
        assertEquals(
            "Commit from null fires a single event.",
            changeReport.eventCount(),
            1);
        assertEquals(
            "Event new value equals Boolean.TRUE.",
            changeReport.lastNewValue(),
            Boolean.TRUE);
        assertEquals(
            "Trigger value equals Boolean.TRUE.",
            trigger.getValue(),
            Boolean.TRUE);

        trigger.setValue(true);
        assertEquals("Nothing changed.", changeReport.eventCount(), 1);
        trigger.triggerCommit();
        assertEquals(
            "Commit from TRUE fires two events.",
            changeReport.eventCount(),
            3);
        assertEquals(
            "Event new value equals Boolean.TRUE.",
            changeReport.lastNewValue(),
            Boolean.TRUE);
        assertEquals(
            "Trigger value equals Boolean.TRUE.",
            trigger.getValue(),
            Boolean.TRUE);

        trigger.setValue(false);
        assertEquals(
            "Changed from true to false.",
            changeReport.eventCount(),
            4);
        trigger.triggerCommit();
        assertEquals(
            "Commit from FALSE fires a single events.",
            changeReport.eventCount(),
            5);
        assertEquals(
            "Event new value equals Boolean.TRUE.",
            changeReport.lastNewValue(),
            Boolean.TRUE);
        assertEquals(
            "Trigger value equals Boolean.TRUE.",
            trigger.getValue(),
            Boolean.TRUE);
    }


    /**
     * Checks that <code>#triggerFlush</code> fires a change event with new
     * value = Boolean.FALSE.
     */
    public void testTriggerFlush() {
        Trigger trigger = new Trigger();
        PropertyChangeReport changeReport = new PropertyChangeReport();

        trigger.setValue(null);
        trigger.addValueChangeListener(changeReport);
        trigger.triggerFlush();
        assertEquals(
            "Flush from null fires a single event.",
            changeReport.eventCount(),
            1);
        assertEquals(
            "Event new value equals Boolean.FALSE.",
            changeReport.lastNewValue(),
            Boolean.FALSE);
        assertEquals(
            "Trigger value equals Boolean.FALSE.",
            trigger.getValue(),
            Boolean.FALSE);

        trigger.setValue(true);
        assertEquals(
            "Changed from false to true.",
            changeReport.eventCount(),
            2);
        trigger.triggerFlush();
        assertEquals(
            "Flush from TRUE fires a single event.",
            changeReport.eventCount(),
            3);
        assertEquals(
            "Event new value equals Boolean.FALSE.",
            changeReport.lastNewValue(),
            Boolean.FALSE);
        assertEquals(
            "Trigger value equals Boolean.FALSE.",
            trigger.getValue(),
            Boolean.FALSE);

        trigger.setValue(false);
        assertEquals("Nothing changed.", changeReport.eventCount(), 3);
        trigger.triggerFlush();
        assertEquals(
            "Flush from FALSE fires two events.",
            changeReport.eventCount(),
            5);
        assertEquals(
            "Event new value equals Boolean.FALSE.",
            changeReport.lastNewValue(),
            Boolean.FALSE);
        assertEquals(
            "Trigger value equals Boolean.FALSE.",
            trigger.getValue(),
            Boolean.FALSE);
    }

}
