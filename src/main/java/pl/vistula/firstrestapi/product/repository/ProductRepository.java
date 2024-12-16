package pl.vistula.firstrestapi.product.repository;

import org.springframework.stereotype.Repository;
import pl.vistula.firstrestapi.product.domain.Product;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ProductRepository {

    protected final Map<Long, Product> map = new HashMap<>();

    protected long counter = 1;

    public Product save(Product entity) {
        return setId(entity);
//        return entity;
    }

    private Product setId(Product entity) {
        entity.setId(counter);
        map.put(counter, entity);
        counter++;
        return entity;
    }
}
