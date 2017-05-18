package view.dialog;


import cont.Login;
import view.ColorScheme;
import view.comp.JLabelFactory;
import view.comp.JTextFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("all")
public class LoginDialog extends JFrame {

    private Login controller;
    private JLabel loginMessage;

    public LoginDialog() {
        super.setTitle("Login");
        super.setLayout(new BorderLayout());
        super.getContentPane().setPreferredSize(new Dimension(325, 200));
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setResizable(false);
        super.setLocationRelativeTo(null);
        this.loginMessage = JLabelFactory.createLabel("Please enter username and password", SwingConstants.CENTER);
        this.loginMessage.setBorder(BorderFactory.createEmptyBorder(20, 5, 20, 20));
        this.loginMessage.setFont(this.loginMessage.getFont().deriveFont(Font.BOLD, 14));
        super.add(this.loginMessage, BorderLayout.PAGE_START);
        this.placeComponents();
        super.pack();
    }

    private void placeComponents() {
        JPanel panel = new JPanel();
        panel.setBackground(ColorScheme.BACKGROUND);
        panel.setLayout(new GridLayout(3, 2, 10, 20));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        panel.add(JLabelFactory.createLabel("Username:", SwingConstants.LEFT));

        JTextField userText = JTextFactory.createTextField();
        panel.add(userText);

        panel.add(JLabelFactory.createLabel("Password:", SwingConstants.LEFT));

        JPasswordField passwordText = JTextFactory.createPasswordField();
        panel.add(passwordText);

        panel.add(Box.createHorizontalGlue());

        JLabel login = JLabelFactory.createOrangeButton("Login");
        panel.add(login);

        login.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller != null)
                    controller.login(userText.getText(), passwordText.getText());
            }
        });

        super.getContentPane().add(panel);
    }

    public void addLoginController(Login controller) {
        this.controller = controller;
    }

    public void setLoginMessage(String string) {
        this.loginMessage.setForeground(ColorScheme.PINK_LIGHT);
        this.loginMessage.setText(string);
    }
}
