package com.cosium.spring.data.jpa.entity.graph.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.cosium.spring.data.jpa.entity.graph.DataRepositoryEnversConfiguration;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.sample.Maker;
import com.cosium.spring.data.jpa.entity.graph.sample.MakerEntityGraph;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import java.util.List;
import java.util.stream.Stream;
import javax.inject.Inject;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created on 17/03/17.
 *
 * @author Reda.Housni-Alaoui
 */
@TestExecutionListeners({
  DependencyInjectionTestExecutionListener.class,
  DirtiesContextTestExecutionListener.class,
  TransactionalTestExecutionListener.class,
  DbUnitTestExecutionListener.class
})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DataRepositoryEnversConfiguration.class})
@DirtiesContext
class EnversRepositoryTest {

  @Inject private MakerRepository makerRepository;

  @Transactional
  @Test
  @DisplayName("Given noop eg when finding makers then country should not be initialized")
  void test1() {
    List<Maker> makers = makerRepository.findByName("Maker 1", EntityGraph.NOOP);
    assertThat(makers).isNotEmpty();
    for (Maker maker : makers) {
      assertThat(Hibernate.isInitialized(maker.getCountry())).isFalse();
    }
  }

  @Transactional
  @Test
  @DisplayName("Given country eg when finding makers then country should be initialized")
  void test2() {
    List<Maker> makers =
        makerRepository.findByName("Maker 1", NamedEntityGraph.loading(Maker.COUNTRY_EG));
    assertThat(makers).isNotEmpty();
    for (Maker maker : makers) {
      assertThat(Hibernate.isInitialized(maker.getCountry())).isTrue();
    }
  }

  @Transactional
  @Test
  @DisplayName(
      "Given noop eg from generated eg when finding makers then country should not be initialized")
  void test3() {
    List<Maker> makers = makerRepository.findByName("Maker 1", MakerEntityGraph.NOOP);
    assertThat(makers).isNotEmpty();
    for (Maker maker : makers) {
      assertThat(Hibernate.isInitialized(maker.getCountry())).isFalse();
    }
  }

  /**
   * Created on 17/03/17.
   *
   * @author Reda.Housni-Alaoui
   */
  public interface MakerRepository
      extends Repository<Maker, Long>, RevisionRepository<Maker, Long, Long> {

    List<Maker> findByName(String name, EntityGraph entityGraph);

    Stream<Maker> readByName(String name, EntityGraph entityGraph);
  }
}
