package com.appstockcontrol.catalogo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.appstockcontrol.catalogo.model.Categoria;
import com.appstockcontrol.catalogo.model.Producto;
import com.appstockcontrol.catalogo.repository.CategoriaRepository;
import com.appstockcontrol.catalogo.repository.ProductoRepository;

@Service
@Transactional
public class CatalogoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public CatalogoService(ProductoRepository productoRepository,
                           CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // ================== CATEGORÍAS ==================

    public List<Categoria> listarCategoriasActivas() {
        return categoriaRepository.findByActivaTrue();
    }

    public Categoria obtenerCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id " + id));
    }

    public Categoria crearCategoria(Categoria categoria) {
        categoria.setId(null);
        if (categoria.getActiva() == null) {
            categoria.setActiva(true);
        }
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizarCategoria(Long id, Categoria datos) {
        Categoria existente = obtenerCategoria(id);
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());

        if (datos.getActiva() != null) {
            existente.setActiva(datos.getActiva());
        }

        return categoriaRepository.save(existente);
    }

    @Deprecated
    public void eliminarLogicoCategoria(Long id) {
        eliminarCategoria(id);
    }

    public void eliminarCategoria(Long id) {
        // valida existencia
        obtenerCategoria(id);

        if (productoRepository.existsByCategoriaId(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar la categoría porque tiene productos asociados. Elimina o reasigna los productos primero."
            );
        }

        categoriaRepository.deleteById(id);
    }

    // ================== PRODUCTOS ==================

    public List<Producto> listarProductosActivos() {
        return productoRepository.findByActivoTrue();
    }

    public List<Producto> listarProductosPorCategoria(Long categoriaId) {
        return productoRepository.findByActivoTrueAndCategoriaId(categoriaId);
    }

    public Producto obtenerProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id " + id));
    }

    public Producto crearProducto(Producto producto) {
        if (producto.getCategoria() == null || producto.getCategoria().getId() == null) {
            throw new IllegalArgumentException("El producto debe tener una categoría válida");
        }

        Categoria categoria = obtenerCategoria(producto.getCategoria().getId());
        producto.setId(null);
        producto.setCategoria(categoria);

        if (producto.getActivo() == null) {
            producto.setActivo(true);
        }

        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Long id, Producto datos) {
        Producto existente = obtenerProducto(id);

        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        existente.setStockActual(datos.getStockActual());
        existente.setStockMinimo(datos.getStockMinimo());
        existente.setPrecioUnitario(datos.getPrecioUnitario());

        if (datos.getCategoria() != null && datos.getCategoria().getId() != null) {
            Categoria categoria = obtenerCategoria(datos.getCategoria().getId());
            existente.setCategoria(categoria);
        }

        if (datos.getActivo() != null) {
            existente.setActivo(datos.getActivo());
        }

        return productoRepository.save(existente);
    }

    @Deprecated
    public void eliminarLogicoProducto(Long id) {
        eliminarProducto(id);
    }

    public void eliminarProducto(Long id) {
        // valida existencia
        obtenerProducto(id);
        productoRepository.deleteById(id);
    }

    public List<Producto> productosBajoStock() {
        return productoRepository.findByActivoTrue()
                .stream()
                .filter(p -> p.getStockActual() != null
                        && p.getStockMinimo() != null
                        && p.getStockActual() <= p.getStockMinimo())
                .collect(Collectors.toList());
    }
}
