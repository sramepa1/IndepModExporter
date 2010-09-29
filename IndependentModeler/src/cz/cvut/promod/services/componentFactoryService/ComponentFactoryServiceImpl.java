package cz.cvut.promod.services.componentFactoryService;

import cz.cvut.promod.services.Service;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import java.awt.*;

import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideToggleButton;
import com.jidesoft.swing.ButtonStyle;
import com.jidesoft.grid.PropertyTable;
import com.jidesoft.pane.OutlookTabbedPane;
import com.jidesoft.status.StatusBar;
import com.jidesoft.status.LabelStatusBarItem;
import com.jidesoft.status.ButtonStatusBarItem;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 16:21:11, 10.10.2009
 */
public class ComponentFactoryServiceImpl implements Service, ComponentFactoryService{

    /** {@inheritDoc} */
    public boolean check() {
        // no tests required for this service
        return true;
    }

    /** {@inheritDoc} */
    public JComboBox createComboBox(){
        return new JComboBox();
    }

    /** {@inheritDoc} */
    public JSpinner createSpinner(){
        return new JSpinner();
    }

    /** {@inheritDoc} */
    public JTextArea createTextArea(){
        return new JTextArea();
    }

    /** {@inheritDoc} */
    public JTextField createTextField(){
        return new JTextField();
    }

    /** {@inheritDoc} */
    public JTextField createTextField(final String text){
        return new JTextField(text);
    }

    /** {@inheritDoc} */
    public JSlider createSlider(){
        return new JSlider();
    }

    /** {@inheritDoc} */
    public JLabel createLabel(final String text){
        return new JLabel(text);
    }

    /** {@inheritDoc} */
    public JTree createTree(){
        return new JTree();
    }

    /** {@inheritDoc} */
    public JTree createTree(final TreeNode node) {
        return new JTree(node);
    }

    /** {@inheritDoc} */
    public JScrollPane createScrollPane(final Component component){
        return new JScrollPane(component);       
    }

    /** {@inheritDoc} */
    public JCheckBox createCheckBox(){
        return new JCheckBox();
    }

    /** {@inheritDoc} */
    public JMenuBar createMenuBar(){
        return new JMenuBar();
    }

    /** {@inheritDoc} */
    public JToolBar createToolBar(){
        return new JToolBar();
    }

    /** {@inheritDoc} */
    public JMenu createMenu(final String text){
        return new JMenu(text);
    }

    /** {@inheritDoc} */
    public JPopupMenu createPopupMenu(){
        return new JPopupMenu();
    }

    /** {@inheritDoc} */
    public JMenuItem createMenuItem(final String text){
        return new JMenuItem(text);
    }

    /** {@inheritDoc} */
    public JButton createButton(final String text, final Icon icon){
        return new JButton(text, icon);
    }

    /** {@inheritDoc} */
    public JToggleButton createToggleButton(final String text, final Icon icon){
        return new JToggleButton(text, icon);
    }

    /** {@inheritDoc} */
    public JideButton createJideButton( final String text, final Icon icon, int orientation ){
        JideToggleButton button = new JideToggleButton(text, icon);
        button.setButtonStyle(ButtonStyle.TOOLBOX_STYLE);
        button.setOrientation(orientation);

        return button;
    }

    /** {@inheritDoc} */
    public JPanel createPanel(){
        return new JPanel();
    }

    /** {@inheritDoc} */
    public JList createList(){
        return new JList();
    }

    /** {@inheritDoc} */
    public JTable createTable(){
        return new JTable();
    }

    /** {@inheritDoc} */
    public PropertyTable createPropertyTable() {
        return new PropertyTable();
    }

    public OutlookTabbedPane createOutlookTabbedPane() {
        return new OutlookTabbedPane();
    }

    /** {@inheritDoc} */
    public StatusBar createStatusBar() {
        return new StatusBar();
    }

    /** {@inheritDoc} */
    public LabelStatusBarItem createLabelStatusBarItem() {
        return new LabelStatusBarItem();
    }

    /** {@inheritDoc} */
    public ButtonStatusBarItem createButtonStatusBarItem() {
        return new ButtonStatusBarItem();
    }

    /** {@inheritDoc} */
    public JColorChooser createColorChooser() {
        return new JColorChooser();
    }

    /** {@inheritDoc} */
    public JRadioButton createRadioButton(final String text) {
        return new JRadioButton(text);
    }

    /** {@inheritDoc} */
    public JTabbedPane createTabbedPane() {
        return new JTabbedPane();
    }
}
