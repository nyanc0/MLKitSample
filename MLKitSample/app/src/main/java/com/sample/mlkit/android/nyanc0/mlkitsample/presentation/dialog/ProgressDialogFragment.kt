package com.sample.mlkit.android.nyanc0.mlkitsample.presentation.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.sample.mlkit.android.nyanc0.mlkitsample.R

class ProgressDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity)
        dialog.let {
            it.requestWindowFeature(Window.FEATURE_NO_TITLE)
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
            it.setContentView(R.layout.fragment_progress)
        }

        val window = dialog.window
        window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
            )
            it.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        }
        return dialog
    }

    override fun show(manager: FragmentManager, tag: String?) {
        dismissProgress(manager, tag)
        try {
            val transaction = manager.beginTransaction()
            transaction.add(this, tag)
            transaction.commitAllowingStateLoss()
        } catch (e: Exception) {
            // 握りつぶす
        }
        super.show(manager, tag)
    }


    fun dismissProgress(manager: FragmentManager, tag: String?) {
        val prevFragment = manager.findFragmentByTag(tag) ?: return
        val dialogFragment = prevFragment as DialogFragment
        val dialog = dialogFragment.dialog
        if (dialog != null && dialog.isShowing) {
            prevFragment.onDismiss(dialog)
        }
    }

    companion object {
        private val TAG = ProgressDialogFragment::class.java.canonicalName

        fun showProgress(fragmentManager: FragmentManager) {
            val progressDialogFragment = ProgressDialogFragment()
            progressDialogFragment.show(fragmentManager, TAG)
        }
    }
}