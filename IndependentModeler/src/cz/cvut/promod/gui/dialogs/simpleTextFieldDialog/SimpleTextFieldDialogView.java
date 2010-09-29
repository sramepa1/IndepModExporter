package cz.cvut.promod.gui.dialogs.simpleTextFieldDialog;

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
 * Date: 23:49:51, 23.10.2009
 *
 * A View component of SimpleTextFieldDialog. 
 */
public class SimpleTextFieldDialogView extends JDialog{

    private final Dimension INIT_SIZE = new Dimension(450, 170); 

    protected JLabel textLabel = ModelerSession.getComponentFactoryService().createLabel("");
    protected JTextField inputTextField = ModelerSession.getComponentFactoryService().createTextField();

    protected final JLabel errorLabel = ModelerSession.getComponentFactoryService().createLabel(""); 

    protected JButton confirmButton = ModelerSession.getComponentFactoryService().createButton("", null);
    protected JButton cancelButton = ModelerSession.getComponentFactoryService().createButton("", null);

    protected SimpleTextFieldDialogView(){
        initLayout();

        errorLabel.setForeground(Color.RED);
    }

    private void initLayout() {
        setSize(INIT_SIZE);

        final JPanel centerPanel = ModelerSession.getComponentFactoryService().createPanel();
        centerPanel.setBorder(Borders.createEmptyBorder(ComponentFactoryService.DEFAULT_FORM_BORDER));

        centerPanel.setLayout(new FormLayout(
                "min(pref;2000dlu):grow",
                "pref, 3dlu, pref, 3dlu, pref, 7dlu"
        ));
        final CellConstraints cellConstraints = new CellConstraints();

        centerPanel.add(textLabel, cellConstraints. xy(1, 1));
        centerPanel.add(inputTextField, cellConstraints.xy(1,3));

        centerPanel.add(errorLabel, cellConstraints.xy(1, 5));

        final JPanel southPanel = ModelerSession.getComponentFactoryService().createPanel();
        southPanel.setBorder(Borders.createEmptyBorder(ComponentFactoryService.DEFAULT_FORM_BORDER));

        southPanel.setLayout(new FormLayout(
                "pref:grow, pref, 3dlu, pref, pref:grow",
                "pref"));

        southPanel.add(confirmButton, cellConstraints.xy(2, 1));
        southPanel.add(cancelButton, cellConstraints.xy(4, 1));

        setLayout(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

}
