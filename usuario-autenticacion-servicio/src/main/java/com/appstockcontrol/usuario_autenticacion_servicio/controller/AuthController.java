package com.appstockcontrol.usuario_autenticacion_servicio.controller;

import com.appstockcontrol.usuario_autenticacion_servicio.dto.LoginRequest;
import com.appstockcontrol.usuario_autenticacion_servicio.dto.RegistroRequest;
import com.appstockcontrol.usuario_autenticacion_servicio.dto.UsuarioResponse;
import com.appstockcontrol.usuario_autenticacion_servicio.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        UsuarioResponse response = usuarioService.registrar(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponse> login(@Valid @RequestBody LoginRequest request) {
        UsuarioResponse response = usuarioService.login(request);
        return ResponseEntity.ok(response);
    }
}