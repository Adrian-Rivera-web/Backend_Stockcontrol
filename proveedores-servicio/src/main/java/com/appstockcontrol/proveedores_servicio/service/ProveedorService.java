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

    public List<ProveedorDTO> listarActivos() {
        return proveedorRepository.findByActivoTrue()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ProveedorDTO> buscar(String query) {
        if (query == null || query.isBlank()) {
            return listarActivos();
        }
        return proveedorRepository.buscarActivosPorQuery(query.trim())
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ProveedorDTO obtenerPorId(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Proveedor no encontrado con id " + id));
        return toDto(proveedor);
    }

    public ProveedorDTO crear(ProveedorDTO dto) {
        String email = normalizarEmail(dto.getEmail());

        if (proveedorRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un proveedor con ese email.");
        }

        Proveedor proveedor = toEntity(dto);
        proveedor.setId(null);
        proveedor.setEmail(email);
        proveedor.setActivo(true);

        Proveedor guardado = proveedorRepository.save(proveedor);
        return toDto(guardado);
    }

    public ProveedorDTO actualizar(Long id, ProveedorDTO dto) {
        Proveedor existente = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Proveedor no encontrado con id " + id));

        String emailNuevo = normalizarEmail(dto.getEmail());

        // Si cambia el email, validar que no exista en otro proveedor
        if (!existente.getEmail().equalsIgnoreCase(emailNuevo)
                && proveedorRepository.existsByEmailIgnoreCase(emailNuevo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un proveedor con ese email.");
        }

        existente.setNombre(dto.getNombre());
        existente.setContacto(dto.getContacto());
        existente.setTelefono(dto.getTelefono());
        existente.setEmail(emailNuevo);
        existente.setDireccion(dto.getDireccion());
        existente.setActivo(dto.isActivo());

        Proveedor actualizado = proveedorRepository.save(existente);
        return toDto(actualizado);
    }

    // ✅ BORRADO FÍSICO (hard delete)
    public void eliminar(Long id) {
        if (!proveedorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado con id " + id);
        }

        try {
            proveedorRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            // Si está relacionado con otros registros (FK), evita 500 y devuelve 409
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar el proveedor porque está relacionado con otros registros.",
                    ex
            );
        }
    }

    private String normalizarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email es obligatorio.");
        }
        return email.trim().toLowerCase();
    }

    // ===== Helpers =====
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
