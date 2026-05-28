package dev.muffar.moneyfikasi.data.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dev.muffar.moneyfikasi.data.receiver.RecurringTransactionReceiver
import dev.muffar.moneyfikasi.data.worker.RecurringTransactionWorker
import java.util.Calendar

class RecurringTransactionScheduler(private val context: Context) {
    fun updateRecurringTransactionSchedule(hasActiveRecurring: Boolean) {
        if (hasActiveRecurring) {
            scheduleRecurringTransaction()
        } else {
            cancelRecurringTransaction()
        }
    }

    fun scheduleRecurringTransaction() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, RecurringTransactionReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            RECURRING_TRANSACTION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_HOUR,
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
