package cz.cvut.promod.services.componentFactoryService;

import com.jidesoft.swing.JideButton;
import com.jidesoft.grid.PropertyTable;
import com.jidesoft.status.StatusBar;
import com.jidesoft.status.LabelStatusBarItem;
import com.jidesoft.status.ButtonStatusBarItem;
import com.jidesoft.pane.OutlookTabbedPane;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import java.awt.*;

import cz.cvut.promod.services.Service;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 21:13:35, 5.1.2010
 */

/**
 * All Plugins and even the Modeler are supposed to use ComponentFactoryClass for creation of GUI components
 * (including Swing components and JideSoft components).
 *
 * This programming style should ensure some kind of visual consistency in ProMod application.
 */
public interface ComponentFactoryService extends Service {

    /**
     * Defines the recommended form border. Usually for usage in JGoodies.Borders factory.
     * @see com.jgoodies.forms.factories.Borders
     */
    public static final String DEFAULT_FORM_BORDER = "5dlu, 5dlu, 5dlu, 5dlu";    


    /** Defines horizontal orientation of an instance of JideButton class */
    public static final int JIDE_BUTTON_HORIZONTAL_ORIENTATION = 0;

    /** Defines vertical orientation of an instance of JideButton class */
    public static final int JIDE_BUTTON_VERTICAL_ORIENTATION = 1;

    /**
     * @return an instance of JComboBox class
     */
    public JComboBox createComboBox();

    /**
     * @return an instance of JSpinner class
     */
    public JSpinner createSpinner();

    /**
     * @return an instance of JTextArea class
     */
    public JTextArea createTextArea();

    /**
     * @return an instance of JTextField class
     */
    public JTextField createTextField();

    /**
     * @param text to be displayed in the JTextField
     * @return an instance of JTextField class
     */
    public JTextField createTextField(final String text);


    /**
     * @return an instance of JSlider class
     */
    public JSlider createSlider();

    /**
     * @param text is a text to be displayed on the Label
     * @return an instance of Label class
     */
    public JLabel createLabel(final String text);

    /**
     * @return an instance of JTree class
     */
    public JTree createTree();

    /**
     * @return an instance of JTree class
     */
    public JTree createTree(final TreeNode node);

    /**
     * @param component is a component to be inserted to the ScrollPane
     * @return an instance of ScrollPane class
     */
    public JScrollPane createScrollPane(final Component component);

    /**
     * @return an instance of JCheckBox class
     */
    public JCheckBox createCheckBox();

    /**
     * @return an instance of MenuBar class
     */
    public JMenuBar createMenuBar();

    /**
     * @return an instance of JToolBar class
     */
    public JToolBar createToolBar();

    /**
     * @param text is a text to be displayed on the Menu
     * @return an instance of Menu class
     */
    public JMenu createMenu(final String text);

    /**
     * @return an instance of JPopupMenu class
     */
    public JPopupMenu createPopupMenu();

    /**
     * @return an instance of OutlookTabbedPane class.
     */
    public OutlookTabbedPane createOutlookTabbedPane();

    /**
     * @param text is a text to be displayed on the MenuItem
     * @return an instance of MenuItem class
     */
    public JMenuItem createMenuItem(final String text);

    /**
     * @param text is a text to be displayed on the Button
     * @param icon is a icon to be displayed on the Button
     * @return an instance of Button class
     */
    public JButton createButton(final String text, final Icon icon);

    /**
     * @param text is a text to be displayed on the ToggleButton
     * @param icon is a icon to be displayed on the ToggleButton
     * @return an instance of ToggleButton class
     */
    public JToggleButton createToggleButton(final String text, final Icon icon);

    /**
     *
     * @param text is a text to be displayed on the button
     * @param icon is an icon to be displayed on the button
     * @param orientation JIDE_BUTTON_HORIZONTAL_ORIENTATION or JIDE_BUTTON_VERTICAL_ORIENTATION
     * @return an instance of JideButton class
     */
    public JideButton createJideButton( final String text, final Icon icon, int orientation );

    /**
     * @return an instance of JPanel class
     */
    public JPanel createPanel();

    /**
     * @return an instance of JList class
     */
    public JList createList();

    /**
     * @return an instance of JTable class
     */
    public JTable createTable();

    /**
     * @return an instance of PropertyTable class
     */
    public PropertyTable createPropertyTable();

    /**
     * @see com.jidesoft.status.StatusBar
     *
     * @return an instance of StatusBar class
     */
    public StatusBar createStatusBar();

    /**
     * @see com.jidesoft.status.LabelStatusBarItem
     *
     * @return an instance of LabelStatusBarItem class
     */
    public LabelStatusBarItem createLabelStatusBarItem();

    /**
     * @see com.jidesoft.status.ButtonStatusBarItem
     *
     * @return an instance of ButtonStatusBarItem class
     */
    public ButtonStatusBarItem createButtonStatusBarItem();

     /**
     * @return an instance of JColorChooser class
     */
    public JColorChooser createColorChooser();

     /**
     * @param text is the text for the button
     * @return an instance of JRadioButton class
     */
    public JRadioButton createRadioButton(final String  text);

     /**
     * @return an instance of JTabbedPane class
     */
    public JTabbedPane createTabbedPane();
}
