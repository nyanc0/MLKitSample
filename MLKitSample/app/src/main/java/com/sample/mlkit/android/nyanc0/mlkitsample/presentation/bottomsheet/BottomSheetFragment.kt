package com.sample.mlkit.android.nyanc0.mlkitsample.presentation.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sample.mlkit.android.nyanc0.mlkitsample.R
import com.sample.mlkit.android.nyanc0.mlkitsample.databinding.DialogBottomSheetBinding
import com.sample.mlkit.android.nyanc0.mlkitsample.model.ImageSelection

class BottomSheetFragment : BottomSheetDialogFragment(),
    ImageSelectionAdapter.OnItemClickListener {

    lateinit var listener: OnItemSelectedListener
    lateinit var binding: DialogBottomSheetBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnItemSelectedListener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_bottom_sheet, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageSelectionList = arrayListOf(
            ImageSelection.PHOTO,
            ImageSelection.LIBRARY
        )
        binding.imageSelectionList.layoutManager = LinearLayoutManager(context)
        binding.imageSelectionList.adapter =
                ImageSelectionAdapter(
                    imageSelectionList,
                    this
                )
    }

    override fun onItemClicked(item: ImageSelection) {
        dismissAllowingStateLoss()
        listener.onItemSelected(item)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        showNow(manager, tag)
    }

    override fun showNow(manager: FragmentManager, tag: String?) {
        if (isShown(manager, tag)) return
        super.showNow(manager, tag)
    }

    /**
     * Dialogが表示されているかをチェックする.
     *
     * @param fragmentManager FragmentManager
     * @param tag Tag
     */
    private fun isShown(fragmentManager: FragmentManager, tag: String?): Boolean {
        val fragment = fragmentManager.findFragmentByTag(tag)
        if (fragment is DialogFragment) {
            val dialog = fragment.dialog
            if (dialog != null && dialog.isShowing) {
                return true
            }
        }
        return false
    }

    interface OnItemSelectedListener {
        fun onItemSelected(item: ImageSelection)
    }

    companion object {
        fun newInstance(): BottomSheetFragment {
            return BottomSheetFragment()
        }

        val TAG = BottomSheetFragment::class.java.canonicalName
    }
}