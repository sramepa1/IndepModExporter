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

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import junit.framework.TestCase;

import com.jgoodies.binding.adapter.PreferencesAdapter;
import com.jgoodies.binding.value.AbstractValueModel;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;


/**
 * A test case for class {@link PreferencesAdapter}.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.9 $
 */
public final class PreferencesAdapterTest extends TestCase {

    private static final String NODE_NAME = "unit-tests";

    private Preferences prefs;

    @Override
    protected void setUp() {
        prefs = Preferences.userRoot().node(NODE_NAME);
    }

    @Override
    protected void tearDown() {
        try {
            prefs.removeNode();
        } catch (BackingStoreException e) {
            // TODO: handle exception
        }
        prefs = null;
    }

    // Parameter Tests ******************************************************

    public void testConstructorRejectsNullValues() {
        String key = "constructorNullTest";
        prefs.remove(key);
        try {
            new PreferencesAdapter(null, key, "default");
            fail("PreferencesAdapter(Preferences, String, Object) failed to reject null Preferences.");
        } catch (NullPointerException ex) {
            // The expected behavior
        }
        try {
            new PreferencesAdapter(prefs, null, "default");
            fail("PreferencesAdapter(Preferences, String, Object) failed to reject a null key.");
        } catch (NullPointerException ex) {
            // The expected behavior
        }
        try {
            new PreferencesAdapter(prefs, key, null);
            fail("PreferencesAdapter(Preferences, String, Object) failed to reject a null default value.");
        } catch (NullPointerException ex) {
            // The expected behavior
        }
    }


    public void testConstructorAcceptsKnownDefaultValueTypes() {
        String key = "constructorIllegalTypeTest";
        prefs.remove(key);
        new PreferencesAdapter(prefs, key, "String");
        new PreferencesAdapter(prefs, key, Boolean.TRUE);
        new PreferencesAdapter(prefs, key, new Double(12.3));
        new PreferencesAdapter(prefs, key, new Float(12.3F));
        new PreferencesAdapter(prefs, key, new Integer(12));
        new PreferencesAdapter(prefs, key, new Long(12L));
    }


    public void testConstructorRejectsIllegalDefaultValueTypes() {
        String key = "constructorIllegalTypeTest";
        prefs.remove(key);
        try {
            new PreferencesAdapter(prefs, key, new ValueHolder(1));
            fail("PreferencesAdapter(Preferences, String, Object) failed to reject an invalid default value type.");
        } catch (IllegalArgumentException ex) {
            // The expected behavior
        }
    }


    // Basic Adapter Features *************************************************

    public void testReadWrittenValue() {
        testReadWrittenValue("Boolean", Boolean.TRUE,   Boolean.FALSE);
        testReadWrittenValue("Double",  new Double(1),  new Double(2));
        testReadWrittenValue("Float",   new Float(1),   new Float(2));
        testReadWrittenValue("Integer", new Integer(1), new Integer(2));
        testReadWrittenValue("Long",    new Long(1),    new Long(2));
        testReadWrittenValue("String",  "default",      "new");
    }

    /**
     * Checks that the PreferencesAdapter returns the default value
     * if no value has been stored in the preferences under the given key.
     * This test first removes the value for the given key - if any.
     */
    public void testReadDefaultValue() {
        testReadDefaultValue("booleanDefault", Boolean.TRUE);
        testReadDefaultValue("doubleDefault",  new Double(1));
        testReadDefaultValue("floatDefault",   new Float(1));
        testReadDefaultValue("integerDefault", new Integer(1));
        testReadDefaultValue("longDefault",    new Long(1));
        testReadDefaultValue("stringDefault",  "default");
    }

    /**
     * Checks that writing the default value actually writes a value.
     * Just checks a String-typed value, because the mechanism is all
     * the same for the different types.
     */
    public void testSettingDefaultValueWrites() {
        String key = "stringTest";
        Object defaultValue = "default";
        Object prototypeValue = "prototype";
        AbstractValueModel writeAdapter =
            new PreferencesAdapter(prefs, key, defaultValue);
        writeAdapter.setValue(defaultValue);
        AbstractValueModel readAdapter =
            new PreferencesAdapter(prefs, key, prototypeValue);
        assertEquals("Failed to return the previously set value.",
                defaultValue,
                readAdapter.getValue());
    }


    public void testRejectsWritingNull() {
        String key = "nullTest";
        Object defaultValue = "default";
        ValueModel adapter =
            new PreferencesAdapter(prefs, key, defaultValue);
        try {
            adapter.setValue(null);
            fail("Failed to reject writing null.");
        } catch (NullPointerException e) {
            // The expected behavior
        }
    }


    public void testRejectsWritingInconsistentType() {
        String key = "inconsistentTypesWriteTest";
        Object defaultValue = "default";
        PreferencesAdapter adapter =
            new PreferencesAdapter(prefs, key, defaultValue);
        try {
            adapter.setInt(3);
            fail("Failed to reject writing a value type inconsistent with the default value type.");
        } catch (ClassCastException e) {
            // The expected behavior
        }
        try {
            adapter.setValue(new Integer(3));
            fail("Failed to reject writing a value type inconsistent with the default value type.");
        } catch (ClassCastException e) {
            // The expected behavior
        }
    }

    public void testRejectsReadingInconsistentType() {
        String key = "inconsistentTypesReadTest";
        Object defaultValue = "default";
        PreferencesAdapter adapter =
            new PreferencesAdapter(prefs, key, defaultValue);
        try {
            adapter.getInt();
            fail("Failed to reject writing a value type inconsistent with the default value type.");
        } catch (ClassCastException e) {
            // The expected behavior
        }
    }


    // Test Implementations ***************************************************

    private void testReadWrittenValue(String key, Object defaultValue, Object newValue) {
        AbstractValueModel adapter =
            new PreferencesAdapter(prefs, key, defaultValue);
        adapter.setValue(newValue);
        assertEquals("Failed to return the previously set value.",
                newValue,
                adapter.getValue());
    }


    private void testReadDefaultValue(String key, Object defaultValue) {
        prefs.remove(key);
        AbstractValueModel adapter =
            new PreferencesAdapter(prefs, key, defaultValue);
        assertEquals("Failed to return the default value.",
                defaultValue,
                adapter.getValue());
    }


}
