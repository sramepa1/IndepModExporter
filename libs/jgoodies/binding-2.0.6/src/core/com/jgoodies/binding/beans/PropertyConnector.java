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

import com.jgoodies.binding.BindingUtils;
import com.jgoodies.binding.value.ValueModel;


/**
 * Keeps two Java Bean properties in synch. This connector supports bound and
 * unbound, read-only and read-write properties. Write-only properties are
 * not supported; connecting two read-only properties won't work;
 * connecting two unbound properties doesn't make sense.<p>
 *
 * If one of the bean properties fires a property change, this connector
 * will set the other to the same value. If a bean property is read-only,
 * the PropertyConnector will not listen to the other bean's property and so
 * won't update the read-only property. And if a bean does not provide support
 * for bound properties, it won't be observed.
 * The properties must be single value bean properties as described by the
 * <a href="http://java.sun.com/products/javabeans/docs/spec.html">Java
 * Bean Secification</a>.<p>
 *
 * <strong>Constraints:</strong> the properties must be type compatible,
 * i. e. values returned by one reader must be accepted by the other's writer,
 * and vice versa.<p>
 *
 * <strong>Examples:</strong><pre>
 * // Connects a ValueModel and a JFormattedTextField
 * JFormattedTextField textField = new JFormattedTextField();
 * textField.setEditable(editable);
 * PropertyConnector connector =
 *     new PropertyConnector(valueModel, "value", textField, "value");
 * connector.updateProperty2();
 *
 * // Connects the boolean property "selectable" with a component enablement
 * JComboBox comboBox = new JComboBox();
 * ...
 * new PropertyConnector(mainModel, "selectable", comboBox, "enabled");
 * </pre>
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.19 $
 *
 * @see     PropertyChangeEvent
 * @see     PropertyChangeListener
 * @see     PropertyDescriptor
 */
public final class PropertyConnector {

    /**
     * Holds the first bean that in turn holds the first property.
     *
     * @see #getBean1()
     */
    private final Object bean1;

    /**
     * Holds the second bean that in turn holds the second property.
     *
     * @see #getBean2()
     */
    private final Object bean2;

    /**
     * Holds the class used to lookup methods for bean1.
     * In a future version this may differ from bean1.getClass().
     */
    private final Class<?> bean1Class;

    /**
     * Holds the class used to lookup methods for bean2.
     * In a future version this may differ from bean2.getClass().
     */
    private final Class<?> bean2Class;

    /**
     * Holds the first property name.
     *
     * @see #getProperty1Name()
     */
    private final String  property1Name;

    /**
     * Holds the second property name.
     *
     * @see #getProperty2Name()
     */
    private final String  property2Name;

    /**
     * The <code>PropertyChangeListener</code> used to handle
     * changes in the first bean property.
     */
    private final PropertyChangeListener property1ChangeHandler;

    /**
     * The <code>PropertyChangeListener</code> used to handle
     * changes in the second bean property.
     */
    private final PropertyChangeListener property2ChangeHandler;

    /**
     * Describes the accessor for property1; basically a getter and setter.
     */
    private final PropertyDescriptor property1Descriptor;

    /**
     * Describes the accessor for property1; basically a getter and setter.
     */
    private final PropertyDescriptor property2Descriptor;


    // Instance creation ****************************************************

