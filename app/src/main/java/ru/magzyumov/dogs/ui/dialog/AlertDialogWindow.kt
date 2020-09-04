package ru.magzyumov.dogs.ui.dialog

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class AlertDialogWindow {
    private var mBuilder: AlertDialog.Builder
    private var mAlertDialog: AlertDialog
    private var mNegativeButtonListener: DialogInterface.OnClickListener

    // Конструктор для Alert c одной кнопкой
    constructor(
        context: Context,
        negativeButtonText: String
    ) {
        mNegativeButtonListener =
            DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() }
        mBuilder = AlertDialog.Builder(context)
        mBuilder.setCancelable(false)
                .setNegativeButton(negativeButtonText, mNegativeButtonListener)
        mAlertDialog = mBuilder.create()
    }

    // Конструктор для Alert c двумя кнопками
    constructor(
        context: Context,
        negativeButtonText: String,
        positiveButtonText: String,
        positiveButtonListener: DialogInterface.OnClickListener
    ) {
        mNegativeButtonListener =
            DialogInterface.OnClickListener { dialog: DialogInterface, which: Int -> dialog.dismiss() }
        mBuilder = AlertDialog.Builder(context)
        mBuilder.setCancelable(false)
            .setNegativeButton(negativeButtonText, mNegativeButtonListener)
            .setPositiveButton(positiveButtonText, positiveButtonListener)
        mAlertDialog = mBuilder.create()
    }

    // Метод показа Alert с динамичным сообщением
    fun show(title: String, message: String) {
        mAlertDialog.setTitle(title)
        mAlertDialog.setMessage(message)
        mAlertDialog.show()
    }
}