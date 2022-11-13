[![Build Status](https://github.com/Cosium/spring-data-jpa-entity-graph/actions/workflows/ci.yml/badge.svg)](https://github.com/Cosium/spring-data-jpa-entity-graph/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)

# Spring Data JPA EntityGraph

[Spring Data JPA](https://github.com/spring-projects/spring-data-jpa) only supports `EntityGraph` through annotations.  
Thus, for a repository method, you must select at most one `EntityGraph` before compilation.  
This prevents you from choosing the best `EntityGraph` considering the runtime context :broken_heart:

[Spring Data JPA EntityGraph](https://github.com/Cosium/spring-data-jpa-entity-graph) allows you to choose `EntityGraph` at runtime! This choice is elegantly made by passing `EntityGraph`, as an argument, to any Spring Data JPA repository method :heart_eyes:

# Quick start
1. Select the correct version from the [compatibility matrix](#compatibility-matrix)
2. In addition to [Spring Data JPA](https://github.com/spring-projects/spring-data-jpa), add [Spring Data JPA EntityGraph](https://github.com/Cosium/spring-data-jpa-entity-graph) dependency :

    ```xml
    <dependency>
      <groupId>com.cosium.spring.data</groupId>
      <artifactId>spring-data-jpa-entity-graph</artifactId>
      <version>${spring-data-jpa-entity-graph.version}</version>
    </dependency>
    ```
3. Set the repository factory bean class to `EntityGraphJpaRepositoryFactoryBean` :

    ```java
    @SpringBootApplication
    @EnableJpaRepositories(repositoryFactoryBeanClass = EntityGraphJpaRepositoryFactoryBean.class)
    public class App {
        //...
    }
    ```
4. If you want to use [type safe EntityGraphs](#type-safe-entitygraph) , add those dependencies:

    ```xml
    <dependency>
      <groupId>org.hibernate</groupId>
      <artifactId>hibernate-jpamodelgen</artifactId>
      <version>${hibernate.version}</version>
      <scope>provided</scope>
    </dependency>
   ```
   ```xml
   <dependency>
     <groupId>com.cosium.spring.data</groupId>
     <artifactId>spring-data-jpa-entity-graph-generator</artifactId>
     <version>${spring-data-jpa-entity-graph.version}</version>
     <scope>provided</scope>
   </dependency>
   ```

# 2.x to 3.x breaking changes

- Moved from Java 8 to 17 as the source language
- `javax.persistence` replaced by `jakarta.persistence`
- `com.cosium.spring.data.jpa.entity.graph.domain` classes (deprecated in 2.7.x) have been removed in favor of `com.cosium.spring.data.jpa.entity.graph.domain2`
- `Default EntityGraph by name pattern` feature (deprecated in 2.7.x) has been removed. `*.default` named EntityGraph are not considered as default EntityGraph anymore. Please read [default EntityGraph](#repository-default-entitygraph) to use the new `Default EntityGraph` feature instead.

# Usage

## On custom repository methods

If you want to define a custom repository method accepting an `EntityGraph`, just do it™.

For example, given an entity having attribute named `label` of type String, you could declare and use a repository like this:

```java
interface MyRepository extends Repository<MyEntity, Long> {
	Optional<MyEntity> findByLabel(String label, EntityGraph entityGraph);
}
```
```java
myRepository.findByLabel("foo", NamedEntityGraph.loading("bar"));
```

## On pre-defined repository methods

[Spring Data JPA EntityGraph](https://github.com/Cosium/spring-data-jpa-entity-graph) provides repository interfaces extending [Spring Data JPA](https://github.com/spring-projects/spring-data-jpa). For each [Spring Data JPA](https://github.com/spring-projects/spring-data-jpa) pre-defined method, the provided interfaces overload the method with `EntityGraph` as an additional argument.

For example, [Spring Data JPA](https://github.com/spring-projects/spring-data-jpa) `CrudRepository` defines method `Optional<T> findById(ID id)`. This method is overloaded by [Spring Data JPA EntityGraph](https://github.com/Cosium/spring-data-jpa-entity-graph) `EntityGraphCrudRepository` as `Optional<T> findById(ID id, EntityGraph entityGraph)`.

To be able to use these overloaded methods, you must extend one of [Spring Data JPA EntityGraph](https://github.com/Cosium/spring-data-jpa-entity-graph) provided repository interfaces.

The following matrix describes the mapping between [Spring Data JPA](https://github.com/spring-projects/spring-data-jpa) and [Spring Data JPA EntityGraph](https://github.com/Cosium/spring-data-jpa-entity-graph) :

| [Spring Data JPA](https://github.com/spring-projects/spring-data-jpa) | [Spring Data JPA EntityGraph](https://github.com/Cosium/spring-data-jpa-entity-graph) |
|-----------------------------------------------------------------------|---------------------------------------------------------------------------------------|
| JpaRepository                                                         | EntityGraphJpaRepository                                                              |
| JpaSpecificationExecutor                                              | EntityGraphJpaSpecificationExecutor                                                   |
| QuerydslPredicateExecutor                                             | EntityGraphQuerydslPredicateExecutor                                                  |
| CrudRepository                                                        | EntityGraphCrudRepository                                                             |
| PagingAndSortingRepository                                            | EntityGraphPagingAndSortingRepository                                                 |
| QueryByExampleExecutor                                                | EntityGraphQueryByExampleExecutor                                                     |

For example, if you wanted to use `Optional<T> findById(ID id, EntityGraph entityGraph)`, you could declare and use your repository like this:

```java
interface MyRepository extends EntityGraphCrudRepository<MyEntity, Long> {
}
```
```java
myRepository.findById(1L, NamedEntityGraph.loading("foo"));
```

## EntityGraph provided implementations

### DynamicEntityGraph

`DynamicEntityGraph` class allows you to create on-the-fly `EntityGraph` by defining their attribute paths. This is similar to [Spring's ad-hoc attribute paths](http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-property-expressions).

For example, let's consider the following entities :
```java
@Entity
class Maker {
   //...
   @OneToOne(fetch = FetchType.LAZY)
   private Address address;
   //...
}
```
```java
@Entity
class Product {
    @Id
    private long id = 0;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private Brand brand;
    @ManyToOne(fetch = FetchType.LAZY)
    private Maker maker;
    //...
}	
```

You could declare the following repository :
```java
public interface MyRepository extends Repository<Product, Long> {
    List<Product> findByName(String name, EntityGraph entityGraph);
}
```

Then perform the `findByName` using ad-hoc `product(brand, maker(address))` `EntityGraph` :
```java
myRepository.findById(1L, DynamicEntityGraph.loading().addPath("brand").addPath("maker", "address").build());
```

### NamedEntityGraph

`NamedEntityGraph` class allows you to reference an `EntityGraph` by its name. Such `EntityGraph` must have beforehand been registered through `EntityManager#createEntityGraph(String graphName)` or declared using `@NamedEntityGraph` annotation.

For example, let's consider the following entity :
```java
@NamedEntityGraphs(value = {
    @NamedEntityGraph(name = "productBrand", attributeNodes = {
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

You could declare the following repository :
```java
public interface MyRepository extends Repository<Product, Long> {
    List<Product> findByName(String name, EntityGraph entityGraph);
}
```

Then perform the `findByName` query using `productBrand` named `EntityGraph` like this :
```java
myRepository.findByName("foo", NamedEntityGraph.loading("productBrand"));
```

### Type safe EntityGraph

Composing entity graphs by hand can be tedious and error-prone. Wouldn't it be great to benefit from autocompletion and strong type checking while composing your entity graph?

[Spring Data JPA EntityGraph Generator](https://github.com/Cosium/spring-data-jpa-entity-graph) has you covered.

This annotation processor makes use of the JPA metamodel information (part of JPA specification) generated by the tool of your choice (e.g. `hibernate-jpamodelgen`) to generate `EntityGraph` composers allowing you to safely and easily compose `EntityGraph` at runtime.

You first need to add a JPA metamodel information generator. `hibernate-jpamodelgen` is great and should be compatible with any JPA ORM:
```xml
<dependency>
  <groupId>org.hibernate</groupId>
  <artifactId>hibernate-jpamodelgen</artifactId>
  <version>${hibernate.version}</version>
  <scope>provided</scope>
</dependency>
```

Then add the entity graph generator annotation processor dependency:

```xml
<dependency>
  <groupId>com.cosium.spring.data</groupId>
  <artifactId>spring-data-jpa-entity-graph-generator</artifactId>
  <version>${spring-data-jpa-entity-graph.version}</version>
  <scope>provided</scope>
</dependency>
```

After compiling your project, you should find `XEntityGraph` classes where `X` is the name of your Entity. 

For example, let's consider the following entity :
```java
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
You could declare the following repository :
```java
public interface MyRepository extends Repository<Product, Long> {
    List<Product> findByName(String name, EntityGraph entityGraph);
}
```

`spring-data-jpa-entity-graph-generator` will detect `Product` and generate in return `ProductEntityGraph` class.
You could then perform the `findByName` query using `product(brand, maker(address))` `EntityGraph` like this :

```java
productRepository.findById(1L, ProductEntityGraph
                               .____()
                               .brand()
                               .____
                               .maker()
                               .address()
                               .____
                               .____());
```

## Repository default EntityGraph

You can declare at most one `default EntityGraph` per repository by overriding `EntityGraphRepository#defaultEntityGraph` method. 

Calling **any** repository query method - custom or pre-defined - without `EntityGraph` or with an `EntityGraph#NOOP` equivalent will lead to the `default EntityGraph` usage. Otherwise, the `EntityGraph` passed as query method argument will always have priority.

You could declare a repository as follows :
```java
interface MyRepository extends EntityGraphCrudRepository<MyEntity, Long> {
  @Override
  default Optional<EntityGraph> defaultEntityGraph() {
    return NamedEntityGraph.loading("foo").execute(Optional::of);
  } 
  
  List<MyEntity> findByName(String name);
  List<MyEntity> findByName(String name, EntityGraph entityGraph);
}
```

The following snippets will lead to the `default EntityGraph` usage:
```java
myRepository.findById(1L);
```
```java
myRepository.findById(1L, EntityGraph.NOOP);
```
```java
myRepository.findByName("bar");
```

The following snippets will ignore the `default EntityGraph` and instead use the `EntityGraph` passed as argument:
```java
myRepository.findById(1L, NamedEntityGraph.loading("alice"));
```
```java
myRepository.findByName("bar", NamedEntityGraph.loading("barry"));
```

## Chaining EntityGraph definition with query execution

If you prefer fluent apis, you can use any instance of `EntityGraph` like this:

```java
List<Product> products = ProductEntityGraph
                          .____()
                          .brand()
                          .____
                          .maker()
                          .address()
                          .____
                          .____()
                          .execute(entityGraph -> myRepository.findByLabel("foo", entityGraph));
```

## EntityGraph Semantics

JPA 2.1 defines 2 semantics:
* [Load Graph Semantics](https://download.oracle.com/otn-pub/jcp/persistence-2_1-fr-eval-spec/JavaPersistence.pdf)
* [Fetch Graph Semantics](https://download.oracle.com/otn-pub/jcp/persistence-2_1-fr-eval-spec/JavaPersistence.pdf)

[Spring Data JPA EntityGraph](https://github.com/Cosium/spring-data-jpa-entity-graph) uses [Load Graph Semantics](https://download.oracle.com/otn-pub/jcp/persistence-2_1-fr-eval-spec/JavaPersistence.pdf) as the default semantic. This means if you don't define a semantic, `EntityGraph` implementations will be built using [Load Graph Semantics](https://download.oracle.com/otn-pub/jcp/persistence-2_1-fr-eval-spec/JavaPersistence.pdf).

Each provided `EntityGraph` implementation provides an easy way to select the `Graph Semantics`.

# Demo

You can play with https://github.com/Cosium/spring-data-jpa-entity-graph-sample to see the extension in action in a simple Spring Application.

# Compatibility matrix

| [Spring Data JPA](https://github.com/spring-projects/spring-data-jpa) version | [Spring Data JPA EntityGraph](https://github.com/Cosium/spring-data-jpa-entity-graph) version                                                                                                                                                                |
|-------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 3.0.x                                                                         | [![Maven Central 3.0.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/3.0.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)   |
| 2.7.x                                                                         | [![Maven Central 2.7.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/2.7.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)   |
| 2.6.x                                                                         | [![Maven Central 2.6.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/2.6.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)   |
| 2.5.x                                                                         | [![Maven Central 2.5.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/2.5.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)   |
| 2.4.x                                                                         | [![Maven Central 2.4.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/2.4.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)   |
| 2.3.x                                                                         | [![Maven Central 2.3.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/2.3.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)   |
| 2.2.x                                                                         | [![Maven Central 2.2.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/2.2.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)   |
| 2.1.x                                                                         | [![Maven Central 2.1.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/2.1.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)   |
| 2.0.x                                                                         | [![Maven Central 2.0.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/2.0.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)   |
| 1.11.x                                                                        | [![Maven Central 1.11.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/1.11.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22) |
| 1.10.x                                                                        | [![Maven Central 1.10.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/1.10.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22) |

For example, if you were using `spring-data-jpa 2.2.x` in your project, you would need to select any `spring-data-jpa-entity-graph 2.2.x`. Thus `spring-data-jpa-entity-graph 2.2.8` would be eligible.

# "Making JPA Great Again" talk

This talk was given at Paris JUG in January 2019.  

The [slides](https://cosium.github.io/making-jpa-great-again/) are in english.  
The video is in French:  
[![Alt text](https://img.youtube.com/vi/7yZgSdkvJDE/0.jpg)](https://www.youtube.com/watch?v=7yZgSdkvJDE)

# Genesis

This project was created following https://github.com/spring-projects/spring-data-jpa/issues/1120 discussion.
