package com.nghiemtuananh.baitapappfilemanagert3h.baseactivity

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.nghiemtuananh.baitapappfilemanagert3h.basefragment.BaseFragment

open class BaseActivity : AppCompatActivity() {
    fun getCurrentFragment(): Fragment? {
//        supportFragmentManager.fragments
        //lay các fragment do supportFragmentManager của activity quản
        for (fragment in supportFragmentManager.fragments) {
            if (fragment != null && fragment.isVisible) {
                return fragment
            }
        }
        return null
    }

    fun onBackRoot() {
        super.onBackPressed()
    }

    override fun onBackPressed() {
        val fr = getCurrentFragment()
        if (fr != null && fr is BaseFragment){
            (fr as BaseFragment).onBackPressForFragment()
        }else {
            onBackRoot()
        }
    }
}