package cz.cvut.promod.hierarchyNotation.frames.jgraphOptions;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import cz.cvut.promod.plugin.notationSpecificPlugIn.DockableFrameData;
import cz.cvut.promod.gui.support.utils.NotationGuiHolder;
import cz.cvut.promod.hierarchyNotation.resources.Resources;

import javax.swing.*;
import java.util.Set;
import java.util.HashSet;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 0:29:23, 27.11.2009
 *
 * Represents a GraphOptions dockable frame of the ProcessHierarchyNotation.
 */
public class GraphOptions extends GraphOptionsView implements DockableFrameData {    

    private final GraphOptionsModel model;

    private final ValueModel gridModel;
    private final ValueModel viewGridModel;
    private final ValueModel cellSizeModel;
    private final ValueModel scaleModel;

    private boolean lastViewGridValue;

    public GraphOptions(final String name) {
        model = new GraphOptionsModel(name);
        final PresentationModel<GraphOptionsModel> presentation = new PresentationModel<GraphOptionsModel>(model);

        gridModel = presentation.getModel(GraphOptionsModel.PROPERTY_GRID);
        viewGridModel = presentation.getModel(GraphOptionsModel.PROPERTY_VIEW_GRID);
        cellSizeModel = presentation.getModel(GraphOptionsModel.PROPERTY_CELL_SIZE);
        scaleModel = presentation.getModel(GraphOptionsModel.PROPERTY_SCALE);

        initBinding();

        initEventHandling();

        // set viewGridCheckBox and cellSizeSpinned disabled
        gridModel.setValue(false);

        // set initial grid cell size
        cellSizeModel.setValue(GraphOptionsModel.INIT_SELL_SIZE);

        lastViewGridValue = (Boolean) viewGridModel.getValue();

        scaleModel.setValue(GraphOptionsModel.INIT_SCALE);
    }

    private void initEventHandling() {
        initialSizeButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                scaleModel.setValue(100);
            }
        });
    }

    private void initBinding() {
        Bindings.bind(gridCheckBox, gridModel);
        Bindings.bind(viewGridCheckBox, viewGridModel);

        scaleSlider.setModel(new BoundedRangeAdapter(
                                scaleModel,
                                GraphOptionsModel.EXTENT,
                                scaleSlider.getMinimum(),
                                scaleSlider.getMaximum()
                            )
        );

        final SpinnerNumberModel scaleSpinnerModel =
            SpinnerAdapterFactory.createNumberAdapter(
                    scaleModel,
                    GraphOptionsModel.INIT_SCALE,
                    GraphOptionsModel.MIN_SCALE,
                    GraphOptionsModel.MAX_SCALE,
                    GraphOptionsModel.INIT_SPINNER_STEP);

        scaleSpinner.setModel(scaleSpinnerModel);

        SpinnerNumberModel gridSpinnerModel =
            SpinnerAdapterFactory.createNumberAdapter(
                    cellSizeModel,
                    GraphOptionsModel.INIT_CELL_SIZE,
                    GraphOptionsModel.MIN_CELL_SIZE,
                    GraphOptionsModel.MAX_CELL_SIZE,
                    GraphOptionsModel.INIT_SPINNER_STEP);

        cellSizeSpinner.setModel(gridSpinnerModel);


        // set viewGridCheckBox and cellSizeSpinned enabled or disabled
        gridModel.addValueChangeListener(new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                final boolean enabled = (Boolean) propertyChangeEvent.getNewValue();

                /* if one don't use the grid, show grid view option should be hidden,
                when the grid is used again, tha last value of view grid is restored */
                if(enabled){
                    viewGridModel.setValue(lastViewGridValue);
                } else {
                    lastViewGridValue = (Boolean) viewGridModel.getValue();
                    viewGridModel.setValue(false);
                }

                cellSizeSpinner.setEnabled(enabled);
                viewGridCheckBox.setEnabled(enabled);
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
        return NotationGuiHolder.Position.LEFT;
    }

    public boolean isMaximizable() {
        return false;
    }

    public Set<NotationGuiHolder.Position> getAllowedDockableFramePositions() {
        final Set<NotationGuiHolder.Position> positions = new HashSet<NotationGuiHolder.Position>();
        positions.add(NotationGuiHolder.Position.LEFT);
        positions.add(NotationGuiHolder.Position.RIGHT);

        return positions;
    }

    public InitialState getInitialState() {
        return InitialState.OPENED;
    }

    public String getDockableFrameTitle() {
        return GraphOptionsModel.TITLE_LABEL;
    }

    public Icon getButtonIcon() {
        return null;
    }

    public ValueModel getGridModel() {
        return gridModel;
    }

    public ValueModel getViewGridModel() {
        return viewGridModel;
    }

    public ValueModel getCellSizeModel() {
        return cellSizeModel;
    }

    public ValueModel getScaleModel() {
        return scaleModel;
    }
}
