package com.yan.feracode.spotify.view.utils

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import com.squareup.picasso.Transformation

class BlurEffectUtils(protected val context: Context, radius: Int) : Transformation {
    private val blurRadius: Int

    init {

        if (radius < LOW_LIMIT) {
            this.blurRadius = LOW_LIMIT
        } else if (radius > UP_LIMIT) {
            this.blurRadius = UP_LIMIT
        } else {
            this.blurRadius = radius
        }
    }

    override fun transform(source: Bitmap): Bitmap {

        val blurredBitmap: Bitmap
        blurredBitmap = Bitmap.createBitmap(source)

        val renderScript = RenderScript.create(context)

        val input = Allocation.createFromBitmap(renderScript, source, Allocation.MipmapControl.MIPMAP_FULL,
                Allocation.USAGE_SCRIPT)

        val output = Allocation.createTyped(renderScript, input.type)

        val script = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))

        script.setInput(input)
        script.setRadius(blurRadius.toFloat())

        script.forEach(output)
        output.copyTo(blurredBitmap)

        return blurredBitmap
    }

    override fun key(): String {
        return "blurred"
    }

    companion object {

        private val UP_LIMIT = 25
        private val LOW_LIMIT = 1
    }
}