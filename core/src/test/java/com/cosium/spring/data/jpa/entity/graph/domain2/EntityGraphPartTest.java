package com.cosium.spring.data.jpa.entity.graph.domain2;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EntityGraphPartTest {

  @Test
  @DisplayName("should create part")
  void shouldCreatePart() {
    EntityGraphPart part = EntityGraphPart.of("brand", "brand.country").build();
    assertThat(part.attributePaths()).containsExactly("brand", "brand.country");
  }

  @Test
  @DisplayName("should deduplicate")
  void shouldDeduplicate() {
    EntityGraphPart part = EntityGraphPart.of("brand", "brand").build();
    assertThat(part.attributePaths()).containsExactly("brand");
  }

  @Test
  @DisplayName("should include other part")
  void shouldIncludeOtherPart() {
    EntityGraphPart p1 = EntityGraphPart.of("brand").build();
    EntityGraphPart p2 = EntityGraphPart.of("maker").build();
    EntityGraphPart combined = EntityGraphPart.of("brand").include(p2).build();
    assertThat(combined.attributePaths()).containsExactly("brand", "maker");

    EntityGraphPart combined2 = EntityGraphPart.of("brand").include(p1).include(p2).build();
    // deduplication preserves first occurrence order
    assertThat(combined2.attributePaths()).containsExactly("brand", "maker");
  }

  @Test
  @DisplayName("should deduplicate via include")
  void shouldDeduplicateViaInclude() {
    EntityGraphPart p1 = EntityGraphPart.of("brand").build();
    EntityGraphPart combined = EntityGraphPart.of("brand").include(p1).build();
    assertThat(combined.attributePaths()).containsExactly("brand");
  }

  @Test
  @DisplayName("equals and hashCode")
  void equalsAndHashCode() {
    EntityGraphPart p1 = EntityGraphPart.of("brand", "maker").build();
    EntityGraphPart p2 = EntityGraphPart.of("brand", "maker").build();
    assertThat(p1).isEqualTo(p2).hasSameHashCodeAs(p2);
  }
}
