package pl.vistula.firstrestapi.product.support;

import org.springframework.stereotype.Component;
import pl.vistula.firstrestapi.product.api.request.ProductRequest;
import pl.vistula.firstrestapi.product.api.response.ProductResponse;
import pl.vistula.firstrestapi.product.domain.Product;

@Component
public class ProductMapper {

    public Product toProduct(ProductRequest productRequest) {
        return new Product(productRequest.getName());
    }

    public ProductResponse toProductResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName());
    }
}
