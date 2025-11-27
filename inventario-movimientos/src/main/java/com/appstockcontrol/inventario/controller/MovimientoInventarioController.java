package com.appstockcontrol.inventario.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appstockcontrol.inventario.dto.MovimientoDTO;
import com.appstockcontrol.inventario.dto.RegistrarEntradaRequest;
import com.appstockcontrol.inventario.dto.RegistrarSalidaRequest;
import com.appstockcontrol.inventario.service.MovimientoInventarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MovimientoInventarioController {

    private final MovimientoInventarioService movimientoService;

    @GetMapping
    public ResponseEntity<List<MovimientoDTO>> listarMovimientos() {
        return ResponseEntity.ok(movimientoService.listarTodos());
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<MovimientoDTO>> listarMovimientosPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(movimientoService.listarPorProducto(productoId));
    }

    @PostMapping("/entrada")
    public ResponseEntity<MovimientoDTO> registrarEntrada(
            @Valid @RequestBody RegistrarEntradaRequest request,
            @RequestParam int stockAnterior,
            @RequestParam int stockNuevo) {

        return ResponseEntity.ok(
                movimientoService.registrarEntrada(request, stockAnterior, stockNuevo)
        );
    }

    @PostMapping("/salida")
    public ResponseEntity<MovimientoDTO> registrarSalida(
            @Valid @RequestBody RegistrarSalidaRequest request,
            @RequestParam int stockAnterior,
            @RequestParam int stockNuevo) {

        return ResponseEntity.ok(
                movimientoService.registrarSalida(request, stockAnterior, stockNuevo)
        );
    }
}
