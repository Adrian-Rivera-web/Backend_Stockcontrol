package com.appstockcontrol.usuario_autenticacion_servicio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistroRequest {

    @NotBlank
    @Size(min = 2, max = 80)
    private String nombre;

    @NotBlank
    @Email
    private String correo;

    @NotBlank
    private String telefono;

    @NotBlank
    @Size(min = 6)
    private String clave;

    @NotBlank
    private String direccion;

    private boolean aceptaTerminos;
}