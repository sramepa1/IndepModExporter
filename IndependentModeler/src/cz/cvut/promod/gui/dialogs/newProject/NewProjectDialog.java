package cz.cvut.promod.gui.dialogs.newProject;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.ValueModel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import cz.cvut.promod.services.projectService.utils.ProjectServiceUtils;
import cz.cvut.promod.services.projectService.ProjectService;
import cz.cvut.promod.services.projectService.results.AddProjectItemResult;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectRoot;
import cz.cvut.promod.services.ModelerSession;
import org.apache.log4j.Logger;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 1:39:54, 20.10.2009
 *
 * The new project dialog.
 */
public class NewProjectDialog extends NewProjectDialogView{

    private final Logger LOG = Logger.getLogger(NewProjectDialog.class);

    private final NewProjectDialogModel model = new NewProjectDialogModel();
    private final PresentationModel<NewProjectDialogModel> presentation = new PresentationModel<NewProjectDialogModel>(model);

    final ValueModel projectNameModel = presentation.getModel(NewProjectDialogModel.PROPERTY_PROJECT_NAME);
    final ValueModel projectLocationModel = presentation.getModel(NewProjectDialogModel.PROPERTY_PROJECT_LOCATION);    

    public static String DUPLICITY_ERROR_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.add.new.project.dialog.error.duplicity");
    public static String EXISTING_PROJECT_FILE_ERROR_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.add.new.project.dialog.error.existing.file");
    public static String GENERAL_ERROR_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.add.new.project.dialog.error.general");
    public static String RELATIVE_PATH_ERROR_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.add.new.project.dialog.error.relative");


    public NewProjectDialog(){
        final JFrame frame = ModelerSession.getFrame();
        if(frame != null){
            setLocationRelativeTo(frame);
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initBinding();
        initEventHandling();

        model.generateInitialProjectName();
        model.generateInitialProjectLocation();

        getRootPane().setDefaultButton(createProjectButton);

        setVisible(true);
    }

    /**
     * Initialize bindings.
     */
    private void initBinding() {
        Bindings.bind(projectNameTextField, projectNameModel);
        Bindings.bind(projectLocationTextField, projectLocationModel);
    }

    /**
     * Initialize event handling.
     */
    private void initEventHandling() {
        projectLocationButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {

                final JFileChooser directoryChooser = new JFileChooser();
                directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                directoryChooser.setCurrentDirectory(new File(model.getProjectLocation()));
                directoryChooser.setMultiSelectionEnabled(false);

                final int returnVal = directoryChooser.showDialog(centerPanel, "create a new project here");

                if(returnVal == directoryChooser.getApproveButtonMnemonic()){
                    model.setProjectLocation(directoryChooser.getSelectedFile().getAbsolutePath());
                }

            }
        });

        cancelButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                closeDialog();
            }
        });

        createProjectButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                final String projectName = model.getProjectName();

                /**
                 *  Test, whether there is no other files with ProMod project file extension.
                 */
                final File projectLocation = new File(model.getProjectLocation());

                // do not take relative paths
                if(!projectLocation.isAbsolute()){
                    errorLabel.setText(RELATIVE_PATH_ERROR_LABEL);
                    return;
                }

                if(projectLocation.exists()){                    
                    for(final File file : projectLocation.listFiles()){
                        if(file.getAbsolutePath().endsWith(ProjectService.PROJECT_FILE_EXTENSION)){
                            LOG.error("Not possible to insert a new project file to an folder, where is already an existing file with project file extension.");
                            errorLabel.setText(EXISTING_PROJECT_FILE_ERROR_LABEL);
                            return;
                        }
                    }
                }

                AddProjectItemResult addProjectItemResult = ModelerSession.getProjectControlService().addProject(
                        new ProjectRoot(projectName, model.getProjectLocation()), true
                );

                switch (addProjectItemResult.getStatus()){
                    case SUCCESS:
                        LOG.info("New project has been created, project name: " + model.getProjectName() + ", project location: " + model.getProjectLocation() + ".");
                        closeDialog();

                        ModelerSession.getProjectControlService().synchronize(
                                addProjectItemResult.getTreePath(),
                                true, false, false, false
                        );
                        
                        return;
                    case NAME_DUPLICITY:
                        LOG.error("Name duplicity error has occurred, " + projectName + ", " + model.getProjectLocation() + ".");
                        errorLabel.setText(DUPLICITY_ERROR_LABEL);
                        break;
                    case INVALID_NAME:
                        publicInvalidNameError(projectName);
                        break;
                    default:
                        LOG.error("Unknown AddProjectItemStatus return value.");
                        errorLabel.setText(GENERAL_ERROR_LABEL);
                }
            }
        });

        projectNameModel.addValueChangeListener(new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent evt) {
                final String projectLocation = (String) projectLocationModel.getValue();
                final String newProjectName = (String) evt.getNewValue();
                final String oldProjectName = (String) evt.getOldValue();

                if(projectLocation != null && projectLocation.endsWith(System.getProperty("file.separator") + oldProjectName)){
                    final String newProjectLocation = projectLocation.substring(0, projectLocation.length() - oldProjectName.length()) + newProjectName;
                    projectLocationModel.setValue(newProjectLocation);
                }
            }
        });

        getRootPane().registerKeyboardAction(new ActionListener(){
                    public void actionPerformed(ActionEvent actionEvent) {
                        setVisible(false);
                        dispose();
                    }
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    /**
     * Shows the invalid name error
     * @param projectName is the project name
     */
    private void publicInvalidNameError(final String projectName) {
        if(!ProjectServiceUtils.isSyntacticallyCorrectName(projectName)){
            errorLabel.setText(
                    ModelerSession.getCommonResourceBundle().getString("modeler.add.new.project.dialog.error.disallowed") +
                            ProjectServiceUtils.getDisallowedNameSymbols(',')
            );
        } else {
            errorLabel.setText(ModelerSession.getCommonResourceBundle().getString("modeler.add.new.project.dialog.error.shortName"));
        }
    }

    /**
     * Hides the dialog.
     */
    private void closeDialog() {
        setVisible(false);
        dispose();  
    }

}
