package ru.hh.school;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {

  public static void main(String[] args) {
    ApplicationContext context = new ClassPathXmlApplicationContext("beans-common.xml", "beans-prod.xml");

    AreaService service = context.getBean(AreaService.class);
    System.out.println(service.haveAreas());
  }
}
