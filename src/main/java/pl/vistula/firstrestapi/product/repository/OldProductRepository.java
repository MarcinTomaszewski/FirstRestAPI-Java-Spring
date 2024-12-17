package pl.vistula.firstrestapi.product.repository;

import org.springframework.stereotype.Repository;
import pl.vistula.firstrestapi.product.domain.Product;

import java.util.*;

@Repository
public class OldProductRepository {

    protected final Map<Long, Product> map = new HashMap<>();

    protected long counter = 1;

    public Product save(Product entity) {
        return setId(entity);
//        return entity;
    }

    public Optional<Product> findById(long id) {
        return Optional.ofNullable(map.get(id));
    }

    private Product setId(Product entity) {
        if (entity.getId() != null) {
            map.put(entity.getId(), entity);
        } else {
            entity.setId(counter);
            map.put(counter, entity);
            counter++;
        }
        return entity;
    }

    public List<Product> findAll() {
        return new ArrayList<>(map.values());
    }

    public void deleteById(Long id) {
        map.remove(id);
    }
}
