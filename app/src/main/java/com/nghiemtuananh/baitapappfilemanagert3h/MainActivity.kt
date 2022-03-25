package com.nghiemtuananh.baitapappfilemanagert3h

import android.Manifest
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.nghiemtuananh.baitapappfilemanagert3h.baseactivity.BaseActivity
import com.nghiemtuananh.baitapappfilemanagert3h.databinding.ActivityMainBinding
import com.nghiemtuananh.baitapappfilemanagert3h.myinterface.IActivityAndHomeFragment

class MainActivity : BaseActivity(), IActivityAndHomeFragment {
    val fragmentManager = supportFragmentManager
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        runtimePermission()
    }

    private fun runtimePermission() {
        Dexter.withContext(this).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                var fragmentHome = HomeFragment()
                fragmentManager.beginTransaction().replace(R.id.fl_content, fragmentHome).commit()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<PermissionRequest>?,
                p1: PermissionToken?,
            ) {
                p1!!.continuePermissionRequest()
            }
        }).check()
    }

    override fun openFolder(list: ArrayList<FileData>?, type: String) {
        when (type) {
            "internal" -> changeFragmentInternal()
            "app" -> changeFragmentCategory(list)
            "video" -> changeFragmentCategory(list)
            "image" -> changeFragmentCategory(list)
            "music" -> changeFragmentCategory(list)
            "recent" -> changeFragmentCategory(list)
            "favorite" -> changeFragmentCategory(list)
            "download" -> changeFragmentCategory(list)
            "compressed" -> changeFragmentCategory(list)
            "document" -> changeFragmentCategory(list)
        }
    }

    fun changeFragmentCategory(list: ArrayList<FileData>?) {
        var fragmentCategory = ListFileCategoryFragment()
        var fragmentTransaction = fragmentManager.beginTransaction()
        var bundle = Bundle()
        bundle.putSerializable("list", list)
        fragmentCategory.arguments = bundle
        fragmentTransaction.replace(R.id.fl_content, fragmentCategory)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun changeFragmentInternal() {
        var fragmentInternal = ListFileInternalFragment()
        var fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_content, fragmentInternal)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}