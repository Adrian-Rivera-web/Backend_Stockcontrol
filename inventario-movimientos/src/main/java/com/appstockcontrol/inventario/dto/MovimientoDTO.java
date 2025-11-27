package com.appstockcontrol.inventario.dto;

import java.time.LocalDateTime;

import com.appstockcontrol.inventario.model.TipoMovimiento;

import lombok.Data;

@Data
public class MovimientoDTO {

    private Long id;
    private Long productoId;
    private TipoMovimiento tipo;
    private Integer cantidad;
    private Integer stockAnterior;
    private Integer stockNuevo;
    private String usuarioResponsable;
    private String motivo;
    private LocalDateTime fechaHora;
}
