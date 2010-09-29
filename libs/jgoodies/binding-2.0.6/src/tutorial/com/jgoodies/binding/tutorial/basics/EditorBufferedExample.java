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

package com.jgoodies.binding.tutorial.basics;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.tutorial.Album;
import com.jgoodies.binding.tutorial.TutorialApplication;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Builds an editor with components bound to the domain object properties
 * using buffered adapting ValueModels created by a PresentationModel.
 * These buffers can be committed on 'Apply' and flushed on 'Reset'.
 * A second set of components displays the domain object properties.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.19 $
 *
 * @see com.jgoodies.binding.PresentationModel
 * @see com.jgoodies.binding.value.BufferedValueModel
 * @see com.jgoodies.binding.value.Trigger
 */
public final class EditorBufferedExample extends TutorialApplication {

    /**
     * Holds the edited Album and vends ValueModels that adapt Album properties.
     * In this example we will request BufferedValueModels for the editor.
     */
    private final PresentationModel<Album> presentationModel;

    private JTextComponent titleField;
    private JTextComponent artistField;
    private JCheckBox      classicalBox;
    private JTextComponent composerField;

    private JTextComponent unbufferedTitleField;
    private JTextComponent unbufferedArtistField;
    private JCheckBox      unbufferedClassicalBox;
    private JTextComponent unbufferedComposerField;

    private JButton applyButton;
    private JButton resetButton;


    // Launching **************************************************************

    public static void main(String[] args) {
        TutorialApplication.launch(EditorBufferedExample.class, args);
    }


    @Override
    protected void startup(String[] args) {
        JFrame frame = createFrame("Binding Tutorial :: Editor (Bound, Buffered)");
        frame.add(buildPanel());
        packAndShowOnScreenCenter(frame);
    }


    // Instance Creation ******************************************************

    /**
     * Constructs a buffered editor on an example Album.
     */
    public EditorBufferedExample() {
        this(Album.ALBUM1);
    }


    /**
     * Constructs a buffered editor for an Album to be edited.
     *
     * @param album    the Album to be edited
     */
    public EditorBufferedExample(Album album) {
        this.presentationModel = new PresentationModel<Album>(album);
    }


    // Initialization *********************************************************

    /**
     * Creates, binds and configures the UI components.
     * Changes are committed to the value models on apply and are
     * flushed on reset.
     */
    private void initComponents() {
        titleField = BasicComponentFactory.createTextField(
                presentationModel.getBufferedModel(Album.PROPERTY_TITLE));
        artistField = BasicComponentFactory.createTextField(
                presentationModel.getBufferedModel(Album.PROPERTY_ARTIST));
        classicalBox = BasicComponentFactory.createCheckBox (
                presentationModel.getBufferedModel(Album.PROPERTY_CLASSICAL),
                "Classical");
        classicalBox.setMnemonic('C');
        composerField = BasicComponentFactory.createTextField(
                presentationModel.getBufferedModel(Album.PROPERTY_COMPOSER));

        unbufferedTitleField = BasicComponentFactory.createTextField(
                presentationModel.getModel(Album.PROPERTY_TITLE));
        unbufferedTitleField.setEditable(false);
        unbufferedArtistField = BasicComponentFactory.createTextField(
                presentationModel.getModel(Album.PROPERTY_ARTIST));
        unbufferedArtistField.setEditable(false);
        unbufferedClassicalBox = BasicComponentFactory.createCheckBox (
                presentationModel.getModel(Album.PROPERTY_CLASSICAL),
                "Classical");
        unbufferedClassicalBox.setEnabled(false);
        unbufferedComposerField = BasicComponentFactory.createTextField(
                presentationModel.getModel(Album.PROPERTY_COMPOSER));
        unbufferedComposerField.setEditable(false);

        applyButton = new JButton(new ApplyAction());
        resetButton = new JButton(new ResetAction());

        updateComposerField();
    }


