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

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;

import com.jgoodies.binding.tutorial.Album;
import com.jgoodies.binding.tutorial.TutorialApplication;
import com.jgoodies.binding.tutorial.TutorialUtils;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Builds an editor that copies data from the domain back and forth.
 * This approach is known as the "copying" approach or "push/pull".<p>
 *
 * The event handling used to enable and disable the composer text field
 * is invoked by a ChangeListener that hooks into the classical check box.
 * Note that this lacks the domain logic, where the composer is set to
 * {@code null} if the classical property is set to false.
 * This logic is deferred until the component values are written to the
 * edited Album via <code>#updateModel</code> when OK is pressed.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.19 $
 */

public final class EditorCopyingExample extends TutorialApplication {

    /**
     * Refers to the Album that is to be edited by this example editor.
     */
    private final Album editedAlbum;

    private JTextComponent titleField;
    private JTextComponent artistField;
    private JCheckBox      classicalBox;
    private JTextComponent composerField;
    private JButton        okButton;
    private JButton        cancelButton;
    private JButton        resetButton;


    // Launching **************************************************************

    public static void main(String[] args) {
        TutorialApplication.launch(EditorCopyingExample.class, args);
    }


    @Override
    protected void startup(String[] args) {
        JFrame frame = createFrame("Binding Tutorial :: Editor (Copying)");
        frame.add(buildPanel());
        updateView();
        packAndShowOnScreenCenter(frame);
    }


    // Instance Creation ******************************************************

    /**
     * Constructs an editor for an example Album.
     */
    public EditorCopyingExample() {
        this(Album.ALBUM1);
    }


    /**
     * Constructs an editor for an Album to be edited.
     *
     * @param album    the Album to be edited
     */
    public EditorCopyingExample(Album album) {
        this.editedAlbum = album;
    }


    // Initialization *********************************************************

    /**
     *  Creates and initializes the UI components.
     */
    private void initComponents() {
        titleField    = new JTextField();
        artistField   = new JTextField();
        classicalBox  = new JCheckBox("Classical");
        classicalBox.setMnemonic('C');
        composerField = new JTextField();
        okButton      = new JButton(new OKAction());
        cancelButton  = new JButton(new CancelAction());
        resetButton   = new JButton(new ResetAction());

        updateComposerField();
    }


    /**
     * Observes the classical check box to update the composer field's
     * enablement and contents. For demonstration purposes a listener
     * is registered that writes all changes to the console.
     */
    private void initEventHandling() {
        classicalBox.addChangeListener(new ClassicalChangeHandler());

        // Report changes in all bound Album properties.
        editedAlbum.addPropertyChangeListener(
                TutorialUtils.createDebugPropertyChangeListener());
    }


    // Copying Data Back and Forth ********************************************

    /**
     * Reads the property values from the edited Album
     * and sets them in this editor's components.
     */
    private void updateView() {
        titleField.setText(editedAlbum.getTitle());
        artistField.setText(editedAlbum.getArtist());
        classicalBox.setSelected(editedAlbum.isClassical());
        composerField.setText(editedAlbum.getComposer());
    }


    /**
     * Reads the values from this editor's components
     * and set the associated Album properties.
     */
    private void updateModel() {
        editedAlbum.setTitle(titleField.getText());
        editedAlbum.setArtist(artistField.getText());
        editedAlbum.setClassical(classicalBox.isSelected());
        editedAlbum.setComposer(composerField.getText());
    }


    // Building ***************************************************************

    /**
     * Builds and returns the editor panel.
     *
     * @return the built panel
     */
    public JComponent buildPanel() {
        initComponents();
        initEventHandling();

        FormLayout layout = new FormLayout(
                "pref, $lcgap, 150dlu:grow",
                "p, $lgap, p, $lgap, p, $lgap, p, 9dlu, p");

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        builder.addLabel("Arti&st:",   cc.xy (1, 1));
        builder.add(artistField,       cc.xy (3, 1));
        builder.addLabel("&Title:",    cc.xy (1, 3));
        builder.add(titleField,        cc.xy (3, 3));
        builder.add(classicalBox,      cc.xy (3, 5));
        builder.addLabel("Co&mposer:", cc.xy (1, 7));
        builder.add(composerField,     cc.xy (3, 7));
        builder.add(buildButtonBar(),  cc.xyw(1, 9, 3));

        return builder.getPanel();
    }


    private JComponent buildButtonBar() {
        return ButtonBarFactory.buildRightAlignedBar(
                okButton, cancelButton, resetButton);
    }


    // Event Handling *********************************************************

    /**
     * Updates the composer field's enablement and contents.
     * Sets the enablement according to the selection state
     * of the classical check box. If the composer is not enabled,
     * we copy the domain logic and clear the composer field's text.
     */
    private void updateComposerField() {
        boolean composerEnabled = classicalBox.isSelected();
        composerField.setEnabled(composerEnabled);
        if (!composerEnabled) {
            composerField.setText("");
        }
    }


    /**
     * Updates the composer field's enablement and text.
     */
    private final class ClassicalChangeHandler implements ChangeListener {

        /**
         * The selection state of the classical check box has changed.
         * Updates the enablement and contents of the composer field.
         */
        public void stateChanged(ChangeEvent evt) {
            updateComposerField();
        }
    }


    // Actions ****************************************************************

    private final class OKAction extends AbstractAction {

        private OKAction() {
            super("OK");
        }

        public void actionPerformed(ActionEvent e) {
            updateModel();
            System.out.println(editedAlbum);
            System.exit(0);
        }
    }


    private final class CancelAction extends AbstractAction {

        private CancelAction() {
            super("Cancel");
        }

        public void actionPerformed(ActionEvent e) {
            // Just ignore the current content.
            System.out.println(editedAlbum);
            System.exit(0);
        }
    }


    private final class ResetAction extends AbstractAction {

        private ResetAction() {
            super("Reset");
            putValue(Action.MNEMONIC_KEY, Integer.valueOf('R'));
        }

        public void actionPerformed(ActionEvent e) {
            updateView();
            System.out.println(editedAlbum);
        }
    }


}
