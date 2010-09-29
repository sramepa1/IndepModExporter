package cz.cvut.promod.gui.dialogs.pluginsOverview;

import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.componentFactoryService.ComponentFactoryService;

import javax.swing.*;
import java.awt.*;

import com.jgoodies.forms.factories.Borders;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 16:38:25, 11.2.2010
 *
 * Implementation of the PluginsOverview dialog.
 */
public class PluginsOverviewDialogView extends JDialog {

    public static int HGAP = 10;
    public static int VGAP = 10;

    public static int LIST_WIDTH = 200;

    private static final Dimension DIMENSION = new Dimension(900, 600);

    protected JTabbedPane tabbedPane = ModelerSession.getComponentFactoryService().createTabbedPane(); 


    public PluginsOverviewDialogView(){
        setSize(DIMENSION);

        initLayout();
    }

    private void initLayout() {
        final JPanel panel = ModelerSession.getComponentFactoryService().createPanel();
        panel.setBorder(Borders.createEmptyBorder(ComponentFactoryService.DEFAULT_FORM_BORDER));

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        panel.setLayout(new BorderLayout());
        panel.add(tabbedPane);
    }

}
