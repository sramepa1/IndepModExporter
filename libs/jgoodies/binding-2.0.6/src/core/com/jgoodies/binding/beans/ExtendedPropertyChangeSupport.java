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

package com.jgoodies.binding.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.beans.PropertyChangeSupport;

/**
 * Differs from its superclass {@link PropertyChangeSupport} in that it can
 * check for changed values using {@code #equals} or {@code ==}.
 * Useful if you want to ensure that a {@code PropertyChangeEvent} is fired
 * if the old and new value are not the same but if they are equal.<p>
 *
 * The <a href="http://java.sun.com/products/javabeans/docs/spec.html">Java
 * Bean Specification</a> <em>recommends</em> to not throw a
 * PropertyChangeEvent if the old and new value of a bound
 * Bean property are equal (see chapter 7.4.4). This can reduce the number
 * of events fired and helps avoid loops. Nevertheless a bound property
 * <em>may</em> fire an event if the old and new value are equal.<p>
 *
 * An example for a condition where the identity check {@code ==}
 * is required and the {@code #equals} test fails is class
 * {@link com.jgoodies.binding.list.SelectionInList}. If the contained
 * {@code ListModel} changes its value, an internal listener is removed
 * from the old value and added to the new value. The listener must be
 * moved from the old instance to the new instance even if these are equal.
 * The <code>PropertyChangeSupport</code> doesn't fire a property change event
 * if such a {@code ListModel} is implemented as a {@link java.util.List}.
 * This is because instances of {@code List} are equal if and only if
 * all list members are equal and if they are in the same sequence.<p>
 *
 * This class provides two means to fire an event if the old and new
 * value are equal but not the same. First, you enable the identity check
 * in constructor {@link #ExtendedPropertyChangeSupport(Object, boolean)}.
 * By default all calls to <code>#firePropertyChange</code> will then
 * check the identity, not the equality. Second, you can invoke
 * {@link #firePropertyChange(PropertyChangeEvent, boolean)} or
 * {@link #firePropertyChange(String, Object, Object, boolean)} and
 * enable or disable the identity check for this call only.<p>
 *
 * TODO: (Issue #5) Use WeakReferences to refer to registered listeners.<p>
 *
 * TODO: (Issue #6) Add an optional check for valid property name
 * when adding a listener for a specific property.<p>
 *
 * TODO: (Issue #7) Add an optional strict check for existing
 * property names when firing a named property change.<p>
 *
 * TODO: (Issue #11) Consider adding an option that saves update notifications
 * if 'checkIdentity' is true but the value types can be compared
 * safely via #equals, for example Strings, Booleans and Numbers.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.13 $
 *
 * @see PropertyChangeSupport
 * @see PropertyChangeEvent
 * @see PropertyChangeListener
 * @see Object#equals(Object)
 * @see java.util.List#equals(Object)
 */
public final class ExtendedPropertyChangeSupport extends PropertyChangeSupport {

    /**
     * The object to be provided as the "source" for any generated events.
     * @serial
     */
    private final Object source;

    /**
     * The default setting for the identity check.
     * Can be overridden by the #firePropertyChange methods
     * that accept a {@code checkIdentity} parameter.
     */
    private final boolean checkIdentityDefault;


//    /**
//     * Describes whether this change support ensures that registered
//     * PropertyChangeListeners are notified in the event dispatch thread (EDT).
//     */
//    private final boolean ensureNotificationInEDT;


    // Instance Creation ******************************************************

    /**
     * Constructs an ExtendedPropertyChangeSupport object.
     *
     * @param sourceBean  The bean to be given as the source for any events.
     */
    public ExtendedPropertyChangeSupport(Object sourceBean) {
        this(sourceBean, false);
    }


    /**
     * Constructs an ExtendedPropertyChangeSupport object
     * with the specified default test method for differences between
     * the old and new property values.
     *
     * @param sourceBean  The object provided as the source for any generated events.
     * @param checkIdentityDefault true enables the identity check by default
     */
    public ExtendedPropertyChangeSupport(
        Object sourceBean,
        boolean checkIdentityDefault) {
        super(sourceBean);
        this.source = sourceBean;
        this.checkIdentityDefault = checkIdentityDefault;
    }


//    /**
//     * Constructs an ExtendedPropertyChangeSupport object
//     * with the specified default test method for differences between
//     * the old and new property values.
//     *
//     * @param sourceBean  The object provided as the source for any generated events.
//     * @param checkIdentityDefault true enables the identity check by default
//     * @param ensureNotificationInEDT   if true, this class ensures that
//     *     listeners are notified in the event dispatch thread (EDT)
//     */
//    public ExtendedPropertyChangeSupport(
//        Object sourceBean,
//        boolean checkIdentityDefault,
//        boolean ensureNotificationInEDT) {
//        super(sourceBean);
//        this.source = sourceBean;
//        this.checkIdentityDefault = checkIdentityDefault;
//        this.ensureNotificationInEDT = ensureNotificationInEDT;
//    }


    // Firing Events **********************************************************

