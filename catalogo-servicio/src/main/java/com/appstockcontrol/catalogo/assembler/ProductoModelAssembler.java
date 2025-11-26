package com.appstockcontrol.catalogo.assembler;

import java.math.BigDecimal;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.appstockcontrol.catalogo.controller.CatalogoController;
import com.appstockcontrol.catalogo.dto.ProductoDTO;
import com.appstockcontrol.catalogo.model.Producto;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<Producto, EntityModel<ProductoDTO>> {

    @Override
    public EntityModel<ProductoDTO> toModel(Producto producto) {
        ProductoDTO dto = toDto(producto);

        return EntityModel.of(
                dto,
                linkTo(methodOn(CatalogoController.class).obtenerProducto(producto.getId())).withSelfRel(),
                linkTo(methodOn(CatalogoController.class).listarProductos()).withRel("productos")
        );
    }

    public ProductoDTO toDto(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());

        if (producto.getPrecioUnitario() != null) {
            dto.setPrecio(BigDecimal.valueOf(producto.getPrecioUnitario()));
        }

        dto.setStock(producto.getStockActual());
        dto.setActivo(producto.getActivo());

        if (producto.getCategoria() != null) {
            dto.setCategoriaId(producto.getCategoria().getId());
        }

        return dto;
    }
}
