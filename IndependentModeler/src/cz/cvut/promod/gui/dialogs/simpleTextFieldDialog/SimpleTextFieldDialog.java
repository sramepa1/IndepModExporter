package cz.cvut.promod.gui.dialogs.simpleTextFieldDialog;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.PresentationModel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 23:49:43, 23.10.2009
 *
 * A implementation of SimpleTextFieldDialog.
 */
public class SimpleTextFieldDialog extends SimpleTextFieldDialogView{

    private final SimpleTextFieldDialogModel model = new SimpleTextFieldDialogModel();

    private final PresentationModel<SimpleTextFieldDialogModel> presentation = new PresentationModel<SimpleTextFieldDialogModel>(model);

    private final SimpleTextFieldDialogExecutor executor;

    
    public SimpleTextFieldDialog(final String title,
                                 final String textLabelString,
                                 final String inputTextFieldText,
                                 final String confirmButtonText,
                                 final String cancelButtonString,
                                 final SimpleTextFieldDialogExecutor executor,
                                 final Component locateRelativeToComponent,
                                 final boolean modal){

        if(locateRelativeToComponent != null){
            setLocationRelativeTo(locateRelativeToComponent);
        }

        //init texts
        setTitle(title);
        
        textLabel.setText(textLabelString);
        confirmButton.setText(confirmButtonText);
        cancelButton.setText(cancelButtonString);

        this.executor = executor;

        initBinding();

        inputTextField.setText(inputTextFieldText);

        setModal(modal);

        initEventHandling();

        getRootPane().setDefaultButton(confirmButton);

        setVisible(true);
    }

    private void initBinding() {
        Bindings.bind(inputTextField, presentation.getModel(SimpleTextFieldDialogModel.PROPERTY_SUBFOLDER_NAME));
    }

    private void initEventHandling() {
        cancelButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                disposeDialog();
            }
        });

        confirmButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                final String result = executor.execute(model.getInputText());

                if(result == null){
                    disposeDialog();
                } else {
                    errorLabel.setText(result);
                }
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

}
