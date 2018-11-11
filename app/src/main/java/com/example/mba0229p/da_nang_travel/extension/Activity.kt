package com.example.mba0229p.da_nang_travel.extension

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.support.annotation.IdRes
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog

internal fun Activity.showDialog(message: String, rightButton: String, leftButton: String, rightButtonListener: DialogInterface.OnClickListener) = AlertDialog.Builder(this)
        .setMessage(message)
        .setPositiveButton(rightButton, rightButtonListener)
        .setNegativeButton(leftButton) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        .create().apply {
            setCanceledOnTouchOutside(false)
        }
        .show()


internal fun Activity.showInformDialog(message: String, leftButton: String) = AlertDialog.Builder(this)
        .setMessage(message)
        .setNegativeButton(leftButton) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        .create().apply {
            setCanceledOnTouchOutside(false)
        }
        .show()

/**
 * Start Activity
 */
internal fun Activity.moveActivity(intent: Intent) {
    startActivity(intent)
}

internal fun FragmentActivity.replaceFragment(@IdRes containerId: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction().apply {
        replace(containerId, fragment)
        commit()
    }
}

internal fun FragmentActivity.addFragment(@IdRes containerId: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction().apply {
        addToBackStack(fragment.tag)
        add(containerId, fragment)
        commit()
    }
}
