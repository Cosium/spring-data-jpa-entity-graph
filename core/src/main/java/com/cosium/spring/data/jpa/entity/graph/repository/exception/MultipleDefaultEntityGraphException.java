package com.cosium.spring.data.jpa.entity.graph.repository.exception;

/**
 * Created on 27/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public class MultipleDefaultEntityGraphException extends RepositoryEntityGraphException {

  public MultipleDefaultEntityGraphException(String message) {
    super(message);
  }

  public MultipleDefaultEntityGraphException(String entityGraph1Name, String entityGraph2Name) {
    super(
        "Multiple default entity graphs detected : "
            + entityGraph1Name
            + " and "
            + entityGraph2Name);
  }
}
