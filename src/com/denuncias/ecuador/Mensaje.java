package com.denuncias.ecuador;

import java.time.LocalDateTime;

public class Mensaje {
    private String remitente;
    private String destinatario;
    private String contenido;
    private LocalDateTime fechaEnvio;
    private String respuesta;

    public Mensaje(String remitente, String destinatario, String contenido) {
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.contenido = contenido;
        this.fechaEnvio = LocalDateTime.now();
    }

    public String getRemitente() { return remitente; }
    public String getDestinatario() { return destinatario; }
    public String getContenido() { return contenido; }
    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public String getRespuesta() { return respuesta; }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}

