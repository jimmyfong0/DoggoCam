package com.example.doggocam.screens.preview

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.lifecycle.ViewModel

class PreviewViewModel: ViewModel() {
    fun rotate(decodedBitmap: Bitmap): Bitmap {
        val width = decodedBitmap.width
        val height = decodedBitmap.height

        val matrix: Matrix = Matrix()
        matrix.setRotate(90F)
        return Bitmap.createBitmap(decodedBitmap, 0, 0, width, height, matrix, true)
    }

    fun decodeAndRotate(image: ByteArray): Bitmap {
        val bitmapImage = BitmapFactory.decodeByteArray(image, 0, image.size)
        return rotate(bitmapImage)
    }
}