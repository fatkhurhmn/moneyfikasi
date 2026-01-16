package dev.muffar.moneyfikasi.domain.usecase.transaction

import dev.muffar.moneyfikasi.domain.model.TransferDetail
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import java.util.UUID

class GetTransferDetail(
    private val repository: TransactionRepository,
) {
    suspend operator fun invoke(id: UUID): TransferDetail? {
        return repository.getTransferDetail(id)
    }
}
