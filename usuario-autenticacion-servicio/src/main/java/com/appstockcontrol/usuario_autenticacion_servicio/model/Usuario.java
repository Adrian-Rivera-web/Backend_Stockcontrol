package com.appstockcontrol.usuario_autenticacion_servicio.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 120)
    private String correo; // usado para login

    @Column(nullable = false, length = 255)
    private String clave;  // contraseña HASHEADA (BCrypt)

    @Column(nullable = false, length = 200)
    private String direccion;

    @Column(length = 20)
    private String telefono;

    @Column(nullable = false)
    private boolean esAdmin = false;

    @Column(nullable = false)
    private boolean activo = true;
}