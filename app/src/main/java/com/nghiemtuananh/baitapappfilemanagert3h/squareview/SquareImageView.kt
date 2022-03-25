package com.nghiemtuananh.baitapappfilemanagert3h.squareview

import android.content.Context
import android.util.AttributeSet
import android.widget.ProgressBar
import androidx.cardview.widget.CardView

class SquareImageView: CardView {
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr)

    constructor(context: Context) : super(context)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

}