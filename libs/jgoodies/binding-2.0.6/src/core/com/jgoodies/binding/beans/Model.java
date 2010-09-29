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

import java.beans.*;
import java.io.Serializable;

import com.jgoodies.binding.BindingUtils;

/**
 * An abstract superclass that minimizes the effort required to provide
 * change support for bound and constrained Bean properties.
 * This class follows the conventions and recommendations as described
 * in the <a href="http://java.sun.com/products/javabeans/docs/spec.html"
 * >Java Bean Specification</a>.<p>
 *
 * Uses class {@link com.jgoodies.binding.beans.ExtendedPropertyChangeSupport},
 * to enable the <code>==</code> or <code>#equals</code> test when
 * changing values.<p>
 *
 * TODO: Consider adding a method <code>#fireChange</code> that invokes
 * <code>#firePropertyChange</code> if and only if
 * <code>new value != old value</code>. The background is, that
 * <code>#firePropertyChange</code> must fire an event
 * if <code>new value==null==old value</code>.<p>
 *
 * TODO: Consider renaming this class to AbstractBean. That better describes
 * what this class is about.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.17 $
 *
 * @see     Observable
 * @see     java.beans.PropertyChangeEvent
 * @see     PropertyChangeListener
 * @see     PropertyChangeSupport
 * @see     ExtendedPropertyChangeSupport
 * @see     VetoableChangeListener
 * @see     VetoableChangeSupport
 */
public abstract class Model implements Observable, Serializable {


    /**
     * If any <code>PropertyChangeListeners</code> have been registered,
     * the <code>changeSupport</code> field describes them.
     *
     * @see #addPropertyChangeListener(PropertyChangeListener)
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     * @see #removePropertyChangeListener(PropertyChangeListener)
     * @see #removePropertyChangeListener(String, PropertyChangeListener)
     * @see PropertyChangeSupport
     */
    private ExtendedPropertyChangeSupport changeSupport;


    /**
     * If any <code>VetoableChangeListeners</code> have been registered,
     * the <code>vetoSupport</code> field describes them.
     *
     * @see #addVetoableChangeListener(VetoableChangeListener)
     * @see #addVetoableChangeListener(String, VetoableChangeListener)
     * @see #removeVetoableChangeListener(VetoableChangeListener)
     * @see #removeVetoableChangeListener(String, VetoableChangeListener)
     * @see #fireVetoableChange(String, Object, Object)
     */
     private VetoableChangeSupport vetoSupport;


     // Managing Property Change Listeners **********************************

    /**
     * Adds a PropertyChangeListener to the listener list. The listener is
     * registered for all bound properties of this class.<p>
     *
     * If listener is {@code null}, no exception is thrown and no action is performed.
     *
     * @param listener      the PropertyChangeListener to be added
     *
     * @see #removePropertyChangeListener(PropertyChangeListener)
     * @see #removePropertyChangeListener(String, PropertyChangeListener)
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     * @see #getPropertyChangeListeners()
     */
    public final synchronized void addPropertyChangeListener(
                                            PropertyChangeListener listener) {
        if (listener == null) {
            return;
        }
        if (changeSupport == null) {
            changeSupport = new ExtendedPropertyChangeSupport(this);
        }
        changeSupport.addPropertyChangeListener(listener);
    }


    /**
     * Removes a PropertyChangeListener from the listener list. This method
     * should be used to remove PropertyChangeListeners that were registered
     * for all bound properties of this class.<p>
     *
     * If listener is {@code null}, no exception is thrown and no action is performed.
     *
     * @param listener      the PropertyChangeListener to be removed
     * @see #addPropertyChangeListener(PropertyChangeListener)
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     * @see #removePropertyChangeListener(String, PropertyChangeListener)
     * @see #getPropertyChangeListeners()
     */
    public final synchronized void removePropertyChangeListener(
                                        PropertyChangeListener listener) {
        if (listener == null || changeSupport == null) {
            return;
        }
        changeSupport.removePropertyChangeListener(listener);
    }


