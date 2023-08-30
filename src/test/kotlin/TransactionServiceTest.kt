import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors


class TransactionServiceTest {

    val transactionService = TransactionService()

    @Test
    fun `should ingest transactions in a concurrent environment`() {
        val transactions = arrayOf(
            "conta-1 conta-2 200.00 2023-01-01",
            "conta-1 conta-3 100.00 2023-01-02",
            "conta-2 conta-3 100.00 2023-01-02",
            "conta-3 conta-4 50.00 2023-01-03",
            "conta-1 conta-3 100.00 2023-01-02",
            "conta-4 conta-1 300.00 2023-01-05",
            "conta-3 conta-2 100.00 2023-01-07"
        )

        val numberOfThreads = 100
        val service = Executors.newFixedThreadPool(100)
        val latch = CountDownLatch(numberOfThreads)


        for (i in 0 until numberOfThreads) {
            service.execute {
                transactions.forEach {
                    try {
                        transactionService.ingest(it)
                    } catch (_: Exception) { }
                }
                latch.countDown()
            }
        }

        latch.await()

        assertEquals(6, transactionService.fetchAll().size)
    }
}
