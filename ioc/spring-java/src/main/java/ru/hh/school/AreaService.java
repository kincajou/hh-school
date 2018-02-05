package ru.hh.school;

public class AreaService {

  private AreaDao areaDao;

  public AreaService(AreaDao areaDao) {
    this.areaDao = areaDao;
  }

  public boolean haveAreas() {
    return areaDao.countAll() > 0;
  }

  public boolean haveAreasOnReplica() {
    return areaDao.countAllOnReplica() > 0;
  }
}
