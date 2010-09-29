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

import java.lang.reflect.InvocationTargetException;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import junit.framework.TestCase;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.TextComponentConnector;
import com.jgoodies.binding.tests.value.ToUpperCaseStringHolder;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

/**
 * A test case for class {@link TextComponentConnector}.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.5 $
 */
public final class TextComponentConnectorTest extends TestCase {

    // Constructor Tests ******************************************************

    public void testConstructorRejectsNullParameters() {
        ValueModel subject = new ValueHolder();
        JTextField textField = new JTextField();
        try {
            new TextComponentConnector(null, textField);
            fail("Constructor failed to reject null subject.");
        } catch (NullPointerException ex) {
            // The expected behavior
        }
        try {
            new TextComponentConnector(subject, (JTextField) null);
            fail("Constructor failed to reject null text field.");
        } catch (NullPointerException ex) {
            // The expected behavior
        }
    }

    public void testConstructorLeavesSubjectAndTextFieldUnchanged() {
        String subjectText  = "subjectText";
        String fieldText = "fieldText";
        ValueModel subject  = new ValueHolder(subjectText);
        JTextField textField = new JTextField(fieldText);
        new TextComponentConnector(subject, textField);
        assertEquals("The constructor must not change the subject value.",
                subjectText,
                subject.getValue());
        assertEquals("The constructor must not change the text field text.",
                fieldText,
                textField.getText());
    }


    public void testAcceptsMultipleReleases() {
        String subjectText  = "subjectText";
        String fieldText = "fieldText";
        ValueModel subject  = new ValueHolder(subjectText);
        JTextField textField = new JTextField(fieldText);
        TextComponentConnector connector =
            new TextComponentConnector(subject, textField);
        connector.release();
        connector.release();
    }


    public void testBindingsUpdatesTextFieldText() {
        String subjectText  = "subjectText";
        String fieldText = "fieldText";
        ValueModel subject  = new ValueHolder(subjectText);
        JTextField textField = new JTextField(fieldText);
        Bindings.bind(textField, subject);
        assertEquals("The #bind method must not change the subject value.",
                subjectText,
                subject.getValue());
        assertEquals("The #bind method must change the text field text.",
                subjectText,
                textField.getText());
    }


    // Synchronization ********************************************************

    public void testSubjectChangeUpdatesTextComponent() {
        String subjectText  = "subjectText";
        String fieldText = "fieldText";
        ValueModel subject  = new ValueHolder(subjectText);
        JTextField textField = new JTextField(fieldText);
        Bindings.bind(textField, subject);
        subject.setValue("newSubjectText");
        assertEquals(
            "Failed to update the text component.",
            subject.getValue(),
            textField.getText());
    }


    public void testTextComponentChangeUpdatesSubject() {
        String subjectText  = "subjectText";
        String fieldText = "fieldText";
        ValueModel subject  = new ValueHolder(subjectText);
        JTextField textField = new JTextField(fieldText);
        Bindings.bind(textField, subject);
        textField.setText("newFieldText");
        assertEquals(
            "Failed to update the text component.",
            textField.getText(),
            subject.getValue());
    }


    public void testTextComponentChangeHonorsSubjectModifications() {
        String subjectText  = "subjectText";
        String fieldText = "fieldText";
        final ValueModel subject  = new ToUpperCaseStringHolder();
        subject.setValue(subjectText);
        final JTextField textField = new JTextField(fieldText);
        Bindings.bind(textField, subject);
        textField.setText("newFieldText");
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    assertEquals(
                        "Failed to honor the subject modifications.",
                        subject.getValue(),
                        textField.getText());
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


}
