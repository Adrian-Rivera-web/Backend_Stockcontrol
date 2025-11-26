package com.appstockcontrol.usuario_autenticacion_servicio.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.appstockcontrol.usuario_autenticacion_servicio.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreo(String correo);

    boolean existsByCorreo(String correo);
}