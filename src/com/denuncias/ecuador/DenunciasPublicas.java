package com.denuncias.ecuador;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DenunciasPublicas extends JFrame {

    public DenunciasPublicas() {
        setTitle("Denuncias Públicas");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        String[] opciones = {"Último día", "Última semana"};
        JComboBox<String> cbFiltro = new JComboBox<>(opciones);
        JButton btnCargar = new JButton("🔍 Ver denuncias");

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Filtrar por:"));
        topPanel.add(cbFiltro);
        topPanel.add(btnCargar);

        add(topPanel, BorderLayout.NORTH);

    
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(area);
        add(scrollPane, BorderLayout.CENTER);


        btnCargar.addActionListener(e -> {
            String filtro = (String) cbFiltro.getSelectedItem();
            List<Denuncia> denuncias = DenunciaDatabase.obtenerDenunciasPublicas(filtro);

            area.setText(""); // Limpiar

            if (denuncias.isEmpty()) {
                area.setText("❌ No se encontraron denuncias públicas recientes.");
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                for (Denuncia d : denuncias) {
                    area.append("📌 " + d.getTitulo() + "\n");
                    area.append("📍 " + d.getCiudad() + " - " + d.getTipo() + "\n");
                    area.append("🗓️  " + d.getFechaEvento().format(formatter) + "\n");
                    area.append("📝 " + d.getDescripcion() + "\n");
                    area.append("────────────────────────────\n");
                }
            }
        });

        setVisible(true);
    }
}
