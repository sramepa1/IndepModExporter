package cz.cvut.promod.epc.frames.vertexInfo;

import cz.cvut.promod.services.ModelerSession;

import javax.swing.*;
import java.awt.*;

import com.jidesoft.grid.PropertyTable;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 0:20:55, 8.12.2009
 *
 * The View component for the Info dockable frame. 
 */
public class VertexInfoView extends JPanel {

    protected final PropertyTable table = ModelerSession.getComponentFactoryService().createPropertyTable();    

    public VertexInfoView(){
        initLayout();
    }

    private void initLayout() {
        setLayout(new BorderLayout());

        add(ModelerSession.getComponentFactoryService().createScrollPane(table));
    }

}
