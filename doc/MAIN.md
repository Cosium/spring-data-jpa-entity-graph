## Quick start
1. In addition to `spring-data-jpa`, add the library dependency :
    
    ```xml
    <dependency>
        <groupId>com.cosium.spring.data</groupId>
        <artifactId>spring-data-jpa-entity-graph</artifactId>
        <version>${spring-data-jpa-entity-graph.version}</version>
    </dependency>
    ```
2. In your Spring configuration, set the repository factory bean class to `EntityGraphJpaRepositoryFactoryBean` :
    
    ```java
    @Configuration
    @EnableJpaRepositories(repositoryFactoryBeanClass = EntityGraphJpaRepositoryFactoryBean.class)
    public class DataRepositoryConfiguration {
        //...
    }
    ```
3. Make sure your repositories extend the Spring Data standard ones or the library provided repositories: 
- `EntityGraphJpaRepository` which is equivalent to standard `JpaRepository`
- `EntityGraphJpaSpecificationExecutor` which is equivalent to standard `JpaSpecificationExecutor`
- `EntityGraphQuerydslPredicateExecutor` which is equivalent to standard `QuerydslPredicateExecutor`
- `EntityGraphCrudRepository` which is equivalent to standard `CrudRepository`
- `EntityGraphPagingAndSortingRepository` which is equivalent to standard `PagingAndSortingRepository`
- `EntityGraphQueryByExampleExecutor` which is equivalent to standard `QueryByExampleExecutor`

## Basic Usage

Let's consider the following entities and repository :
```java
@Entity
public class Brand {
    @Id
    private long id = 0;
    private String name;
    //...
}
```
```java
@NamedEntityGraphs(value = {
    @NamedEntityGraph(name = "Product.brand", attributeNodes = {
        @NamedAttributeNode("brand")
    })
})
@Entity
public class Product {
    @Id
    private long id = 0;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private Brand brand;
    //...
}	
```
```java
public interface ProductRepository extends EntityGraphJpaRepository<Product, Long> {
    List<Product> findByName(String name, EntityGraph entityGraph);
}
```

You can pass the entity graph to the `findByName` method :
```java
// This will apply 'Product.brand' named EntityGraph to findByName
productRepository.findByName("MyProduct", EntityGraphs.named("Product.brand"));
```

Or to the `findOne` method :
```java
// This will apply 'Product.brand' named EntityGraph to findOne
productRepository.findById(1L, EntityGraphs.named("Product.brand"));
```

Or any method you like.

You can also pass a dynamically built EntityGraph by using `DynamicEntityGraph`, it's also accessible through a helper method:

```java
productRepository.findById(1L, EntityGraphUtils.fromAttributePaths("brand", "maker"));
```

This is similar to [Spring's ad-hoc attribute paths](http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-property-expressions),
and equivalent to writing this in your repository's interface:
```java
@EntityGraph(attributePaths = { "brand", "maker" })
Product findById(Long id);
```

## Default EntityGraph

For an Entity, you can define its default EntityGraph.  
An Entity default EntityGraph will be used each time the Entity repository method is called without EntityGraph.  

A default EntityGraph name must end with `.default`. 

```java
@NamedEntityGraphs(value = {
    @NamedEntityGraph(name = "Product.default", attributeNodes = {
        @NamedAttributeNode("brand")
    })
})
@Entity
public class Product {
    @Id
    private long id = 0;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private Brand brand;
    //...
}	
```
```java
// This call will make use of "Product.default" EntityGraph.
productRepository.findById(1L);
```
