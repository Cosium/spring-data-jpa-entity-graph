[![Gitter](https://badges.gitter.im/Cosium/spring-data-jpa-entity-graph.svg)](https://gitter.im/Cosium/spring-data-jpa-entity-graph?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
[![Travis branch](https://img.shields.io/travis/Cosium/spring-data-jpa-entity-graph/master.svg)](https://travis-ci.org/Cosium/spring-data-jpa-entity-graph)
[![Codecov branch](https://img.shields.io/codecov/c/github/Cosium/spring-data-jpa-entity-graph/master.svg)](https://codecov.io/gh/Cosium/spring-data-jpa-entity-graph)
[![Maven Central](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph.svg)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)

# Spring Data JPA EntityGraph

Today, [Spring Data JPA](https://github.com/spring-projects/spring-data-jpa) supports EntityGraph exlusively through annotations.  
Thus, for a method, the choice of EntityGraph must be made before compilation.  

This extension gives the ability to pass EntityGraph on any Spring Data JPA repository method as an argument, making the EntityGraph choice fully dynamic.

Example:
```java
productRepository.findByName("foo", EntityGraphUtils.fromName("Product.brand"));
```

## How to select the correct version

This library follows the Spring Data JPA versionning semantic.

spring-data-jpa branches | spring-data-jpa-entity-graph.version | Documentation
---------------------------- | --------------- | -----------------
1.12.x | Not released yet | Current one
1.11.x | 1.11.01 | [1.11x documentation](https://github.com/Cosium/spring-data-jpa-entity-graph/blob/1.11.x/README.md)
1.10.x | 1.10.14 | [1.10x documentation](https://github.com/Cosium/spring-data-jpa-entity-graph/blob/1.10.x/README.md)

Example: if you were using `spring-data-jpa 1.10.13` in your project, you would need to select any `spring-data-jpa-entity-graph 1.10.x`. Thus `spring-data-jpa-entity-graph 1.10.14` would be eligible.

## Quick start
1. Select the `spring-data-jpa-entity-graph.version` from the above compatibility table
2. In addition to `spring-data-jpa`, add the library dependency :
    
    ```xml
    <dependency>
        <groupId>com.cosium.spring.data</groupId>
        <artifactId>spring-data-jpa-entity-graph</artifactId>
        <version>${spring-data-jpa-entity-graph.version}</version>
    </dependency>
    ```
3. In your Spring configuration, set the repository factory bean class to `EntityGraphJpaRepositoryFactoryBean` :
    
    ```java
    @Configuration
    @EnableJpaRepositories(repositoryFactoryBeanClass = EntityGraphJpaRepositoryFactoryBean.class)
    public class DataRepositoryConfiguration {
        //...
    }
    ```
4. Make sure your repositories extend the Spring Data standard ones or the library provided repositories: 
- `EntityGraphJpaRepository` which is equivalent to standard `JpaRepository`
- `EntityGraphJpaSpecificationExecutor` which is equivalent to standard `JpaSpecificationExecutor`
- `EntityGraphQueryDslPredicateExecutor` which is equivalent to standard `QueryDslPredicateExecutor`
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
productRepository.findByName("MyProduct", EntityGraphUtils.fromName("Product.brand"));
```

Or to the `findOne` method :
```java
// This will apply 'Product.brand' named EntityGraph to findOne
productRepository.findOne(1L, EntityGraphUtils.fromName("Product.brand"));
```

Or any method you like.

You can also pass a dynamically built EntityGraph by using `DynamicEntityGraph`, it's also accessible through a helper method:

```java
productRepository.findOne(1L, EntityGraphUtils.fromAttributePaths("brand", "maker"));
```

This is similar to [Spring's ad-hoc attribute paths](http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-property-expressions),
and equivalent to writing this in your repository's interface:
```java
@EntityGraph(attributePaths = { "brand", "maker" })
Product findOne(Long id);
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
productRepository.findOne(1L);
```
