package com.cosium.spring.data.jpa.entity.graph.repository.sample;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created on 17/03/17.
 *
 * @author Reda.Housni-Alaoui
 */
public interface MakerRepository extends Repository<Maker, Long> {

  List<Maker> findByName(String name, EntityGraph entityGraph);

  Stream<Maker> readByName(String name, EntityGraph entityGraph);
}
