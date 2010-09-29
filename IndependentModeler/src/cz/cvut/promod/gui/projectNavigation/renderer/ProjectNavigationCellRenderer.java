package cz.cvut.promod.gui.projectNavigation.renderer;

import org.apache.log4j.Logger;

import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.HashMap;

import cz.cvut.promod.services.projectService.treeProjectNode.ProjectRoot;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectSubFolder;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagram;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.resources.Resources;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 17:26:52, 30.11.2009
 */

/**
 * Tree node cell renderer that renders special icons for the node that are project roots,
 * special ones for nodes that are project subfolderes and special nodes for nodes, that are
 * project diagrams. Diagrams can have special icon related to it's notation.
 */
public class ProjectNavigationCellRenderer extends DefaultTreeCellRenderer{

    private static final Logger LOG = Logger.getLogger(ProjectNavigationCellRenderer.class);

    private final static Map<String, Icon> iconMap; // holds notation specific icons 

    private final static ImageIcon projectRootIcon;
    private final static ImageIcon projectSubfolderIcon;
    private final static ImageIcon defaultProjectDiagramIcon; // default diagram icon, if no notation specific is set

    // static constructor
    static{
        projectRootIcon = Resources.getIcon(Resources.NAVIGATION + Resources.ICONS + Resources.PROJECT_ICON);
        projectSubfolderIcon = Resources.getIcon(Resources.NAVIGATION + Resources.ICONS + Resources.SUBFOLDER_ICON);
        defaultProjectDiagramIcon = Resources.getIcon(Resources.NAVIGATION + Resources.ICONS + Resources.DIAGRAM_ICON);

        iconMap = new HashMap<String , Icon>();
    }

    @Override
    public Component getTreeCellRendererComponent(final JTree tree,
                                                  final Object object,
                                                  final boolean selected,
                                                  final boolean expanded,
                                                  final boolean leaf,
                                                  final int row,
                                                  final boolean hasFocus) {

        final Component component =  super.getTreeCellRendererComponent(
                tree, object, selected, expanded, leaf,row, hasFocus
        );

        if(component instanceof JLabel){
            if(isProjectRoot(object)){
                setFont(new Font(getFont().getName(), Font.BOLD, getFont().getSize()));
                setIcon(projectRootIcon);

                final JLabel label = (JLabel) component;
                label.setToolTipText(null);

            } else if(isProjectSubfolder(object)){
                setFont(new Font(getFont().getName(), Font.PLAIN, getFont().getSize()));
                setIcon(projectSubfolderIcon);

                final JLabel label = (JLabel) component;
                label.setToolTipText(null);

            } else if(isProjectDiagram(object)){
                setFont(new Font(getFont().getName(), Font.PLAIN, getFont().getSize()));
                setIcon(getProjectDiagramIcon(object));

                try{
                    final ProjectDiagram projectDiagram = (ProjectDiagram) getTreeNode(object).getUserObject();
                    final String toolTip = ModelerSession.getNotationService().getNotation(projectDiagram.getNotationIdentifier()).getNotationToolTip();

                    final JLabel label = (JLabel) component;
                    if(toolTip != null){
                        label.setToolTipText(toolTip);
                    } else {
                        label.setToolTipText(null);
                    }

                } catch (Exception e){
                    LOG.error("Not possible to set the tree item tool tip");
                }
            }

        } else{ 
            LOG.error("Project navigation tree cell component is not an instance of JLabel. Wrong icons can occurred in project navigation.");
        }

        return component;
    }

    /**
     * @param object is the node (DefaultMutableTreeNode)
     * @return the project diagram icon specified by the notation
     */
    private Icon getProjectDiagramIcon(final Object object) {
        final ProjectDiagram projectDiagram = (ProjectDiagram) ((DefaultMutableTreeNode)object).getUserObject();
        final String notationIdentifier = projectDiagram.getNotationIdentifier();

        if(iconMap.containsKey(notationIdentifier)){
            return iconMap.get(notationIdentifier);
        }

        Icon icon = ModelerSession.getNotationService().getNotation(notationIdentifier).getNotationIcon();

        if(icon == null){
            icon = defaultProjectDiagramIcon;           
        }

        iconMap.put(notationIdentifier, icon);

        return icon;
    }

    /**
     * @param object is a node
     * @return object casted as DefaultMutableTreeNode if possible, null otherwise
     */
    private DefaultMutableTreeNode getTreeNode(final Object object){
        if(object instanceof DefaultMutableTreeNode){
            return (DefaultMutableTreeNode) object;
        }

        return null;
    }

    /**
     * @param object is a tree node
     * @return true if the node holds the Project Root
     */
    private boolean isProjectRoot(final Object object){
        final DefaultMutableTreeNode node = getTreeNode(object);

        return node.getUserObject() instanceof ProjectRoot;
    }

    /**
     * @param object is a tree node
     * @return true if the node holds the Project Subfolder
     */
    private boolean isProjectSubfolder(final Object object){
        final DefaultMutableTreeNode node = getTreeNode(object);

        return node.getUserObject() instanceof ProjectSubFolder;
    }

    /**
     * @param object is a tree node
     * @return true if the node holds the Project Diagram
     */
    private boolean isProjectDiagram(final Object object){
        final DefaultMutableTreeNode node = getTreeNode(object);

        return node.getUserObject() instanceof ProjectDiagram;
    }
}
