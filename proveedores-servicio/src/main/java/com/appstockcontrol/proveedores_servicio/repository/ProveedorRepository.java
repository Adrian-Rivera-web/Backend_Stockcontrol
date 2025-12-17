package com.appstockcontrol.proveedores_servicio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.appstockcontrol.proveedores_servicio.model.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    List<Proveedor> findByActivoTrue();

    @Query("""
           SELECT p FROM Proveedor p
           WHERE p.activo = true
             AND (
                LOWER(p.nombre)   LIKE LOWER(CONCAT('%', :query, '%')) OR
                LOWER(p.contacto) LIKE LOWER(CONCAT('%', :query, '%')) OR
                LOWER(p.email)    LIKE LOWER(CONCAT('%', :query, '%'))
             )
           """)
    List<Proveedor> buscarActivosPorQuery(@Param("query") String query);

    boolean existsByEmailIgnoreCase(String email);

    Optional<Proveedor> findByEmailIgnoreCase(String email);
}

