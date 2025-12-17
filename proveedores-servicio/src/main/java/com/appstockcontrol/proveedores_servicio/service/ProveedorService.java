package com.appstockcontrol.proveedores_servicio.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    // ===== Obtener por ID =====
    public ProveedorDTO obtenerPorId(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Proveedor no encontrado con id " + id));
        return toDto(proveedor);
    }

    // ===== Crear proveedor =====
    public ProveedorDTO crear(ProveedorDTO dto) {

        String email = (dto.getEmail() == null) ? null : dto.getEmail().trim().toLowerCase();
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }

        // Si existía un proveedor INACTIVO con el mismo email (por borrado lógico anterior),
        // lo eliminamos físicamente para liberar el email.
        proveedorRepository.findByEmailIgnoreCase(email).ifPresent(existing -> {
            if (!existing.isActivo()) {
                proveedorRepository.deleteById(existing.getId());
            } else {
                throw new IllegalArgumentException("Ya existe un proveedor con ese email.");
            }
        });

        Proveedor proveedor = toEntity(dto);
        proveedor.setId(null); // asegurar que sea nuevo
        proveedor.setEmail(email);
        proveedor.setActivo(true);

        Proveedor guardado = proveedorRepository.save(proveedor);
        return toDto(guardado);
    }

    // ===== Actualizar proveedor =====
    public ProveedorDTO actualizar(Long id, ProveedorDTO dto) {
        Proveedor existente = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Proveedor no encontrado con id " + id));

        String nuevoEmail = (dto.getEmail() == null) ? null : dto.getEmail().trim().toLowerCase();
        if (nuevoEmail == null || nuevoEmail.isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }

        // Si cambia el email, validamos que no exista en otro proveedor
        if (!existente.getEmail().equalsIgnoreCase(nuevoEmail)
                && proveedorRepository.existsByEmailIgnoreCase(nuevoEmail)) {
            throw new IllegalArgumentException("Ya existe un proveedor con ese email.");
        }

        existente.setNombre(dto.getNombre());
        existente.setContacto(dto.getContacto());
        existente.setTelefono(dto.getTelefono());
        existente.setEmail(nuevoEmail);
        existente.setDireccion(dto.getDireccion());
        existente.setActivo(dto.isActivo());

        Proveedor actualizado = proveedorRepository.save(existente);
        return toDto(actualizado);
    }

    // ===== Eliminar (borrado físico) =====
    public void eliminar(Long id) {
        if (!proveedorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado con id " + id);
        }

        try {
            proveedorRepository.deleteById(id); // ✅ hard delete
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar el proveedor porque está relacionado con otros registros.",
                    ex
            );
        }
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
