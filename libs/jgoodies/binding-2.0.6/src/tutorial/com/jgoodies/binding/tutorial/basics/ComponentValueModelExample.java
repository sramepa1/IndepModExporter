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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextField;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.binding.tutorial.TutorialApplication;
import com.jgoodies.binding.value.ComponentValueModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates how to modify the enabled/editable/visible state
 * of a Swing component in a Presentation Model using the new
 * ComponentValueModel that has been introduced in the Binding 1.1.
 * The advantage of this approach is, that a PresentationModel
 * can now easily operate on frequently used GUI state.<p>
 *
 * See the JavaDoc method comment for #initEventHandling
 * in the ExamplePresentationModel.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.12 $
 *
 * @see com.jgoodies.binding.adapter.BasicComponentFactory
 * @see com.jgoodies.binding.adapter.Bindings
 *
 * @since 1.1
 */
public final class ComponentValueModelExample extends TutorialApplication {

    // Holds an ExampleBean and vends ValueModels that adapt its properties.
    private final ExamplePresentationModel presentationModel;

    private JTextField text1Field;
    private JCheckBox  enabledBox;
    private JTextField text2Field;
    private JCheckBox  editableBox;
    private JTextField text3Field;
    private JCheckBox  visibleBox;


    // Launching **************************************************************

    public static void main(String[] args) {
        TutorialApplication.launch(ComponentValueModelExample.class, args);
    }


    @Override
    protected void startup(String[] args) {
        JFrame frame = createFrame("Binding Tutorial :: ComponentValueModel");
        frame.add(buildPanel());
        packAndShowOnScreenCenter(frame);
    }


    // Instance Creation ******************************************************

    /**
     * Constructs the 'Components' example on an instance of ExampleBean.
     */
    public ComponentValueModelExample() {
        presentationModel = new ExamplePresentationModel(new ExampleBean());
    }


    // Component Creation and Initialization **********************************

