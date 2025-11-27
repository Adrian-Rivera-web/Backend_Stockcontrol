package com.appstockcontrol.proveedores_servicio.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appstockcontrol.proveedores_servicio.dto.ProveedorDTO;
import com.appstockcontrol.proveedores_servicio.model.Proveedor;
import com.appstockcontrol.proveedores_servicio.repository.ProveedorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    // ===== Listar todos los proveedores activos =====
    public List<ProveedorDTO> listarActivos() {
        return proveedorRepository.findByActivoTrue()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ===== Buscar proveedores por texto =====
    public List<ProveedorDTO> buscar(String query) {
        if (query == null || query.isBlank()) {
            return listarActivos();
        }
        return proveedorRepository.buscarActivosPorQuery(query.trim())
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ===== Obtener por id =====
    public ProveedorDTO obtenerPorId(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id " + id));
        return toDto(proveedor);
    }

    // ===== Crear nuevo proveedor =====
    public ProveedorDTO crear(ProveedorDTO dto) {

        if (proveedorRepository.existsByEmail(dto.getEmail().trim().toLowerCase())) {
            throw new IllegalArgumentException("Ya existe un proveedor con ese email.");
        }

        Proveedor proveedor = toEntity(dto);
        proveedor.setId(null); // asegurar que sea nuevo
        proveedor.setActivo(true);

        Proveedor guardado = proveedorRepository.save(proveedor);
        return toDto(guardado);
    }

    // ===== Actualizar proveedor =====
    public ProveedorDTO actualizar(Long id, ProveedorDTO dto) {
        Proveedor existente = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id " + id));

        existente.setNombre(dto.getNombre());
        existente.setContacto(dto.getContacto());
        existente.setTelefono(dto.getTelefono());
        existente.setEmail(dto.getEmail());
        existente.setDireccion(dto.getDireccion());
        existente.setActivo(dto.isActivo());

        Proveedor actualizado = proveedorRepository.save(existente);
        return toDto(actualizado);
    }

    // ===== Eliminar (borrado lógico) =====
    public void eliminar(Long id) {
        Proveedor existente = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id " + id));
        existente.setActivo(false);
        proveedorRepository.save(existente);
    }

    // ===== Helpers de mapeo =====
    private ProveedorDTO toDto(Proveedor proveedor) {
        ProveedorDTO dto = new ProveedorDTO();
        dto.setId(proveedor.getId());
        dto.setNombre(proveedor.getNombre());
        dto.setContacto(proveedor.getContacto());
        dto.setTelefono(proveedor.getTelefono());
        dto.setEmail(proveedor.getEmail());
        dto.setDireccion(proveedor.getDireccion());
        dto.setActivo(proveedor.isActivo());
        return dto;
    }

    private Proveedor toEntity(ProveedorDTO dto) {
        return Proveedor.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .contacto(dto.getContacto())
                .telefono(dto.getTelefono())
                .email(dto.getEmail())
                .direccion(dto.getDireccion())
                .activo(dto.isActivo())
                .build();
    }
}