    /**
     * Constructs a PropertyConnector that synchronizes the two bound
     * bean properties as specified by the given pairs of bean and associated
     * property name.
     * If <code>Bean1#property1Name</code> changes it updates
     * <code>Bean2#property2Name</code> and vice versa.
     * If a bean does not provide support for bound properties,
     * changes will not be observed.
     * If a bean property is read-only, this connector will not listen to
     * the other bean's property and so won't update the read-only property.
     *
     * @param bean1             the bean that owns the first property
     * @param property1Name     the name of the first property
     * @param bean2             the bean that owns the second property
     * @param property2Name     the name of the second property
     * @throws NullPointerException
     *     if a bean or property name is {@code null}
     * @throws IllegalArgumentException if the beans are identical and
     *    the property name are equal, or if both properties are read-only
     */
    private PropertyConnector(Object bean1, String property1Name,
                             Object bean2, String property2Name) {
        if (bean1 == null)
            throw new NullPointerException("Bean1 must not be null.");
        if (bean2 == null)
            throw new NullPointerException("Bean2 must not be null.");
        if (property1Name == null)
            throw new NullPointerException("PropertyName1 must not be null.");
        if (property2Name == null)
            throw new NullPointerException("PropertyName2 must not be null.");
        if ((bean1 == bean2) && (property1Name.equals(property2Name)))
            throw new IllegalArgumentException(
                    "Cannot connect a bean property to itself on the same bean.");

        this.bean1 = bean1;
        this.bean2 = bean2;
        this.bean1Class = bean1.getClass();
        this.bean2Class = bean2.getClass();
        this.property1Name = property1Name;
        this.property2Name = property2Name;

        property1Descriptor = getPropertyDescriptor(bean1Class, property1Name);
        property2Descriptor = getPropertyDescriptor(bean2Class, property2Name);

        // Used to check if property2 shall be observed,
        // i.e. if a listener shall be registered with property2.
        boolean property1Writable = property1Descriptor.getWriteMethod() != null;
        boolean property1Readable = property1Descriptor.getReadMethod() != null;

        // Reject write-only property1
        if (property1Writable && !property1Readable) {
            throw new IllegalArgumentException("Property1 must be readable.");
        }

        // Used to check if property1 shall be observed,
        // i.e. if a listener shall be registered with property1.
        boolean property2Writable = property2Descriptor.getWriteMethod() != null;
        boolean property2Readable = property2Descriptor.getReadMethod() != null;

        // Reject write-only property2
        if (property2Writable && !property2Readable) {
            throw new IllegalArgumentException("Property2 must be readable.");
        }
        // Reject to connect to read-only properties
        if (!property1Writable && !property2Writable)
            throw new IllegalArgumentException(
                    "Cannot connect two read-only properties.");

        boolean property1Observable = BeanUtils.supportsBoundProperties(bean1Class);
        boolean property2Observable = BeanUtils.supportsBoundProperties(bean2Class);
        // We do not reject the case where two unobservable beans
        // are connected; this allows a hand-update using #updateProperty1
        // and #updateProperty2.

        // Observe property1 if and only if bean1 provides support for
        // bound bean properties, and if updates can be written to property2.
        if (property1Observable && property2Writable) {
            property1ChangeHandler = new PropertyChangeHandler(
                    bean1, property1Descriptor, bean2, property2Descriptor);
            addPropertyChangeHandler(bean1, bean1Class, property1ChangeHandler);
        } else {
            property1ChangeHandler = null;
        }

        // Observe property2 if and only if bean2 provides support for
        // bound bean properties, and if updates can be written to property1.
        if (property2Observable && property1Writable) {
            property2ChangeHandler = new PropertyChangeHandler(
                    bean2, property2Descriptor, bean1, property1Descriptor);
            addPropertyChangeHandler(bean2, bean2Class, property2ChangeHandler);
        } else {
            property2ChangeHandler = null;
        }
    }


    /**
     * Synchronizes the two bound bean properties as specified
     * by the given pairs of bean and associated property name.
     * If <code>Bean1#property1Name</code> changes it updates
     * <code>Bean2#property2Name</code> and vice versa.
     * If a bean does not provide support for bound properties,
     * changes will not be observed.
     * If a bean property is read-only, this connector won't listen to
     * the other bean's property and so won't update the read-only property.<p>
     *
     * Returns the PropertyConnector that is required if one or the other
     * property shall be updated.
     *
     * @param bean1             the bean that owns the first property
     * @param property1Name     the name of the first property
     * @param bean2             the bean that owns the second property
     * @param property2Name     the name of the second property
     * @return the PropertyConnector used to synchronize the properties,
     *     required if property1 or property2 shall be updated
     *
     * @throws NullPointerException
     *     if a bean or property name is {@code null}
     * @throws IllegalArgumentException if the beans are identical and
     *    the property name are equal
     */
    public static PropertyConnector connect(Object bean1, String property1Name,
                                            Object bean2, String property2Name) {
        return new PropertyConnector(bean1, property1Name,
                                     bean2, property2Name);
    }


