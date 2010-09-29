package cz.cvut.promod.hierarchyNotation.frames.toolChooser;

import cz.cvut.promod.plugin.notationSpecificPlugIn.DockableFrameData;
import cz.cvut.promod.gui.support.utils.NotationGuiHolder;

import javax.swing.*;
import java.util.Set;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.binding.PresentationModel;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 12:48:24, 29.11.2009
 *
 * The ToolChooser dockable frame.
 */
public class ToolChooser extends ToolChooserView implements DockableFrameData{

    private final ToolChooserModel model;

    private final ButtonGroup buttonGroup;

    private final ValueModel selectedToolModel;


    public ToolChooser(final String name){
        model = new ToolChooserModel(name);

        final PresentationModel<ToolChooserModel> presentation = new PresentationModel<ToolChooserModel>(model);

        selectedToolModel = presentation.getModel(ToolChooserModel.PROPERTY_SELECTED_TOOL);

        buttonGroup = new ButtonGroup();
        initButtonGroup();

        initEventHandling();

        controlButton.doClick(); // initial tool is "control"
    }

    private void initButtonGroup() {
        buttonGroup.add(controlButton);
        buttonGroup.add(addProcessButton);
        buttonGroup.add(addEdgeButton);
        buttonGroup.add(removeButton);
    }

    private void initEventHandling() {
        controlButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent) {
                selectedToolModel.setValue(ToolChooserModel.Tool.CONTROL);
            }
        });

        addProcessButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent) {
                selectedToolModel.setValue(ToolChooserModel.Tool.ADD_PROCESS);
            }
        });

        addEdgeButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent) {
                selectedToolModel.setValue(ToolChooserModel.Tool.ADD_EDGE);
            }
        });

        removeButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent) {
                selectedToolModel.setValue(ToolChooserModel.Tool.REMOVE);
            }
        });
    }


    public String getDockableFrameName() {
        return model.getName();
    }

    public JComponent getDockableFrameComponent() {
        return this;
    }

    public NotationGuiHolder.Position getInitialPosition() {
        return NotationGuiHolder.Position.RIGHT;
    }

    public boolean isMaximizable() {
        return false;
    }

    public Set<NotationGuiHolder.Position> getAllowedDockableFramePositions() {
        return model.getAllowedSides();
    }

    public InitialState getInitialState() {
        return InitialState.OPENED;
    }

    public String getDockableFrameTitle() {
        return ToolChooserModel.TITLE_LABEL;
    }

    public Icon getButtonIcon() {
        return null;
    }

    public ValueModel getSelectedToolModel() {
        return selectedToolModel;
    }
}
