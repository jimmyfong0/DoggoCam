package com.example.doggocam.screens.camera

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.doggocam.R
import com.example.doggocam.databinding.FragmentCameraBinding
import io.fotoapparat.Fotoapparat
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.back
import io.fotoapparat.view.CameraView
import kotlinx.android.synthetic.main.fragment_camera.view.*
import java.io.ByteArrayOutputStream

// Initialising maxWidth and maxHeight for Inception processing
private const val maxWidth = 256
private const val maxHeight = 256

class CameraFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentCameraBinding

    private lateinit var camera: CameraView
    private lateinit var captureBtn: ImageButton

    private lateinit var fotoapparat: Fotoapparat
    private lateinit var data: Bitmap

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_camera, container, false)

        binding.setLifecycleOwner(this)

        // INSERT OBSERVERS HERE //

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        captureBtn = binding.captureBtn
        // Make button background transparent
        captureBtn.background.alpha = 0
        // Set click listener
        view.captureBtn.setOnClickListener(this)

        camera = binding.camera
        // Configure Fotoapparat camera instance
        fotoapparat = Fotoapparat(
            context=requireActivity().applicationContext,
            view=camera,
            scaleType = ScaleType.CenterCrop,
            lensPosition=back()
        )
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.captureBtn -> {
                val photoResult = fotoapparat.takePicture()
                photoResult.toBitmap().whenAvailable { bitmapPhoto ->
                    bitmapPhoto?.let {
                        // Convert data to bitmap and then to bytearray to package into bundle
                        data = bitmapPhoto.bitmap
                        // Configure ByteArray Stream
                        val stream: ByteArrayOutputStream = ByteArrayOutputStream()
                        // Compress image into byte array
                        data.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                        val byteArray: ByteArray = stream.toByteArray()
                        // Package image byte array into bundle
                        val bundle = bundleOf("image" to byteArray)
                        navController!!.navigate(R.id.action_cameraFragment_to_previewFragment, bundle)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        fotoapparat.start()
    }

    override fun onStop() {
        super.onStop()
        fotoapparat.stop()
    }
}