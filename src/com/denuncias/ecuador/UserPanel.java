package com.denuncias.ecuador;

import javax.swing.*;
import java.awt.*;

public class UserPanel extends JFrame {

    private User user;

    public UserPanel(User user) {
        this.user = user;

        setTitle("Panel del Usuario - " + user.getUsername());
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Bienvenido, " + user.getUsername(), SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(header, BorderLayout.NORTH);

        //  Panel central con botones
        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        JButton btnDenuncia = new JButton("📄 Ingresar Denuncia");
        JButton btnPublicas = new JButton("🌐 Ver Denuncias Públicas");
        JButton btnBuzon = new JButton("📬 Revisar Buzón de Mensajes");
        JButton btnCerrarSesion = new JButton("🔙 Cerrar sesión");

        centerPanel.add(btnDenuncia);
        centerPanel.add(btnPublicas);
        centerPanel.add(btnBuzon);
        centerPanel.add(btnCerrarSesion);

        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        add(centerPanel, BorderLayout.CENTER);

        //  Acciones de los botones
        btnDenuncia.addActionListener(e -> new DenunciaForm(user).setVisible(true));

        btnPublicas.addActionListener(e -> new DenunciasPublicas().setVisible(true));

        btnBuzon.addActionListener(e -> new BuzonMensajes(user).setVisible(true));

        btnCerrarSesion.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Deseas cerrar sesión?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });

        setVisible(true);
    }
}
