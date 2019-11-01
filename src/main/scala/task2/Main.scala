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

  //Dummy-function for testing thread-function
  def sum(x1:Int,x2:Int):Unit=print(x1+x2)

  private var counter:Int=0
  def IncreaseCounter():Unit=counter+=1

  //Task 2b
  def PrintCounter():Unit=print(counter)

  //Task 2b
  //Remove comments for thread-safe version
  def ThreadedCounter():Unit={
    val p=thread(PrintCounter())
    val c1=thread(/*ThreadSafe*/IncreaseCounter())
    val c2=thread(/*ThreadSafe*/IncreaseCounter())
    c1.start()
    c2.start()

    c1.join()
    c2.join()

    p.start()
  }

  /*
    Upon running this code we will most of the time get 2. But on rare occasions, we will get 1.
    Since c1 and c2 are run concurrently one of the counters might read the counter-value before the other one
    is finished, and thus they end up writing the same value.
    This can be problematic in any situation where different threads does operations on the same variable. In this
    case, a counter.
  */

  //Task 2c
  def ThreadSafeIncreaseCounter():Unit=this.synchronized{counter+=1}

  /*
    Task 2d

    Deadlocks can happen whenever two threads are waiting for resources from each other, but this resource is locked
    from access when a thread is using one resource.

    The function below will not always cause deadlock, only in some cases. If nothing happens for 15 seconds, we have
    a deadlock. The program crashes if a deadlock is held for 15 seconds.

    To prevent a deadlock we can use atomic execution so that the critical region is not parallel. We can also
    make sure that locks happen a prioritized order, such that you ensure that any threads won't be waiting for others.
    E.g. instead of two threads accessing elements in the following order
    1 2
    2 1
    we can ensure that they instead will access them in an order that won't create any conflicts, like this:
    1 2
    1 2
   */

  def DeadLockExample():Unit={
    val deadlock=Future.sequence(Seq(Future {Dead.y} , Future {Lock.x}))
    Await.result(deadlock,Duration(15,"seconds"))
  }

  def main(args: Array[String]): Unit = {
    ThreadedCounter()
    //DeadLockExample()
  }
}
