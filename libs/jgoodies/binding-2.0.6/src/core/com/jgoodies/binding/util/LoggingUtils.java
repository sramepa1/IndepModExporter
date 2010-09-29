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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jgoodies.binding.beans.BeanUtils;

/**
 * Assists in logging changes in bound bean properties.
 *
 * @author Andrej Golovnin
 * @author Karsten Lentzsch
 * @version $Revision: 1.6 $
 *
 * @see Logger
 */
public final class LoggingUtils {

    private static final Logger LOGGER = Logger.getLogger(LoggingUtils.class.getName());

    private static Level defaultLevel = Level.FINE;


    private LoggingUtils() {
        // Override default constructor; prevents instantiation.
    }


    /**
     * Sets the default log level to be used when logging PropertyChangeEvents.
     * The initial default level is {@link Level#FINE}.
     *
     * @param level   the default level to be used if no custom level
     *     has been provided
     *
     * @throws NullPointerException   if the new defaultLevel is null
     */
    public static void setDefaultLevel(Level level) {
        if (level == null) {
            throw new NullPointerException("The log level must not be null.");
        }
        LoggingUtils.defaultLevel = level;
    }


    /**
     * Registers a PropertyChangeListener with the specified bean
     * that logs all PropertyChangeEvents fired by this bean
     * using the default Logger and default log level.
     *
     * @param bean   the bean to log PropertyChangeEvents from
     *
     * @throws NullPointerException if the bean is null
     */
    public static void logPropertyChanges(Object bean) {
        logPropertyChanges(bean, LOGGER);
    }


    /**
     * Registers a PropertyChangeListener with the specified bean, which logs
     * all PropertyChangeEvents fired by the given bean using the specified
     * Logger and the default log level.
     *
     * @param bean   the bean to log PropertyChangeEvents from
     * @param logger the Logger to be used to log PropertyChangeEvents
     *
     * @throws NullPointerException if the bean or logger is null
     */
    public static void logPropertyChanges(Object bean, Logger logger) {
       logPropertyChanges(bean, logger, defaultLevel);
    }


    /**
     * Registers a PropertyChangeListener with the specified bean, which logs
     * all PropertyChangeEvents fired by the given bean using the specified
     * Logger and log level.
     *
     * @param bean   the bean to log PropertyChangeEvents from
     * @param logger the Logger to be used to log PropertyChangeEvents
     * @param level  the log level
     *
     * @throws NullPointerException if the bean, logger, or level is null
     */
    public static void logPropertyChanges(Object bean, Logger logger, Level level) {
        BeanUtils.addPropertyChangeListener(bean, new LogHandler(logger, level));
    }


    /**
     * Registers a named PropertyChangeListener with the specified bean,
     * which logs all PropertyChangeEvents of the given property using
     * the default Logger and default log level.
     *
     * @param bean          the bean to log PropertyChangeEvents from
     * @param propertyName  the name of the property which PropertyChangeEvents
     *                      should be logged
     *
     * @throws NullPointerException if the bean or propertyName is null
     */
    public static void logPropertyChanges(Object bean, String propertyName) {
        logPropertyChanges(bean, propertyName, LOGGER);
    }


    /**
     * Registers a named PropertyChangeListener with the specified bean,
     * which logs all PropertyChangeEvents of the given property using
     * the specified Logger and the default log level.
     *
     * @param bean          the bean to log PropertyChangeEvents from
     * @param propertyName  the name of the property which PropertyChangeEvents
     *                      should be logged
     * @param logger        the Logger to be used to log PropertyChangeEvents
     *
     * @throws NullPointerException if the bean, propertyName, or logger is null
     */
    public static void logPropertyChanges(Object bean, String propertyName, Logger logger) {
        logPropertyChanges(bean, propertyName, logger, defaultLevel);
    }


    /**
     * Registers a named PropertyChangeListener with the specified bean,
     * which logs all PropertyChangeEvents of the given property, Logger,
     * and log level.
     *
     * @param bean          the bean to log PropertyChangeEvents from
     * @param propertyName  the name of the property which PropertyChangeEvents
     *                      should be logged
     * @param logger        the Logger to be used to log PropertyChangeEvents
     * @param level         the log level
     *
     * @throws NullPointerException if the bean, propertyName, logger,
     *     or level is null
     */
    public static void logPropertyChanges(Object bean, String propertyName,
        Logger logger, Level level) {
        BeanUtils.addPropertyChangeListener(
            bean,
            propertyName,
            new LogHandler(logger, level));
    }


    // Helper code ************************************************************

    /**
     * A listener which logs PropertyChangeEvents.
     */
    private static final class LogHandler implements PropertyChangeListener {

        /**
         * Logger to be used to log PropertyChangeEvents.
         */
        private final Logger logger;

        /**
         * The log level used for the logging.
         */
        private final Level level;


        /**
         * Creates and returns LoggingListener, which uses the given Logger
         * to log PropertyChangeEvents.
         *
         * @param logger the logger to be used to log PropertyChangeEvents
         * @param level   the level used for the logging
         */
        LogHandler(Logger logger, Level level) {
            if (logger == null) {
                throw new NullPointerException("The logger must not be null.");
            }
            if (level == null) {
                throw new NullPointerException("The level must not be null.");
            }
            this.logger = logger;
            this.level = level;
        }


        /**
         * Logs the given event.
         */
        public void propertyChange(PropertyChangeEvent e) {
            if (!logger.isLoggable(level))
                return;

            Object newValue = e.getNewValue();
            Object oldValue = e.getOldValue();
            StringBuilder builder = new StringBuilder(e.getSource().toString());
            builder.append(" [propertyName=");
            builder.append(e.getPropertyName());
            builder.append(", oldValue=");
            builder.append(oldValue);
            if (oldValue != null) {
                builder.append(", oldValueType=");
                builder.append(oldValue.getClass());
            }
            builder.append(", newValue=");
            builder.append(newValue);
            if (newValue != null) {
                builder.append(", newValueType=");
                builder.append(newValue.getClass());
            }
            logger.log(level, builder.toString());
        }

    }

}
