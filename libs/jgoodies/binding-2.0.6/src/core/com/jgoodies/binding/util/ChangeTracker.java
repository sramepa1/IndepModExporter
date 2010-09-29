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

package com.jgoodies.binding.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.jgoodies.binding.beans.BeanUtils;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyNotBindableException;
import com.jgoodies.binding.value.ValueModel;


/**
 * Tracks changes in a set of bound bean properties. The tracker itself
 * provides a read-only bound bean property <em>changed</em> that indicates
 * whether one of the observed properties has changed. The changed state
 * can be reset to {@code false} using <code>#reset</code>.<p>
 *
 * The tracker can observe readable bound bean properties if and only if
 * the bean provides the optional support for listening on named properties
 * as described in section 7.4.5 of the
 * <a href="http://java.sun.com/products/javabeans/docs/spec.html">Java Bean
 * Specification</a>. The bean class must provide the following pair of methods:
 * <pre>
 * public void addPropertyChangeListener(String name, PropertyChangeListener l);
 * public void removePropertyChangeListener(String name, PropertyChangeListener l);
 * </pre><p>
 *
 * <strong>Example:</strong><pre>
 * ChangeTracker tracker = new ChangeTracker();
 * tracker.observe(address, "street");
 * tracker.observe(address, "city");
 * tracker.addPropertyChangeListener(new PropertyChangeListener() {
 *
 *     public void propertyChange(PropertyChangeEvent evt) {
 *         System.out.println("Change state: " + evt.getNewValue());
 *     }
 * });
 *
 * // Change the first ValueModel
 * System.out.println(tracker.isChanged()); // Prints "false"
 * address.setStreet("Belsenplatz");        // Prints "Change state: true"
 * System.out.println(tracker.isChanged()); // Prints "true"
 * tracker.reset();                         // Prints "Change state: false"
 * System.out.println(tracker.isChanged()); // Prints "false"
 * </pre><p>
 *
 * <strong>Note:</strong> The classes <code>BeanAdapter</code> and
 * <code>PresentationModel</code> already provide support for tracking changes.
 * Typical binding code can use these classes and there seems to be no need
 * to use the ChangeTracker.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.5 $
 *
 * @see ValueModel
 */
public final class ChangeTracker extends Model {

    /**
     * The name of the read-only bound bean property that
     * indicates whether one of the observed properties has changed.
     *
     * @see #isChanged()
     */
    public static final String PROPERTYNAME_CHANGED = "changed";

    /**
     * Listens to property changes and updates the <em>changed</em> property.
     */
    private final PropertyChangeListener updateHandler;

    /**
     * Indicates whether a registered model has changed.
     */
    private boolean changed = false;


    // Instance Creation *****************************************************

    /**
     * Constructs a change tracker with change state set to {@code false}.
     */
    public ChangeTracker() {
        updateHandler = new UpdateHandler();
    }


    // Accessing the Changed State ********************************************

    /**
     * Answers whether one of the registered ValueModels has changed
     * since this tracker has been reset last time.
     *
     * @return true if an observed property has changed since the last reset
     */
    public boolean isChanged() {
        return changed;
    }


    /**
     * Resets this tracker's changed state to {@code false}.
     */
    public void reset() {
        setChanged(false);
    }


    private void setChanged(boolean newValue) {
        boolean oldValue = isChanged();
        changed = newValue;
        firePropertyChange(PROPERTYNAME_CHANGED, oldValue, newValue);
    }


    // Registering *************************************************************

    /**
     * Observes the specified readable bound bean property in the given bean.
     *
     * @param bean           the bean to be observed
     * @param propertyName   the name of the readable bound bean property
     * @throws NullPointerException if the bean or propertyName is null
     * @throws PropertyNotBindableException if this tracker can't add
     *     the PropertyChangeListener from the bean
     *
     * @see #retractInterestFor(Object, String)
     */
    public void observe(Object bean, String propertyName) {
        if (bean == null)
            throw new NullPointerException("The bean must not be null.");
        if (propertyName == null)
            throw new NullPointerException("The property name must not be null.");

        BeanUtils.addPropertyChangeListener(bean, propertyName, updateHandler);
    }


    /**
     * Observes value changes in the given ValueModel.
     *
     * @param valueModel   the ValueModel to observe
     * @throws NullPointerException if the valueModel is null
     *
     * @see #retractInterestFor(ValueModel)
     */
    public void observe(ValueModel valueModel) {
        if (valueModel == null)
            throw new NullPointerException("The ValueModel must not be null.");
        valueModel.addValueChangeListener(updateHandler);
    }


    /**
     * Retracts interest for the specified readable bound bean property
     * in the given bean.
     *
     * @param bean           the bean to be observed
     * @param propertyName   the name of the readable bound bean property
     * @throws NullPointerException if the bean or propertyName is null
     * @throws PropertyNotBindableException if this tracker can't remove
     *     the PropertyChangeListener from the bean
     *
     * @see #observe(Object, String)
     */
    public void retractInterestFor(Object bean, String propertyName) {
        if (bean == null)
            throw new NullPointerException("The bean must not be null.");
        if (propertyName == null)
            throw new NullPointerException("The property name must not be null.");

        BeanUtils.removePropertyChangeListener(bean, propertyName, updateHandler);
    }


    /**
     * Retracts interest for value changes in the given ValueModel.
     *
     * @param valueModel   the ValueModel to observe
     * @throws NullPointerException if the valueModel is null
     *
     * @see #retractInterestFor(ValueModel)
     */
    public void retractInterestFor(ValueModel valueModel) {
        if (valueModel == null)
            throw new NullPointerException("The ValueModel must not be null.");
        valueModel.removeValueChangeListener(updateHandler);
    }


    // Private Helper Code ****************************************************

    /**
     * Listens to model changes and updates the changed state.
     */
    private final class UpdateHandler implements PropertyChangeListener {

        /**
         * A registered ValueModel has changed.
         * Updates the changed state. If the property that changed is
         * 'changed' we assume that this is another changed state and
         * forward only changes to true. For all other property names,
         * we just update our changed state to true.
         *
         * @param evt   the event that describes the property change
         */
        public void propertyChange(PropertyChangeEvent evt) {
            String propertyName = evt.getPropertyName();
            if (!PROPERTYNAME_CHANGED.equals(propertyName)
                || ((Boolean) evt.getNewValue()).booleanValue()) {
                setChanged(true);
            }
        }
    }

}
