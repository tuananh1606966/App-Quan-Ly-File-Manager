package com.nghiemtuananh.baitapappfilemanagert3h

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat

object CommonApp {
    @JvmStatic
    val FORMAT_TIME = SimpleDateFormat("dd/MM/yyyy hh:mm a")

    @JvmStatic
    @BindingAdapter("selectTv")
    fun selectTv(view: TextView, name: String) {
        view.isSelected = true
        view.setText(name)
    }

    @JvmStatic
    @BindingAdapter("loadNormalImageResource")
    fun loadNormalImageResource(view: ImageView, resource: Int) {
        if (resource == null) {
            view.setImageResource(R.drawable.ic_launcher_background)
            return
        } else {
            view.setImageResource(resource)
        }
    }

    @JvmStatic
    fun setItemFolder(item: Int): String {
        return item.toString()
    }

    @JvmStatic
    fun setSizeFile(size: Int): String {
        var sizeFile = size.toDouble() / 1024

        return if (sizeFile >= 1024) {
            (Math.round((sizeFile / 1024) * 100) / 100.0).toString() + " Mb"
        } else {
            (Math.round((sizeFile * 100)) / 100.0).toString() + "Kb"
        }
    }

    @JvmStatic
    fun setItems(countItem: Int): String {
        if (countItem > 1) {
            return "items"
        } else {
            return "item"
        }
    }
}