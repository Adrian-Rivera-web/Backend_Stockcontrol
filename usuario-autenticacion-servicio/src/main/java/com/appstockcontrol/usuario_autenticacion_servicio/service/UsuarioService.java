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
    private final BCryptPasswordEncoder passwordEncoder;

    // ========== Registro ==========
    public UsuarioResponse registrar(RegistroRequest request) {

        if (!request.isAceptaTerminos()) {
            throw new IllegalArgumentException("Debes aceptar los términos y condiciones.");
        }

        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException("El correo ya se encuentra registrado.");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .correo(request.getCorreo())
                .telefono(request.getTelefono())
                .direccion(request.getDireccion())
                .clave(passwordEncoder.encode(request.getClave()))
                .esAdmin(false)   // siempre usuario normal al registrarse
                .activo(true)
                .build();

        Usuario guardado = usuarioRepository.save(usuario);
        return toResponse(guardado);
    }

    // ========== Login ==========
    public UsuarioResponse login(LoginRequest request) {

        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new IllegalArgumentException("Correo o contraseña incorrectos."));

        if (!usuario.isActivo()) {
            throw new IllegalStateException("El usuario se encuentra inactivo.");
        }

        if (!passwordEncoder.matches(request.getClave(), usuario.getClave())) {
            throw new IllegalArgumentException("Correo o contraseña incorrectos.");
        }

        return toResponse(usuario);
    }

    // ========== CRUD básico para admin ==========
    public List<UsuarioResponse> obtenerTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UsuarioResponse obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id " + id));
        return toResponse(usuario);
    }

    public void eliminar(Long id) {
        // si quieres borrado lógico, cambia a setActivo(false) y save
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
