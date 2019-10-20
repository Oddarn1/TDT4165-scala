package task2

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

  //Task 2c
  def ThreadSafeIncreaseCounter():Unit=this.synchronized{counter+=1}

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


  def main(args: Array[String]): Unit = {
    ThreadedCounter()
  }
}
