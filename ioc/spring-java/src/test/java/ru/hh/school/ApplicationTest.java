package ru.hh.school;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import static org.junit.Assert.assertFalse;

public class ApplicationTest {

  private AnnotationConfigApplicationContext context;

  @Before
  public void setup() {
    context = new AnnotationConfigApplicationContext(CommonConfig.class, TestConfig.class);
  }

  @Test(expected = IllegalStateException.class)
  public void testNotConnected() {
    AreaService service = context.getBean(AreaService.class);
    service.haveAreas();
  }

  @Test
  public void testReplicaHasNoAreas() {
    AreaService service = context.getBean(AreaService.class);
    assertFalse(service.haveAreasOnReplica());
  }
}
