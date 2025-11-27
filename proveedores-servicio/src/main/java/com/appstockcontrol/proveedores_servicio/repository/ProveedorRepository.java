package com.appstockcontrol.proveedores_servicio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.appstockcontrol.proveedores_servicio.model.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    // Para listar solo proveedores activos
    List<Proveedor> findByActivoTrue();

    // Buscar por nombre, contacto o email (solo activos)
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

    boolean existsByEmail(String email);
}
