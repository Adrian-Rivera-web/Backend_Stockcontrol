package com.appstockcontrol.usuario_autenticacion_servicio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.appstockcontrol.usuario_autenticacion_servicio.model.Usuario;
import com.appstockcontrol.usuario_autenticacion_servicio.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // Crea un admin solo la primera vez
        if (usuarioRepository.count() == 0) {
            Usuario admin = Usuario.builder()
                    .nombre("Adrian")
                    .correo("ad.rivera@duocuc.cl")
                    .telefono("+56936778106")
                    .direccion("Duoc UC")
                    .clave(passwordEncoder.encode("Admin_123"))
                    .esAdmin(true)
                    .activo(true)
                    .build();

            usuarioRepository.save(admin);
        }
    }
}
