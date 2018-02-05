package ru.hh.school;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationTest {

  private ApplicationContext context;

  @Before
  public void setup() {
    context = new ClassPathXmlApplicationContext("beans-common.xml", "beans-test.xml");
  }

  @Test(expected = IllegalStateException.class)
  public void testNotConnected() {
    AreaService service = context.getBean(AreaService.class);
    service.haveAreas();
  }
}
