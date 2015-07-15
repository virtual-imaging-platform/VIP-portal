/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.vipcoworkapplet;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 * A modal dialog that asks the user for a user name and password.
 * More information about this class is available from <a target="_top" href=
 * "http://ostermiller.org/utils/PasswordDialog.html">ostermiller.org</a>.
 *
 * <code>
 * <pre>
 * PasswordDialog p = new PasswordDialog(null, "Test");
 * if(p.showDialog()){
 *     System.out.println("Name: " + p.getName());
 *     System.out.println("Pass: " + p.getPass());
 * } else {
 *     System.out.println("User selected cancel");
 * }
 * </pre>
 * </code>
 *
 * @author Stephen Ostermiller http://ostermiller.org/contact.pl?regarding=Java+Utilities
 * @since ostermillerutils 1.00.00
 */
public class PasswordDialog extends JDialog {

    /**
     * Serial version id
     */
    private static final long serialVersionUID = -832548326686122133L;
    /**
     * Where the name is typed.
     *
     * @since ostermillerutils 1.00.00
     */
    protected JTextField name;
    /**
     * Where the password is typed.
     *
     * @since ostermillerutils 1.00.00
     */
    protected JPasswordField pass;
    /**
     * The OK button.
     *
     * @since ostermillerutils 1.00.00
     */
    protected JButton okButton;
    /**
     * The cancel button.
     *
     * @since ostermillerutils 1.00.00
     */
    protected JButton cancelButton;
    /**
     * The label for the field in which the name is typed.
     *
     * @since ostermillerutils 1.00.00
     */
    protected JLabel nameLabel;
    /**
     * The label for the field in which the password is typed.
     *
     * @since ostermillerutils 1.00.00
     */
    protected JLabel passLabel;

    /**
     * Set the name that appears as the default
     * An empty string will be used if this in not specified
     * before the dialog is displayed.
     *
     * @param name default name to be displayed.
     *
     * @since ostermillerutils 1.00.00
     */
    @Override
    public void setName(String name) {
        this.name.setText(name);
    }

    /**
     * Set the password that appears as the default
     * An empty string will be used if this in not specified
     * before the dialog is displayed.
     *
     * @param pass default password to be displayed.
     *
     * @since ostermillerutils 1.00.00
     */
    public void setPass(String pass) {
        this.pass.setText(pass);
    }

    /**
     * Set the label on the OK button.
     * The default is a localized string.
     *
     * @param ok label for the ok button.
     *
     * @since ostermillerutils 1.00.00
     */
    public void setOKText(String ok) {
        this.okButton.setText(ok);
        pack();
    }

    /**
     * Set the label on the cancel button.
     * The default is a localized string.
     *
     * @param cancel label for the cancel button.
     *
     * @since ostermillerutils 1.00.00
     */
    public void setCancelText(String cancel) {
        this.cancelButton.setText(cancel);
        pack();
    }

    /**
     * Set the label for the field in which the name is entered.
     * The default is a localized string.
     *
     * @param name label for the name field.
     *
     * @since ostermillerutils 1.00.00
     */
    public void setNameLabel(String name) {
        this.nameLabel.setText(name);
        pack();
    }

    /**
     * Set the label for the field in which the password is entered.
     * The default is a localized string.
     *
     * @param pass label for the password field.
     *
     * @since ostermillerutils 1.00.00
     */
    public void setPassLabel(String pass) {
        this.passLabel.setText(pass);
        pack();
    }

    /**
     * Get the name that was entered into the dialog before
     * the dialog was closed.
     *
     * @return the name from the name field.
     *
     * @since ostermillerutils 1.00.00
     */
    @Override
    public String getName() {
        return name.getText();
    }

    /**
     * Get the password that was entered into the dialog before
     * the dialog was closed.
     *
     * @return the password from the password field.
     *
     * @since ostermillerutils 1.00.00
     */
    public String getPass() {
        return new String(pass.getPassword());
    }

