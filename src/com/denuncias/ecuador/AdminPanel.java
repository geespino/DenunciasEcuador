package com.denuncias.ecuador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class AdminPanel extends JFrame {

    private JTable tablaDenuncias;
    private JTable tablaMensajes;
    private DefaultTableModel modelDenuncias;
    private JComboBox<String> filtroCiudad, filtroTipo, filtroVisibilidad;

    public AdminPanel() {
        setTitle("Panel de Administración");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 🔹 Encabezado
        JLabel header = new JLabel("📊 Panel del Administrador", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(header, BorderLayout.NORTH);

        // 🔹 Pestañas
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Denuncias Registradas", crearPanelDenuncias());
        tabs.addTab("Buzón de Mensajes", crearPanelMensajes());
        add(tabs, BorderLayout.CENTER);

        // 🔹 Botón de cierre de sesión
        JButton btnCerrar = new JButton("🚪 Cerrar Sesión");
        btnCerrar.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        add(btnCerrar, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ============================================================
    // 🔹 PANEL DE DENUNCIAS
    // ============================================================

    private JPanel crearPanelDenuncias() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel filtrosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filtrosPanel.setBorder(BorderFactory.createTitledBorder("🔍 Filtros de búsqueda"));

        filtroCiudad = new JComboBox<>(new String[]{"Todas", "Quito", "Guayaquil", "Cuenca", "Manta", "Portoviejo"});
        filtroTipo = new JComboBox<>(new String[]{"Todos", "Aseo y Ornato", "Tránsito Vial", "Delito"});
        filtroVisibilidad = new JComboBox<>(new String[]{"Todas", "Pública", "Privada"});

        filtrosPanel.add(new JLabel("Ciudad:"));
        filtrosPanel.add(filtroCiudad);
        filtrosPanel.add(new JLabel("Tipo:"));
        filtrosPanel.add(filtroTipo);
        filtrosPanel.add(new JLabel("Visibilidad:"));
        filtrosPanel.add(filtroVisibilidad);

        JButton btnFiltrar = new JButton("Aplicar filtros");
        filtrosPanel.add(btnFiltrar);
        panel.add(filtrosPanel, BorderLayout.NORTH);

        // Tabla denuncias
        String[] columnas = {"Título", "Descripción", "Fecha", "Ciudad", "Tipo", "Visibilidad"};
        modelDenuncias = new DefaultTableModel(columnas, 0);
        tablaDenuncias = new JTable(modelDenuncias);
        tablaDenuncias.setFillsViewportHeight(true);
        tablaDenuncias.setRowHeight(25);
        tablaDenuncias.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(new JScrollPane(tablaDenuncias), BorderLayout.CENTER);

        JPanel acciones = new JPanel();
        JButton btnActualizar = new JButton("🔄 Actualizar");
        JButton btnEliminar = new JButton("🗑 Eliminar denuncia");
        JButton btnAsignar = new JButton("📤 Enviar al área correspondiente");

        acciones.add(btnActualizar);
        acciones.add(btnEliminar);
        acciones.add(btnAsignar);
        panel.add(acciones, BorderLayout.SOUTH);

        cargarDenuncias();

        btnFiltrar.addActionListener(e -> aplicarFiltros());
        btnActualizar.addActionListener(e -> cargarDenuncias());

        btnEliminar.addActionListener(e -> {
            int fila = tablaDenuncias.getSelectedRow();
            if (fila != -1) {
                String titulo = tablaDenuncias.getValueAt(fila, 0).toString();
                int confirm = JOptionPane.showConfirmDialog(this,
                        "¿Seguro que deseas eliminar la denuncia \"" + titulo + "\"?",
                        "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    DenunciaDatabase.eliminarDenunciaPorTitulo(titulo);
                    cargarDenuncias();
                    JOptionPane.showMessageDialog(this, "Denuncia eliminada correctamente ✅");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una denuncia para eliminar.");
            }
        });

        btnAsignar.addActionListener(e -> {
            int fila = tablaDenuncias.getSelectedRow();
            if (fila != -1) {
                String tipo = tablaDenuncias.getValueAt(fila, 4).toString();
                String area = asignarArea(tipo);
                JOptionPane.showMessageDialog(this,
                        "✅ La denuncia ha sido enviada al área correspondiente:\n\n" + area,
                        "Denuncia Direccionada",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una denuncia para asignar.");
            }
        });

        return panel;
    }

    private void cargarDenuncias() {
        modelDenuncias.setRowCount(0);
        List<Denuncia> lista = DenunciaDatabase.obtenerTodas();

        for (Denuncia d : lista) {
            modelDenuncias.addRow(new Object[]{
                    d.getTitulo(),
                    d.getDescripcion(),
                    d.getFechaEvento(),
                    d.getCiudad(),
                    d.getTipo(),
                    d.isPublica() ? "Pública" : "Privada"
            });
        }
    }

    private void aplicarFiltros() {
        modelDenuncias.setRowCount(0);
        List<Denuncia> lista = DenunciaDatabase.obtenerTodas();

        String ciudadSel = filtroCiudad.getSelectedItem().toString();
        String tipoSel = filtroTipo.getSelectedItem().toString();
        String visSel = filtroVisibilidad.getSelectedItem().toString();

        List<Denuncia> filtradas = lista.stream()
                .filter(d -> ciudadSel.equals("Todas") || d.getCiudad().equalsIgnoreCase(ciudadSel))
                .filter(d -> tipoSel.equals("Todos") || d.getTipo().equalsIgnoreCase(tipoSel))
                .filter(d -> visSel.equals("Todas") ||
                        (visSel.equals("Pública") && d.isPublica()) ||
                        (visSel.equals("Privada") && !d.isPublica()))
                .collect(Collectors.toList());

        for (Denuncia d : filtradas) {
            modelDenuncias.addRow(new Object[]{
                    d.getTitulo(),
                    d.getDescripcion(),
                    d.getFechaEvento(),
                    d.getCiudad(),
                    d.getTipo(),
                    d.isPublica() ? "Pública" : "Privada"
            });
        }

        JOptionPane.showMessageDialog(this, "Se encontraron " + filtradas.size() + " denuncias.");
    }

    // ============================================================
    // 💬 PANEL DE MENSAJES SINCRONIZADO
    // ============================================================

    private JPanel crearPanelMensajes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnas = {"Usuario", "Mensaje Recibido", "Respuesta"};
        DefaultTableModel modeloMensajes = new DefaultTableModel(columnas, 0);
        tablaMensajes = new JTable(modeloMensajes);
        tablaMensajes.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tablaMensajes);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel responderPanel = new JPanel(new BorderLayout(5, 5));
        JTextField txtRespuesta = new JTextField();
        JButton btnResponder = new JButton("📩 Enviar Respuesta");

        responderPanel.add(new JLabel("Responder al usuario:"), BorderLayout.WEST);
        responderPanel.add(txtRespuesta, BorderLayout.CENTER);
        responderPanel.add(btnResponder, BorderLayout.EAST);
        panel.add(responderPanel, BorderLayout.SOUTH);

        // Método para recargar mensajes
        Runnable recargarMensajes = () -> {
            List<Mensaje> mensajes = MensajeDatabase.obtenerMensajesParaAdmin();
            modeloMensajes.setRowCount(0);
            for (Mensaje m : mensajes) {
                modeloMensajes.addRow(new Object[]{
                        m.getRemitente(),
                        m.getContenido(),
                        m.getRespuesta() != null ? m.getRespuesta() : ""
                });
            }
        };

        // Carga inicial
        recargarMensajes.run();

        // Acción de respuesta
        btnResponder.addActionListener(e -> {
            int filaSeleccionada = tablaMensajes.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(panel, "Selecciona un mensaje para responder.");
                return;
            }

            String usuario = (String) tablaMensajes.getValueAt(filaSeleccionada, 0);
            String respuesta = txtRespuesta.getText().trim();

            if (respuesta.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Escribe una respuesta.");
                return;
            }

            MensajeDatabase.responderMensaje(usuario, respuesta);
            JOptionPane.showMessageDialog(panel, "Respuesta enviada correctamente ✅");
            txtRespuesta.setText("");
            recargarMensajes.run();
        });

        // 🔄 Actualización automática cada 5 segundos
        new Timer(5000, e -> recargarMensajes.run()).start();

        return panel;
    }

    private String asignarArea(String tipo) {
        switch (tipo) {
            case "Aseo y Ornato":
                return "Departamento de Limpieza Municipal";
            case "Tránsito Vial":
                return "Agencia Metropolitana de Tránsito";
            case "Delito":
                return "Policía Nacional del Ecuador";
            default:
                return "Área General de Denuncias";
        }
    }
}