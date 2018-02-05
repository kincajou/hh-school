package ru.hh.school;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.hh.school.connection.DataSource;

public class Application {

  public static void main(String[] args) {
    // default constructor allows to call methods and then 'refresh'
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.register(DataSource.class); // ok for default constructor or if constructor expects managed beans
    context.scan("ru.hh.school"); // recursive
    context.refresh();

    AreaService service = context.getBean(AreaService.class);
    System.out.println(service.haveAreas());
  }
}
