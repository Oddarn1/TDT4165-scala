import exceptions._

class Account(val bank: Bank, initialBalance: Double) {

    class Balance(var amount: Double) {}

    val balance = new Balance(initialBalance)

    // TODO
    // for project task 1.2: implement functions
    // for project task 1.3: change return type and update function bodies
    def withdraw(amount: Double): Unit = {
        if (amount < 0) throw new IllegalArgumentException("Can't withdraw negative values")
        val newBalance = balance.amount - amount
        if (newBalance < 0) throw new IllegalArgumentException("Not enough money")
        balance.amount = newBalance
    }
    def deposit (amount: Double): Unit = {
        if (amount < 0) throw new IllegalArgumentException("Can't deposit negative values")
        balance.amount += amount
    }
    def getBalanceAmount: Double = balance.amount

    def transferTo(account: Account, amount: Double) = {
        bank addTransactionToQueue (this, account, amount)
    }


}
