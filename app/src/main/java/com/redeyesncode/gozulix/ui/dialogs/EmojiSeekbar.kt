package com.redeyesncode.gozulix.ui.dialogs

import android.content.Context
import android.util.AttributeSet
import android.widget.SeekBar
import com.redeyesncode.gozulix.R

class EmojiSeekbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatSeekBar(context, attrs, defStyleAttr) {

    init {
        val thumbSize = 30 // Set the desired thumb size in pixels
        thumb = resources.getDrawable(R.drawable.angry_thumb_drawable) // Use your custom thumb drawable
        thumbOffset = thumbSize / 2 // Center the thumb
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Set the size of the thumb here (adjust these values as needed)
        val thumbSize = 100 // Set your desired size in pixels
        thumb = resources.getDrawable(R.drawable.angry_thumb_drawable) // Use your custom thumb drawable
        thumbOffset = thumbSize / 2 // Adjust offset to center the thumb
    }
}
