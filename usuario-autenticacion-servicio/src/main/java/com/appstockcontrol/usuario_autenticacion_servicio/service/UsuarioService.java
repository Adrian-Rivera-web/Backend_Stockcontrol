package com.appstockcontrol.usuario_autenticacion_servicio.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appstockcontrol.usuario_autenticacion_servicio.dto.LoginRequest;
import com.appstockcontrol.usuario_autenticacion_servicio.dto.RegistroRequest;
import com.appstockcontrol.usuario_autenticacion_servicio.dto.UsuarioResponse;
import com.appstockcontrol.usuario_autenticacion_servicio.model.Usuario;
import com.appstockcontrol.usuario_autenticacion_servicio.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ========== REGISTRO ==========
    public UsuarioResponse registrar(RegistroRequest request) {
        if (!request.isAceptaTerminos()) {
            throw new IllegalArgumentException("Debes aceptar los términos y condiciones.");
        }

        if (usuarioRepository.existsByCorreo(request.getCorreo().trim().toLowerCase())) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con ese correo.");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre().trim())
                .correo(request.getCorreo().trim().toLowerCase())
                .telefono(request.getTelefono().trim())
                .direccion(request.getDireccion().trim())
                .clave(passwordEncoder.encode(request.getClave())) // hasheamos la contraseña
                .esAdmin(false) // registro normal
                .activo(true)
                .build();

        Usuario guardado = usuarioRepository.save(usuario);
        return toResponse(guardado);
    }

    // ========== LOGIN ==========
    public UsuarioResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo().trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no registrado."));

        if (!usuario.isActivo()) {
            throw new IllegalArgumentException("Usuario inactivo.");
        }

        if (!passwordEncoder.matches(request.getClave(), usuario.getClave())) {
            throw new IllegalArgumentException("Credenciales incorrectas.");
        }

        return toResponse(usuario);
    }

    // ========== ADMIN: LISTAR / OBTENER / ELIMINAR ==========
    public List<UsuarioResponse> obtenerTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UsuarioResponse obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        return toResponse(usuario);
    }

    public void eliminar(Long id) {
        // puedes hacer borrado lógico si prefieres: setActivo(false)
        usuarioRepository.deleteById(id);
    }

    // ========== Mapeo Entity -> DTO ==========
    private UsuarioResponse toResponse(Usuario usuario) {
        UsuarioResponse dto = new UsuarioResponse();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setCorreo(usuario.getCorreo());
        dto.setTelefono(usuario.getTelefono());
        dto.setDireccion(usuario.getDireccion());
        dto.setEsAdmin(usuario.isEsAdmin());
        return dto;
    }
}