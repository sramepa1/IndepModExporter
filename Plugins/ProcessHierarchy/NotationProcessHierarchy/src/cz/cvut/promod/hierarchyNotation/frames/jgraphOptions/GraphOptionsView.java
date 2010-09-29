package cz.cvut.promod.hierarchyNotation.frames.jgraphOptions;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.builder.PanelBuilder;

import javax.swing.*;

import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.componentFactoryService.ComponentFactoryService;
import cz.cvut.promod.hierarchyNotation.resources.Resources;
import org.apache.log4j.Logger;

import java.util.Hashtable;
import java.awt.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 0:29:49, 27.11.2009
 *
 * The View component for the GraphOptions dockable frame. 
 */
public class GraphOptionsView extends JPanel {

    private final Logger LOG = Logger.getLogger(GraphOptionsModel.class);

    private final int GRID_SPINNER_COLUMNS = 2;
    private final int SCALE_SPINNER_COLUMNS = 3;

    protected final JCheckBox movableBelowZeroCheckBox = ModelerSession.getComponentFactoryService().createCheckBox();

    private final JLabel gridLabel = ModelerSession.getComponentFactoryService().createLabel(
            Resources.getResources().getString("hierarchy.frame.options.grid")
    );
    private final JLabel cellSizeLabel = ModelerSession.getComponentFactoryService().createLabel(
            Resources.getResources().getString("hierarchy.frame.options.cel.size")
    );
    private final JLabel viewGridLabel = ModelerSession.getComponentFactoryService().createLabel(
            Resources.getResources().getString("hierarchy.frame.options.grid.view")
    );
    private final JLabel scaleLabel = ModelerSession.getComponentFactoryService().createLabel(
        Resources.getResources().getString("hierarchy.frame.options.scale")
    );
    protected final JButton initialSizeButton = ModelerSession.getComponentFactoryService().createButton(
            Resources.getResources().getString("hierarchy.frame.options.scale.100"), null
    );    
    protected JSpinner cellSizeSpinner = ModelerSession.getComponentFactoryService().createSpinner();
    protected final JCheckBox viewGridCheckBox = ModelerSession.getComponentFactoryService().createCheckBox();
    protected final JCheckBox gridCheckBox = ModelerSession.getComponentFactoryService().createCheckBox();
    protected final JSlider scaleSlider = ModelerSession.getComponentFactoryService().createSlider();
    protected final JSpinner scaleSpinner = ModelerSession.getComponentFactoryService().createSpinner();


    public GraphOptionsView(){
        initLayout();

        getSpinnerTextField(cellSizeSpinner).setColumns(GRID_SPINNER_COLUMNS);
        getSpinnerTextField(scaleSpinner).setColumns(SCALE_SPINNER_COLUMNS);

        initScaleSlider();
    }

    private void initScaleSlider() {
        scaleSlider.setMinimum(GraphOptionsModel.MIN_SCALE);
        scaleSlider.setMaximum(GraphOptionsModel.MAX_SCALE);

        scaleSlider.setPaintLabels(true);
        scaleSlider.setMajorTickSpacing(100);
        scaleSlider.setMinorTickSpacing(20);

        final Hashtable table = new Hashtable();
        table.put(new Integer(GraphOptionsModel.MIN_SCALE), ModelerSession.getComponentFactoryService().createLabel("min"));
        table.put(new Integer(GraphOptionsModel.MAX_SCALE), ModelerSession.getComponentFactoryService().createLabel("max"));

        scaleSlider.setLabelTable(table);
    }

    private void initLayout() {
        setBorder(Borders.createEmptyBorder(ComponentFactoryService.DEFAULT_FORM_BORDER));

        final FormLayout layout = new FormLayout(
                /* columns */"pref, 3dlu, pref, pref:grow",
                /* rows */
                    /* scale */" pref, 3dlu, " +
                    /* scale slider */ "pref, 3dlu," +
                    /* scale button */ "pref, 25dlu," +
                    /* grid */  "pref, 3dlu," +
                    /* grid */  "pref, 3dlu," +
                    /* grid */  "pref"
            );

        final CellConstraints cellConstraints = new CellConstraints();
        final PanelBuilder builder = new PanelBuilder(layout);

        int row = 1;
        builder.add(scaleLabel, cellConstraints.xy(1, row));
        builder.add(scaleSpinner, cellConstraints.xy(3, row));

        row += 2;
        builder.add(scaleSlider, cellConstraints.xyw(1, row, layout.getColumnCount()));

        row += 2;
        builder.add(initialSizeButton, cellConstraints.xy(1, row));

        row += 2;
        builder.add(gridLabel, cellConstraints.xy(1, row));
        builder.add(gridCheckBox, cellConstraints.xy(3, row));

        row += 2;
        builder.add(viewGridLabel, cellConstraints.xy(1, row));
        builder.add(viewGridCheckBox, cellConstraints.xy(3, row));

        row += 2;
        builder.add(cellSizeLabel, cellConstraints.xy(1, row));
        builder.add(cellSizeSpinner, cellConstraints.xy(3, row));

        setLayout(new BorderLayout());

        add(builder.getPanel(), BorderLayout.NORTH);
    }

    public JFormattedTextField getSpinnerTextField(final JSpinner spinner) {
        final JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            return ((JSpinner.DefaultEditor)editor).getTextField();
        } else {
            LOG.error("Unexpected editor of JSpinner.");
            return null;
        }
    }

}