    /**
     * Adds a PropertyChangeListener to the listener list for a specific
     * property. The specified property may be user-defined.<p>
     *
     * Note that if this Model is inheriting a bound property, then no event
     * will be fired in response to a change in the inherited property.<p>
     *
     * If listener is {@code null}, no exception is thrown and no action is performed.
     *
     * @param propertyName      one of the property names listed above
     * @param listener          the PropertyChangeListener to be added
     *
     * @see #removePropertyChangeListener(String, PropertyChangeListener)
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     * @see #getPropertyChangeListeners(String)
     */
    public final synchronized void addPropertyChangeListener(
                                        String propertyName,
                                        PropertyChangeListener listener) {
        if (listener == null) {
            return;
        }
        if (changeSupport == null) {
            changeSupport = new ExtendedPropertyChangeSupport(this);
        }
        changeSupport.addPropertyChangeListener(propertyName, listener);
    }


    /**
     * Removes a PropertyChangeListener from the listener list for a specific
     * property. This method should be used to remove PropertyChangeListeners
     * that were registered for a specific bound property.<p>
     *
     * If listener is {@code null}, no exception is thrown and no action is performed.
     *
     * @param propertyName      a valid property name
     * @param listener          the PropertyChangeListener to be removed
     *
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     * @see #removePropertyChangeListener(PropertyChangeListener)
     * @see #getPropertyChangeListeners(String)
     */
    public final synchronized void removePropertyChangeListener(
                                        String propertyName,
                                        PropertyChangeListener listener) {
        if (listener == null || changeSupport == null) {
            return;
        }
        changeSupport.removePropertyChangeListener(propertyName, listener);
    }


    // Managing Vetoable Change Listeners ***********************************

    /**
     * Adds a VetoableChangeListener to the listener list. The listener is
     * registered for all bound properties of this class.<p>
     *
     * If listener is {@code null}, no exception is thrown and no action is
     * performed.
     *
     * @param listener      the VetoableChangeListener to be added
     *
     * @see #removeVetoableChangeListener(String, VetoableChangeListener)
     * @see #addVetoableChangeListener(String, VetoableChangeListener)
     * @see #getVetoableChangeListeners()
     */
    public final synchronized void addVetoableChangeListener(
                                            VetoableChangeListener listener) {
        if (listener == null) {
            return;
        }
        if (vetoSupport == null) {
            vetoSupport = new VetoableChangeSupport(this);
        }
        vetoSupport.addVetoableChangeListener(listener);
    }


    /**
     * Removes a VetoableChangeListener from the listener list. This method
     * should be used to remove VetoableChangeListeners that were registered
     * for all bound properties of this class.<p>
     *
     * If listener is {@code null}, no exception is thrown and no action is performed.
     *
     * @param listener      the VetoableChangeListener to be removed
     *
     * @see #addVetoableChangeListener(String, VetoableChangeListener)
     * @see #removeVetoableChangeListener(String, VetoableChangeListener)
     * @see #getVetoableChangeListeners()
     */
    public final synchronized void removeVetoableChangeListener(
                                        VetoableChangeListener listener) {
        if (listener == null || vetoSupport == null) {
            return;
        }
        vetoSupport.removeVetoableChangeListener(listener);
    }


    /**
     * Adds a VetoableChangeListener to the listener list for a specific
     * property. The specified property may be user-defined.<p>
     *
     * Note that if this Model is inheriting a bound property, then no event
     * will be fired in response to a change in the inherited property.<p>
     *
     * If listener is {@code null}, no exception is thrown and no action is performed.
     *
     * @param propertyName      one of the property names listed above
     * @param listener          the VetoableChangeListener to be added
     *
     * @see #removeVetoableChangeListener(String, VetoableChangeListener)
     * @see #addVetoableChangeListener(String, VetoableChangeListener)
     * @see #getVetoableChangeListeners(String)
     */
    public final synchronized void addVetoableChangeListener(
                                        String propertyName,
                                        VetoableChangeListener listener) {
        if (listener == null) {
            return;
        }
        if (vetoSupport == null) {
            vetoSupport = new VetoableChangeSupport(this);
        }
        vetoSupport.addVetoableChangeListener(propertyName, listener);
    }


