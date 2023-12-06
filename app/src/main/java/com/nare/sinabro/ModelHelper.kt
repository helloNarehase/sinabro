package com.nare.sinabro

import android.content.res.AssetManager
import android.util.Log
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.InterpreterApi
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.gpu.GpuDelegate
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

const val OUTPUT_BUFFER_SIZE = 1000
class ModelHelper(
    val luna: Luna,
    val assetManager: AssetManager,
) {

    private val outputBuffer = ByteBuffer.allocateDirect(OUTPUT_BUFFER_SIZE)

    lateinit var inter:InterpreterApi
//    private lateinit var inputImage : TensorImage
    private var modelInputWidths:Int = 0
    private var modelInputHeights:Int = 0
//    private lateinit var result:TensorBuffer
//    private lateinit var outputBuffer: TensorBuffer
    init {
        setModel()
    }
    fun setModel() {
        val compatList = CompatibilityList()
        val options = Interpreter.Options().apply {
            Log.e("tftf", compatList.isDelegateSupportedOnThisDevice.toString())
            if (compatList.isDelegateSupportedOnThisDevice) {
                addDelegate(GpuDelegate(compatList.bestOptionsForThisDevice))
            }
        }

        inter = Interpreter(loadModelFile())
        Log.e("modelInfo", "${inter.getInputTensor(0).dataType()} ${inter.getOutputTensor(0).dataType()}")

   }

    fun gen(text:String){
        outputBuffer.clear()
        inter.run(text, outputBuffer)
        outputBuffer.flip()

        Log.e("modelInfo", "${outputBuffer.char}")

        val bytes = ByteArray(outputBuffer.remaining())
        outputBuffer.get(bytes)
        // Return bytes converted to String
//        Log.e("modelInfo", String(bytes, Charsets.UTF_8))
//        Log.e("modelInfo", "${bytes.toList()}")
        outputBuffer.clear()
        luna.onResult(String(bytes, Charsets.UTF_8))
    }
    private fun loadModelFile(): ByteBuffer {
        val assetFileDescriptor = assetManager.openFd(modelPath)
        val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = fileInputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        Log.e("tftf", "tftf")
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }


    interface Luna{
        fun onResult(
            results: String
        )
    }
    companion object {
        const val modelPath = "NARE_GPT.tflite"
    }
}

