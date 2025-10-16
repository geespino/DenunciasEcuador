package com.denuncias.ecuador;

import java.time.LocalDate;

public class Denuncia {
    private String titulo;
    private String descripcion;
    private LocalDate fechaEvento;
    private String ciudad;
    private String tipo;
    private boolean publica;
    private String autor;

    public Denuncia(String titulo, String descripcion, LocalDate fechaEvento,
                    String ciudad, String tipo, boolean publica, String autor) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaEvento = fechaEvento;
        this.ciudad = ciudad;
        this.tipo = tipo;
        this.publica = publica;
        this.autor = autor;
    }


    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFechaEvento() { return fechaEvento; }
    public String getCiudad() { return ciudad; }
    public String getTipo() { return tipo; }
    public boolean isPublica() { return publica; }
    public String getAutor() { return autor; }
}
