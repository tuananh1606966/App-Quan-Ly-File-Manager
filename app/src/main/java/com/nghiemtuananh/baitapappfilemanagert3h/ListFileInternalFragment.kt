package com.nghiemtuananh.baitapappfilemanagert3h

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nghiemtuananh.baitapappfilemanagert3h.basefragment.BaseFragment
import com.nghiemtuananh.baitapappfilemanagert3h.databinding.FragmentListFileBinding
import com.nghiemtuananh.baitapappfilemanagert3h.myinterface.IActivityLongClick
import com.nghiemtuananh.baitapappfilemanagert3h.myinterface.IDataAndClick
import java.io.File
import java.util.*

open class ListFileInternalFragment : BaseFragment(), IDataAndClick {
    private lateinit var binding: FragmentListFileBinding
    lateinit var inter: IActivityLongClick
    var listFolder: ArrayList<FileData> = arrayListOf()
    open var rootPath: String? = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentListFileBinding.inflate(inflater, container, false)
        binding.rcvListFile.adapter = FolderAdapter(this)
        binding.rcvListFile.layoutManager = LinearLayoutManager(context)
        binding.rcvListFile.addItemDecoration(DividerItemDecoration(context,
            DividerItemDecoration.VERTICAL))
        val anim = AnimationUtils.loadAnimation(binding.root.context, R.anim.alpha)
        binding.rcvListFile.startAnimation(anim)
        rootPath = requireArguments().getString("path")
        initData(rootPath)
        binding.data = rootPath
        inter = context as IActivityLongClick
        return binding.root
    }

    override fun onLongClick(folder: FileData) {
        if (folder.isVisibleCheckBox) {
            selectMoreItem(folder)
        } else {
            afterLongClick(folder)
        }
    }

    private fun selectMoreItem(folder: FileData) {
        var countItemSelected = 0
        folder.isChecked = !folder.isChecked
        for (f in listFolder) {
            if (f.isChecked) {
                countItemSelected++
            }
        }
        inter.selectMoreItem(countItemSelected)
    }

    private fun afterLongClick(folder: FileData) {
        folder.isChecked = true
        for (f in listFolder) {
            f.isVisibleCheckBox = true
        }
        inter.changeToolBarAfterLongClick()
    }

    override fun onClick(folder: FileData) {
        if (folder.isVisibleCheckBox) {
            selectMoreItem(folder)
        }
        if (!folder.isDirectory) {
            openFile(folder)
        }
    }

    private fun openFile(folder: FileData) {
        val intent = Intent()
        val file: File = File(folder.path)
        val map = MimeTypeMap.getSingleton()
        val ext = MimeTypeMap.getFileExtensionFromUrl(file.name)
        val type = map.getMimeTypeFromExtension(ext)
        intent.action = Intent.ACTION_VIEW
        val photoURI = FileProvider.getUriForFile(requireContext(),
            requireActivity().getPackageName().toString() + ".provider",
            File(folder.path))
        intent.setDataAndType(photoURI, type)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent)
    }

    override fun getCount(): Int {
        return listFolder.size
    }

    override fun getItem(position: Int): FileData {
        return listFolder[position]
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initData(path: String?) {
        var itemCount: Int
        var lastModDate: Date
        var resourceId: Int
        var itemData: FileData
        var isDirectory: Boolean
        rootPath = path
        if (File(rootPath).isDirectory) {
            listFolder.clear()
            var listFile = File(rootPath).listFiles()
            if (listFile != null) {
                for (f in listFile) {
                    if (f.isDirectory) {
                        resourceId = R.drawable.baseline_folder_yellow_500_48dp
                        itemCount = f.listFiles().size
                        lastModDate = Date(f.lastModified())
                        isDirectory = true
                    } else if (f.name.lowercase().endsWith(".docs") || f.name.lowercase()
                            .endsWith(".txt")
                    ) {
                        resourceId = R.drawable.baseline_insert_drive_file_yellow_500_48dp
                        lastModDate = Date(f.lastModified())
                        itemCount = f.length().toInt()
                        isDirectory = false
                    } else if (f.name.lowercase().endsWith(".jpg") || f.name.lowercase()
                            .endsWith(".png") || f.name.lowercase().endsWith(".jpeg")
                    ) {
                        resourceId = R.drawable.baseline_crop_original_yellow_500_48dp
                        lastModDate = Date(f.lastModified())
                        itemCount = f.length().toInt()
                        isDirectory = false
                    } else if (f.name.lowercase().endsWith(".apk") || f.name.lowercase()
                            .endsWith(".xapk")
                    ) {
                        resourceId = R.drawable.baseline_android_yellow_500_48dp
                        lastModDate = Date(f.lastModified())
                        itemCount = f.length().toInt()
                        isDirectory = false
                    } else if (f.name.lowercase().endsWith(".mp3") || f.name.lowercase()
                            .endsWith(".wav")
                    ) {
                        resourceId = R.drawable.baseline_music_note_yellow_500_48dp
                        lastModDate = Date(f.lastModified())
                        itemCount = f.length().toInt()
                        isDirectory = false
                    } else if (f.name.lowercase().endsWith(".pdf")) {
                        resourceId = R.drawable.baseline_picture_as_pdf_yellow_500_48dp
                        lastModDate = Date(f.lastModified())
                        itemCount = f.length().toInt()
                        isDirectory = false
                    } else if (f.name.lowercase().endsWith(".mp4")) {
                        resourceId = R.drawable.baseline_ondemand_video_yellow_500_48dp
                        lastModDate = Date(f.lastModified())
                        itemCount = f.length().toInt()
                        isDirectory = false
                    } else {
                        resourceId = R.drawable.baseline_insert_drive_file_yellow_500_48dp
                        lastModDate = Date(f.lastModified())
                        itemCount = f.length().toInt()
                        isDirectory = false
                    }
                    itemData = FileData(
                        f.name,
                        lastModDate,
                        resourceId,
                        itemCount,
                        f.path,
                        isDirectory
                    )
                    listFolder.add(itemData)
                }
            }
        }
        binding.rcvListFile.adapter?.notifyDataSetChanged()
    }

    override fun onBackPressForFragment() {
        if (listFolder.size != 0) {
            if (listFolder[0].isVisibleCheckBox) {
                returnNornal()
                inter.returnNormal("Internal storage")
            } else {
                super.onBackPressForFragment()
            }
        } else
            super.onBackPressForFragment()
    }



    @SuppressLint("NotifyDataSetChanged")
    fun returnNornal() {
        for (f in listFolder) {
            f.isChecked = false
            f.isVisibleCheckBox = false
        }
        binding.rcvListFile.adapter!!.notifyDataSetChanged()
    }
}