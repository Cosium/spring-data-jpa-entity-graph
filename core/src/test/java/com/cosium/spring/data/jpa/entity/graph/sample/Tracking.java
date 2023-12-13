package com.cosium.spring.data.jpa.entity.graph.sample;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;

@Embeddable
public class Tracking {
  @ManyToOne(fetch = FetchType.LAZY)
  private User modifier;
}
