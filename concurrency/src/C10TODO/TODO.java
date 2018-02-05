package C10TODO;

import java.util.Map;

public class TODO {

  // - final fields
  // - Actors. Do not share state. You can only pass a message to an actor's mailbox.
  //
  // Further reading:
  // - Brian Goetz. Java concurrency in practice.
  // - java.util.concurrent collections http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/package-summary.html
  // - https://docs.oracle.com/javase/specs/jls/se8/html/jls-17.html
  // - Shipilev JMM lectures https://shipilev.net/
  //
  // Exercise 1.
  // Implement:
  interface Counters {
    void increment(String tag); // called often from different threads
    Map<String, Long> getCountersAndClear(); // called rarely (for example once per minute by a scheduled thread)
  }
  // Can be used for example to accumulate number of views per resume.
  // Each resume view increments counter with tag = resumeId.
  // A scheduled thread once per minute gets accumulated data and sends it to a stats system.
  // In the same time all internal data structures are cleared to reduce memory footprint.
  // You can use java.util.concurrent but not any external library.
  //
  // Exercise 2.
  // Given:
  interface Bucket {
    void awaitDrop(); // blocks until some other thread calls Drop.arrived()
                      // second invocation of the await() must wait another thread to call Drop.ready()
    void leak();      // unblocks a thread arrived at Drop.arrived() point in a FIFO order.
  }
  interface Drop {
    void arrived();  // notifies Bucket.awaitDrop() that a thread has arrived to a barrier.
                     // then blocks until Bucket.leak() is called.
  }
  // Implement class BucketBarrier implements Bucket, Drop.
  // It is useful for example for testing purposes.
  // You create several worker threads that arrive to some critical point (they drop into the bucket).
  // In the test you ensure that all "drops" are in the "bucket" by calling Bucket.awaitDrop() method.
  // Finally you let worker threads to pass a barrier one by one (the bucket leaks drops one by one).
  // You can NOT use java.util.concurrent.

}
