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
import javax.swing.text.JTextComponent;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.tutorial.TutorialApplication;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates three different styles when to commit changes:
 * on key typed, on focus lost, on OK/Apply pressed.
 * Therefore we bind 3 JTextFields to 3 String typed ValueModels
 * that honor the commit style. And we bind 3 JLabels directly
 * to these ValueModels that display the current value.<p>
 *
 * The ValueModels used in this example are requested from a
 * PresentationModel that adapts text properties of a TextBean.
 * This is just to demonstrate a consistent binding style.
 * The same techniques work with any ValueModel.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.14 $
 *
 * @see com.jgoodies.binding.PresentationModel
 */
public final class CommitStylesExample extends TutorialApplication {

    /**
     * Holds a TextBean and vends ValueModels that adapt TextBean properties.
     * As an alternative to this PresentationModel we could use 3 ValueModels,
     * for example 3 ValueHolders, or any other ValueModel implementation.
     */
    private final PresentationModel<TextBean> presentationModel;

    private JTextComponent onKeyTypedField;
    private JTextComponent onFocusLostField;
    private JTextComponent onApplyField;
    private JLabel onKeyTypedLabel;
    private JLabel onFocusLostLabel;
    private JLabel onApplyLabel;
    private JButton applyButton;


    // Launching **************************************************************

    public static void main(String[] args) {
        TutorialApplication.launch(CommitStylesExample.class, args);
    }


    @Override
    protected void startup(String[] args) {
        JFrame frame = createFrame("Binding Tutorial :: Commit Styles");
        frame.add(buildPanel());
        packAndShowOnScreenCenter(frame);
    }


    // Instance Creation ******************************************************

    /**
     * Constructs the example with a PresentationModel on the a TextBean.
     */
    public CommitStylesExample() {
        this.presentationModel = new PresentationModel<TextBean>(new TextBean());
    }


    // Component Creation and Initialization **********************************

    /**
     * Creates,binds and configures the UI components.<p>
     */
    private void initComponents() {
        onKeyTypedField = BasicComponentFactory.createTextField(
                presentationModel.getModel(TextBean.PROPERTYNAME_TEXT1), false);
        onKeyTypedLabel = BasicComponentFactory.createLabel(
                presentationModel.getModel(TextBean.PROPERTYNAME_TEXT1));

        onFocusLostField = BasicComponentFactory.createTextField(
                presentationModel.getModel(TextBean.PROPERTYNAME_TEXT2));
        onFocusLostLabel = BasicComponentFactory.createLabel(
                presentationModel.getModel(TextBean.PROPERTYNAME_TEXT2));

        onApplyField = BasicComponentFactory.createTextField(
                presentationModel.getBufferedModel(TextBean.PROPERTYNAME_TEXT3));
        onApplyLabel = BasicComponentFactory.createLabel(
                presentationModel.getModel(TextBean.PROPERTYNAME_TEXT3));

        applyButton = new JButton(new ApplyAction());
    }


    // Building ***************************************************************

    /**
     * Builds and returns the panel.
     *
     * @return the built panel
     */
    public JComponent buildPanel() {
        initComponents();

        FormLayout layout = new FormLayout(
                "[50dlu,pref], $lcgap, 50dlu, 9dlu, 50dlu",
                "p, 6dlu, p, $lgap, p, $lgap, p, 17dlu, p");

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        builder.addTitle("Commit Style",     cc.xy (1, 1));
        builder.addTitle("Value",            cc.xy (5, 1));
        builder.addLabel("&Key Typed:",      cc.xy (1, 3));
        builder.add(onKeyTypedField,         cc.xy (3, 3));
        builder.add(onKeyTypedLabel,         cc.xy (5, 3));
        builder.addLabel("&Focus Lost:",     cc.xy (1, 5));
        builder.add(onFocusLostField,        cc.xy (3, 5));
        builder.add(onFocusLostLabel,        cc.xy (5, 5));
        builder.addLabel("Apply &Pressed:",  cc.xy (1, 7));
        builder.add(onApplyField,            cc.xy (3, 7));
        builder.add(onApplyLabel,            cc.xy (5, 7));
        builder.add(buildButtonBar(),        cc.xyw(1, 9, 5));

        return builder.getPanel();
    }


    private JComponent buildButtonBar() {
        return ButtonBarFactory.buildRightAlignedBar(
                applyButton);
    }


    // Actions ****************************************************************

    private final class ApplyAction extends AbstractAction {

        private ApplyAction() {
            super("Apply");
            putValue(Action.MNEMONIC_KEY, Integer.valueOf('A'));
        }

        public void actionPerformed(ActionEvent e) {
            presentationModel.triggerCommit();
        }
    }


    // Helper Class ***********************************************************

    /**
     * A simple bean that just provides three bound read-write text properties.
     */
    public static final class TextBean extends Model {

        // Names for the Bound Bean Properties ---------------------

        public static final String PROPERTYNAME_TEXT1 = "text1";
        public static final String PROPERTYNAME_TEXT2 = "text2";
        public static final String PROPERTYNAME_TEXT3 = "text3";

        private String text1;
        private String text2;
        private String text3;


        // Instance Creation ---------------------------------------

        private TextBean() {
            text1 = "Text1";
            text2 = "Text2";
            text3 = "Text3";
        }


        // Accessors -----------------------------------------------

        public String getText1() {
            return text1;
        }

        public void setText1(String newText1) {
            String oldText1 = getText1();
            text1 = newText1;
            firePropertyChange(PROPERTYNAME_TEXT1, oldText1, newText1);
        }


        public String getText2() {
            return text2;
        }

        public void setText2(String newText2) {
            String oldText2 = getText2();
            text2 = newText2;
            firePropertyChange(PROPERTYNAME_TEXT2, oldText2, newText2);
        }


        public String getText3() {
            return text3;
        }

        public void setText3(String newText3) {
            String oldText3 = getText3();
            text3 = newText3;
            firePropertyChange(PROPERTYNAME_TEXT3, oldText3, newText3);
        }

    }


}
