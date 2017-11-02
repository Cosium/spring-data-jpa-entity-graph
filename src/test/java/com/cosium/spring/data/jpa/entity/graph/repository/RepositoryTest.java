package com.cosium.spring.data.jpa.entity.graph.repository;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphUtils;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.Maker;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.MakerRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
  public void given_empty_eg_when_finding_makers_then_country_should_not_be_initialized() {
    List<Maker> makers = makerRepository.findByName("Maker 1", EntityGraphUtils.empty());
    assertThat(makers).isNotEmpty();
    for (Maker maker : makers) {
      assertThat(Hibernate.isInitialized(maker.getCountry())).isFalse();
    }
  }

  @Transactional
  @Test
  public void given_country_eg_when_finding_makers_then_country_should_be_initialized() {
    List<Maker> makers =
        makerRepository.findByName("Maker 1", EntityGraphUtils.fromName(Maker.COUNTRY_EG));
    assertThat(makers).isNotEmpty();
    for (Maker maker : makers) {
      assertThat(Hibernate.isInitialized(maker.getCountry())).isTrue();
    }
  }
}
