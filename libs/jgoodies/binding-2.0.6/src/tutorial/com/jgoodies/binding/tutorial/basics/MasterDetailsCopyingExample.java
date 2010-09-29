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

import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;

import com.jgoodies.binding.tutorial.Album;
import com.jgoodies.binding.tutorial.TutorialApplication;
import com.jgoodies.binding.tutorial.TutorialUtils;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates how to connect a master list with a copying details view.
 * It builds a JList of Albums with an attached Album details panel
 * that presents the current Album selection. The details data
 * is copied back and forth from the domain to the details UI components.<p>
 *
 * A bound variant of this example is the MasterDetailsBoundExample
 * that binds the details UI components to the ValueModels provided
 * by a details PresentationModel. An even simpler variant is the
 * MasterDetailsSelectionInListExample that uses the SelectionInList
 * as bean channel for the details PresentationModel.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.19 $
 *
 * @see com.jgoodies.binding.tutorial.basics.MasterDetailsBoundExample
 * @see com.jgoodies.binding.tutorial.basics.MasterDetailsSelectionInListExample
 */
public final class MasterDetailsCopyingExample extends TutorialApplication {

    /**
     * The Albums displayed in the overview list.
     */
    private final List<Album> albums;

    /**
     * Holds the list selection, which is the currently edited Album.
     */
    private Album editedAlbum;

    private JList          albumsList;
    private JTextComponent titleField;
    private JTextComponent artistField;
    private JTextComponent classicalField;
    private JTextComponent composerField;
    private JButton        closeButton;


    // Launching **************************************************************

    public static void main(String[] args) {
        TutorialApplication.launch(MasterDetailsCopyingExample.class, args);
    }


    @Override
    protected void startup(String[] args) {
        JFrame frame = createFrame("Binding Tutorial :: Master/Details (Copying)");
        frame.add(buildPanel());
        packAndShowOnScreenCenter(frame);
    }


    // Instance Creation ******************************************************

    /**
     * Constructs a list editor using a example Album list.
     */
    public MasterDetailsCopyingExample() {
        this(Album.ALBUMS);
    }


    /**
     * Constructs a list editor for editing the given list of Albums.
     *
     * @param albums   the list of Albums to edit
     */
    public MasterDetailsCopyingExample(List<Album> albums) {
        this.albums = albums;
    }


    // Component Creation and Initialization **********************************

    /**
     * Creates and initializes the UI components.
     * All components in the details view are read-only.
     */
    private void initComponents() {
        albumsList = new JList(albums.toArray());
        albumsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        albumsList.setCellRenderer(TutorialUtils.createAlbumListCellRenderer());

        titleField = TutorialUtils.createReadOnlyTextField();
        artistField = TutorialUtils.createReadOnlyTextField();
        classicalField = TutorialUtils.createReadOnlyTextField();
        composerField = TutorialUtils.createReadOnlyTextField();
        closeButton = new JButton(TutorialUtils.createCloseAction());
    }


    private void initEventHandling() {
        albumsList.addListSelectionListener(new AlbumSelectionHandler());
    }


    // Copying Data Back and Forth ********************************************

    /**
     * Reads the property values from the edited Album
     * and sets them in this editor's components.
     *
     * @param album    the Album to read property values from
     */
    private void updateView(Album album) {
        titleField.setText(album.getTitle());
        artistField.setText(album.getArtist());
        classicalField.setText(album.isClassical() ? "Yes" : "No");
        composerField.setText(album.getComposer());
    }


    // Building ***************************************************************

    /**
     * Builds and returns a panel that consists of
     * a master list and a details form.
     *
     * @return the built panel
     */
    public JComponent buildPanel() {
        initComponents();
        initEventHandling();

        FormLayout layout = new FormLayout(
                "pref, $lcgap, 150dlu:grow",
                "p, 1dlu, p, 9dlu, p, $lg, p, $lg, p, $lg, p, 9dlu, p");

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        builder.addLabel("&Albums:",     cc.xyw(1,  1, 3));
        builder.add(new JScrollPane(
                        albumsList),     cc.xyw(1,  3, 3));

        builder.addLabel("Artist:",      cc.xy (1,  5));
        builder.add(artistField,         cc.xy (3,  5));
        builder.addLabel("Title:",       cc.xy (1,  7));
        builder.add(titleField,          cc.xy (3,  7));
        builder.addLabel("Classical:",   cc.xy (1,  9));
        builder.add(classicalField,      cc.xy (3,  9));
        builder.addLabel("Composer:",    cc.xy (1, 11));
        builder.add(composerField,       cc.xy (3, 11));
        builder.add(buildButtonBar(),    cc.xyw(1, 13, 3));

        return builder.getPanel();
    }


    private JComponent buildButtonBar() {
        return ButtonBarFactory.buildRightAlignedBar(closeButton);
    }


    // Event Handling *********************************************************

    /**
     * Updates the view using the selected album.
     */
    private final class AlbumSelectionHandler implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting())
                return;
            // Now set the current selection as edited album.
            editedAlbum = (Album) albumsList.getSelectedValue();
            // Then copy the album data to the component values.
            updateView(editedAlbum);
        }
    }


}
