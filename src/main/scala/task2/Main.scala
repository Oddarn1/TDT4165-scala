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
    p.start()
    c1.start()
    c2.start()
  }

  //Task 2c
  def ThreadSafeIncreaseCounter():Unit=this.synchronized{counter+=1}

  //Task2d
  /*Deadlock can happen when two threads are waiting for resources from each other, but this resource is locked
  from access when a thread is using one resource.*/

  //Function will not always cause deadlock, but in some cases. If nothing happens for 15 seconds, we have a deadlock
  //The program crashes if deadlock is held for 15 seconds.
  def DeadLockExample():Unit={
    val deadlock=Future.sequence(Seq(Future {Dead.y} , Future {Lock.x}))
    Await.result(deadlock,Duration(15,"seconds"))
  }

  def main(args: Array[String]): Unit = {
    DeadLockExample()
  }
}
