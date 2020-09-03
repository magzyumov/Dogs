package ru.magzyumov.dogs.ui.dialog

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class AlertDialogWindow {
    private var builder: AlertDialog.Builder
    private var alertDialog: AlertDialog
    private var negativeButtonListener: DialogInterface.OnClickListener

    // Конструктор для Alert c одной кнопкой
    constructor(
        context: Context,
        negativeButtonText: String
    ) {
        negativeButtonListener =
            DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() }
        builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
                .setNegativeButton(negativeButtonText, negativeButtonListener)
        alertDialog = builder.create()
    }

    // Конструктор для Alert c двумя кнопками
    constructor(
        context: Context,
        negativeButtonText: String,
        positiveButtonText: String,
        positiveButtonListener: DialogInterface.OnClickListener
    ) {
        negativeButtonListener =
            DialogInterface.OnClickListener { dialog: DialogInterface, which: Int -> dialog.dismiss() }
        builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
            .setNegativeButton(negativeButtonText, negativeButtonListener)
            .setPositiveButton(positiveButtonText, positiveButtonListener)
        alertDialog = builder.create()
    }

    // Метод показа Alert с динамичным сообщением
    fun show(title: String, message: String) {
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.show()
    }
}