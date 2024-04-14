package dev.muffar.moneyfikasi.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.muffar.moneyfikasi.domain.model.LoanType
import org.threeten.bp.LocalDateTime
import java.util.UUID

@Entity(tableName = "loans")
data class LoanEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: UUID,

    @ColumnInfo(name = "wallet_id")
    val walletId: UUID,

    @ColumnInfo(name = "type")
    val type: LoanType,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "amount")
    val amount: Double,

    @ColumnInfo(name = "note")
    val note: String? = null,

    @ColumnInfo(name = "loan_date")
    val loanDate: LocalDateTime,

    @ColumnInfo(name = "due_date")
    val dueDate: LocalDateTime,

    @ColumnInfo(name = "is_paid_off")
    val isPaidOff: Boolean,
)
