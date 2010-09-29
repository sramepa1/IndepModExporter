package cz.cvut.promod.gui.dialogs.pluginsOverview;

import cz.cvut.promod.gui.dialogs.pluginsOverview.notations.NotationOverviewTab;
import cz.cvut.promod.gui.dialogs.pluginsOverview.extensions.ExtensionOverviewTab;
import cz.cvut.promod.services.ModelerSession;

import javax.swing.*;
import java.awt.event.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 16:38:19, 11.2.2010
 *
 * Dialog for plugin overview.
 */
public class PluginsOverviewDialogDialog extends PluginsOverviewDialogView {

    public static final String TITLE =
            ModelerSession.getCommonResourceBundle().getString("modeler.plugins.overview");    
    public static final String NOTATION_AND_MODULE_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.plugins.overview.notations.title");
    public static final String EXTENSIONS_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.plugins.overview.extension.title");
    public static final String IDENTIFIER_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.plugins.overview.identifier");
    public static final String NAME_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.plugins.overview.name");
    public static final String DESCRIPTION_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.plugins.overview.description");
    public static final String ABBREVIATION_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.plugins.overview.abbreviation");
    public static final String FULL_NAME_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.plugins.overview.full.name");
    public static final String TOOL_TIP_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.plugins.overview.tool.tip");    
    public static final String FILE_EXTENSION_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.plugins.overview.file.ext");
    public static final String EXTENSION_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.plugin.extension");
    public static final String NOTATION_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.plugin.notation");
    public static final String MODULE_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.plugin.module");    

    public PluginsOverviewDialogDialog(){
        initTabbedPane();

        setModal(true);
        
        setTitle(TITLE);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(ModelerSession.getFrame());

        initEventHandling();

        setVisible(true);
    }

    private void initEventHandling() {
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                disposeDialog();
            }
        });

        getRootPane().registerKeyboardAction(new ActionListener(){
                    public void actionPerformed(ActionEvent actionEvent) {
                        disposeDialog();
                            }
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);        
    }

    /**
     * Hides the dialog.
     */
    private void disposeDialog(){
        setVisible(false);
        dispose();
    }

    private void initTabbedPane() {
        tabbedPane.addTab(NOTATION_AND_MODULE_LABEL, null, new NotationOverviewTab());
        tabbedPane.addTab(EXTENSIONS_LABEL, null, new ExtensionOverviewTab());
    }

}
