package com.appstockcontrol.inventario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.appstockcontrol.inventario.model.MovimientoInventario;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    List<MovimientoInventario> findAllByOrderByFechaHoraDesc();

    List<MovimientoInventario> findByProductoIdOrderByFechaHoraDesc(Long productoId);
}
