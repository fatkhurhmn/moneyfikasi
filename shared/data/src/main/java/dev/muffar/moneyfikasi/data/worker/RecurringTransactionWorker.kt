package dev.muffar.moneyfikasi.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.muffar.moneyfikasi.data.utils.NotificationHelper
import dev.muffar.moneyfikasi.data.utils.RecurringTransactionScheduler
import dev.muffar.moneyfikasi.domain.usecase.preferences.ui.UiSettingsUseCases
import dev.muffar.moneyfikasi.domain.usecase.recurring_transaction.RecurringTransactionUseCases
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand
import kotlinx.coroutines.flow.first

@HiltWorker
class RecurringTransactionWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val recurringTransactionUseCases: RecurringTransactionUseCases,
    private val uiSettingsUseCases: UiSettingsUseCases,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val processed = recurringTransactionUseCases.processRecurringTransactions()
            val uiSettings = uiSettingsUseCases.getUiSettings().first()
            val notificationsEnabled = uiSettings.isAllowNotification &&
                    uiSettings.isRecurringTransactionNotificationEnabled
            if (notificationsEnabled) {
                val notificationHelper = NotificationHelper(applicationContext)
                processed.forEach {
                    notificationHelper.showRecurringNotification(
                        name = it.name,
                        amount = it.amount.formatThousand(),
                        transactionId = it.transactionId,
                        recurringId = it.recurringId,
                        type = it.type,
                        isEnded = it.isEnded
                    )
                }
            }
            RecurringTransactionScheduler(applicationContext).scheduleRecurringTransaction()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
