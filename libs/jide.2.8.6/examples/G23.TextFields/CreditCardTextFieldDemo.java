import com.jidesoft.field.creditcard.*;
import com.jidesoft.list.CheckBoxListWithAllSelectionModel;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.CheckBoxList;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.StyledLabel;
import com.jidesoft.swing.StyledLabelBuilder;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashSet;
import java.util.Set;


public class CreditCardTextFieldDemo extends AbstractDemo {

    static CreditCardTextField _field = null;
    protected StyledLabel _message;

    public CreditCardTextFieldDemo() {
    }

    public String getName() {
        return "CreditCardField Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        _field = new CreditCardTextField();
        panel.add(_field, BorderLayout.CENTER);
        panel.add(createValidateButton(), BorderLayout.AFTER_LINE_ENDS);
        _message = new StyledLabel();
        panel.add(_message, BorderLayout.AFTER_LAST_LINE);
        JPanel topPanel = new JPanel(new BorderLayout(12, 12));
        topPanel.add(panel, BorderLayout.BEFORE_FIRST_LINE);
        topPanel.add(new JScrollPane(new JTextArea("" +
                "Here are some examples of valid credit card\n" +
                "numbers you can use to test:\n" +
                "--------------------------------\n" +
                "MasterCard: 5105105105105100\n" +
                "MasterCard: 5555555555554444\n" +
                "VISA: 4222222222222\n" +
                "VISA: 4111111111111111\n" +
                "VISA: 4012888888881881\n" +
                "AMEX: 378282246310005\n" +
                "AMEX: 371449635398431\n" +
                "AMEX Corp: 378734493671000\n" +
                "Discover: 6011111111111117\n" +
                "Discover: 6011000990139424\n" +
                "Diner's Club: 38520000023237\n" +
                "Diner's Club: 30569309025904\n" +
                "JCB: 3530111333300000\n" +
                "JCB: 3566002020360505\n" +
                "--------------------------------\n" +
                "Those are of course not real card numbers", 20, 20)), BorderLayout.CENTER);
        return topPanel;
    }


    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS, 2));
        final CheckBoxList allowedCreditCards = new CheckBoxList(new String[]{
                "(All)",
                VISA.NAME,
                MasterCard.NAME,
                AmericanExpress.NAME,
                Discover.NAME,
                DinersClub.NAME,
                JCB.NAME,
        });
        allowedCreditCards.setCheckBoxListSelectionModel(new CheckBoxListWithAllSelectionModel());
        allowedCreditCards.setVisibleRowCount(7);
        allowedCreditCards.getCheckBoxListSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int[] selected = allowedCreditCards.getCheckBoxListSelectedIndices();
                if (selected.length > 0) {
                    if (selected[0] == 0) {
                        _field.setAllowedCardIssuerNames(null);
                    }
                    else {
                        Set<String> names = new HashSet();
                        for (int s : selected) {
                            switch (s) {
                                case 1:
                                    names.add(VISA.NAME);
                                    break;
                                case 2:
                                    names.add(MasterCard.NAME);
                                    break;
                                case 3:
                                    names.add(AmericanExpress.NAME);
                                    break;
                                case 4:
                                    names.add(Discover.NAME);
                                    break;
                                case 5:
                                    names.add(DinersClub.NAME);
                                    break;
                                case 6:
                                    names.add(JCB.NAME);
                                    break;
                            }
                        }
                        _field.setAllowedCardIssuerNames(names.toArray(new String[names.size()]));
                    }
                }
                else {
                    _field.setAllowedCardIssuerNames(new String[0]);
                }
            }
        });
        allowedCreditCards.setCheckBoxListSelectedIndex(0);
        panel.add(new JLabel("Allowed Credit Card Type: "));
        panel.add(new JScrollPane(allowedCreditCards));
        panel.add(Box.createVerticalStrut(4));

        final JCheckBox validate = new JCheckBox("Validate on Fly");
        panel.add(validate);

        validate.setSelected(_field.isValidateOnFly());
        validate.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _field.setValidateOnFly(validate.isSelected());
            }
        });

        final JCheckBox mask = new JCheckBox("Mask Enabled");
        panel.add(mask);

        mask.setSelected(_field.isMaskEnabled());
        mask.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _field.setMaskEnabled(mask.isSelected());
            }
        });
        return panel;
    }

    private AbstractButton createValidateButton() {
        final JButton button = new JButton("Validate");
        button.setMnemonic('V');
        AbstractAction validateAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                boolean valid = _field.validateCardNumber();
                if (valid) {
                    StyledLabelBuilder.setStyledText(_message, "It is a valid {" + _field.getCardIssuer().getName() + ":f:blue} card number.");
                }
                else {
                    StyledLabelBuilder.setStyledText(_message, "It is an {invalid:f:red} card number.");
                }
            }
        };
        button.addActionListener(validateAction);
        _field.getTextField().addActionListener(validateAction);
        button.setFocusable(false);
        button.setRequestFocusEnabled(false);
        return button;
    }

    @Override
    public String getDescription() {
        return "This is a demo of CreditCardTextField. It can accept and verify credit card numbers. " +
                "After user enters the number, it has method to verify if the number is a valid credit card number. " +
                "If validated, what type of credit card it is and then display an icon for the credit card type next to it.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.field.CreditCardTextField";
    }

    @Override
    public String getDemoFolder() {
        return "G23.TextFields";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new CreditCardTextFieldDemo());
    }
}
