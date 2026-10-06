package com.cosium.spring.data.jpa.entity.graph.domain2;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DynamicEntityGraphCompositionTest {

  @Test
  @DisplayName("should include part")
  void shouldIncludePart() {
    EntityGraphPart brand = EntityGraphPart.of("brand", "brand.country").build();
    DynamicEntityGraph g =
        DynamicEntityGraph.loading().include(brand).addPath("maker.address").build();
    assertThat(g.attributePaths())
        .containsExactlyInAnyOrder("brand", "brand.country", "maker.address");
  }

  @Test
  @DisplayName("should include multiple parts")
  void shouldIncludeMultipleParts() {
    EntityGraphPart p1 = EntityGraphPart.of("brand").build();
    EntityGraphPart p2 = EntityGraphPart.of("maker").build();
    DynamicEntityGraph g = DynamicEntityGraph.loading().include(p1).include(p2).build();
    assertThat(g.attributePaths()).hasSize(2);
    assertThat(g.attributePaths()).containsExactlyInAnyOrder("brand", "maker");
  }

  @Test
  @DisplayName("should deduplicate via builder")
  void shouldDeduplicateViaBuilder() {
    DynamicEntityGraph g =
        DynamicEntityGraph.loading().addPaths("brand", "brand").addPath("brand").build();
    assertThat(g.attributePaths()).containsExactly("brand");
  }

  @Test
  @DisplayName("should addPaths varargs and collection")
  void shouldAddPaths() {
    DynamicEntityGraph g =
        DynamicEntityGraph.loading()
            .addPaths("brand", "maker")
            .addPaths(java.util.List.of("maker", "brand.country"))
            .build();
    assertThat(g.attributePaths()).containsExactly("brand", "maker", "brand.country");
  }
}
