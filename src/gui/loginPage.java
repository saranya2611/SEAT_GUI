package gui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;

public class loginPage extends JFrame {
    database dB;
    public JLabel userNameLabel;
    public JLabel passwordLabel;
    public JPasswordField loginPasswordField;
    public JLabel titleField;
    public JTextField userNameTextField;
    public JButton loginButton;
    public JPanel firstLoginPanel;

    public static void main(String[] args) {
        loginPage frame = new loginPage();
    }

    public loginPage() {
        super("Login Authentication Wizard");
        dB = new database();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 4 - this.getSize().width / 2, dim.height / 4 - this.getSize().height / 2);
        setSize(600, 500);

        // GUI initializer generated by IntelliJ IDEA GUI Designer
        // >>> IMPORTANT!! <<<
        // DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();

        // Button listener for login-button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String loginUserName = userNameTextField.getText();
                String loginPassword = String.valueOf(loginPasswordField.getPassword());
                if (loginUserName.equals("admin") && loginPassword.equals("admin123")) {
                    System.out.println("\n Login details are authorized");
                    JOptionPane.showMessageDialog(null, "Welcome to Student Elective Allocation Team " + loginUserName);
                    inputToAllotmentsWizard1 input = new inputToAllotmentsWizard1();
                    input.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Unauthorized login", "Failed Login", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Key listener for login button
        loginButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                String loginUserName = userNameTextField.getText();
                String loginPassword = String.valueOf(loginPasswordField.getPassword());
                if (loginUserName.equals("admin") && loginPassword.equals("admin123")) {
                    System.out.println("\n Login details are authorized");
                    JOptionPane.showMessageDialog(null, "Welcome to Student Elective Allocation Team " + loginUserName);
                    inputToAllotmentsWizard1 input = new inputToAllotmentsWizard1();
                    input.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Unauthorized login", "Failed Login", JOptionPane.ERROR_MESSAGE);
                }
                super.keyPressed(e);
            }
        });

        getContentPane().add(firstLoginPanel);
        setVisible(true);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        firstLoginPanel = new JPanel();
        firstLoginPanel.setLayout(new FormLayout("fill:243px:grow,left:4dlu:noGrow,fill:273px:noGrow,left:4dlu:noGrow,fill:89px:noGrow", "top:40dlu:noGrow,top:4dlu:noGrow,center:44px:noGrow,top:34dlu:noGrow,center:31px:noGrow,top:26dlu:noGrow,center:92px:noGrow"));
        firstLoginPanel.setBackground(new Color(-14793908));
        firstLoginPanel.setForeground(new Color(-131585));
        userNameLabel = new JLabel();
        Font userNameLabelFont = this.$$$getFont$$$("Century Schoolbook L", -1, 20, userNameLabel.getFont());
        if (userNameLabelFont != null) userNameLabel.setFont(userNameLabelFont);
        userNameLabel.setForeground(new Color(-3564016));
        userNameLabel.setIcon(new ImageIcon(getClass().getResource("/user1.png")));
        userNameLabel.setText(" User Name:");
        CellConstraints cc = new CellConstraints();
        firstLoginPanel.add(userNameLabel, cc.xy(1, 4, CellConstraints.CENTER, CellConstraints.CENTER));
        passwordLabel = new JLabel();
        Font passwordLabelFont = this.$$$getFont$$$("Century Schoolbook L", -1, 20, passwordLabel.getFont());
        if (passwordLabelFont != null) passwordLabel.setFont(passwordLabelFont);
        passwordLabel.setForeground(new Color(-3564016));
        passwordLabel.setIcon(new ImageIcon(getClass().getResource("/passwd.png")));
        passwordLabel.setText("  Password:");
        firstLoginPanel.add(passwordLabel, cc.xy(1, 6, CellConstraints.CENTER, CellConstraints.CENTER));
        loginPasswordField = new JPasswordField();
        firstLoginPanel.add(loginPasswordField, cc.xy(3, 6, CellConstraints.FILL, CellConstraints.CENTER));
        titleField = new JLabel();
        Font titleFieldFont = this.$$$getFont$$$("Lobster Two", Font.BOLD, 60, titleField.getFont());
        if (titleFieldFont != null) titleField.setFont(titleFieldFont);
        titleField.setForeground(new Color(-301758));
        titleField.setText("Welcome to SEAT");
        firstLoginPanel.add(titleField, cc.xyw(1, 1, 5, CellConstraints.CENTER, CellConstraints.DEFAULT));
        userNameTextField = new JTextField();
        firstLoginPanel.add(userNameTextField, cc.xy(3, 4, CellConstraints.FILL, CellConstraints.CENTER));
        loginButton = new JButton();
        loginButton.setBorderPainted(false);
        Font loginButtonFont = this.$$$getFont$$$("Century Schoolbook L", -1, 18, loginButton.getFont());
        if (loginButtonFont != null) loginButton.setFont(loginButtonFont);
        loginButton.setHorizontalTextPosition(10);
        loginButton.setIcon(new ImageIcon(getClass().getResource("/login.png")));
        loginButton.setText("Login");
        firstLoginPanel.add(loginButton, cc.xy(3, 7, CellConstraints.CENTER, CellConstraints.DEFAULT));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return firstLoginPanel;
    }
}
