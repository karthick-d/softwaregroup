package com.softwaregroup.imageconcept.ui

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.softwaregroup.imageconcept.databinding.FileChooserDialogBinding

/**
 * TODO
 *
 * @property onItemClicked
 * @property visibility
 * @property value
 */

class OptionDialogFragment(
    var onItemClicked: OnItemSelect,
    var visibility: Boolean,
    var value: Int
) : DialogFragment() {

    private lateinit var dataBinding: FileChooserDialogBinding

    interface OnItemSelect {
        fun onGalleryButtonClicked()
        fun onCameraButtonClicked()

        fun onCancelOutSide()
    }

    companion object {
        const val TAG = "OptionDialogFragment"

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            FileChooserDialogBinding.inflate(LayoutInflater.from(context), container, false)
        return dataBinding.root
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onItemClicked.onCancelOutSide()
        Log.e("Error", "Dialog click out side")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setupView(view)
        setupClickListeners()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupClickListeners() {
        dataBinding.buttonCamera.setOnClickListener {
            onItemClicked.onCameraButtonClicked()
            dismiss()
        }

        dataBinding.buttonGallery.setOnClickListener {
            onItemClicked.onGalleryButtonClicked()
            dismiss()
        }

    }

}