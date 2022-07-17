package yuriua.es.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import yuriua.es.entity.Product;

@Repository
public interface ProductDao extends ElasticsearchRepository<Product, Long> {

}