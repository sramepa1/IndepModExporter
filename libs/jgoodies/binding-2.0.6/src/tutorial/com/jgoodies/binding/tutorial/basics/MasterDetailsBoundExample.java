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

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.tutorial.Album;
import com.jgoodies.binding.tutorial.TutorialApplication;
import com.jgoodies.binding.tutorial.TutorialUtils;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates a "hand-made" way how to connect a master list with a bound
 * details view. It builds a JList of Albums with an attached details panel
 * that presents the current Album selection. The details panel's components
 * are bound to the domain using ValueModels returned by a PresentationModel.<p>
 *
 * This example handles selection changes with a custom ListSelectionListener,
 * the AlbumSelectionHandler, that sets the JList's selected values as new
 * bean of the details PresentationModel. A simpler means to achieve the same
 * effect is demonstrated by the MasterDetailsSelectionInListExample that uses
 * the SelectionInList as bean channel for the details PresentationModel.<p>
 *
 * Another variant of this example is the MasterDetailsCopyingExample
 * that copies the details data on list selection changes, instead of binding
 * the details UI components to the details PresentationModel's ValueModels.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.18 $
 *
 * @see com.jgoodies.binding.PresentationModel
 * @see com.jgoodies.binding.tutorial.basics.MasterDetailsCopyingExample
 * @see com.jgoodies.binding.tutorial.basics.MasterDetailsSelectionInListExample
 */
public final class MasterDetailsBoundExample extends TutorialApplication {

    /**
     * The Albums displayed in the master list.
     */
    private final List<Album> albums;

    /**
     * Holds the edited Album and vends ValueModels that adapt Album properties.
     */
    private final PresentationModel<Album> detailsModel;

    private JList          albumsList;
    private JTextComponent titleField;
    private JTextComponent artistField;
    private JTextComponent classicalField;
    private JTextComponent composerField;
    private JButton        closeButton;


    // Launching **************************************************************

    public static void main(String[] args) {
        TutorialApplication.launch(MasterDetailsBoundExample.class, args);
    }


    @Override
    protected void startup(String[] args) {
        JFrame frame = createFrame("Binding Tutorial :: Master/Details (Bound)");
        frame.add(buildPanel());
        packAndShowOnScreenCenter(frame);
    }


    // Instance Creation ******************************************************

    /**
     * Constructs a list editor using a example Album list.
     */
    public MasterDetailsBoundExample() {
        this(Album.ALBUMS);
    }

    /**
     * Constructs a list editor for editing the given list of Albums.
     *
     * @param albums   the list of Albums to edit
     */
    public MasterDetailsBoundExample(List<Album> albums) {
        this.albums = albums;
        detailsModel = new PresentationModel<Album>(new ValueHolder(null, true));
    }


    // Component Creation and Initialization **********************************

    /**
     * Creates, binds, and configures the UI components.
     * All components in the details view are read-only.<p>
     *
     * The coding style used here is based on standard Swing components.
     * Therefore we can create and bind the components in one step.
     * And that's the purpose of the BasicComponentFactory class.<p>
     *
     * If you need to bind custom components, for example MyTextField,
     * MyCheckBox, MyComboBox, you can use the more basic Bindings class.
     * The code would then read:<pre>
     * titleField = new MyTextField();
     * Bindings.bind(titleField,
     *               detailsModel.getModel(Album.PROPERTYNAME_TITLE));
     * </pre><p>
     *
     * I strongly recommend to use either the BasicComponentFactory or
     * the Bindings class. These classes hide details of the binding.
     * So you better <em>not</em> write the following code:<pre>
     * titleField = new JTextField();
     * titleField.setDocument(new DocumentAdapter(
     *     detailsModel.getModel(Album.PROPERTYNAME_TITLE)));
     * </pre>
     */
    private void initComponents() {
        albumsList = new JList(albums.toArray());
        albumsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        albumsList.setCellRenderer(TutorialUtils.createAlbumListCellRenderer());

        titleField = TutorialUtils.createReadOnlyTextField(
                detailsModel.getModel(Album.PROPERTY_TITLE));
        artistField = TutorialUtils.createReadOnlyTextField(
                detailsModel.getModel(Album.PROPERTY_ARTIST));
        classicalField = TutorialUtils.createReadOnlyTextField(
                ConverterFactory.createBooleanToStringConverter(
                        detailsModel.getModel(Album.PROPERTY_CLASSICAL),
                        "Yes",
                        "No"));
        composerField = TutorialUtils.createReadOnlyTextField(
                detailsModel.getModel(Album.PROPERTY_COMPOSER));
        closeButton = new JButton(TutorialUtils.createCloseAction());
    }


    private void initEventHandling() {
        albumsList.addListSelectionListener(new AlbumSelectionHandler());
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
                "p, 1dlu, p, 9dlu, p, $lgap, p, $lgap, p, $lgap, p, 9dlu, p");

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        builder.addLabel("&Albums:",    cc.xyw(1,  1, 3));
        builder.add(new JScrollPane(
                        albumsList),    cc.xyw(1,  3, 3));

        builder.addLabel("Artist:",     cc.xy (1,  5));
        builder.add(artistField,        cc.xy (3,  5));
        builder.addLabel("Title:",      cc.xy (1,  7));
        builder.add(titleField,         cc.xy (3,  7));
        builder.addLabel("Classical:",  cc.xy (1,  9));
        builder.add(classicalField,     cc.xy (3,  9));
        builder.addLabel("Composer:",   cc.xy (1, 11));
        builder.add(composerField,      cc.xy (3, 11));
        builder.add(buildButtonBar(),   cc.xyw(1, 13, 3));

        return builder.getPanel();
    }


    private JComponent buildButtonBar() {
        return ButtonBarFactory.buildRightAlignedBar(closeButton);
    }


    // Event Handling ********************************************************

    /**
     * Sets the selected album as bean in the details model.
     */
    private final class AlbumSelectionHandler implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting())
                return;
            detailsModel.setBean((Album) albumsList.getSelectedValue());
        }
    }


}
