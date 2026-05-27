package dev.muffar.moneyfikasi.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.muffar.moneyfikasi.domain.usecase.recurring_transaction.RecurringTransactionUseCases

@HiltWorker
class RecurringTransactionWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val recurringTransactionUseCases: RecurringTransactionUseCases
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            recurringTransactionUseCases.processRecurringTransactions()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        const val NAME = "recurring_transaction_worker"
    }
}
