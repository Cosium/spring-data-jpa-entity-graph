package com.cosium.spring.data.jpa.entity.graph.domain;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public class EntityGraphUtils {

	public static EntityGraph fromName(String name){
		return new NamedEntityGraph(name);
	}

}
