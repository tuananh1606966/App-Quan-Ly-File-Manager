package com.nghiemtuananh.baitapappfilemanagert3h.myinterface

import com.nghiemtuananh.baitapappfilemanagert3h.FileData

interface IDataAndClick {
    fun getCount(): Int
    fun getItem(position: Int): FileData
    fun onClick(folder: FileData)
    fun onLongClick(folder: FileData)
}