    /**
     * Observes the buffered <em>classical</em> property
     * to update the composer field's enablement and contents.<p>
     *
     * If the AlbumPresentationModel would provide a property
     * <em>bufferedComposerEnabled</em> we could use that instead.
     * I've not added the latter property to the AlbumPresentationModel
     * so it remains close to the example used in Martin Fowler's
     * description of the
     * <a href="http://martinfowler.com/eaaDev/PresentationModel.html">Presentation
     * Model</a> pattern.
     */
    private void initEventHandling() {
        ValueModel bufferedClassicalModel =
            presentationModel.getBufferedModel(Album.PROPERTY_CLASSICAL);
        bufferedClassicalModel.addValueChangeListener(
                new BufferedClassicalChangeHandler());
    }


    // Building ***************************************************************

    /**
     * Builds and returns a panel that consists of the editor and display.
     *
     * @return the built panel
     */
    public JComponent buildPanel() {
        initComponents();
        initEventHandling();

        FormLayout layout = new FormLayout(
                "pref, $lcgap, 150dlu:grow",
                "p, $lgap, p, $lgap, p, $lgap, p, $lgap, p, 17dlu, " +
                "p, $lgap, p, $lgap, p, $lgap, p, $lgap, p, 17dlu, p");

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        builder.addSeparator("Buffered",         cc.xyw(1,  1, 3));
        builder.addLabel("Arti&st:",             cc.xy (1,  3));
        builder.add(artistField,                 cc.xy (3,  3));
        builder.addLabel("&Title:",              cc.xy (1,  5));
        builder.add(titleField,                  cc.xy (3,  5));
        builder.add(classicalBox,                cc.xy (3,  7));
        builder.addLabel("Co&mposer:",           cc.xy (1,  9));
        builder.add(composerField,               cc.xy (3,  9));

        builder.addSeparator("Unbuffered",      cc.xyw(1, 11, 3));
        builder.addLabel("Artist:",             cc.xy (1, 13));
        builder.add(unbufferedArtistField,      cc.xy (3, 13));
        builder.addLabel("Title:",              cc.xy (1, 15));
        builder.add(unbufferedTitleField,       cc.xy (3, 15));
        builder.add(unbufferedClassicalBox,     cc.xy (3, 17));
        builder.addLabel("Composer:",           cc.xy (1, 19));
        builder.add(unbufferedComposerField,    cc.xy (3, 19));

        builder.add(buildButtonBar(),           cc.xyw(1, 21, 3));

        return builder.getPanel();
    }


    private JComponent buildButtonBar() {
        return ButtonBarFactory.buildRightAlignedBar(
                applyButton,
                resetButton);
    }


    // Event Handling *********************************************************

    /**
     * Updates the composer field's enablement and contents.
     * Sets the enablement according to the boolean state of
     * the buffered classical property. If the composer is not enabled,
     * we copy the domain logic and set the composer to {@code null}.
     */
    private void updateComposerField() {
        boolean composerEnabled = ((Boolean) presentationModel
                .getBufferedValue(Album.PROPERTY_CLASSICAL)).booleanValue();
        composerField.setEnabled(composerEnabled);
        if (!composerEnabled) {
            presentationModel.setBufferedValue(Album.PROPERTY_COMPOSER, null);
        }
    }


    /**
     * Updates the composer field's enablement and the buffered composer name.
     */
    private final class BufferedClassicalChangeHandler implements PropertyChangeListener {

        /**
         * The buffered (Boolean) classical property has changed.
         * Updates the enablement and contents of the composer field.
         */
        public void propertyChange(PropertyChangeEvent evt) {
            updateComposerField();
        }
    }


    // Actions ****************************************************************

    /**
     * Commits the Trigger used to buffer the editor contents.
     */
    private final class ApplyAction extends AbstractAction {

        private ApplyAction() {
            super("Apply");
            putValue(Action.MNEMONIC_KEY, Integer.valueOf('A'));
        }

        public void actionPerformed(ActionEvent e) {
            presentationModel.triggerCommit();
        }
    }


    /**
     * Flushed the Trigger used to buffer the editor contents.
     */
    private final class ResetAction extends AbstractAction {

        private ResetAction() {
            super("Reset");
            putValue(Action.MNEMONIC_KEY, Integer.valueOf('R'));
        }

        public void actionPerformed(ActionEvent e) {
            presentationModel.triggerFlush();
        }
    }


}
