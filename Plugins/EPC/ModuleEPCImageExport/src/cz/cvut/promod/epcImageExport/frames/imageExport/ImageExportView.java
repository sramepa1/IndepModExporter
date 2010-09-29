package cz.cvut.promod.epcImageExport.frames.imageExport;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import javax.swing.*;
import java.awt.*;

import cz.cvut.promod.services.componentFactoryService.ComponentFactoryService;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.epcImageExport.resources.Resources;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 23:48:43, 13.12.2009
 *
 * The view component of the Image Export dockable frame.
 */
public class ImageExportView extends JPanel {

    protected final JPanel optionsPanel = ModelerSession.getComponentFactoryService().createPanel();

    private final JLabel formatLabel = ModelerSession.getComponentFactoryService().createLabel(
            Resources.getResources().getString(ImageExportModel.EXPORT_AS_LABEL_RES)
    );

    protected JComboBox formatComboBox = ModelerSession.getComponentFactoryService().createComboBox(); 

    protected final JButton exportButton = ModelerSession.getComponentFactoryService().createButton(
            Resources.getResources().getString(ImageExportModel.EXPORT_BUTTON_RES), null
    );

    protected final CardLayout cardLayout = new CardLayout();

    public ImageExportView() {
        initLayout();
    }

    private void initLayout() {
        optionsPanel.setLayout(cardLayout);

        setBorder(Borders.createEmptyBorder(ComponentFactoryService.DEFAULT_FORM_BORDER));

        setLayout(new FormLayout(
                "pref, 3dlu, pref, 3dlu, pref, pref:grow",
                "pref, 3dlu, fill:pref:grow"
        ));
        final CellConstraints cellConstraints = new CellConstraints();

        add(formatLabel, cellConstraints.xy(1,1));
        add(formatComboBox, cellConstraints.xy(3,1));
        add(exportButton, cellConstraints.xy(5,1));

        add(optionsPanel, cellConstraints.xyw(1,3,6));
    }

    /**
     * Adds the format option components.
     *
     * @param key is the unique key for the card layout
     * @param panel is the option component
     */
    public void registerOptionPanel(final String key, JPanel panel){
        optionsPanel.add(panel, key);
    }
}
