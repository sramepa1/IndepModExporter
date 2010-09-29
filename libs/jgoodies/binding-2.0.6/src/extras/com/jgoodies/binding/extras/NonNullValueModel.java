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

package com.jgoodies.binding.extras;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.jgoodies.binding.value.AbstractValueModel;
import com.jgoodies.binding.value.ValueModel;



/**
 * A {@link ValueModel} implementation that avoids {@code null} values.
 * It wraps another ValueModel and returns a (non-null) default value
 * if the wrapped ValueModel returns null.<p>
 *
 * Note that value change events fired by this model may use null
 * as old and/or new value. This is because PropertyChangeEvents use null
 * to indicate that the old and/or new value is not provided by the event.<p>
 *
 * <strong>Note:</strong> This class is not yet part of the binary Binding
 * library; it comes with the Binding distributions as an extra.
 * <strong>The API is work in progress and may change without notice;
 * this class may even be completely removed from future distributions.</strong>
 * If you want to use this class, you may consider copying it into
 * your code base.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.8 $
 *
 * @since 1.1
 */
public final class NonNullValueModel extends AbstractValueModel {

    /**
     * Holds the wrapped subject ValueModel.
     */
    private final ValueModel subject;

    /**
     * The value returned by this model whenever the
     * underlying (wrapped) ValueModel returns {@code null}.
     */
    private final Object defaultValue;


    // Instance Creation ******************************************************

    /**
     * Constructs an NonNullValueModel for the given ValueModel.
     *
     * @param subject   the underlying (or wrapped) ValueModel
     * @param defaultValue   the value used whenever the wrapped model
     *     returns {@code null}
     *
     * @throws NullPointerException if the subject or defaultValue is {@code null}
     */
    public NonNullValueModel(ValueModel subject, Object defaultValue) {
        this.subject = subject;
        this.defaultValue = defaultValue;
        if (defaultValue == null)
            throw new NullPointerException("The default value must not be null.");

        subject.addValueChangeListener(new SubjectValueChangeHandler());
    }


    // ValueModel Implementation **********************************************

    /**
     * Returns this model's current subject value.
     *
     * @return this model's current subject value.
     */
    public Object getValue() {
        Object subjectValue = subject.getValue();
        return subjectValue != null
            ? subjectValue
            : defaultValue;
    }


    /**
     * Sets the given value to the wrapped ValueModel.
     * The value set can be {@code null}.
     *
     * @param newValue   the value to set
     */
    public void setValue(Object newValue) {
        subject.setValue(newValue);
    }


    // Event Handling *********************************************************

    /**
     * Forwards value changes in the subject to listeners of this model.
     * We don't use the default value if the old and/or new value is null.
     * This is because null has a different meaning in PropertyChangeEvents:
     * it indicates that the value is not available.
     */
    private final class SubjectValueChangeHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            fireValueChange(evt.getOldValue(), evt.getNewValue(), true);
        }
    }

}
