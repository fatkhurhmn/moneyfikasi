package dev.muffar.moneyfikasi.export.utils

import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.format
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.OutputStream
import java.io.OutputStreamWriter

class ExportManager {

    fun exportToCsv(transactions: List<Transaction>, outputStream: OutputStream) {
        val writer = OutputStreamWriter(outputStream)
        writer.write("Date,Type,Category,Wallet,Amount,Note\n")
        transactions.forEach { transaction ->
            val line = "${transaction.date.format("yyyy-MM-dd HH:mm")}," +
                    "${transaction.type.value}," +
                    "${transaction.category.name}," +
                    "${transaction.wallet.name}," +
                    "${transaction.amount}," +
                    "${transaction.note ?: ""}\n"
            writer.write(line)
        }
        writer.flush()
        writer.close()
    }

    fun exportToExcel(transactions: List<Transaction>, outputStream: OutputStream) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Transactions")

        val headerRow = sheet.createRow(0)
        val headers = listOf("Date", "Type", "Category", "Wallet", "Amount", "Note")
        headers.forEachIndexed { index, header ->
            headerRow.createCell(index).setCellValue(header)
        }

        transactions.forEachIndexed { index, transaction ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(transaction.date.format("yyyy-MM-dd HH:mm"))
            row.createCell(1).setCellValue(transaction.type.value)
            row.createCell(2).setCellValue(transaction.category.name)
            row.createCell(3).setCellValue(transaction.wallet.name)
            row.createCell(4).setCellValue(transaction.amount)
            row.createCell(5).setCellValue(transaction.note ?: "")
        }

        workbook.write(outputStream)
        workbook.close()
    }
}