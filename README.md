# Spring Data JPA EntityGraph

Today, [Spring Data JPA](https://github.com/spring-projects/spring-data-jpa) supports EntityGraph exlusively through annotations.  
Thus, for a method, the choice of EntityGraph must be made before compilation.  

This extension gives the ability to pass EntityGraph on any Spring Data JPA repository method as an argument, making the EntityGraph choice fully dynamic.

Example:
```java
productRepository.findByName("foo", EntityGraphUtils.fromName("Product.brand"));
```

## Compatibility

This library follows the Spring Data JPA versionning semantic.

spring-data-jpa-entity-graph | spring-data-jpa
---------------------------- | ---------------
1.11.x | 1.11.y
1.10.x | 1.10.y

## Quick start

1. In addition to spring-data-jpa, add the library dependency :
    
    ```xml
    <dependency>
        <groupId>com.cosium.spring.data</groupId>
        <artifactId>spring-data-jpa-entity-graph</artifactId>
        <version>1.10.07</version>
    </dependency>
    ```
2. In your Spring configuration, set the repository factory bean class to `JpaEntityGraphRepositoryFactoryBean` :
    
    ```java
    @Configuration
    @EnableJpaRepositories(repositoryFactoryBeanClass = JpaEntityGraphRepositoryFactoryBean.class)
    public class DataRepositoryConfiguration {
        ...
    }
    ```
3. Make sure your repositories extend `JpaEntityGraphRepository`, `JpaEntityGraphSpecificationExecutor` and/or `JpaEntityGraphQueryDslPredicateExecutor`

## Basic Usage

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
public interface ProductRepository extends JpaEntityGraphRepository<Product, Long> {
    List<Product> findByName(String name, EntityGraph entityGraph);
}
```

You can pass the entity graph to the `findByName` method :
```java
productRepository.findByName("MyProduct", EntityGraphUtils.fromName("Product.brand"));
```

Or to the `findOne` method :
```java
productRepository.findOne(1L, EntityGraphUtils.fromName("Product.brand"));
```

Or any method you like.

You can also pass a dynamically built EntityGraph by using DynamicEntityGraph implementation.

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
productRepository.findOne(1L);
```
