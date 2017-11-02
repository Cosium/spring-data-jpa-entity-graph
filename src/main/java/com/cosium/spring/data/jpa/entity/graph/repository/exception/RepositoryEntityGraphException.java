package com.cosium.spring.data.jpa.entity.graph.repository.exception;

/**
 * Created on 27/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public abstract class RepositoryEntityGraphException extends RuntimeException {

  public RepositoryEntityGraphException(String message) {
    super(message);
  }
}
