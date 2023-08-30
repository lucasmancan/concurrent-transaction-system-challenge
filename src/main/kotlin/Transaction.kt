import java.math.BigDecimal


data class Transaction(
    val originAccount: String,
    val accountDestiny: String,
    val transactionValue: BigDecimal,
    val transactionDate: String
)

