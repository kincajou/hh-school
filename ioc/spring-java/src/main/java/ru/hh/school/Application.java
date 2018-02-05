package ru.hh.school;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

  public static void main(String[] args) {
    // no need to do 'refresh' here - using constructor with args
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ProdConfig.class, CommonConfig.class);

    AreaService service = context.getBean(AreaService.class);
    System.out.println(service.haveAreas());
    System.out.println(service.haveAreasOnReplica());
  }
}
