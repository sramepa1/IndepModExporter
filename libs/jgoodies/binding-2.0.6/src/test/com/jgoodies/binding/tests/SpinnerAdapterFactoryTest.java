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

import java.util.Date;

import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import junit.framework.TestCase;

import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

/**
 * A test case for class {@link SpinnerAdapterFactory}.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.4 $
 */
public final class SpinnerAdapterFactoryTest extends TestCase {

    // #connect ***************************************************************

    public void testConnectRejectsNullParameters() {
        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 0, 10, 2);
        ValueModel   valueModel   = new ValueHolder(1);
        Object       defaultValue = new Integer(1);
        try {
            SpinnerAdapterFactory.connect(null, valueModel, defaultValue);
            fail("#connect must reject null spinner models.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            SpinnerAdapterFactory.connect(spinnerModel, null, defaultValue);
            fail("#connect must reject null value models.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            SpinnerAdapterFactory.connect(spinnerModel, valueModel, null);
            fail("#connect must reject null default values.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
    }


    // #createDateAdapter *****************************************************

    public void testCreateDateAdapterRejectsNullParameters() {
        try {
            SpinnerAdapterFactory.createDateAdapter(null, new Date());
            fail("The factory must reject null value models.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
        try {
            SpinnerAdapterFactory.createDateAdapter(new ValueHolder(), null);
            fail("The factory must reject null default dates.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
    }


    public void testCreateDateAdapterAcceptsInitialNullValueModelValues() {
        Object initialValue = null;
        ValueModel valueModel = new ValueHolder(initialValue);
        Date defaultDate = new Date();
        SpinnerModel spinnerModel = SpinnerAdapterFactory.createDateAdapter(
                valueModel, defaultDate);
        assertEquals("The spinner model's value is the default date.",
                defaultDate,
                spinnerModel.getValue());
    }


    public void testCreateDateAdapterAcceptsNullValueModelValues() {
        Object initialValue = new Date(51267);
        ValueModel valueModel = new ValueHolder(initialValue);
        Date defaultDate = new Date();
        SpinnerModel spinnerModel = SpinnerAdapterFactory.createDateAdapter(
                valueModel, defaultDate);
        assertEquals("The spinner model's value is the value model's value.",
                valueModel.getValue(),
                spinnerModel.getValue());
        valueModel.setValue(null);
        assertEquals("The spinner model's value is the default date.",
                defaultDate,
                spinnerModel.getValue());
    }


    // #createNumberAdapter ***************************************************

    public void testCreateNumberAdapterRejectsNullValueModel() {
        try {
            SpinnerAdapterFactory.createNumberAdapter(null, 0, 0, 10, 1);
            fail("The factory must reject null value models.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
    }


    public void testCreateNumberAdapterAcceptsInitialNullValueModelValues() {
        Object initialValue = null;
        ValueModel valueModel = new ValueHolder(initialValue);
        int defaultValue = 5;
        SpinnerModel spinnerModel = SpinnerAdapterFactory.createNumberAdapter(
                valueModel, defaultValue, 0, 10, 2);
        assertEquals("The spinner model's value is the default value.",
                new Integer(defaultValue),
                spinnerModel.getValue());
    }


    public void testCreateNumberAdapterAcceptsNullValueModelValues() {
        Object initialValue = new Integer(1);
        ValueModel valueModel = new ValueHolder(initialValue);
        int defaultValue = 5;
        SpinnerModel spinnerModel = SpinnerAdapterFactory.createNumberAdapter(
                valueModel, defaultValue, 0, 10, 2);
        assertEquals("The spinner model's value is the value model's value.",
                valueModel.getValue(),
                spinnerModel.getValue());
        valueModel.setValue(null);
        assertEquals("The spinner model's value is the default value.",
                new Integer(defaultValue),
                spinnerModel.getValue());
    }


}
