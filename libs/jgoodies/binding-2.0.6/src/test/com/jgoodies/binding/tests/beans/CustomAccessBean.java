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

package com.jgoodies.binding.tests.beans;

import com.jgoodies.binding.beans.Model;

/**
 * A Java Bean that provides a bunch of properties that
 * are accessed via custom accessors that do not follow the
 * Java Bean naming conventions.
 * This class is intended to be used by the Binding unit test suite.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.10 $
 */
public final class CustomAccessBean extends Model {

    public Object  readWriteObjectProperty;
    public boolean readWriteBooleanProperty;
    public int     readWriteIntProperty;

    public Object  readOnlyObjectProperty;
    public boolean readOnlyBooleanProperty;
    public int     readOnlyIntProperty;

    public Object  writeOnlyObjectProperty;
    public boolean writeOnlyBooleanProperty;
    public int     writeOnlyIntProperty;


    // Getters and Setters for the Read/Write Properties **********************

    public Object readRWObjectProperty() {
        return readWriteObjectProperty;
    }

    public void writeRWObjectProperty(Object newValue) {
        Object oldValue = readRWObjectProperty();
        readWriteObjectProperty = newValue;
        firePropertyChange("readWriteObjectProperty", oldValue, newValue);
    }


    public boolean readRWBooleanProperty() {
        return readWriteBooleanProperty;
    }

    public void writeRWBooleanProperty(boolean newValue) {
        boolean oldValue = readRWBooleanProperty();
        readWriteBooleanProperty = newValue;
        firePropertyChange("readWriteBooleanProperty", oldValue, newValue);
    }


    public int readRWIntProperty() {
        return readWriteIntProperty;
    }

    public void writeRWIntProperty(int newValue) {
        int oldValue = readRWIntProperty();
        readWriteIntProperty = newValue;
        firePropertyChange("readWriteIntProperty", oldValue, newValue);
    }


    public Object readROObjectProperty() {
        return readOnlyObjectProperty;
    }

    public boolean readROBooleanProperty() {
        return readOnlyBooleanProperty;
    }

    public int readROIntProperty() {
        return readOnlyIntProperty;
    }


    // Setters for the Write-Only Properties **********************************

    public void writeWOObjectProperty(Object newValue) {
        writeOnlyObjectProperty = newValue;
        firePropertyChange("writeOnlyObjectProperty", null, newValue);
    }

    public void writeWOBooleanProperty(boolean newValue) {
        writeOnlyBooleanProperty = newValue;
        firePropertyChange("writeOnlyBooleanProperty", null, Boolean.valueOf(newValue));
    }

    public void writeWOIntProperty(int newValue) {
        writeOnlyIntProperty = newValue;
        firePropertyChange("writeOnlyIntProperty", null, new Integer(newValue));
    }


    // Getters and Setters that Throw Runtime Exceptions **********************

    public void setReadWriteObjectProperties(Object object, boolean b, int i) {
        readWriteObjectProperty  = object;
        readWriteBooleanProperty = b;
        readWriteIntProperty     = i;
        fireMultiplePropertiesChanged();
    }


}
