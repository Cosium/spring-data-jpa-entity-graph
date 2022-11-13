package com.cosium.spring.data.jpa.entity.graph.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.domain2.DynamicEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.sample.Maker;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Optional;
import javax.inject.Inject;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Réda Housni Alaoui
 */
@DatabaseSetup(BaseTest.DATASET)
class DefaultEntityGraphTest extends BaseTest {

  @Inject private MyEntityRepository repository;

  @Transactional
  @Test
  @DisplayName("Querying without EntityGraph leads to the default EntityGraph usage")
  void test1() {
    MyEntity entity = repository.findById(1L).orElseThrow(RuntimeException::new);
    assertThat(Hibernate.isInitialized(entity.getMaker())).isTrue();
  }

  @Transactional
  @Test
  @DisplayName("Querying with EntityGraph#NOOP leads to the default EntityGraph usage")
  void test2() {
    MyEntity entity = repository.findById(1L, EntityGraph.NOOP).orElseThrow(RuntimeException::new);
    assertThat(Hibernate.isInitialized(entity.getMaker())).isTrue();
  }

  @Transactional
  @Test
  @DisplayName("Querying with EntityGraph skips default EntityGraph usage")
  void test3() {
    MyEntity entity =
        repository
            .findById(1L, DynamicEntityGraph.loading().build())
            .orElseThrow(RuntimeException::new);
    assertThat(Hibernate.isInitialized(entity.getMaker())).isFalse();
  }

  @Entity
  @Table(name = "defaultentitygraphtest_entity")
  public static class MyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(value = AccessType.PROPERTY)
    private long id = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    private Maker maker;

    public long getId() {
      return id;
    }

    public void setId(long id) {
      this.id = id;
    }

    public Maker getMaker() {
      return maker;
    }

    public void setMaker(Maker maker) {
      this.maker = maker;
    }
  }

  interface MyEntityRepository extends EntityGraphCrudRepository<MyEntity, Long> {

    @Override
    default Optional<EntityGraph> defaultEntityGraph() {
      return DynamicEntityGraph.loading().addPath("maker").build().execute(Optional::of);
    }
  }
}
