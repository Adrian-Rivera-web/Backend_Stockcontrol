package com.appstockcontrol.proveedores_servicio.dto;

import lombok.Data;

@Data
public class ProveedorDTO {

    private Long id;
    private String nombre;
    private String contacto;
    private String telefono;
    private String email;
    private String direccion;
    private boolean activo;
}
