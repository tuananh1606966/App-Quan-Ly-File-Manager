package com.nghiemtuananh.baitapappfilemanagert3h.basefragment

import androidx.fragment.app.Fragment
import com.nghiemtuananh.baitapappfilemanagert3h.baseactivity.BaseActivity

open class BaseFragment: Fragment() {
    open fun onBackPressForFragment(){
        if ( activity is BaseActivity){
            (activity as BaseActivity).onBackRoot()
        }
    }
}