    /**
     * Synchronizes the ValueModel with the specified bound bean property,
     * and updates the bean immediately.
     * If the ValueModel changes, it updates <code>Bean2#property2Name</code>
     * and vice versa. If the bean doesn't provide support for bound properties,
     * changes will not be observed.
     * If the bean property is read-only, this connector will not listen
     * to the ValueModel and so won't update the read-only property.
     *
     * @param valueModel        the ValueModel that provides a bound value
     * @param bean2             the bean that owns the second property
     * @param property2Name     the name of the second property
     * @throws NullPointerException
     *     if the ValueModel, bean or property name is {@code null}
     * @throws IllegalArgumentException if the bean is the ValueModel
     *    and the property name is <code>"value"</code>
     *
     * @since 2.0
     */
    public static void connectAndUpdate(
            ValueModel valueModel,
            Object bean2, String property2Name) {
        PropertyConnector connector = new PropertyConnector(
                valueModel, "value", bean2, property2Name);
        connector.updateProperty2();
    }


    // Property Accessors *****************************************************

    /**
     * Returns the Java Bean that holds the first property.
     *
     * @return the Bean that holds the first property
     */
    public Object getBean1() {
        return bean1;
    }

    /**
     * Returns the Java Bean that holds the first property.
     *
     * @return the Bean that holds the first property
     */
    public Object getBean2() {
        return bean2;
    }

    /**
     * Returns the name of the first Java Bean property.
     *
     * @return the name of the first property
     */
    public String getProperty1Name() {
        return property1Name;
    }

    /**
     * Returns the name of the second Java Bean property.
     *
     * @return the name of the second property
     */
    public String getProperty2Name() {
        return property2Name;
    }


    // Sychronization *********************************************************

    /**
     * Reads the value of the second bean property and sets it as new
     * value of the first bean property.
     *
     * @see #updateProperty2()
     */
    public void updateProperty1() {
        Object property2Value = BeanUtils.getValue(bean2, property2Descriptor);
        setValueSilently(bean2, property2Descriptor,
                         bean1, property1Descriptor, property2Value);
    }

    /**
     * Reads the value of the first bean property and sets it as new
     * value of the second bean property.
     *
     * @see #updateProperty1()
     */
    public void updateProperty2() {
        Object property1Value = BeanUtils.getValue(bean1, property1Descriptor);
        setValueSilently(bean1, property1Descriptor,
                         bean2, property2Descriptor, property1Value);
    }


    // Release ****************************************************************

    /**
     * Removes the PropertyChangeHandler from the observed bean,
     * if the bean is not null and if property changes are not observed.
     * This connector must not be used after calling <code>#release</code>.<p>
     *
     * To avoid memory leaks it is recommended to invoke this method,
     * if the connected beans live much longer than this connector.<p>
     *
     * As an alternative you may use event listener lists in the connected
     * beans that are implemented using <code>WeakReference</code>.
     *
     * @see java.lang.ref.WeakReference
     */
    public void release() {
        removePropertyChangeHandler(bean1, bean1Class, property1ChangeHandler);
        removePropertyChangeHandler(bean2, bean2Class, property2ChangeHandler);
    }


    /**
     * Used to add this class' PropertyChangeHandler to the given bean
     * if it is not {@code null}. First checks if the bean class
     * supports <em>bound properties</em>, i.e. it provides a pair of methods
     * to register multicast property change event listeners;
     * see section 7.4.1 of the Java Beans specification for details.
     *
     * @param bean      the bean to add a property change listener
     * @param listener  the property change listener to be added
     * @throws NullPointerException
     *     if the listener is {@code null}
     * @throws PropertyUnboundException
     *     if the bean does not support bound properties
     * @throws PropertyNotBindableException
     *     if the property change handler cannot be added successfully
     */
    private static void addPropertyChangeHandler(
            Object bean,
            Class<?> beanClass,
            PropertyChangeListener listener) {
        if (bean != null) {
            BeanUtils.addPropertyChangeListener(bean, beanClass, listener);
        }
    }


    /**
     * Used to remove this class' PropertyChangeHandler from the given bean
     * if it is not {@code null}.
     *
     * @param bean      the bean to remove the property change listener from
     * @param listener  the property change listener to be removed
     * @throws PropertyUnboundException
     *     if the bean does not support bound properties
     * @throws PropertyNotBindableException
     *     if the property change handler cannot be removed successfully
     */
    private static void removePropertyChangeHandler(
            Object bean,
            Class<?> beanClass,
            PropertyChangeListener listener) {
        if (bean != null) {
            BeanUtils.removePropertyChangeListener(bean, beanClass, listener);
        }
    }


    // Helper Methods to Get and Set a Property Value *************************

