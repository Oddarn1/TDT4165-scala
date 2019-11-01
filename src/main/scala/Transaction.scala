import scala.collection.mutable

object TransactionStatus extends Enumeration {
  val SUCCESS, PENDING, FAILED = Value
}

class TransactionQueue {

    // project task 1.1
    // Add datastructure to contain the transactions
    private var transactionQueue:mutable.Queue[Transaction]=new mutable.Queue[Transaction]

    // Remove and return the first element from the queue
    def pop: Transaction = this.synchronized{
      while (transactionQueue.isEmpty) {
        this.wait()
      }
      transactionQueue.dequeue()
    }

    // Return whether the queue is empty
    def isEmpty: Boolean = transactionQueue.isEmpty

    // Add new element to the back of the queue
    def push(t: Transaction): Unit = this.synchronized(transactionQueue.enqueue(t))

    // Return the first element from the queue without removing it
    def peek: Transaction = transactionQueue.front

    // Return an iterator to allow you to iterate over the queue
    def iterator: Iterator[Transaction] = transactionQueue.iterator
}

class Transaction(val transactionsQueue: TransactionQueue,
                  val processedTransactions: TransactionQueue,
                  val from: Account,
                  val to: Account,
                  val amount: Double,
                  val allowedAttemps: Int) extends Runnable {

  var status: TransactionStatus.Value = TransactionStatus.PENDING
  var attempt = 0

  override def run(): Unit = {

    def doTransaction(): Either[Unit, String] = {
      val x=from withdraw amount
      if (x.isLeft) {
        to deposit amount
      }
      x
    }

    if (status == TransactionStatus.PENDING) {
      val result=doTransaction()
      status = result match {
        case Left(_) => TransactionStatus.SUCCESS
        case Right(_) =>
          attempt += 1
          if (attempt == allowedAttemps) {
            TransactionStatus.FAILED
          } else {
            TransactionStatus.PENDING
          }
      }
      Thread.sleep(50) // you might want this to make more room for
      // new transactions to be added to the queue
    }
  }
}
