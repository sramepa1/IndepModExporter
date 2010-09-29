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

import java.awt.Color;
import java.util.Date;

import javax.swing.JFormattedTextField;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import junit.framework.TestCase;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

/**
 * A test case for class {@link BasicComponentFactory}.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.11 $
 */
public final class BasicComponentFactoryTest extends TestCase {

    // Color Chooser **********************************************************

    public void testRejectPlainColorChooserCreationWithInitialNullValue() {
        ValueModel valueModel = new ValueHolder();
        try {
            BasicComponentFactory.createColorChooser(valueModel);
            fail("The ValueModel's value must not be null at creation time.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
    }

    public void testPlainColorChooserWithNullValue() {
        ValueModel valueModel = new ValueHolder(Color.RED);
        BasicComponentFactory.createColorChooser(valueModel);
        valueModel.setValue(null);
    }


    public void testRejectColorChooserCreationWithNullDefaultColor() {
        ValueModel valueModel = new ValueHolder(Color.RED);
        try {
            BasicComponentFactory.createColorChooser(valueModel, null);
            fail("The default color must not be null.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
    }


    public void testAcceptColorChooserCreationWithInitialNullValue() {
        ValueModel valueModel = new ValueHolder();
        BasicComponentFactory.createColorChooser(valueModel, Color.RED);
    }


    // Dates ******************************************************************

    public void testDateFieldMapsNullToEmpty() {
        ValueModel dateModel = new ValueHolder();
        JFormattedTextField dateField = BasicComponentFactory.createDateField(
                dateModel);
        assertEquals("Date field maps null to an empty string.",
                "",
                dateField.getText());

        dateModel.setValue(new Date(32168));
        setTextAndCommitEdit(dateField, "");
        assertEquals("Date field maps the empty string to null.",
                null,
                dateModel.getValue());
        dateModel.setValue(new Date(424242));
        setTextAndCommitEdit(dateField, "  ");
        assertEquals("Date field maps blank strings to null.",
                null,
                dateModel.getValue());
    }


    public void testDateFieldIsValidOnEmptyAndBlank() {
        JFormattedTextField dateField = BasicComponentFactory.createDateField(
                new ValueHolder());
        assertTrue("The empty field is valid.", dateField.isEditValid());
        dateField.setText("  ");
        assertTrue("The blank field is valid.", dateField.isEditValid());
    }


    // Integers ***************************************************************

    public void testDefaultIntegerFieldMapsNullToEmpty() {
        ValueHolder integerModel = new ValueHolder();
        JFormattedTextField integerField = BasicComponentFactory.createIntegerField(
                integerModel);
        assertEquals("Default integer field maps null to an empty string.",
                "",
                integerField.getText());

        integerModel.setValue(42L);
        setTextAndCommitEdit(integerField, "");
        assertEquals("Default integer field maps the empty string to null.",
                null,
                integerModel.getValue());
        integerModel.setValue(32168);
        setTextAndCommitEdit(integerField, "  ");
        assertEquals("Default integer field maps blank strings to null.",
                null,
                integerModel.getValue());
    }


    public void testCustomIntegerFieldMapsEmptyValueToEmpty() {
        Integer emptyValue = new Integer(-1);
        ValueHolder integerModel = new ValueHolder(emptyValue);
        JFormattedTextField integerField = BasicComponentFactory.createIntegerField(
                integerModel,
                emptyValue.intValue());
        assertEquals("Custom integer field maps the empty value to an empty string.",
                "",
                integerField.getText());

        integerModel.setValue(42L);
        setTextAndCommitEdit(integerField, "");
        assertEquals("Custom integer field maps the empty string to the empty value.",
                emptyValue,
                integerModel.getValue());
        integerModel.setValue(32168);
        setTextAndCommitEdit(integerField, "  ");
        assertEquals("Custom integer field maps blank strings to the empty value.",
                emptyValue,
                integerModel.getValue());
    }


    public void testDefaultIntegerFieldIsValidOnEmptyAndBlank() {
        JFormattedTextField integerField = BasicComponentFactory.createIntegerField(
                new ValueHolder());
        assertTrue("The empty field is valid.", integerField.isEditValid());
        integerField.setText("  ");
        assertTrue("The blank field is valid.", integerField.isEditValid());
    }


    // Longs ******************************************************************

    public void testDefaultLongFieldMapsNullToEmpty() {
        ValueHolder longModel = new ValueHolder();
        JFormattedTextField longField = BasicComponentFactory.createLongField(
                longModel);
        assertEquals("Default long field maps null to an empty string.",
                "",
                longField.getText());

        longModel.setValue(42L);
        setTextAndCommitEdit(longField, "");
        assertEquals("Default long field maps the empty string to null.",
                null,
                longModel.getValue());
        longModel.setValue(32168);
        setTextAndCommitEdit(longField, "  ");
        assertEquals("Default long field maps blank strings to null.",
                null,
                longModel.getValue());
    }


    public void testCustomLongFieldMapsEmptyValueToEmpty() {
        Long emptyValue = new Long(-1);
        ValueHolder longModel = new ValueHolder(emptyValue);
        JFormattedTextField longField = BasicComponentFactory.createLongField(
                longModel,
                emptyValue.longValue());
        assertEquals("Custom long field maps the empty value to an empty string.",
                "",
                longField.getText());

        longModel.setValue(42L);
        setTextAndCommitEdit(longField, "");
        assertEquals("Custom long field maps the empty string to the empty value.",
                emptyValue,
                longModel.getValue());
        longModel.setValue(32168);
        setTextAndCommitEdit(longField, "  ");
        assertEquals("Custom long field maps blank strings to the empty value.",
                emptyValue,
                longModel.getValue());
    }


    public void testDefaultLongFieldIsValidOnEmptyAndBlank() {
        JFormattedTextField longField = BasicComponentFactory.createLongField(
                new ValueHolder());
        assertTrue("The empty field is valid.", longField.isEditValid());
        longField.setText("  ");
        assertTrue("The blank field is valid.", longField.isEditValid());
    }


    // Filtering **************************************************************

    private static final String TEXT_WITH_NEWLINE =
        "First line\nsecond line";

    private static final String TEXT_WITHOUT_NEWLINE =
        TEXT_WITH_NEWLINE.replace('\n', ' ');


    public void testPasswordFieldFiltersNewlines() {
        ValueModel subject = new ValueHolder(TEXT_WITH_NEWLINE);
        JPasswordField passwordField = BasicComponentFactory.createPasswordField(subject);
        assertEquals("The password field's text contains no newlines.",
                TEXT_WITHOUT_NEWLINE, new String(passwordField.getPassword()));
    }


    public void testTextAreaRetainsNewlines() {
        ValueModel subject = new ValueHolder(TEXT_WITH_NEWLINE);
        JTextArea textArea = BasicComponentFactory.createTextArea(subject);
        assertEquals("The text area's text contains newlines.",
                TEXT_WITH_NEWLINE, textArea.getText());
    }


    public void testTextFieldFiltersNewlines() {
        ValueModel subject = new ValueHolder(TEXT_WITH_NEWLINE);
        JTextField textField = BasicComponentFactory.createTextField(subject);
        assertEquals("The text field's text contains no newlines.",
                TEXT_WITHOUT_NEWLINE, textField.getText());
    }


    // Helper Code ************************************************************

    private void setTextAndCommitEdit(
            JFormattedTextField formattedTextField, String text) {
        formattedTextField.setText(text);
        try {
            formattedTextField.commitEdit();
        } catch (Exception e) {
            fail("Failed to commit the text '" + text + "'.");
        }
    }


}

