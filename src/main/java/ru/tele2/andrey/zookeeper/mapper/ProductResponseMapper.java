package ru.tele2.andrey.zookeeper.mapper;

import org.mapstruct.*;
import ru.tele2.andrey.zookeeper.enumeration.ProductResponseStatus;
import ru.tele2.andrey.zookeeper.model.ProductResponse;
import ru.tele2.andrey.zookeeper.webclient.product.entity.Product;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductResponseMapper {

    @Mappings({
            @Mapping(target = "id", source = "product.id"),
            @Mapping(target = "productName", source = "product.productName")
    })
    ProductResponse fromProduct(Product product);

    @AfterMapping
    default void setStatus(@MappingTarget ProductResponse productResponse) {
        productResponse.setStatus(ProductResponseStatus.BP_OK);
    }
}
