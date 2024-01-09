package pl.test.learn.productdemo.models.mappers;

import org.mapstruct.*;
import pl.test.learn.productdemo.models.Product;
import pl.test.learn.productdemo.models.dtos.ProductDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    Product toEntity(ProductDTO productDTO);

    ProductDTO toDto(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product partialUpdate(ProductDTO productDTO, @MappingTarget Product product);
}