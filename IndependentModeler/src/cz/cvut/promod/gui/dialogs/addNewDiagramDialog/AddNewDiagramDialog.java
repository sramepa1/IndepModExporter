package cz.cvut.promod.gui.dialogs.addNewDiagramDialog;

import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.projectService.utils.ProjectServiceUtils;
import cz.cvut.promod.services.projectService.results.AddProjectItemResult;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagram;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

import org.apache.log4j.Logger;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.PresentationModel;


/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 15:58:45, 24.10.2009
 *
 * The dialog for adding new diagrams into a project.
 */
public class AddNewDiagramDialog extends AddNewDiagramDialogView  {

    private final Logger LOG = Logger.getLogger(AddNewDiagramDialog.class);

    private static final String ILLEGAL_PARENT_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.add.new.diagram.dialog.error.illegalParent");
    private static final String DUPLICITY_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.add.new.diagram.dialog.error.duplicity");
    private static final String ILLEGAL_NOTATION_LABEL =
        ModelerSession.getCommonResourceBundle().getString("modeler.add.new.diagram.dialog.error.illegalNotation");
    private static final String ERROR_LABEL =
        ModelerSession.getCommonResourceBundle().getString("modeler.add.new.diagram.dialog.error");
    private static final String INVALID_NAME_LABEL =
        ModelerSession.getCommonResourceBundle().getString("modeler.add.new.diagram.dialog.error.disallowed");

    private AddNewDiagramDialogModel model;

    private Map<String, ImageIcon> images;

    private PresentationModel<AddNewDiagramDialogModel> presentation;


    public AddNewDiagramDialog(){
        final JFrame frame = ModelerSession.getFrame();
        if(frame != null){
            setLocationRelativeTo(frame);
        }

        setTitle(ModelerSession.getCommonResourceBundle().getString("modeler.add.new.diagram.dialog.title"));

        model = new AddNewDiagramDialogModel();
        presentation = new PresentationModel<AddNewDiagramDialogModel>(model);

        images = new HashMap<String, ImageIcon>();

        initList();

        initBinding();

        initEventHandling();
       
        if(model.getSize() == 0){
            confirmButton.setEnabled(false);
        } else {
            notationsList.setSelectedIndex(0);
        }

        getRootPane().setDefaultButton(confirmButton);

        setVisible(true);
    }

    /**
     * Initialize bindings.
     */
    private void initBinding() {
        Bindings.bind(newDiagramNameTextField, presentation.getModel(AddNewDiagramDialogModel.PROPERTY_NEW_DIAGRAM_NAME));
    }

    /**
     * Initialize list of notations.
     */
    private void initList() {
        notationsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        notationsList.setModel(model);
    }

    /**
     * Initialize event handling.
     */
    private void initEventHandling() {
        notationsList.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {                
                final String notationIdentifier =
                        ((AddNewDiagramDialogModel.NotationLabelWrapper)model.getElementAt(notationsList.getSelectedIndex())).getIdentifier();

                updateDialogView(notationIdentifier);
                model.setSelectedNotationIdentifier(notationIdentifier);
            }
        });

        cancelButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                disposeDialog();
            }
        });

        confirmButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                addDiagram();
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
     * Adds the diagram.
     */
    private void addDiagram() {
        final String notationIdentifier = model.getSelectedNotationIdentifier();
        final String diagramName = model.getNewDiagramName();

        final UUID uuid = UUID.randomUUID();

        final ProjectDiagram projectDiagram = new ProjectDiagram(
                diagramName,
                notationIdentifier,
                uuid,
                ModelerSession.getNotationService().getNotation(notationIdentifier).getDiagramModelFactory().createEmptyDiagramModel()
        );

        AddProjectItemResult result = ModelerSession.getProjectControlService().addDiagram(projectDiagram, true);

        switch(result.getStatus()){
            case SUCCESS:
                disposeDialog();

                ModelerSession.getProjectControlService().synchronize(
                        result.getTreePath(),
                        true, true, false, false
                );

                break;
            case ILLEGAL_PARENT:
                errorLabel.setText(ILLEGAL_PARENT_LABEL);
                break;
            case NAME_DUPLICITY:
                errorLabel.setText(DUPLICITY_LABEL);
                break;
            case INVALID_NAME:
                publicInvalidNameError(diagramName);
                break;
            case ILLEGAL_NOTATION:
                errorLabel.setText(ILLEGAL_NOTATION_LABEL);
                break;
            default:
                errorLabel.setText(ERROR_LABEL);
                LOG.error("No such a choice for add diagram result messages.");
        }
    }

    /**
     * Updates the dialog view when a notation is selected.
     * @param notationIdentifier is the identifier of selected notation.
     */
    private void updateDialogView(final String notationIdentifier) {
        final String text = ModelerSession.getNotationService().getNotation(notationIdentifier).getNotationPreviewText();

        if(text != null){
            notationInfoTextArea.setText(text);
        } else {
            notationInfoTextArea.setText("");
        }

        if(images.containsKey(notationIdentifier)){
            imagePanel.setImage(images.get(notationIdentifier));
        } else {
            final ImageIcon image = ModelerSession.getNotationService().getNotation(notationIdentifier).getNotationPreviewImage();

            images.put(notationIdentifier, image);
            imagePanel.setImage(image);
        }
    }

    /**
     * Hides the dialog.
     */
    private void disposeDialog(){
        setVisible(false);
        dispose();
    }

    /**
     * Publish invalid name error.
     *
     * @param projectName is the project name
     */
    private void publicInvalidNameError(final String projectName) {
        if(!ProjectServiceUtils.isSyntacticallyCorrectName(projectName)){
            errorLabel.setText(INVALID_NAME_LABEL + ProjectServiceUtils.getDisallowedNameSymbols(','));
        }
    }

}