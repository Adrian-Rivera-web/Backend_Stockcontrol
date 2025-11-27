package com.appstockcontrol.inventario.dto;

import lombok.Data;

@Data
public class RegistrarEntradaRequest {

    private Long productoId;
    private Integer cantidad;
    private String usuarioResponsable;
    private String motivo;
}