    /**
     * Creates, binds and configures the UI components.<p>
     *
     * If possible, the components are created using the BasicComponentFactory,
     * or the Bindings class.
     */
    private void initComponents() {
        text1Field = BasicComponentFactory.createTextField(
                presentationModel.getComponentModel(ExampleBean.PROPERTY_TEXT1));
        text2Field = BasicComponentFactory.createTextField(
                presentationModel.getComponentModel(ExampleBean.PROPERTY_TEXT2));
        text3Field = BasicComponentFactory.createTextField(
                presentationModel.getComponentModel(ExampleBean.PROPERTY_TEXT3));

        enabledBox = BasicComponentFactory.createCheckBox(
                presentationModel.getModel(ExampleBean.PROPERTY_ENABLED),
                "enabled");
        enabledBox.setMnemonic('n');
        editableBox = BasicComponentFactory.createCheckBox(
                presentationModel.getModel(ExampleBean.PROPERTY_EDITABLE),
                "editable");
        editableBox.setMnemonic('d');
        visibleBox = BasicComponentFactory.createCheckBox(
                presentationModel.getModel(ExampleBean.PROPERTY_VISIBLE),
                "visible");
        visibleBox.setMnemonic('v');
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
                "pref, $lcgap, 50dlu, $rgap, pref",
                "p, $lgap, p, $lgap, p");

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.addLabel("Text1:",     cc.xy(1, 1));
        builder.add(text1Field,        cc.xy(3, 1));
        builder.add(enabledBox,        cc.xy(5, 1));
        builder.addLabel("Text2:",     cc.xy(1, 3));
        builder.add(text2Field,        cc.xy(3, 3));
        builder.add(editableBox,       cc.xy(5, 3));
        builder.addLabel("Text3:",     cc.xy(1, 5));
        builder.add(text3Field,        cc.xy(3, 5));
        builder.add(visibleBox,        cc.xy(5, 5));
        return builder.getPanel();
    }


    // Helper Code ************************************************************

    public static final class ExampleBean extends Model {

        // Names of the Bound Bean Properties *********************************

        public static final String PROPERTY_ENABLED  = "enabled";
        public static final String PROPERTY_EDITABLE = "editable";
        public static final String PROPERTY_VISIBLE  = "visible";
        public static final String PROPERTY_TEXT1    = "text1";
        public static final String PROPERTY_TEXT2    = "text2";
        public static final String PROPERTY_TEXT3    = "text3";


        // Fields *************************************************************

        private boolean enabled;
        private boolean editable;
        private boolean visible;
        private String text1;
        private String text2;
        private String text3;


        // Instance Creation **************************************************

        public ExampleBean() {
            enabled = true;
            editable = false;
            visible = true;
            text1 = "Sample text1";
            text2 = "Sample text2";
            text3 = "Sample text3";
        }


        // Accessors **********************************************************

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean newEnabled) {
            boolean oldEnabled = isEnabled();
            enabled = newEnabled;
            firePropertyChange(PROPERTY_ENABLED, oldEnabled, newEnabled);
        }


        public boolean isEditable() {
            return editable;
        }

        public void setEditable(boolean newEditable) {
            boolean oldEditable = isEditable();
            editable = newEditable;
            firePropertyChange(PROPERTY_EDITABLE, oldEditable, newEditable);
        }


        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean newVisible) {
            boolean oldVisible = isVisible();
            visible = newVisible;
            firePropertyChange(PROPERTY_VISIBLE, oldVisible, newVisible);
        }


        public String getText1() {
            return text1;
        }

        public void setText1(String newText) {
            String oldText = getText1();
            text1 = newText;
            firePropertyChange(PROPERTY_TEXT1, oldText, newText);
        }


        public String getText2() {
            return text2;
        }

        public void setText2(String newText) {
            String oldText = getText2();
            text2 = newText;
            firePropertyChange(PROPERTY_TEXT2, oldText, newText);
        }


        public String getText3() {
            return text3;
        }

        public void setText3(String newText) {
            String oldText = getText3();
            text3 = newText;
            firePropertyChange(PROPERTY_TEXT3, oldText, newText);
        }


    }


    /**
     * A custom PresentationModel that provides a SelectionInList
     * for the bean's ListModel and the bean's list selection.
     */
    private static final class ExamplePresentationModel
        extends PresentationModel<ExampleBean> {


        // Instance Creation -----------------------------------------

        private ExamplePresentationModel(ExampleBean exampleBean) {
            super(exampleBean);
            initEventHandling();
        }


        // Event Handling ---------------------------------------------

        /**
         * Initializes the event handling. The three domain properties
         * enabled, editable, and visible are bound to the related
         * properties of the ComponentValueModels for text1, text2, text3.<p>
         *
         * The first approach demonstrates how to register a hand-made
         * value change handler with the domain's enabled model.
         * This handler updates the enabled state of the ComponentValueModel
         * whenever the domain changes. The ComponentValueModel change will
         * in turn update all components bound to it.<p>
         *
         * A shorter way to write the above is to use a PropertyConnector.
         * Whenever the editable or visible domain model changes,
         * the PropertyConnector will update the connected property
         * in the ComponentValueModel, which in turn will update all
         * components bound to it.<p>
         *
         * Both approaches require to synchronize the ComponentValueModel state
         * with the domain state at initialization time. In this example
         * the initial domain state for enabled and visible is true,
         * but the initial editable domain state is false, so the
         * view bound to text 2 shall be non-editable first.
         */
        private void initEventHandling() {
            // Observe changes in the domain's enabled property
            // to update the enablement of the text1 views.
            getModel(ExampleBean.PROPERTY_ENABLED).addValueChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    updateText1ViewsEnablement();
                }
            });
            // Update the enablement of all views bound to text1 now.
            updateText1ViewsEnablement();


            // Observe changes in the domain's editable property
            // to update the editable state of text2 views.
            // Update the editable state of all views bound to text2 now.
            PropertyConnector.connectAndUpdate(
                    getModel(ExampleBean.PROPERTY_EDITABLE),
                    getComponentModel(ExampleBean.PROPERTY_TEXT2),
                    ComponentValueModel.PROPERTYNAME_EDITABLE);


            PropertyConnector.connectAndUpdate(
                    getModel(ExampleBean.PROPERTY_VISIBLE),
                    getComponentModel(ExampleBean.PROPERTY_TEXT3),
                    ComponentValueModel.PROPERTYNAME_VISIBLE);
        }


        private void updateText1ViewsEnablement() {
            boolean enabled = getModel(ExampleBean.PROPERTY_ENABLED).booleanValue();
            getComponentModel(ExampleBean.PROPERTY_TEXT1).setEnabled(enabled);
        }

    }


}
