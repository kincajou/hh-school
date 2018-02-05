package ru.hh.school.stuff.setter;

public class Application {

  public static void main(String[] args) {
    AreaDao dao = new AreaDao();
    AreaService service = new AreaService();
    service.setAreaDao(dao);
    System.out.println(service.haveAreas());
  }
}
