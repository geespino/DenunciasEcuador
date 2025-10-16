package com.denuncias.ecuador;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BuzonMensajes extends JFrame {
    private final User user;
    private final JPanel panelChat;
    private final JScrollPane scrollPane;
    private final JTextField txtNuevoMensaje;
    private final JButton btnEnviar;

    public BuzonMensajes(User user) {
        this.user = user;

        setTitle("📬 Buzón de Mensajes - " + user.getUsername());
        setSize(550, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 247, 250));

        // ===== TÍTULO SUPERIOR =====
        JLabel titulo = new JLabel("💬 Comunicación con la Autoridad", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        // ===== PANEL DE CHAT =====
        panelChat = new JPanel();
        panelChat.setLayout(new BoxLayout(panelChat, BoxLayout.Y_AXIS));
        panelChat.setBackground(new Color(245, 247, 250));

        scrollPane = new JScrollPane(panelChat);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // ===== PANEL DE ENVÍO =====
        JPanel panelEnvio = new JPanel(new BorderLayout(10, 10));
        panelEnvio.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelEnvio.setBackground(new Color(240, 242, 245));

        txtNuevoMensaje = new JTextField();
        txtNuevoMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNuevoMensaje.setBorder(new CompoundBorder(
                new LineBorder(new Color(180, 180, 180), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));

        btnEnviar = new JButton("Enviar 🚀");
        btnEnviar.setBackground(new Color(70, 130, 180));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEnviar.setFocusPainted(false);
        btnEnviar.setBorder(new EmptyBorder(8, 15, 8, 15));

        panelEnvio.add(txtNuevoMensaje, BorderLayout.CENTER);
        panelEnvio.add(btnEnviar, BorderLayout.EAST);
        add(panelEnvio, BorderLayout.SOUTH);

        // ===== EVENTOS =====
        btnEnviar.addActionListener(e -> enviarMensaje());
        txtNuevoMensaje.addActionListener(e -> enviarMensaje());

        // ===== CARGA INICIAL =====
        cargarMensajes();

        setVisible(true);
    }

    private void cargarMensajes() {
        panelChat.removeAll();
        List<Mensaje> mensajes = MensajeDatabase.obtenerMensajesDeUsuario(user.getUsername());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        if (mensajes.isEmpty()) {
            JLabel vacio = new JLabel("📭 No tienes mensajes en tu buzón.", SwingConstants.CENTER);
            vacio.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            vacio.setForeground(Color.GRAY);
            panelChat.add(vacio);
        } else {
            for (Mensaje m : mensajes) {
                boolean esUsuario = m.getRemitente().equals(user.getUsername());
                JPanel burbuja = crearBurbujaMensaje(
                        (esUsuario ? "Tú" : "Autoridad"),
                        m.getContenido(),
                        m.getFechaEnvio().format(formatter),
                        esUsuario
                );
                panelChat.add(burbuja);
                panelChat.add(Box.createVerticalStrut(8)); // separación entre mensajes
            }
        }

        panelChat.revalidate();
        panelChat.repaint();

        // Mover scroll al final automáticamente
        SwingUtilities.invokeLater(() -> {
            JScrollBar barra = scrollPane.getVerticalScrollBar();
            barra.setValue(barra.getMaximum());
        });
    }

    private JPanel crearBurbujaMensaje(String remitente, String mensaje, String fecha, boolean esUsuario) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);

        JTextArea texto = new JTextArea(mensaje);
        texto.setWrapStyleWord(true);
        texto.setLineWrap(true);
        texto.setEditable(false);
        texto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        texto.setBackground(esUsuario ? new Color(180, 220, 250) : new Color(220, 240, 220));
        texto.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));

        JLabel info = new JLabel(remitente + " • " + fecha);
        info.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        info.setForeground(Color.DARK_GRAY);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setOpaque(false);
        contenedor.add(texto, BorderLayout.CENTER);
        contenedor.add(info, BorderLayout.SOUTH);

        if (esUsuario) {
            panel.add(contenedor, BorderLayout.EAST);
        } else {
            panel.add(contenedor, BorderLayout.WEST);
        }

        return panel;
    }

    private void enviarMensaje() {
        String texto = txtNuevoMensaje.getText().trim();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe un mensaje para enviar.");
            return;
        }

        // Crear y guardar mensaje
        Mensaje m = new Mensaje(user.getUsername(), "autoridad", texto);
        MensajeDatabase.enviarMensaje(m);

        txtNuevoMensaje.setText("");
        cargarMensajes();
    }
}

