import exceptions._

class Account(val bank: Bank, initialBalance: Double) {

    class Balance(var amount: Double) {}

    val balance = new Balance(initialBalance)

    // TODO
    // for project task 1.3: change return type and update function bodies
    def withdraw(amount: Double): Either[Unit, String] = this.synchronized {
        if (amount < 0) return Right("Can't withdraw negative values")
        val newBalance = balance.amount - amount
        if (newBalance < 0) return Right("Not enough money")
        return Left(balance.amount = newBalance)
    }
    def deposit (amount: Double): Either[Unit, String] = this.synchronized {
        if (amount < 0) return Right("Can't deposit negative values")
        return Left(balance.amount += amount)
    }
    def getBalanceAmount: Double = balance.amount

    def transferTo(account: Account, amount: Double):Unit = {
        bank addTransactionToQueue (this, account, amount)
    }

}
