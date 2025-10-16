package com.denuncias.ecuador;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DenunciaDatabase {
    private static final String FILE_PATH = "denuncias.txt";
    private static List<Denuncia> denuncias = new ArrayList<>();

    static {
        cargarDesdeArchivo();
    }


    public static void registrarDenuncia(Denuncia d) {
        denuncias.add(d);
        guardarEnArchivo(d);
    }

 
    public static List<Denuncia> obtenerDenunciasPublicas(String filtroTiempo) {
        LocalDate hoy = LocalDate.now();
        LocalDate desde = filtroTiempo.equals("Última semana") ? hoy.minusDays(7) : hoy.minusDays(1);

        return denuncias.stream()
                .filter(d -> d.isPublica() && !d.getFechaEvento().isBefore(desde))
                .collect(Collectors.toList());
    }


    public static List<Denuncia> obtenerDenunciasUsuario(String username) {
        return denuncias.stream()
                .filter(d -> d.getAutor().equals(username))
                .collect(Collectors.toList());
    }

    public static List<Denuncia> obtenerTodas() {
        return new ArrayList<>(denuncias);
    }


    public static void eliminarDenunciaPorTitulo(String titulo) {
        boolean eliminado = denuncias.removeIf(d -> d.getTitulo().equalsIgnoreCase(titulo));
        if (eliminado) {
            guardarListaCompleta();
        }
    }

    private static void guardarEnArchivo(Denuncia d) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(formatearDenuncia(d));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void guardarListaCompleta() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Denuncia d : denuncias) {
                writer.write(formatearDenuncia(d));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void cargarDesdeArchivo() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                Denuncia d = parsearDenuncia(linea);
                if (d != null) denuncias.add(d);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String formatearDenuncia(Denuncia d) {
        return String.join(";",
                d.getTitulo(),
                d.getDescripcion(),
                d.getFechaEvento().toString(),
                d.getCiudad(),
                d.getTipo(),
                String.valueOf(d.isPublica()),
                d.getAutor()
        );
    }


    private static Denuncia parsearDenuncia(String linea) {
        try {
            String[] datos = linea.split(";");
            if (datos.length != 7) return null;

            String titulo = datos[0];
            String descripcion = datos[1];
            LocalDate fecha = LocalDate.parse(datos[2]);
            String ciudad = datos[3];
            String tipo = datos[4];
            boolean publica = Boolean.parseBoolean(datos[5]);
            String autor = datos[6];

            return new Denuncia(titulo, descripcion, fecha, ciudad, tipo, publica, autor);
        } catch (Exception e) {
            System.err.println("⚠️ Error al leer denuncia: " + linea);
            return null;
        }
    }
}