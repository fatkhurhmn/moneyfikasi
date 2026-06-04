package dev.muffar.moneyfikasi.data.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dev.muffar.moneyfikasi.data.db.MoneyfikasiDatabase
import dev.muffar.moneyfikasi.data.receiver.RecurringTransactionReceiver
import dev.muffar.moneyfikasi.data.worker.RecurringTransactionWorker
import kotlinx.coroutines.runBlocking

class RecurringTransactionScheduler(private val context: Context) {
    fun updateRecurringTransactionSchedule(hasActiveRecurring: Boolean) {
        if (hasActiveRecurring) {
            scheduleRecurringTransaction()
        } else {
            cancelRecurringTransaction()
        }
    }

    fun scheduleRecurringTransaction() {
        val nextRun = runBlocking {
            MoneyfikasiDatabase.create(context).recurringTransactionDao().getMinNextRun()
        }

        if (nextRun == null) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, RecurringTransactionReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            RECURRING_TRANSACTION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerAt = if (nextRun <= System.currentTimeMillis()) {
            System.currentTimeMillis() + 1000 // Run almost immediately if in the past
        } else {
            nextRun
        }

        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAt,
            pendingIntent
        )
    }

    fun cancelRecurringTransaction() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, RecurringTransactionReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            RECURRING_TRANSACTION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    fun runRecurringTransactionWorker() {
        val workRequest = OneTimeWorkRequestBuilder<RecurringTransactionWorker>().build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }

    companion object {
        const val RECURRING_TRANSACTION_REQUEST_CODE = 1001
    }
}
