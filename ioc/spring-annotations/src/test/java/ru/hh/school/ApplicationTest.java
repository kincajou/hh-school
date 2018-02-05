package ru.hh.school;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.hh.school.connection.TestDataSource;

public class ApplicationTest {

  private AnnotationConfigApplicationContext context;

  @Before
  public void setup() {
    context = new AnnotationConfigApplicationContext();
    context.register(TestDataSource.class);
    context.scan("ru.hh.school");
    context.refresh();
  }

  @Test(expected = IllegalStateException.class)
  public void testNotConnected() {
    AreaService service = context.getBean(AreaService.class);
    service.haveAreas();
  }
}
