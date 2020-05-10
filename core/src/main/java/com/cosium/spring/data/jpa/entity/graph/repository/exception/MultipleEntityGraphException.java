package com.cosium.spring.data.jpa.entity.graph.repository.exception;

/**
 * Created on 27/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public class MultipleEntityGraphException extends RuntimeException {

  public MultipleEntityGraphException(String message) {
    super(message);
  }
}
