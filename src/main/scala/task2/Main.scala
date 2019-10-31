package task2

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object Dead{
  lazy val x="Deadlock"
  lazy val y:String=Lock.x
}

object Lock{
  lazy val x:String=Dead.x
}

object Main {

  //task 2a
  def thread(body: => Unit): Thread = {
    new Thread {
      override def run(): Unit = body
    }
  }

  private var counter:Int=0

  def IncreaseCounter():Unit=counter+=1

  //Task 2b
  def PrintCounter(safe:Boolean):Unit=print(if(safe)safeCounter else counter)

  //Task 2b
  //Use safe=false for not-threadsafe counter.

  /*With standard threaded counter, there might be concurrency problems, where the print is executed before
  * one or both counters has been started. This is known as a race condition, and is a problem when executing
  * multiple operations at once where you have no way to tell which is executed first, and the order is important.*/
  def ThreadedCounter(safe:Boolean):Unit={
    val p=thread(PrintCounter(safe))
    val c1=if(safe) thread(ThreadSafeIncreaseCounter()) else thread(IncreaseCounter())
    val c2=if(safe) thread(ThreadSafeIncreaseCounter()) else thread(IncreaseCounter())
    c1.start()
    c2.start()
    p.start()
  }

  //Task 2c
  var safeCounter=0
  def ThreadSafeIncreaseCounter():Unit=this.synchronized(safeCounter+=1)

  //Task2d
  /*Deadlock can happen when two threads are waiting for resources from each other, but this resource is locked
  from access when a thread is using one resource.*/

  //Function will not always cause deadlock, but in some cases. If nothing happens for 10 seconds, we have a deadlock
  //The program crashes if deadlock is held for 10 seconds.
  def DeadLockExample():Unit={
    val deadlock=Future.sequence(Seq(Future {Dead.y} , Future {Lock.x}))
    Await.result(deadlock,Duration(10,"seconds"))
  }

  def main(args: Array[String]): Unit = {
    print("Disclaimer: Wanted results may not always happen. Threaded counter and DeadlockExample may give different" +
      " results on different runs. \nPlease try again if wanted result is not present.\n\n")
    print("Threaded counter:")
    ThreadedCounter(safe=false)
    Thread.sleep(500)
    print("\nThreadsafe counter:")
    ThreadedCounter(safe=true)
    Thread.sleep(500)
    print("\nIf nothing happens for 10 seconds, there is a deadlock, program will crash.")
    DeadLockExample()
  }
}
