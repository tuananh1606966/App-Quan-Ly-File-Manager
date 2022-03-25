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
import android.view.animation.AnimationUtils
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.view.isGone
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
import kotlin.collections.ArrayList

class ListFileCategoryFragment : BaseFragment(), IDataAndClick {
    lateinit var binding: FragmentListFileBinding
    var listFolder: ArrayList<FileData> = arrayListOf()
    @SuppressLint("NotifyDataSetChanged")
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
        binding.tvTitle.setText("Category")
        var list = requireArguments().getSerializable("list") as ArrayList<FileData>
        listFolder.addAll(list)
        val anim = AnimationUtils.loadAnimation(binding.root.context, R.anim.alpha)
        binding.rcvListFile.startAnimation(anim)
        binding.rcvListFile.adapter?.notifyDataSetChanged()
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun returnNormal() {
        binding.tvTitle.setText("Category")
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

    override fun getCount(): Int {
        return listFolder.size
    }

    override fun getItem(position: Int): FileData {
        return listFolder[position]
    }

    override fun onClick(folder: FileData) {
        if (folder.isVisibleCheckBox) {
            selectMoreItem(folder)
        } else {
            openFile(folder)
        }
    }

    override fun onBackPressForFragment() {
        if (listFolder.size != 0) {
            if (listFolder[0].isVisibleCheckBox) {
                returnNormal()
            } else {
                super.onBackPressForFragment()
            }
        } else {
            super.onBackPressForFragment()
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
}