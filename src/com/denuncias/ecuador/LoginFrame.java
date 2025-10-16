package com.denuncias.ecuador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean isRegistering = false;

    public LoginFrame() {
        setTitle("DenunciasEcuador - Ingreso");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("DenunciasEcuador", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        formPanel.add(new JLabel("Usuario:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Contrasena:"));
        formPanel.add(passwordField);
        add(formPanel, BorderLayout.CENTER);

        JButton actionButton = new JButton("Iniciar Sesion");
        JButton toggleButton = new JButton("¿No tienes cuenta? Registrate");

        actionButton.addActionListener(this::handleAction);
        toggleButton.addActionListener(e -> toggleMode(actionButton, toggleButton));

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonPanel.add(actionButton);
        buttonPanel.add(toggleButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void toggleMode(JButton actionButton, JButton toggleButton) {
        isRegistering = !isRegistering;
        if (isRegistering) {
            actionButton.setText("Registrar");
            toggleButton.setText("¿Ya tienes cuenta? Inicia sesion");
        } else {
            actionButton.setText("Iniciar Sesion");
            toggleButton.setText("¿No tienes cuenta? Registrate");
        }
    }

    private void handleAction(ActionEvent e) {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos.");
            return;
        }

        if (isRegistering) {
            if (UserDatabase.registrarUsuario(user, pass)) {
                JOptionPane.showMessageDialog(this, "Registro exitoso. Ahora puedes iniciar sesion.");
                isRegistering = false;
            } else {
                JOptionPane.showMessageDialog(this, "El usuario ya existe.");
            }
        } else {
            User u = UserDatabase.login(user, pass);
            if (u != null) {
                JOptionPane.showMessageDialog(this, "Bienvenido " + u.getUsername() + "!");
                dispose();
                if (u.isAdmin()) {
                    new AdminPanel().setVisible(true);
                } else {
                    new UserPanel(u).setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}