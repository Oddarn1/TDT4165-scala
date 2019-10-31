import exceptions._

import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object TransactionStatus extends Enumeration {
  val SUCCESS, PENDING, FAILED = Value
}

class TransactionQueue {

    // TODO: Add thread-safety
    // project task 1.1
    // Add datastructure to contain the transactions
    private var transactionQueue:mutable.Queue[Transaction]=new mutable.Queue[Transaction]

    // Remove and return the first element from the queue
    def pop: Transaction = transactionQueue.synchronized{
      val head:Transaction=transactionQueue.front
      transactionQueue=transactionQueue.tail
      head
    }

    // Return whether the queue is empty
    def isEmpty: Boolean = transactionQueue.synchronized(transactionQueue.isEmpty)

    // Add new element to the back of the queue
    def push(t: Transaction): Unit = transactionQueue.synchronized(transactionQueue.enqueue(t))

    // Return the first element from the queue without removing it
    def peek: Transaction = transactionQueue.synchronized(transactionQueue.front)

    // Return an iterator to allow you to iterate over the queue
    def iterator: Iterator[Transaction] = transactionQueue.synchronized(transactionQueue.iterator)
}

class Transaction(val transactionsQueue: TransactionQueue,
                  val processedTransactions: TransactionQueue,
                  val from: Account,
                  val to: Account,
                  val amount: Double,
                  val allowedAttemps: Int) extends Runnable {

  var status: TransactionStatus.Value = TransactionStatus.PENDING
  var attempt = 0

  override def run: Unit = {

    def doTransaction(): Unit = {
      var result = from.withdraw(amount)
      if (result.isLeft) {
        to.deposit(amount)
        status = TransactionStatus.SUCCESS
        processedTransactions.push(this)
      } else if (attempt >= allowedAttemps) {
        status = TransactionStatus.FAILED
        processedTransactions.push(this)
      } else if (result.isRight){
        attempt+=1
      }
    }
    // TODO - project task 3
    // Extend this method to satisfy requirements

      // TODO - project task 3
      // make the code below thread safe
    if (status == TransactionStatus.PENDING) {
      doTransaction()
      Thread.sleep(50) // you might want this to make more room for
      // new transactions to be added to the queue
      }


    }
}
