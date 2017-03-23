[![Gitter](https://badges.gitter.im/Cosium/spring-data-jpa-entity-graph.svg)](https://gitter.im/Cosium/spring-data-jpa-entity-graph?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
[![Travis branch](https://img.shields.io/travis/Cosium/spring-data-jpa-entity-graph/1.10.x.svg)](https://travis-ci.org/Cosium/spring-data-jpa-entity-graph)
[![Codecov branch](https://img.shields.io/codecov/c/github/Cosium/spring-data-jpa-entity-graph/1.10.x.svg)](https://codecov.io/gh/Cosium/spring-data-jpa-entity-graph)
[![Maven Central](https://img.shields.io/maven-central/v/com.cosium.spring.data/spring-data-jpa-entity-graph.svg)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cosium.spring.data%22%20AND%20a%3A%22spring-data-jpa-entity-graph%22)

# Spring Data JPA EntityGraph

Today, [Spring Data JPA](https://github.com/spring-projects/spring-data-jpa) supports EntityGraph exclusively through annotations.  
Thus, for a method, the choice of EntityGraph must be made before compilation.  

This extension gives the ability to pass EntityGraph on any Spring Data JPA repository method as an argument, making the EntityGraph choice fully dynamic.

Example:
```java
productRepository.findByName("foo", EntityGraphUtils.fromName("Product.brand"));
```

## How to select the correct version

This library follows the Spring Data JPA versionning semantic.

spring-data-jpa branches | Latest spring-data-jpa-entity-graph version | Documentation
---------------------------- | --------------- | -----------------
1.12.x | Not released yet | [1.12.x documentation](https://github.com/Cosium/spring-data-jpa-entity-graph/blob/master/doc/MAIN.md)
1.11.x | 1.11.02 | [1.11.x documentation](https://github.com/Cosium/spring-data-jpa-entity-graph/blob/1.11.x/doc/MAIN.md)
1.10.x | 1.10.15 | [1.10.x documentation](doc/MAIN.md)

Example: if you were using `spring-data-jpa 1.10.13` in your project, you would need to select any `spring-data-jpa-entity-graph 1.10.x`. Thus `spring-data-jpa-entity-graph 1.10.14` would be eligible.

## Genesis

This project was created following the discussion in Spring Data Tracker issue [DATAJPA-749 - Context enabled JPA 2.1 @EntityGraph](https://jira.spring.io/browse/DATAJPA-749) .