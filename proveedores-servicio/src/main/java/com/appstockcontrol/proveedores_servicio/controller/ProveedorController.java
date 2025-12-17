package com.appstockcontrol.proveedores_servicio.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appstockcontrol.proveedores_servicio.dto.ProveedorDTO;
import com.appstockcontrol.proveedores_servicio.service.ProveedorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProveedorController {

    private final ProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<List<ProveedorDTO>> listarProveedores() {
        return ResponseEntity.ok(proveedorService.listarActivos());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProveedorDTO>> buscarProveedores(@RequestParam("query") String query) {
        return ResponseEntity.ok(proveedorService.buscar(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorDTO> obtenerProveedor(@PathVariable Long id) {
        return ResponseEntity.ok(proveedorService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProveedorDTO> crearProveedor(@Valid @RequestBody ProveedorDTO dto) {
        return ResponseEntity.ok(proveedorService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorDTO> actualizarProveedor(
            @PathVariable Long id,
            @Valid @RequestBody ProveedorDTO dto) {
        return ResponseEntity.ok(proveedorService.actualizar(id, dto));
    }

    // ✅ Eliminar proveedor (borrado físico)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProveedor(@PathVariable Long id) {
        proveedorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
