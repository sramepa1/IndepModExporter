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

import javax.swing.BoundedRangeModel;

import junit.framework.TestCase;

import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

/**
 * A test case for class {@link BoundedRangeAdapter}.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.7 $
 */
public final class BoundedRangeAdapterTest extends TestCase {


    // Constructor Tests ******************************************************

    public void testConstructorRejectsNullSubject() {
        try {
            new BoundedRangeAdapter(null, 1, 0, 10);
            fail("BoundedRangeAdapter constructor failed to reject a null subject.");
        } catch (NullPointerException ex) {
            // The expected behavior
        }
    }

    public void testConstructorAcceptsNullSubjectValue() {
        new BoundedRangeAdapter(new ValueHolder(), 1, 0, 10);
    }

    public void testConstructorRejectsIllegalArguments() {
        ValueModel subject = new ValueHolder(1);
        try {
            new BoundedRangeAdapter(subject, 1, 0, -1);
            fail("Constructor must reject if min > max.");
        } catch (IllegalArgumentException ex) {
            // The expected behavior
        }
        try {
            new BoundedRangeAdapter(subject, 1, 2, 0);
            fail("Constructor must reject if initial value < min.");
        } catch (IllegalArgumentException ex) {
            // The expected behavior
        }
        try {
            new BoundedRangeAdapter(subject, 1, 0, 1);
            fail("Constructor must reject if initial value + extent > max.");
        } catch (IllegalArgumentException ex) {
            // The expected behavior
        }
        try {
            new BoundedRangeAdapter(subject, -1, 0, 10);
            fail("Constructor must reject if extent < 0.");
        } catch (IllegalArgumentException ex) {
            // The expected behavior
        }
    }


    // Basic Adapter Features *************************************************

    public void testAcceptsNullSubjectValue() {
        BoundedRangeModel adapter = new BoundedRangeAdapter(new ValueHolder(), 1, 0, 10);
        adapter.getValue();
    }


    public void testReadsSubjectValue() {
        ValueModel subject = new ValueHolder(1);
        BoundedRangeModel adapter = new BoundedRangeAdapter(subject, 1, 0, 10);
        assertEquals("The adapter returns the initial subject value.",
                subject.getValue(),
                new Integer(adapter.getValue()));
        subject.setValue(new Integer(2));
        assertEquals("The adapter reflects the subject value change.",
                subject.getValue(),
                new Integer(adapter.getValue()));
    }


    public void testWritesSubjectValue() {
        ValueModel subject = new ValueHolder(1);
        BoundedRangeModel adapter = new BoundedRangeAdapter(subject, 1, 0, 10);
        adapter.setValue(2);
        assertEquals("The subject returns the new adapter value.",
                subject.getValue(),
                new Integer(adapter.getValue()));
    }


}
