package com.nghiemtuananh.baitapappfilemanagert3h

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.nghiemtuananh.baitapappfilemanagert3h.basefragment.BaseFragment
import com.nghiemtuananh.baitapappfilemanagert3h.databinding.FragmentListFileBinding
import com.nghiemtuananh.baitapappfilemanagert3h.myinterface.IDataAndClick
import java.io.File
import java.util.*

class ListFileInternalFragment : BaseFragment(), IDataAndClick {
    private lateinit var binding: FragmentListFileBinding
    var listFolder: ArrayList<FileData> = arrayListOf()
    var rootPath: String? = ""
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
        binding.ibtnMenu.setOnClickListener {
            if (listFolder.size != 0) {
                if (listFolder[0].isVisibleCheckBox) {
                    returnNormal()
                }
            }
        }
        binding.ibtnCancelSelect.setOnClickListener {
            if (listFolder.size != 0) {
                if (listFolder[0].isVisibleCheckBox) {
                    returnNormal()
                }
            }
        }
        rootPath = Environment.getExternalStorageDirectory().path
        initData(rootPath)
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
        binding.tvTitle.setText("$countItemSelected selected")
    }

    private fun afterLongClick(folder: FileData) {
        binding.ibtnSearch.isGone = true
        binding.ibtnDelete.isGone = false
        binding.ibtnCancelSelect.isGone = false
        binding.ibtnMenu.setImageResource(R.drawable.baseline_arrow_back_black_48dp)
        binding.tvTitle.setText("1 selected")
        binding.tvTitle.setTextColor(Color.BLACK)
        binding.tvTitle.setTextSize(24F)
        folder.isChecked = true
        for (f in listFolder) {
            f.isVisibleCheckBox = true
        }
    }

    override fun onClick(folder: FileData) {
        if (folder.isVisibleCheckBox) {
            selectMoreItem(folder)
        } else {
            initData(folder.path)
        }
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
        } else {
            val intent = Intent()
            val file: File = File(rootPath)
            val map = MimeTypeMap.getSingleton()
            val ext = MimeTypeMap.getFileExtensionFromUrl(file.name)
            val type = map.getMimeTypeFromExtension(ext)
            intent.action = Intent.ACTION_VIEW
            val photoURI = FileProvider.getUriForFile(requireContext(),
                requireActivity().getPackageName().toString() + ".provider",
                File(rootPath))
            intent.setDataAndType(photoURI, type)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent)
        }
        binding.rcvListFile.adapter?.notifyDataSetChanged()
    }

    override fun onBackPressForFragment() {
        if (listFolder.size != 0) {
            if (listFolder[0].isVisibleCheckBox) {
                returnNormal()
            } else {
                backFolder()
            }
        } else {
            backFolder()
        }
    }

    private fun backFolder() {
        if (rootPath.equals(Environment.getExternalStorageDirectory().path)) {
            super.onBackPressForFragment()
        } else {
            rootPath = File(rootPath).parent!!
            initData(rootPath)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun returnNormal() {
        binding.tvTitle.setText("Internal storage")
        binding.tvTitle.setTextSize(28f)
        binding.tvTitle.setTextColor(Color.parseColor("#03A9F4"))
        binding.ibtnMenu.setImageResource(R.drawable.baseline_menu_black_48dp)
        binding.ibtnCancelSelect.isGone = true
        binding.ibtnDelete.isGone = true
        binding.ibtnSearch.isGone = false
        for (f in listFolder) {
            f.isChecked = false
            f.isVisibleCheckBox = false
        }
        binding.rcvListFile.adapter!!.notifyDataSetChanged()
    }
}