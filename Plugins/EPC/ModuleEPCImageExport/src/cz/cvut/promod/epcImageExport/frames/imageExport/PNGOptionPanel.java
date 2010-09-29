package cz.cvut.promod.epcImageExport.frames.imageExport;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import javax.swing.*;

import cz.cvut.promod.services.componentFactoryService.ComponentFactoryService;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.epcImageExport.resources.Resources;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 20:09:17, 14.12.2009
 * 
 * Option panel for the png export.
 */
public class PNGOptionPanel extends JPanel {

    private JLabel insetLabel = ModelerSession.getComponentFactoryService().createLabel(
            Resources.getResources().getString(ImageExportModel.PNG_OPTIONS_INSET_RES)            
    );

    protected JSpinner insetSpinner = ModelerSession.getComponentFactoryService().createSpinner();

    public PNGOptionPanel(final SpinnerNumberModel insetModel){
        insetSpinner.setModel(insetModel);        

        initLayout();
    }

    private void initLayout() {
        setBorder(Borders.createEmptyBorder(ComponentFactoryService.DEFAULT_FORM_BORDER));

        setLayout(new FormLayout(
                "pref, 3dlu, pref",
                "pref"
        ));
        final CellConstraints cellConstraints = new CellConstraints();

        add(insetLabel, cellConstraints.xy(1,1));
        add(insetSpinner, cellConstraints.xy(3,1));
    }
}
