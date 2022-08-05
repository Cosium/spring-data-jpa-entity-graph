[![Build Status](https://github.com/Cosium/spring-data-jpa-entity-graph/actions/workflows/ci.yml/badge.svg)](https://github.com/Cosium/spring-data-jpa-entity-graph/actions/workflows/ci.yml)
[![Codecov branch](https://img.shields.io/codecov/c/github/Cosium/spring-data-jpa-entity-graph/master.svg)](https://codecov.io/gh/Cosium/spring-data-jpa-entity-graph)

[![Maven Central 1.10.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/1.10.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)
[![Maven Central 1.11.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/1.11.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)
[![Maven Central 1.11.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/2.0.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)
[![Maven Central 2.1.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/2.1.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)
[![Maven Central 2.2.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/2.2.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)
[![Maven Central 2.3.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/2.3.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)
[![Maven Central 2.4.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/2.4.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)
[![Maven Central 2.5.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/2.5.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)
[![Maven Central 2.6.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/2.6.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)
[![Maven Central 2.7.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/2.7.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)

# Spring Data JPA EntityGraph

## Life without spring-data-jpa-entity-graph

[Spring Data JPA](https://github.com/spring-projects/spring-data-jpa) only supports EntityGraph through annotations.  
Thus, for a repository method, you must select at most one EntityGraph before compilation.  
This prevents you from choosing the best EntityGraph considering the runtime context :broken_heart:

## Life with spring-data-jpa-entity-graph

Thanks to [spring-data-jpa-entity-graph](https://github.com/Cosium/spring-data-jpa-entity-graph), you can choose EntityGraph at runtime!  
This choice is elegantly made by passing EntityGraph, as an argument, to any Spring Data JPA repository method :heart_eyes:

```java
// This will apply "product(brand, category, maker(country))" dynamic EntityGraph to findByLabel
productRepository.findByLabel(
            "foo", 
            // ProductEntityGraph was generated at compilation time
            ProductEntityGraph.____()
                              .brand()
                              .____
                              .category()
                              .____
                              .maker()
                              .country()
                              .____
                              .____()
);
```

```java
// This will apply "product(brand, category, maker(country))" dynamic EntityGraph to findByLabel
productRepository.findByLabel(
            "foo", 
            DynamicEntityGraph.loading()
                              .addPath("brand")
		                      .addPath("category")
		                      .addPath("maker", "country")
                              .build()
);
```

```java
// This will apply 'Product.brand' named EntityGraph to findByLabel
productRepository.findByLabel("foo", NamedEntityGraph.loading("Product.brand"));
```

Now run to the documentation !

## Documentation

This library follows the Spring Data JPA versioning semantic.

| spring-data-jpa branches | Latest spring-data-jpa-entity-graph version                                                                                                                                                                                                                  | Documentation                                                                                          |
|--------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------|
| 2.7.x                    | [![Maven Central 2.7.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/2.7.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)   | [2.7.x documentation](doc/MAIN.md)                                                                     |
| 2.6.x                    | [![Maven Central 2.6.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/2.6.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)   | [2.6.x documentation](https://github.com/Cosium/spring-data-jpa-entity-graph/blob/2.4.x/doc/MAIN.md)                                                                     |
| 2.5.x                    | [![Maven Central 2.5.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/2.5.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)   | [2.5.x documentation](https://github.com/Cosium/spring-data-jpa-entity-graph/blob/2.4.x/doc/MAIN.md)                                                                     |
| 2.4.x                    | [![Maven Central 2.4.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/2.4.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)   | [2.4.x documentation](https://github.com/Cosium/spring-data-jpa-entity-graph/blob/2.4.x/doc/MAIN.md)   |
| 2.3.x                    | [![Maven Central 2.3.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/2.3.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)   | [2.3.x documentation](https://github.com/Cosium/spring-data-jpa-entity-graph/blob/2.3.x/doc/MAIN.md)   |
| 2.2.x                    | [![Maven Central 2.2.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/2.2.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)   | [2.2.x documentation](https://github.com/Cosium/spring-data-jpa-entity-graph/blob/2.2.x/doc/MAIN.md)   |
| 2.1.x                    | [![Maven Central 2.1.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/2.1.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)   | [2.1.x documentation](https://github.com/Cosium/spring-data-jpa-entity-graph/blob/2.1.x/doc/MAIN.md)   |
| 2.0.x                    | [![Maven Central 2.0.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/2.0.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)   | [2.0.x documentation](https://github.com/Cosium/spring-data-jpa-entity-graph/blob/2.0.x/doc/MAIN.md)   |
| 1.11.x                   | [![Maven Central 1.11.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/1.11.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22) | [1.11.x documentation](https://github.com/Cosium/spring-data-jpa-entity-graph/blob/1.11.x/doc/MAIN.md) |
| 1.10.x                   | [![Maven Central 1.10.x](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph/1.10.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22) | [1.10.x documentation](https://github.com/Cosium/spring-data-jpa-entity-graph/blob/1.10.x/doc/MAIN.md) |

For example, if you were using `spring-data-jpa 2.2.x` in your project, you would need to select any `spring-data-jpa-entity-graph 2.2.x`. Thus `spring-data-jpa-entity-graph 2.2.8` would be eligible.

## Demo

You can play with https://github.com/Cosium/spring-data-jpa-entity-graph-sample to see the extension in action in a simple Spring Application.

## "Making JPA Great Again" talk

This talk was given at Paris JUG in January 2019.  

The [slides](https://cosium.github.io/making-jpa-great-again/) are in english.  
The video is in French:  
[![Alt text](https://img.youtube.com/vi/7yZgSdkvJDE/0.jpg)](https://www.youtube.com/watch?v=7yZgSdkvJDE)

## Genesis

This project was created following the discussion in Spring Data Tracker issue [DATAJPA-749 - Context enabled JPA 2.1 @EntityGraph](https://jira.spring.io/browse/DATAJPA-749) .