    /**
     * Removes a VetoableChangeListener from the listener list for a specific
     * property. This method should be used to remove VetoableChangeListeners
     * that were registered for a specific bound property.<p>
     *
     * If listener is {@code null}, no exception is thrown and no action is performed.
     *
     * @param propertyName      a valid property name
     * @param listener          the VetoableChangeListener to be removed
     *
     * @see #addVetoableChangeListener(String, VetoableChangeListener)
     * @see #removeVetoableChangeListener(VetoableChangeListener)
     * @see #getVetoableChangeListeners(String)
     */
    public final synchronized void removeVetoableChangeListener(
                                        String propertyName,
                                        VetoableChangeListener listener) {
        if (listener == null || vetoSupport == null) {
            return;
        }
        vetoSupport.removeVetoableChangeListener(propertyName, listener);
    }


    // Requesting Listener Sets ***********************************************

    /**
     * Returns an array of all the property change listeners
     * registered on this component.
     *
     * @return all of this component's <code>PropertyChangeListener</code>s
     *         or an empty array if no property change
     *         listeners are currently registered
     *
     * @see #addPropertyChangeListener(PropertyChangeListener)
     * @see #removePropertyChangeListener(PropertyChangeListener)
     * @see #getPropertyChangeListeners(String)
     * @see PropertyChangeSupport#getPropertyChangeListeners()
     */
    public final synchronized PropertyChangeListener[] getPropertyChangeListeners() {
        if (changeSupport == null) {
            return new PropertyChangeListener[0];
        }
        return changeSupport.getPropertyChangeListeners();
    }


