package cz.cvut.promod.gui.dialogs.addNewDiagramDialog;

import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.componentFactoryService.ComponentFactoryService;

import javax.swing.*;
import java.awt.*;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 15:58:50, 24.10.2009
 *
 * The view component for AddNewDiagramDialog.
 */
public class AddNewDiagramDialogView extends JDialog {

    private static final Dimension INIT_IMAGE_SIZE = new Dimension(400, 201);
    private static final Dimension INIT_SIZE = new Dimension(750, 470);

    private static final int BORDER_LAYOUT_HGAP = 0;
    private static final int BORDER_LAYOUT_VGAP = 10;

    protected final JLabel notationsListLabel = ModelerSession.getComponentFactoryService().createLabel(
            ModelerSession.getCommonResourceBundle().getString("modeler.add.new.diagram.dialog.tableTitle"));

    protected final JList notationsList = ModelerSession.getComponentFactoryService().createList();

    private final JLabel newDiagramNameLabel = ModelerSession.getComponentFactoryService().createLabel(
            ModelerSession.getCommonResourceBundle().getString("modeler.add.new.diagram.dialog.nameTitle"));

    protected final JTextField newDiagramNameTextField = ModelerSession.getComponentFactoryService().createTextField();

    protected final JTextArea notationInfoTextArea = ModelerSession.getComponentFactoryService().createTextArea();

    protected JButton confirmButton = ModelerSession.getComponentFactoryService().createButton(
            ModelerSession.getCommonResourceBundle().getString("modeler.add.new.diagram.dialog.confirm"), null);

    protected JButton cancelButton = ModelerSession.getComponentFactoryService().createButton(
            ModelerSession.getCommonResourceBundle().getString("modeler.cancel"), null);

    protected final JLabel errorLabel = ModelerSession.getComponentFactoryService().createLabel("");

    protected final ImagePanel imagePanel = new ImagePanel();

    
    protected AddNewDiagramDialogView(){
        setModal(true);

        errorLabel.setForeground(Color.RED);

        notationInfoTextArea.setLineWrap(true);
        notationInfoTextArea.setEditable(false);

        notationsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        initLayout();
    }

    private void initLayout() {
        setSize(INIT_SIZE);

        final JPanel centerPanel = ModelerSession.getComponentFactoryService().createPanel();
        centerPanel.setBorder(Borders.createEmptyBorder(ComponentFactoryService.DEFAULT_FORM_BORDER));
        centerPanel.setLayout(new BorderLayout());

        final JPanel centerWestPanel = ModelerSession.getComponentFactoryService().createPanel();
        centerWestPanel.setBorder(Borders.createEmptyBorder(ComponentFactoryService.DEFAULT_FORM_BORDER));
        centerWestPanel.setLayout(new BorderLayout());

        final JPanel centerSouthPanel = ModelerSession.getComponentFactoryService().createPanel();
        centerSouthPanel.setBorder(Borders.createEmptyBorder(ComponentFactoryService.DEFAULT_FORM_BORDER));
        centerSouthPanel.setLayout(new FormLayout("max(pref; 100dlu):grow", "pref, 3dlu, pref"));

        final CellConstraints cellConstraints = new CellConstraints();

        centerSouthPanel.add(newDiagramNameLabel, cellConstraints.xy(1,1));
        centerSouthPanel.add(newDiagramNameTextField, cellConstraints.xy(1,3));

        centerWestPanel.add(notationsListLabel, BorderLayout.NORTH);
        centerWestPanel.add(ModelerSession.getComponentFactoryService().createScrollPane(notationsList), BorderLayout.CENTER);
        centerWestPanel.add(centerSouthPanel, BorderLayout.SOUTH);

        final JPanel centerCenterPanel = ModelerSession.getComponentFactoryService().createPanel();
        centerCenterPanel.setBorder(Borders.createEmptyBorder(ComponentFactoryService.DEFAULT_FORM_BORDER));
        centerCenterPanel.setLayout(new BorderLayout(BORDER_LAYOUT_HGAP, BORDER_LAYOUT_VGAP));

        centerCenterPanel.add(imagePanel, BorderLayout.NORTH);
        centerCenterPanel.add(ModelerSession.getComponentFactoryService().createScrollPane(notationInfoTextArea), BorderLayout.CENTER);

        centerPanel.add(centerWestPanel, BorderLayout.WEST);
        centerPanel.add(centerCenterPanel, BorderLayout.CENTER);

        final JPanel southPanel = ModelerSession.getComponentFactoryService().createPanel();
        southPanel.setBorder(Borders.createEmptyBorder(ComponentFactoryService.DEFAULT_FORM_BORDER));

        southPanel.setLayout(new FormLayout(
                "pref:grow, pref, 3dlu, pref, pref:grow",
                "pref, 5dlu, pref"));

        southPanel.add(errorLabel, cellConstraints.xyw(1,1,5));

        southPanel.add(confirmButton, cellConstraints.xy(2, 3));
        southPanel.add(cancelButton, cellConstraints.xy(4, 3));

        setLayout(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    protected  static class ImagePanel extends JPanel{

        private final int POSITION_Y = 0;

        private int position_x = 0;

        private ImageIcon image = null;

        public ImagePanel(){
            setPreferredSize(INIT_IMAGE_SIZE);
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int recWidth;
            int recHeight;

            if(image != null){
                position_x = this.getWidth() - image.getIconWidth();
                position_x = position_x > 0 ? position_x / 2 : 0;
                position_x = Math.max(1, position_x);

                recWidth = image.getIconWidth();
                recHeight = image.getIconHeight();

                g.drawImage(image.getImage(), position_x, POSITION_Y, null);

            } else {
                position_x = this.getWidth() - (int)INIT_IMAGE_SIZE.getWidth();
                position_x = position_x > 0 ? position_x / 2 : 0;
                position_x = Math.max(1, position_x);

                recWidth = (int)INIT_IMAGE_SIZE.getWidth();
                recHeight = (int)INIT_IMAGE_SIZE.getHeight() - 1;

                //over-paint the old image
                g.setColor(Color.WHITE);
                g.fillRect(position_x, POSITION_Y, recWidth, recHeight);
            }

            g.setColor(Color.BLACK);
            g.drawRect(position_x, POSITION_Y, recWidth, recHeight);
        }

        public void setImage(final ImageIcon image){
            this.image = image;
            repaint();
        }
    }


}
