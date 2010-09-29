package cz.cvut.promod.diagramReferenceModule.frame;

import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.componentFactoryService.ComponentFactoryService;
import cz.cvut.promod.diagramReferenceModule.DiagramReferenceModuleModel;

import javax.swing.*;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.factories.Borders;


/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 16:36:20, 16.4.2010
 *
 * The view component of the Diagram Reference dockable frame.
 */
public class ReferenceFrameView extends JPanel {

    protected final JLabel connectedDiagramLabel = ModelerSession.getComponentFactoryService().createLabel("");
    protected final JLabel connectedDiagramIDLabel = ModelerSession.getComponentFactoryService().createLabel("");

    protected final JButton goToDiagramButton = ModelerSession.getComponentFactoryService().createButton(
            DiagramReferenceModuleModel.GO_TO_DIAGRAM, null);
    protected final JButton connectDiagramButton = ModelerSession.getComponentFactoryService().createButton(
            DiagramReferenceModuleModel.CONNECT_DIAGRAM, null);
    protected final JButton removeConnectionButton = ModelerSession.getComponentFactoryService().createButton(
            DiagramReferenceModuleModel.REMOVE_CONNECTION_DIAGRAM, null);

    public ReferenceFrameView() {
        initLayout();
    }

    private void initLayout() {
        setBorder(Borders.createEmptyBorder(ComponentFactoryService.DEFAULT_FORM_BORDER));

        goToDiagramButton.setEnabled(false);
        connectDiagramButton.setEnabled(false);
        removeConnectionButton.setEnabled(false);

        setLayout(new FormLayout(
                "pref:grow",
                "pref, 3dlu, pref, 10dlu, pref, 5dlu, pref, 5dlu, pref"
        ));
        final CellConstraints cellConstraints = new CellConstraints();

        add(connectedDiagramLabel, cellConstraints.xy(1,1));
        add(connectedDiagramIDLabel, cellConstraints.xy(1,3));
        add(goToDiagramButton, cellConstraints.xy(1,5));
        add(connectDiagramButton, cellConstraints.xy(1,7));
        add(removeConnectionButton, cellConstraints.xy(1,9));
    }
}
