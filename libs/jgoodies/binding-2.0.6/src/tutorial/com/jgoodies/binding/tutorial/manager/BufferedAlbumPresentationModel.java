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

package com.jgoodies.binding.tutorial.manager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.jgoodies.binding.tutorial.Album;
import com.jgoodies.binding.tutorial.AlbumPresentationModel;

/**
 * In addition to its superclass AlbumPresentationModel this class provides
 * a bound read-only property and presentation logic for views that present
 * buffered Album contents.<p>
 *
 * The <em>bufferedComposerEnabled</em> property is true if and only if
 * the underlying Album is non-{@code null} and the buffered classical
 * property is true. This aggregated property is intended to be used by views
 * that present buffered albums and want to indicate the composer enablement.<p>
 *
 * Also, this buffered presentation model adds presentation logic that
 * is copied from the domain: if the buffered classical property is deselected,
 * the buffered composer content is set to {@code null}.<p>
 *
 * This code could be moved to the AlbumPresentationModel. But I wanted to
 * keep the AlbumPresentationModel close to the example that Martin Fowler
 * uses to describe the
 * <a href="http://martinfowler.com/eaaDev/PresentationModel.html">Presentation
 * Model</a> pattern.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.9 $
 *
 * @see AlbumPresentationModel
 */
public final class BufferedAlbumPresentationModel extends AlbumPresentationModel {

    /**
     * The name of the bound read-only property that indicates that a buffered
     * presentation for the composer property should be enabled/disabled,
     * editable/not editable, or visible/hidden.
     *
     * @see #isBufferedComposerEnabled()
     */
    public static final String PROPERTYNAME_BUFFERED_COMPOSER_ENABLED =
        "bufferedComposerEnabled";


    // Instance Creation ******************************************************

    /**
     * Constructs a BufferedAlbumPresentationModel for the given Album.
     *
     * @param album   the initial Album to adapt and work with
     */
    public BufferedAlbumPresentationModel(Album album) {
        super(album);
        initEventHandling();
    }


    // Initialization *********************************************************

    /**
     * Registers a listener that is notified about changes in the
     * buffered classical property of the current Album or a new Album.
     * That in turn fire a change of the buffered composer enabled property
     * and resets the buffered composer content, if the buffered classical
     * property is false.
     */
    private void initEventHandling() {
        getBufferedModel(Album.PROPERTY_CLASSICAL).addValueChangeListener(
                new BufferedClassicalChangeHandler());
    }


    // Accessing Aggregated Properties ****************************************

    /**
     * Checks and answers if the underlying Album is non-{@code null}
     * and the buffered classical property is true. This property is intended
     * to be used with a variety of presentation styles that indicate that
     * a composer is absent and cannot be edited. For example a presentations
     * may choose to switch the view's enablement or the editable state,
     * or it may hide or show a field or editor section.
     *
     * @return true if this model's current Album is non-{@code null}
     *     and the buffered classical property is true.
     */
    public boolean isBufferedComposerEnabled() {
        return isBufferedClassical();
    }

    /**
     * Checks and answers if the underlying Album is non-{@code null}
     * and the buffered classical property is true.
     *
     * @return true if this model's current Album is non-{@code null}
     *     and the buffered classical property is true.
     */
    private boolean isBufferedClassical() {
        return Boolean.TRUE.equals(getBufferedModel(Album.PROPERTY_CLASSICAL).getValue());
    }


    // Event Handling *********************************************************

    /**
     * Copies domain logic for the buffered album. If the buffered
     * classical property has changed, the buffered composer enabled
     * property is updated and the buffered composer name is reset.
     */
    private final class BufferedClassicalChangeHandler implements PropertyChangeListener {

        /**
         * The Album's buffered classical property has changed. Notifies
         * listeners about a change in the "bufferedComposerEnabled" property.
         * Fires an event that uses {@code null} as old value, and the
         * current value of this model's "bufferedComposerEnabled" property
         * as new value.<p>
         *
         * Also sets the buffered composer content to {@code null},
         * if the buffered classical property is false.
         *
         * @param evt    the event that describes the classical change
         */
        public void propertyChange(PropertyChangeEvent evt) {
            firePropertyChange(
                    PROPERTYNAME_BUFFERED_COMPOSER_ENABLED,
                    null,
                    Boolean.valueOf(isBufferedComposerEnabled()));
            if (!isBufferedClassical()) {
                getBufferedModel(Album.PROPERTY_COMPOSER).setValue(null);
            }
        }
    }


}
