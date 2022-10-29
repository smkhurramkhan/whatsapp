package com.iceka.whatsappclone.newfrags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.iceka.whatsappclone.R
import com.iceka.whatsappclone.adapters.HomeSliderAdapter
import com.iceka.whatsappclone.adapters.MenuAdapter
import com.iceka.whatsappclone.databinding.HomeFragmentBinding
import com.iceka.whatsappclone.models.MenuModel
import com.iceka.whatsappclone.models.ModelSlider

class FragmentHome : Fragment() {

    private lateinit var binding: HomeFragmentBinding
    private var sliderList = mutableListOf<ModelSlider>()
    private var sliderAdapter: HomeSliderAdapter? = null

    private var dataList = mutableListOf<MenuModel>()
    private var menuAdapter: MenuAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSliderList()
        setDataList()

    }

    private fun setDataList() {
        dataList.add(
            MenuModel(1, "Missed Call Alert", R.drawable.ic_phone_red, false)
        )
        dataList.add(
            MenuModel(2, "Smart SMS", R.drawable.ic_sms_red, false)
        )
        dataList.add(
            MenuModel(3, "Collect Call", R.drawable.ic_collect_call_red, false)
        )
        dataList.add(
            MenuModel(4, "VIP Call", R.drawable.ic_vip_call_red, true)
        )
        dataList.add(
            MenuModel(5, "Intro Me", R.drawable.ic_intro_me_red, false)
        )
        dataList.add(
            MenuModel(6, "Sponsor Me", R.drawable.ic_sponsor_me_red, true)
        )
        dataList.add(
            MenuModel(7, "Jazz Share", R.drawable.ic_jazz_share_red, true)
        )
        dataList.add(
            MenuModel(8, "Jazz Tunes", R.drawable.ic_jazz_tunes_red, false)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapterForSlider()
        setAdapterForMenu()
    }


    private fun setAdapterForSlider() {
        sliderAdapter = HomeSliderAdapter(
            requireContext(),
            sliderList
        )
        val horizontalManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.sliderRecycler.setHasFixedSize(true)
        binding.sliderRecycler.isNestedScrollingEnabled = false
        binding.sliderRecycler.layoutManager = horizontalManager
        binding.sliderRecycler.itemAnimator = DefaultItemAnimator()
        binding.sliderRecycler.adapter = sliderAdapter

    }

    private fun setAdapterForMenu() {
        menuAdapter = MenuAdapter(
            requireContext(),
            dataList,
            onClick = {
                when (it) {
                    1 -> {
                        Toast.makeText(requireContext(), "Coming Soon", Toast.LENGTH_SHORT).show()
                    }
                    2 -> {
                        Toast.makeText(requireContext(), "Coming Soon", Toast.LENGTH_SHORT).show()
                    }
                    3 -> {
                        Toast.makeText(requireContext(), "Coming Soon", Toast.LENGTH_SHORT).show()
                    }
                    4 -> {
                        Toast.makeText(requireContext(), "Coming Soon", Toast.LENGTH_SHORT).show()
                    }
                    5 -> {
                        Toast.makeText(requireContext(), "Coming Soon", Toast.LENGTH_SHORT).show()
                    }
                    6 -> {
                        Toast.makeText(requireContext(), "Coming Soon", Toast.LENGTH_SHORT).show()
                    }
                    7 -> {
                        Toast.makeText(requireContext(), "Coming Soon", Toast.LENGTH_SHORT).show()
                    }
                    8 -> {
                        Toast.makeText(requireContext(), "Coming Soon", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )

        val horizontalManager = GridLayoutManager(
            requireContext(),
            4
        )
        binding.menuRecycler.setHasFixedSize(true)
        binding.menuRecycler.isNestedScrollingEnabled = false
        binding.menuRecycler.layoutManager = horizontalManager
        binding.menuRecycler.itemAnimator = DefaultItemAnimator()
        binding.menuRecycler.adapter = menuAdapter
    }

    private fun setSliderList() {
        sliderList.add(
            ModelSlider(1, "slider 1", R.drawable.slider1)
        )
        sliderList.add(
            ModelSlider(2, "slider 2", R.drawable.slider2)
        )

        sliderList.add(
            ModelSlider(4, "slider 4", R.drawable.slider4)
        )

        sliderList.add(
            ModelSlider(3, "slider 3", R.drawable.slider3)
        )
    }
}