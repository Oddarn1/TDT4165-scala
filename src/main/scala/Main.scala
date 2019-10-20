
object Main extends App {

    def thread(body: => Unit): Thread = {
        val t = new Thread {
            override def run() = body
        }
        t.start
        t
    }

    override def main(args: Array[String]): Unit = {
        print("Hello World!")
    }
  
}