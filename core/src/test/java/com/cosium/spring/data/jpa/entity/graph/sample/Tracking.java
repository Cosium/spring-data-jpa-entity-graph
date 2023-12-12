package com.cosium.spring.data.jpa.entity.graph.sample;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;

@Embeddable
public class Tracking {
  @ManyToOne(fetch = FetchType.LAZY)
  private User modifier;

  @ManyToOne(fetch = FetchType.LAZY)
  private User creator;

  public User getModifier() {
    return modifier;
  }

  public void setModifier(User modifier) {
    this.modifier = modifier;
  }

  public User getCreator() {
    return creator;
  }

  public void setCreator(User creator) {
    this.creator = creator;
  }
}
