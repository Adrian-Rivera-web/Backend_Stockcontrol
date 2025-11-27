package com.appstockcontrol.inventario.dto;

import lombok.Data;

@Data
public class RegistrarSalidaRequest {

    private Long productoId;
    private Integer cantidad;
    private String usuarioResponsable;
    private String motivo;
}
