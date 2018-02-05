package ru.hh.school;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;

public class ApplicationTest {

  private Injector injector;

  @Before
  public void setup() {
    injector = Guice.createInjector(new CommonModule(), new TestModule());
  }

  @Test(expected = IllegalStateException.class)
  public void testNotConnected() {
    AreaService service = injector.getInstance(AreaService.class);
    service.haveAreas();
  }

  @Test
  public void testReplicaHasNoAreas() {
    AreaService service = injector.getInstance(AreaService.class);
    assertFalse(service.haveAreasOnReplica());
  }
}
