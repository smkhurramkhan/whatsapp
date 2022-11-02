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
            MenuModel(1, "Missed Call Alert", R.drawable.missed_sms_alert, false)
        )
        dataList.add(
            MenuModel(2, "Smart SMS", R.drawable.smart_sms, false)
        )
        dataList.add(
            MenuModel(3, "Collect Call", R.drawable.collact_call, false)
        )
        dataList.add(
            MenuModel(4, "VIP CALL",R.drawable.vip_call, true)
        )
        dataList.add(
            MenuModel(4, "Notify Me", R.drawable.notify_me, true)
        )

        dataList.add(
            MenuModel(6, "Sponsor Me", R.drawable.sponsor_me, true)
        )
        dataList.add(
            MenuModel(7, "My Status", R.drawable.my_status, true)
        )
        dataList.add(
            MenuModel(8, "Jazz Tunes", R.drawable.jazz_tunes, false)
        )
        dataList.add(
            MenuModel(8, "Youth Central", R.drawable.youth_central, false)
        )
        dataList.add(
            MenuModel(8, "Bima Insurance", R.drawable.bima_insurance, false)
        )
        dataList.add(
            MenuModel(8, "Mobile Magazine", R.drawable.mobile_magazine, false)
        )
        dataList.add(
            MenuModel(8, "Jazz Drive", R.drawable.jazz_drive, false)
        )
        dataList.add(
            MenuModel(8, "Self Service Dial", R.drawable.self_service_dial_codes, false)
        )
        dataList.add(
            MenuModel(8, "Job Alerts", R.drawable.job_alerts, false)
        )
        dataList.add(
            MenuModel(8, "Zero Balance Call", R.drawable.zero_balance_call, false)
        )
        dataList.add(
            MenuModel(8, "Jazz Parhu", R.drawable.jazz_parho, false)
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
            ModelSlider(1, "slider 1", R.drawable.jazz_mosafir)
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