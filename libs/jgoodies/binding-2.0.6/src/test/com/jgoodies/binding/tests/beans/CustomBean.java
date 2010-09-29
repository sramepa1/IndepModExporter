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
 * A Java Bean that provides a bunch of properties.
 * The property types are: Object, boolean and int;
 * the access types are: read-write, read-only and write-only.
 * In addition, there's a property that doesn't check for changes
 * - and may cause problems with event notification cycles.
 * This class is intended to be used by the Binding unit test suite.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.10 $
 */
public final class CustomBean extends Model {

    public Object   readWriteObjectProperty;
    public boolean readWriteBooleanProperty;
    public int      readWriteIntProperty;
    public Integer  readWriteIntegerProperty;

    public Object   readOnlyObjectProperty;
    public boolean readOnlyBooleanProperty;
    public int      readOnlyIntProperty;

    public Object   writeOnlyObjectProperty;
    public boolean writeOnlyBooleanProperty;
    public int      writeOnlyIntProperty;


    // Getters and Setters for the Read/Write Properties **********************

    public Object getReadWriteObjectProperty() {
        return readWriteObjectProperty;
    }

    public void setReadWriteObjectProperty(Object newValue) {
        Object oldValue = getReadWriteObjectProperty();
        readWriteObjectProperty = newValue;
        firePropertyChange("readWriteObjectProperty", oldValue, newValue);
    }


    public boolean isReadWriteBooleanProperty() {
        return readWriteBooleanProperty;
    }

    public void setReadWriteBooleanProperty(boolean newValue) {
        boolean oldValue = isReadWriteBooleanProperty();
        readWriteBooleanProperty = newValue;
        firePropertyChange("readWriteBooleanProperty", oldValue, newValue);
    }


    public int getReadWriteIntProperty() {
        return readWriteIntProperty;
    }

    public void setReadWriteIntProperty(int newValue) {
        int oldValue = getReadWriteIntProperty();
        readWriteIntProperty = newValue;
        firePropertyChange("readWriteIntProperty", oldValue, newValue);
    }


    public Integer getReadWriteIntegerProperty() {
        return readWriteIntegerProperty;
    }

    public void setReadWriteIntegerProperty(Integer newValue) {
        Integer oldValue = getReadWriteIntegerProperty();
        readWriteIntegerProperty = newValue;
        firePropertyChange("readWriteIntegerProperty", oldValue, newValue);
    }


    // Getters for the Read-Only Properties ***********************************

    public Object getReadOnlyObjectProperty() {
        return readOnlyObjectProperty;
    }

    public boolean isReadOnlyBooleanProperty() {
        return readOnlyBooleanProperty;
    }

    public int getReadOnlyIntProperty() {
        return readOnlyIntProperty;
    }


    // Setters for the Write-Only Properties **********************************

    public void setWriteOnlyObjectProperty(Object newValue) {
        writeOnlyObjectProperty = newValue;
        firePropertyChange("writeOnlyObjectProperty", null, newValue);
    }

    public void setWriteOnlyBooleanProperty(boolean newValue) {
        writeOnlyBooleanProperty = newValue;
        firePropertyChange("writeOnlyBooleanProperty", null, Boolean.valueOf(newValue));
    }

    public void setWriteOnlyIntProperty(int newValue) {
        writeOnlyIntProperty = newValue;
        firePropertyChange("writeOnlyIntProperty", null, new Integer(newValue));
    }


    // Getters and Setters that Throw Runtime Exceptions **********************

    /**
     * Throws a runtime exception.
     *
     * @return nothing - won't return
     */
    public Object getNoAccess() {
        return new Integer(3 / 0);
    }

    /**
     * Throws a runtime exception.
     *
     * @param object   an arbitrary object
     */
    public void setNoAccess(Object object) {
        if (3 / 0 < 4  || object == null) return;
    }


    // Multiple Updates *******************************************************

    public void setReadWriteObjectProperties(Object object, boolean b, int i) {
        readWriteObjectProperty  = object;
        readWriteBooleanProperty = b;
        readWriteIntProperty     = i;
        fireMultiplePropertiesChanged();
    }


}
