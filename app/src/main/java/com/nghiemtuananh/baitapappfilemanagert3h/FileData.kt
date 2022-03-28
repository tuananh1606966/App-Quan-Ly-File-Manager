package com.nghiemtuananh.baitapappfilemanagert3h

import android.graphics.Bitmap
import java.io.Serializable
import java.util.*

data class FileData(val name: String, val date: Date, val resourceId: Int, val countItem: Int?, val path: String, val isDirectory: Boolean, val bitmap: Bitmap?, var isChecked: Boolean = false, var isVisibleCheckBox: Boolean = false): Serializable