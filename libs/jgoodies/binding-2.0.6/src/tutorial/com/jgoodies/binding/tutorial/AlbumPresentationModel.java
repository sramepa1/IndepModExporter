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

package com.jgoodies.binding.tutorial;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.ValueHolder;

/**
 * In addition to its superclass this class provides a bound
 * read-only property <em>composerEnabled</em> that is true
 * if and only if the underlying Album is non-{@code null} and classical.
 * This property is intended to be used by a variety of presentation properties,
 * not just a component's <em>enabled</em> state. You can enable or disable
 * a composer presentation, or switch a composer field's <em>editable</em>
 * property, or to hide and show a larger composer editor section.<p>
 *
 * I've added the <em>composerEnabled</em> property primarily for learning
 * and demonstration purposes. And I wanted to closely follow the example
 * used in Martin Fowler's description of the
 * <a href="http://martinfowler.com/eaaDev/PresentationModel.html">Presentation
 * Model</a> pattern. This example shows how "raw" domain object properties
 * can be aggregated or filtered by the presentation model layer for use in
 * the presentation layer. In cases where there's a direct mapping between
 * a raw property and the aggregated property used in the UI, it may be easier
 * to observe the raw property. But if this mapping becomes more complex
 * an aggregated property is recommended.<p>
 *
 * Note that a UI that buffers the classical property cannot use
 * <em>composerEnabled</em> but requires a separate aggregated property,
 * for example <em>bufferedComposerEnabled</em>. To keep this code close
 * to Fowler's original, I've implemented this feature in a subclass, see
 * {@link com.jgoodies.binding.tutorial.manager.BufferedAlbumPresentationModel}.
 * The buffered composer enabled property is an example where the eager
 * property aggregation adds overhead and complexity, and it may turn out
 * that using the buffered classical property would be easier to understand.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.14 $
 *
 * @see com.jgoodies.binding.tutorial.manager.BufferedAlbumPresentationModel
 */
public class AlbumPresentationModel extends PresentationModel<Album> {

    /**
     * The name of the bound read-only property that indicates that
     * a presentation for the composer property should be enabled/disabled,
     * editable/not editable, or visible/hidden.
     *
     * @see #isComposerEnabled()
     */
    public static final String PROPERTYNAME_COMPOSER_ENABLED = "composerEnabled";


    // Instance Creation ******************************************************

    /**
     * Constructs an AlbumPresentationModel for the given Album.
     *
     * @param album   the initial Album to adapt and work with
     */
    public AlbumPresentationModel(Album album) {
        super(new ValueHolder(album, true));
        initEventHandling();
    }


    // Initialization *********************************************************

    /**
     * Registers a listener that is notified about changes in the
     * classical property of the current Album or a new Album.
     */
    private void initEventHandling() {
        getModel(Album.PROPERTY_CLASSICAL).addValueChangeListener(
                new ClassicalChangeHandler());
    }


    // Accessing Aggregated Properties ****************************************

    /**
     * Checks and answers if the underlying Album is non-{@code null}
     * and classical. This property is intended to be used with a variety
     * of presentation styles that indicate that a composer is absent
     * and cannot be edited. For example a presentations may choose
     * to switch the view's enablement or the editable state, or it
     * may hide or show a field or editor section.
     *
     * @return true if this model's current Album is non-{@code null}
     *     and classical.
     */
    public boolean isComposerEnabled() {
        return Boolean.TRUE.equals(getModel(Album.PROPERTY_CLASSICAL).getValue());
    }


    // Event Handling *********************************************************

    /**
     * Handles changes in the <em>classical</em> property.
     * Just notifies listeners about a change in the aggregated property
     * <em>composerEnabled</em>.
     */
    private final class ClassicalChangeHandler implements PropertyChangeListener {

        /**
         * The Album's classical property has changed. Notifies listeners
         * about a change in the "composerEnabled" property. Fires an event
         * that uses {@code null} as old value, and the current value
         * of this model's "composerEnabled" property as new value.
         *
         * @param evt    the event that describes the classical change
         */
        public void propertyChange(PropertyChangeEvent evt) {
            firePropertyChange(
                    PROPERTYNAME_COMPOSER_ENABLED,
                    null,
                    Boolean.valueOf(isComposerEnabled()));
        }
    }

}
