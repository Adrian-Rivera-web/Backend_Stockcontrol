package com.appstockcontrol.inventario.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appstockcontrol.inventario.dto.MovimientoDTO;
import com.appstockcontrol.inventario.dto.RegistrarEntradaRequest;
import com.appstockcontrol.inventario.dto.RegistrarSalidaRequest;
import com.appstockcontrol.inventario.model.MovimientoInventario;
import com.appstockcontrol.inventario.model.TipoMovimiento;
import com.appstockcontrol.inventario.repository.MovimientoInventarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoRepo;

    public List<MovimientoDTO> listarTodos() {
        return movimientoRepo.findAllByOrderByFechaHoraDesc()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<MovimientoDTO> listarPorProducto(Long productoId) {
        return movimientoRepo.findByProductoIdOrderByFechaHoraDesc(productoId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public MovimientoDTO registrarEntrada(RegistrarEntradaRequest request,
                                          int stockAnterior,
                                          int stockNuevo) {

        MovimientoInventario mov = MovimientoInventario.builder()
                .productoId(request.getProductoId())
                .tipo(TipoMovimiento.ENTRADA)
                .cantidad(request.getCantidad())
                .stockAnterior(stockAnterior)
                .stockNuevo(stockNuevo)
                .usuarioResponsable(request.getUsuarioResponsable())
                .motivo(request.getMotivo())
                .fechaHora(LocalDateTime.now())
                .build();

        return toDto(movimientoRepo.save(mov));
    }

    public MovimientoDTO registrarSalida(RegistrarSalidaRequest request,
                                         int stockAnterior,
                                         int stockNuevo) {

        MovimientoInventario mov = MovimientoInventario.builder()
                .productoId(request.getProductoId())
                .tipo(TipoMovimiento.SALIDA)
                .cantidad(request.getCantidad())
                .stockAnterior(stockAnterior)
                .stockNuevo(stockNuevo)
                .usuarioResponsable(request.getUsuarioResponsable())
                .motivo(request.getMotivo())
                .fechaHora(LocalDateTime.now())
                .build();

        return toDto(movimientoRepo.save(mov));
    }

    private MovimientoDTO toDto(MovimientoInventario mov) {
        MovimientoDTO dto = new MovimientoDTO();
        dto.setId(mov.getId());
        dto.setProductoId(mov.getProductoId());
        dto.setTipo(mov.getTipo());
        dto.setCantidad(mov.getCantidad());
        dto.setStockAnterior(mov.getStockAnterior());
        dto.setStockNuevo(mov.getStockNuevo());
        dto.setUsuarioResponsable(mov.getUsuarioResponsable());
        dto.setMotivo(mov.getMotivo());
        dto.setFechaHora(mov.getFechaHora());
        return dto;
    }
}
