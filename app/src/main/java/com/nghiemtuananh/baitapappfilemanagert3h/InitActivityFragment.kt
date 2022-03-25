package com.nghiemtuananh.baitapappfilemanagert3h

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nghiemtuananh.baitapappfilemanagert3h.basefragment.BaseFragment
import com.nghiemtuananh.baitapappfilemanagert3h.databinding.FragmentHomeBinding

class InitActivityFragment: BaseFragment() {
    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
}