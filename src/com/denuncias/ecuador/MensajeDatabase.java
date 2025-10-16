package com.denuncias.ecuador;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MensajeDatabase {
    private static final List<Mensaje> mensajes = new ArrayList<>();

    public static void enviarMensaje(Mensaje mensaje) {
        mensajes.add(mensaje);
    }

    public static List<Mensaje> obtenerMensajesDeUsuario(String username) {
        return mensajes.stream()
                .filter(m -> m.getRemitente().equals(username) || m.getDestinatario().equals(username))
                .collect(Collectors.toList());
    }

    public static List<Mensaje> obtenerMensajesParaAdmin() {
        return mensajes.stream()
                .filter(m -> m.getDestinatario().equalsIgnoreCase("autoridad"))
                .collect(Collectors.toList());
    }

    public static void responderMensaje(String remitente, String respuesta) {
        for (Mensaje m : mensajes) {
            if (m.getRemitente().equals(remitente) && m.getRespuesta() == null) {
                m.setRespuesta(respuesta);
            }
        }


        Mensaje respuestaMsg = new Mensaje("autoridad", remitente, respuesta);
        mensajes.add(respuestaMsg);
    }
}

