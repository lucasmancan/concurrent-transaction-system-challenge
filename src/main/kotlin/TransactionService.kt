class TransactionService {

    private val transactionsDb: MutableSet<Transaction> = mutableSetOf()
    private val foo = Any()

    fun ingest(transaction: String) {
        val parsedTransaction = parseTransaction(transaction)

        /*
            Although the Set data struct prevent duplication in database,
            in a real system is necessary to prevent the insert operation in case of data duplication
         */
        synchronized(this){
            if (exists(parsedTransaction)) {
                throw TransactionAlreadyExistsException()
            }
            transactionsDb.add(parsedTransaction)
        }
    }

    private fun parseTransaction(transaction: String): Transaction {
        val transactionMetadata = transaction.split(" ")

        return Transaction(
            originAccount = transactionMetadata[0],
            accountDestiny = transactionMetadata[1],
            transactionValue = transactionMetadata[2].toBigDecimal(),
            transactionDate = transactionMetadata[3]
        )
    }

    private fun exists(transaction: Transaction): Boolean {
        return transactionsDb.contains(transaction)
    }

    fun fetchHistory(accountId: String): List<Transaction> {
        return transactionsDb.filter { it.accountDestiny == accountId || it.originAccount == accountId }
    }

    fun fetchAll(): Set<Transaction> {
        return transactionsDb
    }
}