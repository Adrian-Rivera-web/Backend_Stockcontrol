package com.appstockcontrol.usuario_autenticacion_servicio.dto;

import lombok.Data;

@Data
public class UsuarioResponse {

    private Long id;
    private String nombre;
    private String correo;
    private String telefono;
    private String direccion;
    private boolean esAdmin;
}