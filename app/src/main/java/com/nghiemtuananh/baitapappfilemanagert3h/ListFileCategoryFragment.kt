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
import com.nghiemtuananh.baitapappfilemanagert3h.myinterface.IActivityAndCategory
import com.nghiemtuananh.baitapappfilemanagert3h.myinterface.IActivityLongClick
import com.nghiemtuananh.baitapappfilemanagert3h.myinterface.IDataAndClick
import java.io.File
import kotlin.collections.ArrayList

class ListFileCategoryFragment : BaseFragment(), IDataAndClick {
    lateinit var inter: IActivityAndCategory
    lateinit var interLongClick: IActivityLongClick
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
        var list = requireArguments().getSerializable("list") as ArrayList<FileData>
        listFolder.addAll(list)
        inter = context as IActivityAndCategory
        inter.reloadCheckNavigation()
        val anim = AnimationUtils.loadAnimation(binding.root.context, R.anim.alpha)
        binding.rcvListFile.startAnimation(anim)
        binding.rcvListFile.adapter?.notifyDataSetChanged()
        interLongClick = context as IActivityLongClick
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    fun returnNormal() {
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
                interLongClick.returnNormal("Category")
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
        interLongClick.selectMoreItem(countItemSelected)
    }

    private fun afterLongClick(folder: FileData) {
        folder.isChecked = true
        for (f in listFolder) {
            f.isVisibleCheckBox = true
        }
        interLongClick.changeToolBarAfterLongClick()
    }
}