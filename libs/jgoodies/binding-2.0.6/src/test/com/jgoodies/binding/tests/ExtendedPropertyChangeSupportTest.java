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

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;
import com.jgoodies.binding.tests.event.PropertyChangeReport;

/**
 * A test case for class {@link ExtendedPropertyChangeSupport}.<p>
 *
 * TODO: Test multicast events.
 *
 * @author <a href="mailto:neuling@dakosy.de">Mattias Neuling</a>
 * @author Karsten Lentzsch
 * @version $Revision: 1.11 $
 */
public final class ExtendedPropertyChangeSupportTest extends TestCase {

    private String one;
    private String two;
    private List<String> emptyList1;
    private List<String> emptyList2;
    private List<String> list1a;
    private List<String> list1b;

    /**
     * @throws Exception   in case of an unexpected problem
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        one = "one";
        two = "two";
        emptyList1 = new ArrayList<String>();
        emptyList2 = new ArrayList<String>();
        list1a = new ArrayList<String>();
        list1a.add(one);
        list1b = new ArrayList<String>();
        list1b.add(one);
    }

    /**
     * @throws Exception   in case of an unexpected problem
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        emptyList1 = null;
        emptyList2 = null;
        list1a = null;
        list1b = null;
    }

    public void testCommonBehavior() {
        testCommon("name1", null, null, null);
        testCommon("name1", null, null, one);
        testCommon("name1", null, one, null);
        testCommon("name1", null, one, one);
        testCommon("name1", null, one, two);
        testCommon("name1", null, emptyList1, emptyList2);
        testCommon("name1", null, list1a, list1b);

        testCommon("name1", "name1", null, null);
        testCommon("name1", "name1", null, one);
        testCommon("name1", "name1", one, null);
        testCommon("name1", "name1", one, one);
        testCommon("name1", "name1", one, two);
        testCommon("name1", "name1", emptyList1, emptyList2);
        testCommon("name1", "name1", list1a, list1b);

        testCommon("name1", "name2", null, null);
        testCommon("name1", "name2", null, one);
        testCommon("name1", "name2", one, null);
        testCommon("name1", "name2", one, one);
        testCommon("name1", "name2", one, two);
        testCommon("name1", "name2", emptyList1, emptyList2);
        testCommon("name1", "name2", list1a, list1b);
    }

    public void testDifferences() {
        testDifference("name1", "name1", emptyList1, emptyList2);
        testDifference("name1", "name1", list1a, list1b);
    }

    private void testCommon(
            String observedPropertyName,
            String changedPropertyName,
            Object oldValue,
            Object newValue) {
        fireAndCount(true, observedPropertyName, changedPropertyName, oldValue, newValue, false, false);
    }

    private void testDifference(
            String observedPropertyName,
            String changedPropertyName,
            Object oldValue,
            Object newValue) {
        fireAndCount(false, observedPropertyName, changedPropertyName, oldValue, newValue, true, false);
        fireAndCount(false, observedPropertyName, changedPropertyName, oldValue, newValue, false, true);
    }

    private void fireAndCount(
            boolean countsShallBeEqual,
            String observedPropertyName,
            String changedPropertyName,
            Object oldValue,
            Object newValue,
            boolean checkIdentityDefault,
            boolean checkIdentity) {
        PropertyChangeReport epcsNamedCounter     = new PropertyChangeReport();
        PropertyChangeReport epcsMulticastCounter = new PropertyChangeReport();
        PropertyChangeReport pcsNamedCounter      = new PropertyChangeReport();
        PropertyChangeReport pcsMulticastCounter  = new PropertyChangeReport();

        ExtendedPropertyChangeSupport epcs = new ExtendedPropertyChangeSupport(this, checkIdentityDefault);
        PropertyChangeSupport         pcs  = new PropertyChangeSupport(this);

        epcs.addPropertyChangeListener(observedPropertyName, epcsNamedCounter);
        epcs.addPropertyChangeListener(epcsMulticastCounter);
        pcs.addPropertyChangeListener(observedPropertyName, pcsNamedCounter);
        pcs.addPropertyChangeListener(pcsMulticastCounter);

        if (checkIdentity) {
            epcs.firePropertyChange(changedPropertyName, oldValue, newValue, true);
        } else {
            epcs.firePropertyChange(changedPropertyName, oldValue, newValue);
        }
        pcs.firePropertyChange(changedPropertyName,  oldValue, newValue);

        boolean namedCountersAreEqual =
            pcsNamedCounter.eventCount() == epcsNamedCounter.eventCount();

        boolean multicastCountersAreEqual =
            pcsMulticastCounter.eventCount() == epcsMulticastCounter.eventCount();

        if (countsShallBeEqual) {
            assertTrue(
                "Named counters shall be equal",
                namedCountersAreEqual);
            assertTrue(
                "Multicast counters shall be equal",
                multicastCountersAreEqual);
        } else {
            assertFalse(
                "Named counters shall differ",
                namedCountersAreEqual);
            assertFalse(
                "Multicast counters shall differ",
                multicastCountersAreEqual);
        }
    }

}
