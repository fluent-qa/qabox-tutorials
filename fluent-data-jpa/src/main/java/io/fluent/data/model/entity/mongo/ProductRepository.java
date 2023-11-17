package io.fluent.data.model.entity.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface ProductRepository extends MongoRepository<Product, Long>{

}