    /**
     * Returns an array of all the listeners which have been associated
     * with the named property.
     *
     * @param propertyName   the name of the property to lookup listeners
     * @return all of the <code>PropertyChangeListeners</code> associated with
     *         the named property or an empty array if no listeners have
     *         been added
     *
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     * @see #removePropertyChangeListener(String, PropertyChangeListener)
     * @see #getPropertyChangeListeners()
     */
    public final synchronized PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        if (changeSupport == null) {
            return new PropertyChangeListener[0];
        }
        return changeSupport.getPropertyChangeListeners(propertyName);
    }


    /**
     * Returns an array of all the property change listeners
     * registered on this component.
     *
     * @return all of this component's <code>VetoableChangeListener</code>s
     *         or an empty array if no property change
     *         listeners are currently registered
     *
     * @see #addVetoableChangeListener(VetoableChangeListener)
     * @see #removeVetoableChangeListener(VetoableChangeListener)
     * @see #getVetoableChangeListeners(String)
     * @see VetoableChangeSupport#getVetoableChangeListeners()
     */
    public final synchronized VetoableChangeListener[] getVetoableChangeListeners() {
        if (vetoSupport == null) {
            return new VetoableChangeListener[0];
        }
        return vetoSupport.getVetoableChangeListeners();
    }


    /**
     * Returns an array of all the listeners which have been associated
     * with the named property.
     *
     * @param propertyName   the name of the property to lookup listeners
     * @return all of the <code>VetoableChangeListeners</code> associated with
     *         the named property or an empty array if no listeners have
     *         been added
     *
     * @see #addVetoableChangeListener(String, VetoableChangeListener)
     * @see #removeVetoableChangeListener(String, VetoableChangeListener)
     * @see #getVetoableChangeListeners()
     */
    public final synchronized VetoableChangeListener[] getVetoableChangeListeners(String propertyName) {
        if (vetoSupport == null) {
            return new VetoableChangeListener[0];
        }
        return vetoSupport.getVetoableChangeListeners(propertyName);
    }


    // Firing Changes for Bound Properties **********************************

    /**
     * General support for reporting bound property changes. Sends the given
     * PropertyChangeEvent to any registered PropertyChangeListener.<p>
     *
     * Most bean setters will invoke the fireXXX methods that get
     * a property name and the old and new value. However some frameworks
     * and setters may prefer to use this general method.
     * Also, this method allows to fire IndexedPropertyChangeEvents
     * that have been introduced in Java 5.
     *
     * @param event   describes the property change
     *
     * @since 1.3
     */
    protected final void firePropertyChange(PropertyChangeEvent event) {
        PropertyChangeSupport aChangeSupport = this.changeSupport;
        if (aChangeSupport == null) {
            return;
        }
        aChangeSupport.firePropertyChange(event);
    }


    /**
     * Support for reporting bound property changes for Object properties.
     * This method can be called when a bound property has changed and it will
     * send the appropriate PropertyChangeEvent to any registered
     * PropertyChangeListeners.
     *
     * @param propertyName      the property whose value has changed
     * @param oldValue          the property's previous value
     * @param newValue          the property's new value
     */
    protected final void firePropertyChange(String propertyName,
                                        Object oldValue,
                                        Object newValue) {
        PropertyChangeSupport aChangeSupport = this.changeSupport;
        if (aChangeSupport == null) {
            return;
        }
        aChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }


    /**
     * Support for reporting bound property changes for Object properties.
     * This method can be called when a bound property has changed and it will
     * send the appropriate PropertyChangeEvent to any registered
     * PropertyChangeListeners.<p>
     *
     * The boolean parameter specifies whether differences between the old
     * and new value are tested using <code>==</code> or <code>#equals</code>.
     *
     * @param propertyName      the property whose value has changed
     * @param oldValue          the property's previous value
     * @param newValue          the property's new value
     * @param checkIdentity     true to check differences using <code>==</code>
     *     false to use <code>#equals</code>.
     */
    protected final void firePropertyChange(
        String propertyName,
        Object oldValue,
        Object newValue,
        boolean checkIdentity) {

        if (changeSupport == null) {
            return;
        }
        changeSupport.firePropertyChange(
            propertyName,
            oldValue,
            newValue,
            checkIdentity);
    }


    /**
     * Support for reporting bound property changes for boolean properties.
     * This method can be called when a bound property has changed and it will
     * send the appropriate PropertyChangeEvent to any registered
     * PropertyChangeListeners.
     *
     * @param propertyName      the property whose value has changed
     * @param oldValue          the property's previous value
     * @param newValue          the property's new value
     */
    protected final void firePropertyChange(String propertyName,
                                        boolean oldValue,
                                        boolean newValue) {
        PropertyChangeSupport aChangeSupport = this.changeSupport;
        if (aChangeSupport == null) {
            return;
        }
        aChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }


    /**
     * Support for reporting bound property changes for integer properties.
     * This method can be called when a bound property has changed and it will
     * send the appropriate PropertyChangeEvent to any registered
     * PropertyChangeListeners.
     *
     * @param propertyName      the property whose value has changed
     * @param oldValue          the property's previous value
     * @param newValue          the property's new value
     */
    protected final void firePropertyChange(String propertyName,
                                        double oldValue,
                                        double newValue) {
        firePropertyChange(propertyName, Double.valueOf(oldValue), Double.valueOf(newValue));
    }


    /**
     * Support for reporting bound property changes for integer properties.
     * This method can be called when a bound property has changed and it will
     * send the appropriate PropertyChangeEvent to any registered
     * PropertyChangeListeners.
     *
     * @param propertyName      the property whose value has changed
     * @param oldValue          the property's previous value
     * @param newValue          the property's new value
     */
    protected final void firePropertyChange(String propertyName,
                                        float oldValue,
                                        float newValue) {
        firePropertyChange(propertyName, Float.valueOf(oldValue), Float.valueOf(newValue));
    }


    /**
     * Support for reporting bound property changes for integer properties.
     * This method can be called when a bound property has changed and it will
     * send the appropriate PropertyChangeEvent to any registered
     * PropertyChangeListeners.
     *
     * @param propertyName      the property whose value has changed
     * @param oldValue          the property's previous value
     * @param newValue          the property's new value
     */
    protected final void firePropertyChange(String propertyName,
                                        int oldValue,
                                        int newValue) {
        PropertyChangeSupport aChangeSupport = this.changeSupport;
        if (aChangeSupport == null) {
            return;
        }
        aChangeSupport.firePropertyChange(propertyName,
                Integer.valueOf(oldValue), Integer.valueOf(newValue));
    }


    /**
     * Support for reporting bound property changes for integer properties.
     * This method can be called when a bound property has changed and it will
     * send the appropriate PropertyChangeEvent to any registered
     * PropertyChangeListeners.
     *
     * @param propertyName      the property whose value has changed
     * @param oldValue          the property's previous value
     * @param newValue          the property's new value
     */
    protected final void firePropertyChange(String propertyName,
                                        long oldValue,
                                        long newValue) {
        firePropertyChange(propertyName, Long.valueOf(oldValue), Long.valueOf(newValue));
    }

    /**
     * Indicates that an arbitrary set of bound properties have changed.
     * Sends a PropertyChangeEvent with property name, old and new value
     * set {@code null} to any registered PropertyChangeListeners.
     *
     * @see java.beans.PropertyChangeEvent
     *
     * @since 1.0.3
     */
    protected final void fireMultiplePropertiesChanged() {
        firePropertyChange(null, null, null);
    }


    // Firing Indexed Changes *************************************************

    /**
     * Report a bound indexed property update to any registered listeners.<p>
     *
     * No event is fired if old and new values are equal and non-null.
     *
     * @param propertyName The programmatic name of the property that
     *                     was changed.
     * @param index        index of the property element that was changed.
     * @param oldValue     The old value of the property.
     * @param newValue     The new value of the property.
     *
     * @since 2.0
     */
    protected final void fireIndexedPropertyChange(String propertyName, int index,
                      Object oldValue, Object newValue) {
        PropertyChangeSupport aChangeSupport = this.changeSupport;
        if (aChangeSupport == null) {
            return;
        }
        aChangeSupport.fireIndexedPropertyChange(propertyName, index,
                oldValue, newValue);
    }


    /**
     * Report an <code>int</code> bound indexed property update
     * to any registered listeners.<p>
     *
     * No event is fired if old and new values are equal and non-null.<p>
     *
     * This is merely a convenience wrapper around the more general
     * fireIndexedPropertyChange method which takes Object values.
     *
     * @param propertyName The programmatic name of the property that
     *                     was changed.
     * @param index        index of the property element that was changed.
     * @param oldValue     The old value of the property.
     * @param newValue     The new value of the property.
     *
     * @since 2.0
     */
    protected final void fireIndexedPropertyChange(String propertyName, int index,
                      int oldValue, int newValue) {
        if (oldValue == newValue) {
            return;
        }
        fireIndexedPropertyChange(propertyName, index,
                      Integer.valueOf(oldValue),
                      Integer.valueOf(newValue));
    }


    /**
     * Report a <code>boolean</code> bound indexed property update
     * to any registered listeners.<p>
     *
     * No event is fired if old and new values are equal and non-null.<p>
     *
     * This is merely a convenience wrapper around the more general
     * fireIndexedPropertyChange method which takes Object values.
     *
     * @param propertyName The programmatic name of the property that
     *                     was changed.
     * @param index        index of the property element that was changed.
     * @param oldValue     The old value of the property.
     * @param newValue     The new value of the property.
     *
     * @since 2.0
     */
    protected final void fireIndexedPropertyChange(String propertyName, int index,
                      boolean oldValue, boolean newValue) {
        if (oldValue == newValue) {
            return;
        }
        fireIndexedPropertyChange(propertyName, index,
                Boolean.valueOf(oldValue), Boolean.valueOf(newValue));
    }


    // Firing Changes for Constrained Properties ****************************

    /**
     * General support for reporting constrained property changes. Sends the
     * given PropertyChangeEvent to any registered PropertyChangeListener.<p>
     *
     * Most bean setters will invoke the fireXXX methods that get
     * a property name and the old and new value. However some frameworks
     * and setters may prefer to use this general method.
     * Also, this method allows to fire IndexedPropertyChangeEvents
     * that have been introduced in Java 5.
     *
     * @param event   describes the property change
     * @throws PropertyVetoException  if a constrained property change is rejected
     *
     * @since 1.3
     */
    protected final void fireVetoableChange(PropertyChangeEvent event)
        throws PropertyVetoException {
        VetoableChangeSupport aVetoSupport = this.vetoSupport;
        if (aVetoSupport == null) {
            return;
        }
        aVetoSupport.fireVetoableChange(event);
    }


    /**
     * Support for reporting changes for constrained Object properties. This
     * method can be called before a constrained property will be changed and
     * it will send the appropriate PropertyChangeEvent to any registered
     * VetoableChangeListeners.
     *
     * @param propertyName      the property whose value has changed
     * @param oldValue          the property's previous value
     * @param newValue          the property's new value
     * @throws PropertyVetoException  if a constrained property change is rejected
     */
    protected final void fireVetoableChange(String propertyName,
                                        Object oldValue,
                                        Object newValue)
                                        throws PropertyVetoException {
        VetoableChangeSupport aVetoSupport = this.vetoSupport;
        if (aVetoSupport == null) {
            return;
        }
        aVetoSupport.fireVetoableChange(propertyName, oldValue, newValue);
    }


    /**
     * Support for reporting changes for constrained boolean properties. This
     * method can be called before a constrained property will be changed and
     * it will send the appropriate PropertyChangeEvent to any registered
     * VetoableChangeListeners.
     *
     * @param propertyName      the property whose value has changed
     * @param oldValue          the property's previous value
     * @param newValue          the property's new value
     * @throws PropertyVetoException  if a constrained property change is rejected
     */
    protected final void fireVetoableChange(String propertyName,
                                        boolean oldValue,
                                        boolean newValue)
                                            throws PropertyVetoException {
        VetoableChangeSupport aVetoSupport = this.vetoSupport;
        if (aVetoSupport == null) {
            return;
        }
        aVetoSupport.fireVetoableChange(propertyName, oldValue, newValue);
    }


    /**
     * Support for reporting changes for constrained integer properties. This
     * method can be called before a constrained property will be changed and
     * it will send the appropriate PropertyChangeEvent to any registered
     * VetoableChangeListeners.
     *
     * @param propertyName      the property whose value has changed
     * @param oldValue          the property's previous value
     * @param newValue          the property's new value
     * @throws PropertyVetoException  if a constrained property change is rejected
     */
    protected final void fireVetoableChange(String propertyName,
                                        double oldValue,
                                        double newValue)
                                            throws PropertyVetoException {
        fireVetoableChange(propertyName, Double.valueOf(oldValue), Double.valueOf(newValue));
    }


    /**
     * Support for reporting changes for constrained integer properties. This
     * method can be called before a constrained property will be changed and
     * it will send the appropriate PropertyChangeEvent to any registered
     * VetoableChangeListeners.
     *
     * @param propertyName      the property whose value has changed
     * @param oldValue          the property's previous value
     * @param newValue          the property's new value
     * @throws PropertyVetoException  if a constrained property change is rejected
     */
    protected final void fireVetoableChange(String propertyName,
                                        int oldValue,
                                        int newValue)
                                            throws PropertyVetoException {
        VetoableChangeSupport aVetoSupport = this.vetoSupport;
        if (aVetoSupport == null) {
            return;
        }
        aVetoSupport.fireVetoableChange(propertyName,
                Integer.valueOf(oldValue), Integer.valueOf(newValue));
    }


    /**
     * Support for reporting changes for constrained integer properties. This
     * method can be called before a constrained property will be changed and
     * it will send the appropriate PropertyChangeEvent to any registered
     * VetoableChangeListeners.
     *
     * @param propertyName      the property whose value has changed
     * @param oldValue          the property's previous value
     * @param newValue          the property's new value
     * @throws PropertyVetoException  if a constrained property change is rejected
     */
    protected final void fireVetoableChange(String propertyName,
                                        float oldValue,
                                        float newValue)
                                            throws PropertyVetoException {
        fireVetoableChange(propertyName, Float.valueOf(oldValue), Float.valueOf(newValue));
    }


    /**
     * Support for reporting changes for constrained integer properties. This
     * method can be called before a constrained property will be changed and
     * it will send the appropriate PropertyChangeEvent to any registered
     * VetoableChangeListeners.
     *
     * @param propertyName      the property whose value has changed
     * @param oldValue          the property's previous value
     * @param newValue          the property's new value
     * @throws PropertyVetoException  if a constrained property change is rejected
     */
    protected final void fireVetoableChange(String propertyName,
                                        long oldValue,
                                        long newValue)
                                            throws PropertyVetoException {
        fireVetoableChange(propertyName, Long.valueOf(oldValue), Long.valueOf(newValue));
    }


    // Convenience Methods **************************************************

    /**
     * Checks and answers if the two objects are both {@code null} or equal.
     *
     * @param o1        the first object to compare
     * @param o2        the second object to compare
     * @return boolean  true if and only if both objects are {@code null}
     *    or equal
     */
    protected final boolean equals(Object o1, Object o2) {
        return BindingUtils.equals(o1, o2);
    }

}
