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
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

/**
 * Tests old and new values when the bean, value or subject changes in
 * BeanAdapter, PropertyAdapter, PresentationModel and BufferedValueModel.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.9 $
 */
public final class ValueHolderTest extends TestCase {


    // Public Tests ***********************************************************

    public void testEquityTestingHolderSendsProperEvents() {
        ValueHolder holder = new ValueHolder();

        Object obj1  = new Integer(1);
        Object obj2a = new Integer(2);
        Object obj2b = new Integer(2);
        testValueChangeSendsProperEvent(holder, null, obj1,   true);
        testValueChangeSendsProperEvent(holder, obj1, null,   true);
        testValueChangeSendsProperEvent(holder, obj1, obj1,   false);
        testValueChangeSendsProperEvent(holder, obj1, obj2a,  true);
        testValueChangeSendsProperEvent(holder, obj2a, obj2b, false); // equals
        testValueChangeSendsProperEvent(holder, null, null,   false);
    }


    public void testIdentityTestingHolderSendsProperEvents() {
        ValueHolder holder = new ValueHolder(null, true);

        Object obj1  = new Integer(1);
        Object obj2a = new Integer(2);
        Object obj2b = new Integer(2);
        testValueChangeSendsProperEvent(holder, null, obj1,   true);
        testValueChangeSendsProperEvent(holder, obj1, null,   true);
        testValueChangeSendsProperEvent(holder, obj1, obj1,   false);
        testValueChangeSendsProperEvent(holder, obj1, obj2a,  true);
        testValueChangeSendsProperEvent(holder, obj2a, obj2b, true); // !=
        testValueChangeSendsProperEvent(holder, null, null,   false);
    }


    // Test Implementations ***************************************************

    private void testValueChangeSendsProperEvent(
            ValueModel valueModel, Object oldValue, Object newValue, boolean eventExpected) {
        valueModel.setValue(oldValue);
        PropertyChangeReport changeReport = new PropertyChangeReport();
        valueModel.addValueChangeListener(changeReport);
        int expectedEventCount = eventExpected ? 1 : 0;

        valueModel.setValue(newValue);
        assertEquals(
                "Expected event count after ( "
              + oldValue + " -> " + newValue + ").",
                expectedEventCount,
                changeReport.eventCount());
        if (eventExpected) {
            assertEquals("Event's old value.", oldValue, changeReport.lastOldValue());
            assertEquals("Event's new value.", newValue, changeReport.lastNewValue());
        }
    }


}
