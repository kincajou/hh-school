package ru.hh.school.stuff.constructor;

public class Application {

  public static void main(String[] args) {
    AreaDao dao = new AreaDao();
    AreaService service = new AreaService(dao);
    System.out.println(service.haveAreas());
  }
}
