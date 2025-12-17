package com.appstockcontrol.catalogo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appstockcontrol.catalogo.assembler.ProductoModelAssembler;
import com.appstockcontrol.catalogo.dto.ProductoDTO;
import com.appstockcontrol.catalogo.model.Categoria;
import com.appstockcontrol.catalogo.model.Producto;
import com.appstockcontrol.catalogo.service.CatalogoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/catalogo")
@RequiredArgsConstructor
public class CatalogoController {

    private final CatalogoService catalogoService;
    private final ProductoModelAssembler productoModelAssembler;

    // ============ CATEGORÍAS ============

    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> listarCategorias() {
        return ResponseEntity.ok(catalogoService.listarCategoriasActivas());
    }

    @GetMapping("/categorias/{id}")
    public ResponseEntity<Categoria> obtenerCategoria(@PathVariable Long id) {
        return ResponseEntity.ok(catalogoService.obtenerCategoria(id));
    }

    @PostMapping("/categorias")
    public ResponseEntity<Categoria> crearCategoria(@Valid @RequestBody Categoria categoria) {
        Categoria creada = catalogoService.crearCategoria(categoria);
        return ResponseEntity.ok(creada);
    }

    @PutMapping("/categorias/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable Long id,
                                                         @Valid @RequestBody Categoria datos) {
        return ResponseEntity.ok(catalogoService.actualizarCategoria(id, datos));
    }

    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        catalogoService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }

    // ============ PRODUCTOS ============

    @GetMapping("/productos")
    public ResponseEntity<CollectionModel<EntityModel<ProductoDTO>>> listarProductos() {
        List<Producto> productos = catalogoService.listarProductosActivos();

        List<EntityModel<ProductoDTO>> modelos = productos.stream()
                .map(productoModelAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(
                        modelos,
                        linkTo(methodOn(CatalogoController.class).listarProductos()).withSelfRel()
                )
        );
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<EntityModel<ProductoDTO>> obtenerProducto(@PathVariable Long id) {
        Producto producto = catalogoService.obtenerProducto(id);
        return ResponseEntity.ok(productoModelAssembler.toModel(producto));
    }

    @GetMapping("/productos/categoria/{categoriaId}")
    public ResponseEntity<CollectionModel<EntityModel<ProductoDTO>>> productosPorCategoria(
            @PathVariable Long categoriaId) {

        List<Producto> productos = catalogoService.listarProductosPorCategoria(categoriaId);
        List<EntityModel<ProductoDTO>> modelos = productos.stream()
                .map(productoModelAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(
                        modelos,
                        linkTo(methodOn(CatalogoController.class).productosPorCategoria(categoriaId)).withSelfRel()
                )
        );
    }

    @GetMapping("/productos/bajo-stock")
    public ResponseEntity<CollectionModel<EntityModel<ProductoDTO>>> productosBajoStock() {
        List<Producto> productos = catalogoService.productosBajoStock();
        List<EntityModel<ProductoDTO>> modelos = productos.stream()
                .map(productoModelAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(
                        modelos,
                        linkTo(methodOn(CatalogoController.class).productosBajoStock()).withSelfRel()
                )
        );
    }

    @PostMapping("/productos")
    public ResponseEntity<EntityModel<ProductoDTO>> crearProducto(@Valid @RequestBody Producto producto) {
        Producto creado = catalogoService.crearProducto(producto);
        return ResponseEntity.ok(productoModelAssembler.toModel(creado));
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<EntityModel<ProductoDTO>> actualizarProducto(@PathVariable Long id,
                                                                       @Valid @RequestBody Producto datos) {
        Producto actualizado = catalogoService.actualizarProducto(id, datos);
        return ResponseEntity.ok(productoModelAssembler.toModel(actualizado));
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        catalogoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
