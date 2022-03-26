package com.nghiemtuananh.baitapappfilemanagert3h

import android.Manifest
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import com.google.android.material.navigation.NavigationView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.nghiemtuananh.baitapappfilemanagert3h.baseactivity.BaseActivity
import com.nghiemtuananh.baitapappfilemanagert3h.databinding.ActivityMainBinding
import com.nghiemtuananh.baitapappfilemanagert3h.myinterface.IActivityAndCategory
import com.nghiemtuananh.baitapappfilemanagert3h.myinterface.IActivityAndHomeFragment
import com.nghiemtuananh.baitapappfilemanagert3h.myinterface.IActivityLongClick
import com.nghiemtuananh.baitapappfilemanagert3h.myinterface.IDataAndClick

class MainActivity : BaseActivity(), IActivityAndHomeFragment, IDataAndClick, IActivityAndCategory,
    IActivityLongClick, View.OnClickListener,
    NavigationView.OnNavigationItemSelectedListener {
    private val FRAGMENT_HOME = 1
    var currentFragment = 0
    private val fragmentManager = supportFragmentManager
    lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        runtimePermission()
        setSupportActionBar(binding.toolbar)
        binding.navView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.Open_drawer,
            R.string.Close_drawer)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.ibtnBack.setOnClickListener(this)
        binding.ibtnCancelSelect.setOnClickListener(this)
    }

    private fun runtimePermission() {
        Dexter.withContext(this).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                openHomeFragment()
                binding.navView.menu.findItem(R.id.nav_home).isChecked = true
                currentFragment = FRAGMENT_HOME
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<PermissionRequest>?,
                p1: PermissionToken?,
            ) {
                p1!!.continuePermissionRequest()
            }
        }).check()
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.ibtnBack -> {
                returnNormalToolBar()
            }
            binding.ibtnCancelSelect -> {
                returnNormalToolBar()
            }
        }
    }

    private fun returnNormalToolBar() {
        val fragment = getCurrentFragment()
        if (fragment is ListFileCategoryFragment) {
            fragment.returnNormal()
            returnNormal("Category")
        } else if (fragment is ListFileInternalFragment)
            fragment.returnNormal()
        returnNormal("Internal storage")
    }

    override fun changeToolBarAfterLongClick() {
        binding.ibtnBack.isGone = false
        binding.ibtnSearch.isGone = true
        binding.ibtnDelete.isGone = false
        binding.ibtnCancelSelect.isGone = false
        binding.toolbar.title = "1 selected"
        binding.toolbar.setTitleTextColor(Color.BLACK)
    }

    override fun selectMoreItem(countItem: Int) {
        binding.toolbar.title = "$countItem selected"
    }

    override fun reloadCheckNavigation() {
        binding.navView.menu.findItem(R.id.nav_sdcard).isChecked = false
    }

    override fun reloadToolBar() {
        binding.toolbar.title = "File Manager"
        binding.navView.menu.findItem(R.id.nav_home).isChecked = true
        binding.navView.menu.findItem(R.id.nav_sdcard).isChecked = false
    }

    private fun openHomeFragment() {
        val fragmentHome = HomeFragment()
        fragmentManager.beginTransaction().replace(R.id.fl_content, fragmentHome).commit()
        for (i in 0 until fragmentManager.backStackEntryCount) {
            fragmentManager.popBackStack()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                if (currentFragment != FRAGMENT_HOME) {
                    openHomeFragment()
                    currentFragment = FRAGMENT_HOME
                }
            }
            R.id.nav_sdcard -> {
                val listFileInternalFragment = getCurrentFragment()
                if (listFileInternalFragment != null && listFileInternalFragment is ListFileInternalFragment) {
                    if (listFileInternalFragment.rootPath != Environment.getExternalStorageDirectory().path) {
                        changeFragmentInternal()
                        binding.navView.menu.findItem(R.id.nav_sdcard).isChecked = true
                    }
                } else {
                    changeFragmentInternal()
                    binding.navView.menu.findItem(R.id.nav_sdcard).isChecked = true
                }
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun returnNormal(title: String) {
        binding.ibtnBack.isGone = true
        binding.ibtnCancelSelect.isGone = true
        binding.ibtnDelete.isGone = true
        binding.ibtnSearch.isGone = false
        binding.toolbar.title = title
        binding.toolbar.setTitleTextColor(Color.parseColor("#03A9F4"))
    }

    override fun onBackPressed() {
        currentFragment = 0
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
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

    override fun getCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItem(position: Int): FileData {
        TODO("Not yet implemented")
    }

    override fun onClick(folder: FileData) {
        currentFragment = 0
        val fr = getCurrentFragment()
        if (fr != null && fr is ListFileInternalFragment && folder.isDirectory) {
            val path = folder.path
            val fragmentInternal = ListFileInternalFragment()
            val bundle = Bundle()
            bundle.putString("path", path)
            fragmentInternal.arguments = bundle
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fl_content, fragmentInternal)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    override fun onLongClick(folder: FileData) {
        TODO("Not yet implemented")
    }

    private fun changeFragmentCategory(list: ArrayList<FileData>?) {
        binding.navView.menu.findItem(R.id.nav_home).isChecked = false
        binding.navView.menu.findItem(R.id.nav_sdcard).isChecked = false
        currentFragment = 0
        val fragmentCategory = ListFileCategoryFragment()
        val fragmentTransaction = fragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putSerializable("list", list)
        fragmentCategory.arguments = bundle
        fragmentTransaction.replace(R.id.fl_content, fragmentCategory)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        binding.toolbar.title = "Category"
    }

    private fun changeFragmentInternal() {
        binding.navView.menu.findItem(R.id.nav_sdcard).isChecked = true
        binding.navView.menu.findItem(R.id.nav_home).isChecked = false
        currentFragment = 0
        val rootPath = Environment.getExternalStorageDirectory().path
        val fragmentInternal = ListFileInternalFragment()
        val bundle = Bundle()
        bundle.putString("path", rootPath)
        fragmentInternal.arguments = bundle
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_content, fragmentInternal)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        binding.toolbar.title = "Internal storage"
    }
}