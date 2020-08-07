package com.example.doggocam.screens.preview

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.doggocam.R
import com.example.doggocam.databinding.FragmentPreviewBinding

class PreviewFragment : Fragment() {

    private lateinit var viewModel: PreviewViewModel

    private lateinit var binding: FragmentPreviewBinding

    private lateinit var display: ImageView

    private lateinit var image: ByteArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        image = requireArguments().getByteArray("image")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_preview, container, false)

        // Setup view model so bound layout can access view model data
        viewModel = ViewModelProvider(this).get(PreviewViewModel::class.java)
        binding.previewViewModel = viewModel

        // Declare life cycle owner so binding can observe LiveData updates
        binding.setLifecycleOwner(this)

        // Configure variables from binding
        display = binding.imageView

        // Decode bitmap and set imageView
        val rotatedBitmapImage = viewModel.decodeAndRotate(image)
        showImage(rotatedBitmapImage)

        return binding.root
    }

    private fun showImage(decodedBitmap: Bitmap) {
        display.setImageBitmap(decodedBitmap)
    }
}