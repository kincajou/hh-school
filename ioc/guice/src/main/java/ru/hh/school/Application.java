package ru.hh.school;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.name.Names;

public class Application {

  public static void main(String[] args) throws InterruptedException {
    // same as ApplicationContext in spring
    Injector injector = Guice.createInjector(new CommonModule(), new ProdModule());

    // same as context.getBean in spring
    AreaService service = injector.getInstance(AreaService.class);
    System.out.println(service.haveAreas());
    System.out.println(service.haveAreasOnReplica());

    // provider allows to return changing values / lazy instantiation
    Provider<Long> millis = injector.getProvider(Key.get(Long.class, Names.named("millis")));
    System.out.println(millis.get());
    Thread.sleep(511);
    System.out.println(millis.get());
    Thread.sleep(511);
    System.out.println(millis.get());
    Thread.sleep(511);
    System.out.println(millis.get());

    // same as
    System.out.println(injector.getInstance(Key.get(Long.class, Names.named("millis"))));
    Thread.sleep(511);
    System.out.println(injector.getInstance(Key.get(Long.class, Names.named("millis"))));
    Thread.sleep(511);
    System.out.println(injector.getInstance(Key.get(Long.class, Names.named("millis"))));
    Thread.sleep(511);
  }
}