    /**
     * Finds out if user used the OK button or an equivalent action
     * to close the dialog.
     * Pressing enter in the password field may be the same as
     * 'OK' but closing the dialog and pressing the cancel button
     * are not.
     *
     * @return true if the the user hit OK, false if the user canceled.
     *
     * @since ostermillerutils 1.00.00
     */
    public boolean okPressed() {
        return pressed_OK;
    }
    /**
     * update this variable when the user makes an action
     *
     * @since ostermillerutils 1.00.00
     */
    private boolean pressed_OK = false;

    /**
     * Create this dialog with the given parent and title.
     *
     * @param parent window from which this dialog is launched
     * @param title the title for the dialog box window
     *
     * @since ostermillerutils 1.00.00
     */
    public PasswordDialog(Frame parent, String title) {

        super(parent, title, true);

        setLocale(Locale.getDefault());

        if (title == null) {
            title = "Please login";
        }
        if (parent != null) {
            setLocationRelativeTo(parent);
        }
        // super calls dialogInit, so we don't need to do it again.
    }

    /**
     * Create this dialog with the given parent and the default title.
     *
     * @param parent window from which this dialog is launched
     *
     * @since ostermillerutils 1.00.00
     */
    public PasswordDialog(Frame parent) {
        this(parent, null);
    }

    /**
     * Create this dialog with the default title.
     *
     * @since ostermillerutils 1.00.00
     */
    public PasswordDialog() {
        this(null, null);
    }

    /**
     * Called by constructors to initialize the dialog.
     *
     * @since ostermillerutils 1.00.00
     */
    @Override
    protected void dialogInit() {


        name = new JTextField("", 20);
        pass = new JPasswordField("", 20);
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        nameLabel = new JLabel("Login ");
        passLabel = new JLabel("Password ");

        super.dialogInit();

        KeyListener keyListener = (new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE
                        || (e.getSource() == cancelButton
                        && e.getKeyCode() == KeyEvent.VK_ENTER)) {
                    pressed_OK = false;
                    PasswordDialog.this.setVisible(false);
                }
                if (e.getSource() == okButton
                        && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    pressed_OK = true;
                    PasswordDialog.this.setVisible(false);
                }
            }
        });
        addKeyListener(keyListener);

        ActionListener actionListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source == name) {
                    // the user pressed enter in the name field.
                    name.transferFocus();
                } else {
                    // other actions close the dialog.
                    pressed_OK = (source == pass || source == okButton);
                    PasswordDialog.this.setVisible(false);
                }
            }
        };

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.insets.top = 5;
        c.insets.bottom = 5;
        JPanel pane = new JPanel(gridbag);
        pane.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));
        c.anchor = GridBagConstraints.EAST;
        gridbag.setConstraints(nameLabel, c);
        pane.add(nameLabel);

        gridbag.setConstraints(name, c);
        name.addActionListener(actionListener);
        name.addKeyListener(keyListener);
        pane.add(name);

        c.gridy = 1;
        gridbag.setConstraints(passLabel, c);
        pane.add(passLabel);

        gridbag.setConstraints(pass, c);
        pass.addActionListener(actionListener);
        pass.addKeyListener(keyListener);
        pane.add(pass);

        c.gridy = 2;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.CENTER;
        JPanel panel = new JPanel();
        okButton.addActionListener(actionListener);
        okButton.addKeyListener(keyListener);
        panel.add(okButton);
        cancelButton.addActionListener(actionListener);
        cancelButton.addKeyListener(keyListener);
        panel.add(cancelButton);
        gridbag.setConstraints(panel, c);
        pane.add(panel);

        getContentPane().add(pane);

        pack();
    }

    /**
     * Shows the dialog and returns true if the user pressed ok.
     *
     * @return true if the the user hit OK, false if the user canceled.
     *
     * @since ostermillerutils 1.00.00
     */
    public boolean showDialog() {
        center();
        setVisible(true);
        return okPressed();
    }

    private void center() {

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);
    }
}
