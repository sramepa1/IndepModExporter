package cz.cvut.promod.epc.settings.settingPages;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.value.BufferedValueModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import cz.cvut.promod.epc.resources.Resources;
import cz.cvut.promod.epc.settings.EPCSettings;
import cz.cvut.promod.epc.settings.EPCSettingsModel;
import cz.cvut.promod.gui.settings.SettingPagePanel;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.componentFactoryService.ComponentFactoryService;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 11:28:01, 26.1.2010
 *
 * General Page for Setting Dialog of the EPCNotation plugin.
 */
public class GeneralPage extends SettingPagePanel{

    private static Logger LOG = Logger.getLogger(GeneralPage.class);

    private static final int UNDO_SPINNER_COLUMNS = 3;
    
    private static final String GENERALS_LABEL = Resources.getResources().getString("epc.settings.generals");

    private final JLabel undoLimitLabel = ModelerSession.getComponentFactoryService().createLabel(
         Resources.getResources().getString("epc.settings.undo.limit")
    );
    protected final JSpinner undoLimitSpinner = ModelerSession.getComponentFactoryService().createSpinner();

    private final BufferedValueModel undoLimitModel;

    private final PresentationModel<EPCSettingsModel> presentation;

    private final ApplyAction applyAction = new ApplyAction();
    private final CancelAction cancelAction = new CancelAction();


    public GeneralPage(final PresentationModel<EPCSettingsModel> presentation, final BufferedValueModel undoLimitModel){
        //super(SETTINGS_LABEL);

        this.presentation = presentation;
        this.undoLimitModel = undoLimitModel;
    }

    public void lazyInitialize() {
        initValues();

        initLayout();

        getSpinnerTextField(undoLimitSpinner).setColumns(UNDO_SPINNER_COLUMNS);

        initEventHandling();
    }

    @Override
    public AbstractAction getApplyAction() {
        return this.applyAction;
    }

    @Override
    public AbstractAction getCancelAction() {
        return this.cancelAction;
    }

    @Override
    public AbstractAction getOkAction() {
        return this.applyAction;
    }

    private void initValues() {
        undoLimitSpinner.setValue(EPCSettings.getInstance().getUndoLimit());
    }

    private void initEventHandling() {
       final int initUndoLimitValue = (Integer) undoLimitModel.getValue();
       final SpinnerNumberModel scaleSpinnerModel = SpinnerAdapterFactory.createNumberAdapter(
                undoLimitModel,
                initUndoLimitValue,
                EPCSettingsModel.MIN_UNDO,
                EPCSettingsModel.MAX_UNDO,
                EPCSettingsModel.INIT_UNDO_STEP
       );

        undoLimitSpinner.setModel(scaleSpinnerModel);

        undoLimitSpinner.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                //fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);
                fireApplyActionEnable();
            }
        });
    }

    private void initLayout() {
        final FormLayout layout = new FormLayout(
                "pref, 3dlu, pref, pref:grow",
                "pref, 3dlu, pref"
        );
        setBorder(Borders.createEmptyBorder(ComponentFactoryService.DEFAULT_FORM_BORDER));

        final PanelBuilder panelBuilder = new PanelBuilder(layout);
        final CellConstraints cellConstraints = new CellConstraints();

        int row = 1;
        panelBuilder.addSeparator(GENERALS_LABEL, cellConstraints.xyw(1, row, 4));

        row += 2;
        panelBuilder.add(undoLimitLabel, cellConstraints.xy(1, row));
        panelBuilder.add(undoLimitSpinner, cellConstraints.xy(3, row));

        setLayout(new BorderLayout());
        add(panelBuilder.getPanel(), BorderLayout.CENTER);
    }

    public JFormattedTextField getSpinnerTextField(final JSpinner spinner) {
        final JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            return ((JSpinner.DefaultEditor)editor).getTextField();
        } else {
            LOG.error("Unexpected editor of JSpinner.");
            return null;
        }
    }


    /**
     * Commits the Trigger used to buffer the editor contents.
     */
    private final class ApplyAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            presentation.triggerCommit();
        }

    }


    /**
     * Flushed the Trigger used to buffer the editor contents.
     */
    private final class CancelAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            presentation.triggerFlush();
        }

    }

}