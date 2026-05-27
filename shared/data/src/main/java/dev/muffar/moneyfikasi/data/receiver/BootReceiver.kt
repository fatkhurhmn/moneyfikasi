package dev.muffar.moneyfikasi.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dev.muffar.moneyfikasi.data.utils.RecurringTransactionScheduler

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            RecurringTransactionScheduler(context).scheduleRecurringTransaction()
        }
    }
}