    private void setValueSilently(
            Object sourceBean,
            PropertyDescriptor sourcePropertyDescriptor,
            Object targetBean,
            PropertyDescriptor targetPropertyDescriptor,
            Object newValue) {
        Object targetValue = BeanUtils.getValue(targetBean, targetPropertyDescriptor);
        if (targetValue == newValue) {
            return;
        }
        if (property1ChangeHandler != null) {
            removePropertyChangeHandler(bean1, bean1Class, property1ChangeHandler);
        }
        if (property2ChangeHandler != null) {
            removePropertyChangeHandler(bean2, bean2Class, property2ChangeHandler);
        }
        try {
            // Set the new value in the target bean.
            BeanUtils.setValue(targetBean, targetPropertyDescriptor, newValue);
        } catch (PropertyVetoException e) {
            // Silently ignore this situation here, will be handled below.
        }
        // The target bean setter may have modified the new value.
        // Read the value set in the target bean.
        targetValue = BeanUtils.getValue(targetBean, targetPropertyDescriptor);
        // If the new value and the value read differ,
        // update the source bean's value.
        // This ignores that the source bean setter may modify the value again.
        // But we won't end in a loop.
        if (!BindingUtils.equals(targetValue, newValue)) {
            boolean sourcePropertyWritable = sourcePropertyDescriptor.getWriteMethod() != null;
            if (sourcePropertyWritable) {
                try {
                    BeanUtils.setValue(sourceBean, sourcePropertyDescriptor, targetValue);
                } catch (PropertyVetoException e) {
                    // Ignore. The value set is a modified variant
                    // of a value that had been accepted before.
                }
            }
        }
        if (property1ChangeHandler != null) {
            addPropertyChangeHandler(bean1, bean1Class, property1ChangeHandler);
        }
        if (property2ChangeHandler != null) {
            addPropertyChangeHandler(bean2, bean2Class, property2ChangeHandler);
        }
    }


    /**
     * Looks up, lazily initializes and returns a <code>PropertyDescriptor</code>
     * for the given Java Bean and property name.
     *
     * @param beanClass     the Java Bean class used to lookup the property from
     * @param propertyName  the name of the property
     * @return the descriptor for the given bean and property name
     * @throws PropertyNotFoundException   if the property could not be found
     */
    private static PropertyDescriptor getPropertyDescriptor(
        Class<?> beanClass,
        String propertyName) {
        try {
            return BeanUtils.getPropertyDescriptor(beanClass, propertyName);
        } catch (IntrospectionException e) {
            throw new PropertyNotFoundException(propertyName, beanClass, e);
        }
    }

    /**
     * Listens to changes of a bean property and updates the property.
     */
    private final class PropertyChangeHandler implements PropertyChangeListener {

        /**
         * Holds the bean that sends updates.
         */
        private final Object sourceBean;

        /**
         * Holds the property descriptor for the bean to read from.
         */
        private final PropertyDescriptor sourcePropertyDescriptor;

        /**
         * Holds the bean to update.
         */
        private final Object targetBean;

        /**
         * Holds the property descriptor for the bean to update.
         */
        private final PropertyDescriptor targetPropertyDescriptor;


        private PropertyChangeHandler(
                Object sourceBean,
                PropertyDescriptor sourcePropertyDescriptor,
                Object targetBean,
                PropertyDescriptor targetPropertyDescriptor) {
            this.sourceBean = sourceBean;
            this.sourcePropertyDescriptor = sourcePropertyDescriptor;
            this.targetBean = targetBean;
            this.targetPropertyDescriptor = targetPropertyDescriptor;
        }

        /**
         * A property in the observed bean has changed. First checks,
         * if this listener should handle the event, because the event's
         * property name is the one to be observed or the event indicates
         * that any property may have changed. In case the event provides
         * no new value, it is read from the source bean.
         *
         * @param evt   the property change event to be handled
         */
        public void propertyChange(PropertyChangeEvent evt) {
            String sourcePropertyName = sourcePropertyDescriptor.getName();
            String propertyName = evt.getPropertyName();
            if    ((propertyName == null)
                || (propertyName.equals(sourcePropertyName))) {
                Object newValue = evt.getNewValue();
                if ((newValue == null) || (propertyName == null)) {
                    newValue = BeanUtils.getValue(sourceBean, sourcePropertyDescriptor);
                }
                setValueSilently(sourceBean, sourcePropertyDescriptor,
                                 targetBean, targetPropertyDescriptor,
                                 newValue);
            }
        }

    }

}
