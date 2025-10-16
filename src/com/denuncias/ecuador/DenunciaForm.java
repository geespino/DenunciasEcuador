package com.denuncias.ecuador;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DenunciaForm extends JFrame {

    public DenunciaForm(User user) {
        setTitle("Ingresar Denuncia");
        setSize(420, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));


        JLabel header = new JLabel("📝 Registrar Nueva Denuncia", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(header, BorderLayout.NORTH);

      
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JTextField txtTitulo = new JTextField();
        JTextArea txtDescripcion = new JTextArea(3, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JTextField txtFecha = new JTextField(LocalDate.now().toString()); // fecha por defecto hoy
        JTextField txtCiudad = new JTextField();
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Aseo y Ornato", "Tránsito Vial", "Delito"});
        JComboBox<String> cbVisibilidad = new JComboBox<>(new String[]{"Pública", "Privada"});

        formPanel.add(new JLabel("Título:"));
        formPanel.add(txtTitulo);
        formPanel.add(new JLabel("Descripción:"));
        formPanel.add(new JScrollPane(txtDescripcion));
        formPanel.add(new JLabel("Fecha del evento:"));
        formPanel.add(txtFecha);
        formPanel.add(new JLabel("Ciudad/Provincia:"));
        formPanel.add(txtCiudad);
        formPanel.add(new JLabel("Tipo:"));
        formPanel.add(cbTipo);
        formPanel.add(new JLabel("Visibilidad:"));
        formPanel.add(cbVisibilidad);

        add(formPanel, BorderLayout.CENTER);

 
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnEnviar = new JButton("📤 Enviar denuncia");
        JButton btnCancelar = new JButton("❌ Cancelar");
        bottomPanel.add(btnEnviar);
        bottomPanel.add(btnCancelar);
        add(bottomPanel, BorderLayout.SOUTH);


        btnEnviar.addActionListener(e -> {
            String titulo = txtTitulo.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            String fechaTexto = txtFecha.getText().trim();
            String ciudad = txtCiudad.getText().trim();
            String tipo = (String) cbTipo.getSelectedItem();
            boolean publica = cbVisibilidad.getSelectedItem().equals("Pública");

            if (titulo.isEmpty() || descripcion.isEmpty() || ciudad.isEmpty() || fechaTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Todos los campos son obligatorios.");
                return;
            }

            try {
                LocalDate fechaEvento = LocalDate.parse(fechaTexto);

                Denuncia denuncia = new Denuncia(
                        titulo,
                        descripcion,
                        fechaEvento,
                        ciudad,
                        tipo,
                        publica,
                        user.getUsername()
                );

      
                DenunciaDatabase.registrarDenuncia(denuncia);

                JOptionPane.showMessageDialog(this, "✅ Denuncia enviada correctamente.");
                dispose();

            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "⚠️ Fecha inválida. Usa el formato AAAA-MM-DD.");
            }
        });


        btnCancelar.addActionListener(e -> dispose());

        setVisible(true);
    }
}