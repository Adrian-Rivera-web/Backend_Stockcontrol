package com.appstockcontrol.catalogo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.appstockcontrol.catalogo.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByActivoTrue();

    List<Producto> findByActivoTrueAndStockActualLessThanEqual(Integer stock);

    List<Producto> findByActivoTrueAndCategoriaId(Long categoriaId);

    boolean existsByCategoriaId(Long categoriaId);
}
