package C6WaitNotify;

interface Producer<T> {
  /** blocks until some consumer thread call consume **/
  void produce(T task) throws InterruptedException;
}
