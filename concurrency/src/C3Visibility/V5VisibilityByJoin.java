package C3Visibility;

public class V5VisibilityByJoin {

  // Wait! How does ThreadConfinement example work? There is neither synchronized nor volatile.
  // Well, there is a Thread.join, which also guarantees visibility.
  // There are some other things that guarantee visibility.
  // piggybacking example - V1NoVisibility with System.out.println
  // Besides, "visibility" is a simplification. For more details see:
  // https://docs.oracle.com/javase/specs/jls/se15/html/jls-17.html
  // https://www.youtube.com/watch?v=noDnSV7NCtw and https://www.youtube.com/watch?v=Ky1_5mabd18  // Shipilev JMM

}
