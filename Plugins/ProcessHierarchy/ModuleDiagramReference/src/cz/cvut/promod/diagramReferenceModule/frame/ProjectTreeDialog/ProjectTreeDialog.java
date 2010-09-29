package cz.cvut.promod.diagramReferenceModule.frame.ProjectTreeDialog;

import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagram;
import cz.cvut.promod.hierarchyNotation.modelFactory.vertexes.ProcessVertex;
import cz.cvut.promod.diagramReferenceModule.DiagramReferenceModuleModel;

import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.UUID;

import org.apache.log4j.Logger;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 16:46:55, 16.4.2010
 * 
 * Represents the tree dialog, wherer user can choose a diagram to be opened.
 */
public class ProjectTreeDialog extends JDialog implements TreeSelectionListener{

    private static final Logger LOG = Logger.getLogger(ProjectTreeDialog.class);

    private final JTree tree = ModelerSession.getComponentFactoryService().createTree();

    private final Dimension DIMENSION = new Dimension(400, 600);

    private final ProcessVertex processVertex;

    private final JButton okButton = ModelerSession.getComponentFactoryService().createButton(
            ModelerSession.getCommonResourceBundle().getString("modeler.ok"), null
    );

    private final JButton cancelButton = ModelerSession.getComponentFactoryService().createButton(
            ModelerSession.getCommonResourceBundle().getString("modeler.cancel"), null
    );

    private UUID uuid;
    private ProjectDiagram projectDiagram;


    public ProjectTreeDialog(final ProcessVertex processVertex) {
        setModal(true);
        
        setSize(DIMENSION);

        setTitle(DiagramReferenceModuleModel.DIALOG_TITLE);

        this.processVertex = processVertex;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initEventHandling();

        okButton.setEnabled(false);

        initLayout();

        initTreeData();

        if(ModelerSession.getFrame() != null){
            setLocationRelativeTo(ModelerSession.getFrame());
        }

        tree.expandRow(1);

        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        tree.setRootVisible(false);
        
        setVisible(true);
    }

    private void initEventHandling() {
        tree.addTreeSelectionListener(this);

        okButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if((uuid != null) && (processVertex != null)){
                    processVertex.setUuid(uuid);
                    processVertex.setName(projectDiagram.getDisplayName());

                    ModelerSession.getProjectControlService().registerDiagramListener(
                            projectDiagram,
                            processVertex
                    );
                }

                disposeDialog();
            }
        });

        cancelButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
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

    private void disposeDialog(){
        setVisible(false);
        dispose();
    }

    private void initTreeData() {
        tree.setModel(ModelerSession.getProjectControlService().getProjectTreeModel());
    }

    private void initLayout() {
        setLayout(new BorderLayout());

        final JPanel buttonPanel = ModelerSession.getComponentFactoryService().createPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);        

        add(ModelerSession.getComponentFactoryService().createScrollPane(tree), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void valueChanged(TreeSelectionEvent e) {
        okButton.setEnabled(false);
        uuid = null;
        this.projectDiagram = null;

        final TreePath selectedTreePath = e.getNewLeadSelectionPath();

        if(selectedTreePath != null){
            try{
                final DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedTreePath.getLastPathComponent();

                if(node.getUserObject() instanceof ProjectDiagram){
                    final ProjectDiagram projectDiagram = (ProjectDiagram) node.getUserObject();

                    if(!ModelerSession.getProjectService().getSelectedDiagram().getUuid().equals(projectDiagram.getUuid())){
                    okButton.setEnabled(true);

                    uuid = projectDiagram.getUuid();
                    this.projectDiagram = projectDiagram;
                }
                }
            } catch (Exception exception){
                LOG.error("Invalid project root item.");
            }
        }
    }
}
