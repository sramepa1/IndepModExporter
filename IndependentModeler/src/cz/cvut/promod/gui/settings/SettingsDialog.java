package cz.cvut.promod.gui.settings;

import com.jidesoft.dialog.*;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.PartialEtchedBorder;

import java.awt.*;
import java.awt.event.*;

import cz.cvut.promod.services.ModelerSession;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.border.EtchedBorder;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 14:47:40, 24.1.2010
 */

/**
 * Implementation of the common settings dialog.
 */
public class SettingsDialog extends MultiplePageDialog{

    private static final String OK_LABEL = ModelerSession.getCommonResourceBundle().getString("modeler.ok");
    private static final String CANCEL_LABEL = ModelerSession.getCommonResourceBundle().getString("modeler.cancel");
    private static final String APPLY_LABEL = ModelerSession.getCommonResourceBundle().getString("modeler.apply");

    private static final int INIT_WIDTH = 800;
    private static final int INIT_HEIGHT = 600;

    public static final String TITLE_LABEL = ModelerSession.getCommonResourceBundle().getString("modeler.settings");

    private final  AbstractAction cancelAction;
    private final  AbstractAction okAction;
    private final AbstractAction applyAction;
    

    public SettingsDialog(final Frame parentFrame, final String title){
        super(parentFrame, title);

        applyAction = new AbstractAction(APPLY_LABEL) {
            public void actionPerformed(ActionEvent e) {
                fireButtonEvent(ButtonNames.APPLY);
                setEnabled(false);
            }
        };

        cancelAction = new AbstractAction(CANCEL_LABEL) {
            public void actionPerformed(ActionEvent e) {
                fireButtonEvent(ButtonNames.CANCEL);
                setDialogResult(RESULT_CANCELLED);
                setVisible(false);
                applyAction.setEnabled(false);
                dispose();
            }
        };

        okAction = new AbstractAction(OK_LABEL) {
            public void actionPerformed(ActionEvent e) {
                fireButtonEvent(ButtonNames.OK);
                setDialogResult(RESULT_AFFIRMED);
                setVisible(false);
                applyAction.setEnabled(false);
                dispose();
            }
        };

        getRootPane().registerKeyboardAction(new ActionListener(){
                    public void actionPerformed(ActionEvent actionEvent) {
                       cancelAction.actionPerformed(null);
                    }
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        initEventHandling();
    }

    @Override
    protected JTree createTree(DefaultMutableTreeNode root) {
        return ModelerSession.getComponentFactoryService().createTree(root);
    }

    private void initEventHandling() {
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                cancelAction.actionPerformed(null);
            }
        });
    }


    @Override
    public ButtonPanel createButtonPanel() {
        final ButtonPanel buttonPanel = super.createButtonPanel();

        applyAction.setEnabled(false);

        ((JButton) buttonPanel.getButtonByName(ButtonNames.OK)).setAction(okAction);
        ((JButton) buttonPanel.getButtonByName(ButtonNames.CANCEL)).setAction(cancelAction);
        ((JButton) buttonPanel.getButtonByName(ButtonNames.APPLY)).setAction(applyAction);

        setDefaultAction(okAction);

        return buttonPanel;
    }

    /**
     * When an button of settings dialog has been pressed (i.g. OK, CANCEL, ...) all pages the has a registered
     * instance of ButtonListener class will be notified about this event.
     *
     * @param buttonName is the name of the button that has been pressed
     */
    private void fireButtonEvent(final String buttonName){
        final PageList pageList = getPageList();

        for(int i = 0; i < pageList.getSize(); i++){
            final AbstractDialogPage page = pageList.getPage(i);

            page.fireButtonEvent(0, buttonName);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(INIT_WIDTH, INIT_HEIGHT);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        getContentPanel().setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        getIndexPanel().setBackground(Color.white);

        getButtonPanel().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        getPagesPanel().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 10, 0, 0),
                new PartialEtchedBorder(EtchedBorder.LOWERED, PartialEtchedBorder.SOUTH)));
    }

    /**
     * Shows a settings dialog.
     *
     * @param settingPagesModel is the list of settings pages defined by plugins
     */
    public static void showOptionsDialog(final PageList settingPagesModel) {
        final PageList pages = new PageList();

        for(int i = 0; i < settingPagesModel.getPageCount(); i++){
            pages.append(settingPagesModel.getPage(i));
        }

        final MultiplePageDialog dialog = new SettingsDialog(ModelerSession.getFrame(), TITLE_LABEL);

        dialog.setStyle(MultiplePageDialog.TREE_STYLE);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);       

        dialog.setPageList(pages);                

        dialog.pack();
        JideSwingUtilities.globalCenterWindow(dialog);
        dialog.setVisible(true);
    }

}
