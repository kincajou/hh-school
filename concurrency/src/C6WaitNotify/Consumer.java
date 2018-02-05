package C6WaitNotify;

interface Consumer<T> {
  /** blocks until there is a task available to consume **/
  T consume() throws InterruptedException;
}
