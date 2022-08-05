package com.cosium.spring.data.jpa.entity.graph.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.sample.Maker;
import com.cosium.spring.data.jpa.entity.graph.sample.MakerRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.hibernate.Hibernate;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created on 17/03/17.
 *
 * @author Reda.Housni-Alaoui
 */
@DatabaseSetup(BaseTest.DATASET)
@DatabaseTearDown
public class RepositoryTest extends BaseTest {

  @Inject private MakerRepository makerRepository;

  @Transactional
  @Test
  public void given_noop_eg_when_finding_makers_then_country_should_not_be_initialized() {
    List<Maker> makers = makerRepository.findByName("Maker 1", EntityGraph.NOOP);
    assertThat(makers).isNotEmpty();
    for (Maker maker : makers) {
      assertThat(Hibernate.isInitialized(maker.getCountry())).isFalse();
    }
  }

  @Transactional
  @Test
  public void given_country_eg_when_finding_makers_then_country_should_be_initialized() {
    List<Maker> makers =
        makerRepository.findByName("Maker 1", NamedEntityGraph.loading(Maker.COUNTRY_EG));
    assertThat(makers).isNotEmpty();
    for (Maker maker : makers) {
      assertThat(Hibernate.isInitialized(maker.getCountry())).isTrue();
    }
  }

  @Ignore
  @Transactional
  @Test
  public void given_country_eg_when_streaming_makers_then_country_should_be_initialized() {
    List<Maker> makers =
        makerRepository
            .readByName("Maker 1", NamedEntityGraph.loading(Maker.COUNTRY_EG))
            .collect(Collectors.toList());
    for (Maker maker : makers) {
      assertThat(Hibernate.isInitialized(maker.getCountry())).isTrue();
    }
    makers.forEach(maker -> assertThat(Hibernate.isInitialized(maker.getCountry())).isTrue());
  }
}
