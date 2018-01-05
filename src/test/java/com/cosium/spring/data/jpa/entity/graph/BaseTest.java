package com.cosium.spring.data.jpa.entity.graph;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@TestExecutionListeners({
  DependencyInjectionTestExecutionListener.class,
  DirtiesContextTestExecutionListener.class,
  TransactionalTestExecutionListener.class,
  DbUnitTestExecutionListener.class
})
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DataRepositoryConfiguration.class})
@DirtiesContext
public abstract class BaseTest {

  public static final String DATASET =
      "classpath:com/cosium/spring/data/jpa/entity/graph/dataset.xml";
}
