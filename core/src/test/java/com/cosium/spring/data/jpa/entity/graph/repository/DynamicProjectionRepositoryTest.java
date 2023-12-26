package com.cosium.spring.data.jpa.entity.graph.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.sample.Brand;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@DatabaseSetup(BaseTest.DATASET)
class DynamicProjectionRepositoryTest extends BaseTest {

  @Inject private BrandRepository repository;

  @Test
  @Transactional
  @DisplayName("dynamic projections should work when you have a super class with generics")
  void test1() {
    var result = repository.findById(1L, Brand.class);
    assertThat(result).map(Brand::getId).isPresent();
  }

  @NoRepositoryBean
  public interface EntityGraphBaseRepository<T, I extends Serializable> extends Repository<T, I> {
    <X> Optional<X> findById(I id, Class<X> clazz);
  }

  @org.springframework.stereotype.Repository
  public interface BrandRepository extends EntityGraphBaseRepository<Brand, Long> {}
}