    /**
     * Fires the specified PropertyChangeEvent to any registered listeners.
     * Uses the default test ({@code #equals} vs. {@code ==})
     * to determine whether the event's old and new values are different.
     * No event is fired if old and new value are the same.
     *
     * @param evt  The PropertyChangeEvent object.
     *
     * @see PropertyChangeSupport#firePropertyChange(PropertyChangeEvent)
     */
    @Override
    public void firePropertyChange(PropertyChangeEvent evt) {
        firePropertyChange(evt, checkIdentityDefault);
    }


    /**
     * Reports a bound property update to any registered listeners.
     * Uses the default test ({@code #equals} vs. {@code ==})
     * to determine whether the event's old and new values are different.
     * No event is fired if old and new value are the same.
     *
     * @param propertyName  The programmatic name of the property
     *      that was changed.
     * @param oldValue  The old value of the property.
     * @param newValue  The new value of the property.
     *
     * @see PropertyChangeSupport#firePropertyChange(String, Object, Object)
     */
    @Override
    public void firePropertyChange(
        String propertyName,
        Object oldValue,
        Object newValue) {
        firePropertyChange(propertyName, oldValue, newValue, checkIdentityDefault);
    }


    /**
     * Fires an existing PropertyChangeEvent to any registered listeners.
     * The boolean parameter specifies whether differences between the old
     * and new value are tested using {@code ==} or {@code #equals}.
     * No event is fired if old and new value are the same.
     *
     * @param evt  The PropertyChangeEvent object.
     * @param checkIdentity  true to check differences using {@code ==}
     *     false to use {@code #equals}.
     */
    public void firePropertyChange(
            final PropertyChangeEvent evt,
            final boolean checkIdentity) {
        Object oldValue = evt.getOldValue();
        Object newValue = evt.getNewValue();
        if (oldValue != null && oldValue == newValue) {
            return;
        }
//        if (!ensureNotificationInEDT || EventQueue.isDispatchThread()) {
        firePropertyChange0(evt, checkIdentity);
//            return;
//        }
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                ExtendedPropertyChangeSupport.this.firePropertyChange0(
//                        evt,
//                        checkIdentity);
//            }
//        });
    }


    /**
     * Reports a bound property update to any registered listeners.
     * No event is fired if the old and new value are the same.
     * If checkIdentity is {@code true} an event is fired in all
     * other cases. If this parameter is {@code false}, an event is fired
     * if old and new values are not equal.
     *
     * @param propertyName  The programmatic name of the property
     *      that was changed.
     * @param oldValue  The old value of the property.
     * @param newValue  The new value of the property.
     * @param checkIdentity  true to check differences using {@code ==}
     *     false to use {@code #equals}.
     */
    public void firePropertyChange(
        final String propertyName,
        final Object oldValue,
        final Object newValue,
        final boolean checkIdentity) {

        if (oldValue != null && oldValue == newValue) {
            return;
        }
//        if (!ensureNotificationInEDT || EventQueue.isDispatchThread()) {
        firePropertyChange0(propertyName, oldValue, newValue, checkIdentity);
//            return;
//        }
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                ExtendedPropertyChangeSupport.this.firePropertyChange0(
//                        propertyName,
//                        oldValue,
//                        newValue,
//                        checkIdentity);
//            }
//        });
    }


    // Helper Code ************************************************************

    private void firePropertyChange0(PropertyChangeEvent evt, boolean checkIdentity) {
        if (checkIdentity) {
            fireUnchecked(evt);
        } else {
            super.firePropertyChange(evt);
        }
    }


    private void firePropertyChange0(String propertyName, Object oldValue, Object newValue, boolean checkIdentity) {
        if (checkIdentity) {
            fireUnchecked(
                new PropertyChangeEvent(
                    source,
                    propertyName,
                    oldValue,
                    newValue));
        } else {
            super.firePropertyChange(propertyName, oldValue, newValue);
        }
    }


    /**
     * Fires a PropertyChangeEvent to all its listeners without checking via
     * equals method if the old value is equal to new value. The instance
     * equality check is done by the calling firePropertyChange method
     * (to avoid instance creation of the PropertyChangeEvent).<p>
     *
     * If some listeners have been added with a named property, then
     * {@code PropertyChangeSupport#getPropertyChangeListeners()} returns
     * an array with a mixture of PropertyChangeListeners
     * and {@code PropertyChangeListenerProxy}s. We notify all non-proxies
     * and those proxies that have a property name that is equals to the
     * event's property name.
     *
     * @param evt event to fire to the listeners
     *
     * @see PropertyChangeListenerProxy
     * @see PropertyChangeSupport#getPropertyChangeListeners()
     */
    private void fireUnchecked(PropertyChangeEvent evt) {
        PropertyChangeListener[] listeners;
        synchronized (this) {
            listeners = getPropertyChangeListeners();
        }
        String propertyName = evt.getPropertyName();
        for (PropertyChangeListener listener : listeners) {
            if (listener instanceof PropertyChangeListenerProxy) {
                PropertyChangeListenerProxy proxy =
                    (PropertyChangeListenerProxy) listener;
                if (proxy.getPropertyName().equals(propertyName)) {
                    proxy.propertyChange(evt);
                }
            } else {
                listener.propertyChange(evt);
            }
        }
    }

}
