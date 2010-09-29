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

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.jgoodies.binding.tutorial.Album;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * A dialog for editing an Album. Uses an AlbumEditorView to build
 * the editor form and just adds buttons to accept or cancel the edit.
 * These buttons trigger a commit or flush in the dialog's PresentationModel.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.19 $
 *
 * @see AlbumEditorView
 * @see AlbumManagerModel
 * @see com.jgoodies.binding.tutorial.AlbumPresentationModel
 * @see com.jgoodies.binding.PresentationModel
 */

public final class AlbumEditorDialog extends JDialog {

    /**
     * Holds the edited album and vends ValueModels that adapt album properties.
     */
    private final BufferedAlbumPresentationModel albumPresentationModel;

    /**
     * Will be set to {@code true} if the dialog is canceled.
     *
     * @see #hasBeenCanceled()
     */
    private boolean canceled;


    // Instance Creation ******************************************************

    /**
     * Constructs an AlbumEditorDialog for the given Album.
     *
     * @param parent   this dialog's parent frame
     * @param album    the Album to be edited
     */
    public AlbumEditorDialog(Frame parent, Album album) {
        super(parent, "Album Editor", true);
        this.albumPresentationModel = new BufferedAlbumPresentationModel(album);
        canceled = false;
    }


    // Dialog Life Cycle ******************************************************

    /**
     * Builds the dialog content, marks it as not canceled and makes it visible.
     */
    public void open() {
        build();
        canceled = false;
        setVisible(true);
    }


    /**
     * Closes the dialog: releases obsolete bindings, and disposes the dialog,
     * which in turn releases all required OS resources.
     */
    public void close() {
        release();
        dispose();
    }


    /**
     * Checks and answers whether the dialog has been canceled.
     * This indicator is set in #doAccept and #doCancel.
     *
     * @return true indicates that the dialog has been canceled
     */
    public boolean hasBeenCanceled() {
        return canceled;
    }


    /**
     * Removes listeners from the dialog's Album that are created
     * by the PresentationModel when requesting adapting ValueModels
     * via <code>#getModel</code> or <code>#getBufferedModel</code>.
     * Setting the presentation model's bean to {@code null}
     * removes all these listeners.
     *
     * @see com.jgoodies.binding.PresentationModel
     * @see com.jgoodies.binding.beans.BeanAdapter#release()
     */
    private void release() {
        albumPresentationModel.setBean(null);
    }


    // Building ***************************************************************

    /**
     * Builds the dialog's content pane, packs it, sets the resizable property,
     * and locates it on the screen. The dialog is then ready to be opened.<p>
     *
     * Subclasses should rarely override this method.
     */
    private void build() {
        setContentPane(buildContentPane());
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }


    private JComponent buildContentPane() {
        FormLayout layout = new FormLayout(
                "fill:pref",
                "fill:pref, $ugap, pref");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.getPanel().setBorder(new EmptyBorder(18, 12, 12, 12));
        CellConstraints cc = new CellConstraints();
        builder.add(buildEditorPanel(), cc.xy(1, 1));
        builder.add(buildButtonBar(),   cc.xy(1, 3));
        return builder.getPanel();
    }

    private JComponent buildEditorPanel() {
        return new AlbumEditorView(albumPresentationModel).buildPanel();
    }

    private JComponent buildButtonBar() {
        JPanel bar = ButtonBarFactory.buildOKCancelBar(
                new JButton(new OKAction()),
                new JButton(new CancelAction()));
        bar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
        return bar;
    }


    // Actions ****************************************************************

    private final class OKAction extends AbstractAction {

        private OKAction() {
            super("OK");
        }

        public void actionPerformed(ActionEvent e) {
            albumPresentationModel.triggerCommit();
            canceled = false;
            close();
        }
    }


    private final class CancelAction extends AbstractAction {

        private CancelAction() {
            super("Cancel");
        }

        public void actionPerformed(ActionEvent e) {
            albumPresentationModel.triggerFlush();
            canceled = true;
            close();
        }
    }


}
