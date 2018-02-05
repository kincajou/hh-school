package ru.hh.school;

import javax.inject.Inject;
import org.springframework.stereotype.Service;

@Service
public class AreaService {

  private AreaDao areaDao;

  @Inject
  public AreaService(AreaDao areaDao) {
    this.areaDao = areaDao;
  }

  public boolean haveAreas() {
    return areaDao.countAll() > 0;
  }
}
