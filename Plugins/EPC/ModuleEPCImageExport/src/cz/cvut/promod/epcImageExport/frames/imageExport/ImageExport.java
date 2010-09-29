package cz.cvut.promod.epcImageExport.frames.imageExport;

import cz.cvut.promod.plugin.notationSpecificPlugIn.DockableFrameData;
import cz.cvut.promod.gui.support.utils.NotationGuiHolder;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.actionService.actionUtils.ProModAction;
import cz.cvut.promod.epcImageExport.EPCImageExportModuleModel;
import cz.cvut.promod.epcImageExport.resources.Resources;
import org.apache.log4j.Logger;
import org.jgraph.JGraph;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.io.*;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 23:48:35, 13.12.2009
 *
 * The Image Export dockable frame.
 */
public class ImageExport extends ImageExportView implements DockableFrameData{

    private static final Logger LOG = Logger.getLogger(ImageExport.class);

    public static final String EMPTY_IMAGE_TITLE_RES = "epc.imageexport.empty.image.error.title";
    public static final String EMPTY_IMAGE_RES = "epc.imageexport.empty.image.error";

    private final JGraph graph;

    private final ImageExportModel model;

    private ProModAction pngExportAction;

    private final ValueModel insetModel;

    private final SpinnerNumberModel scaleSpinnerModel;

    private final PresentationModel<ImageExportModel> presentationModel;

    public ImageExport(final JGraph graph,
                       final Map<String, ProModAction> actions) {

        this.graph = graph;

        model = new ImageExportModel();

        initComboBox();

        initActions(actions);

        initEventHandling();

        presentationModel = new PresentationModel<ImageExportModel>(model);
        this.insetModel = presentationModel.getModel(ImageExportModel.PROPERTY_INSET);

        scaleSpinnerModel =
            SpinnerAdapterFactory.createNumberAdapter(
                    insetModel,
                    ImageExportModel.INIT_INSET,
                    ImageExportModel.MIN_INSET,
                    ImageExportModel.MAX_INSET,
                    ImageExportModel.INIT_SPINNER_STEP);

        initOptionPanels();

        //initial settings
        formatComboBox.setSelectedItem(ImageExportModel.PNG_FORMAT_NAME);
    }

    private void initActions(final Map<String, ProModAction> actions) {
        pngExportAction = new ProModAction(
                Resources.getResources().getString(EPCImageExportModuleModel.PNG_EXPORT_ACTION),
                null,
                null){

            public void actionPerformed(ActionEvent event) {
                final JFileChooser saveDialog = new JFileChooser();

                final BufferedImage image = graph.getImage(Color.WHITE, (Integer)insetModel.getValue());

                if(image == null){
                    JOptionPane.showMessageDialog(
                            ModelerSession.getFrame(),
                            Resources.getResources().getString(EMPTY_IMAGE_RES),
                            Resources.getResources().getString(EMPTY_IMAGE_TITLE_RES),
                            JOptionPane.ERROR_MESSAGE);

                    LOG.error("It's not possible to save empty image.");
                    return;
                }

                int returnVal = saveDialog.showSaveDialog(ModelerSession.getFrame());

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try{
                        final File file = saveDialog.getSelectedFile();
                        final OutputStream out = new FileOutputStream(getFilePNGFileName(file));

                        ImageIO.write(image, ImageExportModel.PNG_FORMAT_NAME, out);

                        out.flush();
                        out.close();

                        LOG.info("EPC image export saved: " + file.getAbsolutePath() + ".");

                    } catch (FileNotFoundException e1) {
                        LOG.error("", e1);
                    } catch (IOException e1) {
                        LOG.error("", e1);
                    }
                }
            }
        };

        actions.put(EPCImageExportModuleModel.PNG_EXPORT_ACTION, pngExportAction);
    }

    private void initOptionPanels() {
        registerOptionPanel(ImageExportModel.PNG_FORMAT_NAME, new PNGOptionPanel(scaleSpinnerModel));
    }

    private void initComboBox() {
        formatComboBox.addItem(ImageExportModel.PNG_FORMAT_NAME);
    }

    private void initEventHandling() {
        formatComboBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                final JComboBox comboBox = (JComboBox)e.getSource();
                final String selectedFormat = (String) comboBox.getSelectedItem();

                cardLayout.show(optionsPanel, selectedFormat);
            }
        });

        exportButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                if(ImageExportModel.PNG_FORMAT_NAME.equals(formatComboBox.getSelectedItem())){
                    pngExportAction.actionPerformed(null);
                }
            }
        });
    }

    private String getFilePNGFileName(final File file) {
        if(file.getAbsolutePath().endsWith(ImageExportModel.PNG_EXTENSION)){
            return file.getName();
        }

        return file.getAbsolutePath() + ImageExportModel.PNG_EXTENSION;
    }

    public String getDockableFrameName() {
        return "epc.image.export.jshdbsahcd";
    }

    public JComponent getDockableFrameComponent() {
        return this;
    }

    public NotationGuiHolder.Position getInitialPosition() {
        return NotationGuiHolder.Position.BOTTOM;
    }

    public boolean isMaximizable() {
        return false;
    }

    public Set<NotationGuiHolder.Position> getAllowedDockableFramePositions() {
        final Set<NotationGuiHolder.Position> positions = new HashSet<NotationGuiHolder.Position>();
        positions.add(NotationGuiHolder.Position.BOTTOM);
        positions.add(NotationGuiHolder.Position.TOP);

        return positions;
    }

    public InitialState getInitialState() {
        return InitialState.HIDDEN;
    }

    public String getDockableFrameTitle() {
        return Resources.getResources().getString(ImageExportModel.FRAME_TITLE_RES);
    }

    public Icon getButtonIcon() {
        return null;
    }

    public PresentationModel<ImageExportModel> getPresentationModel() {
        return presentationModel;
    }
}
