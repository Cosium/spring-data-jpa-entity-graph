### Why?

Today, Spring Data JPA supports EntityGraph exlusively through annotations.
Thus, for a method, the choice of EntityGraph must be made before compilation.
This library gives the ability to pass EntityGraph on any Spring Data JPA repository method as an argument, making the EntityGraph choice fully dynamic.

### Quick start in 2 steps

Add the dependency :

```xml
<dependency>
    <groupId>com.cosium.spring.data</groupId>
    <artifactId>spring-data-jpa-entity-graph</artifactId>
    <version>1.11.00-SNAPSHOT</version>
</dependency>
```

In your Spring configuration, set the repository factory bean class to `JpaEntityGraphRepositoryFactoryBean` :

```java
@Configuration
@EnableJpaRepositories(repositoryFactoryBeanClass = JpaEntityGraphRepositoryFactoryBean.class)
public class DataRepositoryConfiguration {
    ...
}
```

### Usage

Let's consider the following entities and repositories:
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
public interface ProductRepository extends JpaEntityGraphRepository<Product, Long> {
    List<Product> findByName(String name, EntityGraph entityGraph);
}
```

You can pass the entity graph to the `findByName` method:
```java
productRepository.findByName("MyProduct", EntityGraphUtils.fromName("Product.brand");
```

Or to the findOne method:
```java
productRepository.findOne(1L, EntityGraphUtils.fromName("Product.brand");
```

You can also pass a dynamically built EntityGraph by using DynamicEntityGraph implementation.