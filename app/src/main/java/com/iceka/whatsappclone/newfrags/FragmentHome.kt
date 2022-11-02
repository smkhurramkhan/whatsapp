package com.iceka.whatsappclone.newfrags

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI.getApplicationContext
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.iceka.whatsappclone.R
import com.iceka.whatsappclone.adapters.HomeSliderAdapter
import com.iceka.whatsappclone.adapters.MenuAdapter
import com.iceka.whatsappclone.databinding.HomeFragmentBinding
import com.iceka.whatsappclone.interfaces.NetworkInterfaceCalls
import com.iceka.whatsappclone.models.MenuModel
import com.iceka.whatsappclone.models.ModelSlider
import com.iceka.whatsappclone.network.SubNetworkCall
import com.iceka.whatsappclone.network.UnsubscibeNetworkCall
import com.iceka.whatsappclone.networkmodels.SubModel
import com.iceka.whatsappclone.status.MyStatusActivity
import timber.log.Timber


class FragmentHome : Fragment(), NetworkInterfaceCalls
{

    private lateinit var binding: HomeFragmentBinding
    private var sliderList = mutableListOf<ModelSlider>()
    private var sliderAdapter: HomeSliderAdapter? = null

    private var dataList = mutableListOf<MenuModel>()
    private var menuAdapter: MenuAdapter? = null
    private var subNetworkCall: SubNetworkCall? = null
    private var UnsubNetworkCall: UnsubscibeNetworkCall? = null
    private var subModel: SubModel? = null

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
            MenuModel(9, "Youth Central", R.drawable.youth_central, false)
        )
        dataList.add(
            MenuModel(10, "Bima Insurance", R.drawable.bima_insurance, false)
        )
        dataList.add(
            MenuModel(11, "Mobile Magazine", R.drawable.mobile_magazine, false)
        )
        dataList.add(
            MenuModel(12, "Jazz Drive", R.drawable.jazz_drive, false)
        )
        dataList.add(
            MenuModel(13, "Self Service Dial", R.drawable.self_service_dial_codes, false)
        )
        dataList.add(
            MenuModel(14, "Job Alerts", R.drawable.job_alerts, false)
        )
        dataList.add(
            MenuModel(15, "Zero Balance Call", R.drawable.zero_balance_call, false)
        )
        dataList.add(
            MenuModel(16, "Jazz Parhu", R.drawable.jazz_parho, false)
        )
        dataList.add(
            MenuModel(17, "Bakhabar Kissan", R.drawable.bakhabar_kissan, false)
        )
        dataList.add(
            MenuModel(18, "Conference Call", R.drawable.conference_call, false)
        )
        dataList.add(
            MenuModel(19, "Intro Me", R.drawable.intro_me, false)
        )
        dataList.add(
            MenuModel(20, "Jazz Alert", R.drawable.jazz_alert, false)
        )
        dataList.add(
            MenuModel(21, "Jazz Menu", R.drawable.jazz_menu, false)
        )
        dataList.add(
            MenuModel(22, "Jazz Rozgar", R.drawable.jazz_rozgar, false)
        )
        dataList.add(
            MenuModel(23, "Missed Call Alert", R.drawable.missed_call_alert, false)
        )
        dataList.add(
            MenuModel(24, "Power Tools", R.drawable.power_tools, false)
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
        subNetworkCall = SubNetworkCall(this)
        UnsubNetworkCall = UnsubscibeNetworkCall(this)



        setAdapterForSlider()
        setAdapterForMenu()
    }


    private fun setAdapterForSlider()
    {
        sliderAdapter = HomeSliderAdapter(
            requireContext(),
            sliderList,
            onClick = {
                when (it) {

                    0-> {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://link.fitflexapp.com/Home"))
                            startActivity(browserIntent)
                       // Toast.makeText(requireContext(), "Fitflex", Toast.LENGTH_SHORT).show()

                    }
                    1 -> {
                        val browserIntent =  Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=pk.mosafir.travsol&hl=en&gl=US"))
                        startActivity(browserIntent)
                     //   Toast.makeText(requireContext(), "Jazz Mosafir", Toast.LENGTH_SHORT).show()

                    }
                    2 -> {
                        val browserIntent =  Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.switchsolutions.farmtohome"))
                        startActivity(browserIntent)
                     //   Toast.makeText(requireContext(), "Farm To Home", Toast.LENGTH_SHORT).show()


                    }
                    3 -> {
                        val browserIntent =  Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.weatherwalay.pakweather.weathertoday"))
                        startActivity(browserIntent)
                    //    Toast.makeText(requireContext(), "Weather Walay", Toast.LENGTH_SHORT).show()
                    }

                }
            }
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
                        Toast.makeText(requireContext(), "Coming Soon dfdsf", Toast.LENGTH_SHORT).show()
                       /* val requestObject = JsonObject()
                        requestObject.addProperty("b_party", "3239978847")
                        requestObject.addProperty("channel", "USSD")
                        requestObject.addProperty("service_mode", "1")
                        subNetworkCall?.subscribeUser(requestObject)*/

                    }
                    2 -> {
                        Toast.makeText(requireContext(), "Coming Soon Unsub", Toast.LENGTH_SHORT).show()


                      //  UnsubNetworkCall?.unSubscribeUser("3239978847")

                    }
                    3 -> {
                        Toast.makeText(requireContext(), "Coming Soon", Toast.LENGTH_SHORT).show()
                        //   sendSMS("923211609211","hello")

                    }
                    4 -> {
                        Toast.makeText(requireContext(), "Coming Soon", Toast.LENGTH_SHORT).show()
                    }
                    5 -> {
                        Toast.makeText(requireContext(), "Coming Soon", Toast.LENGTH_SHORT).show()
                    }
                    6 -> {
                        val intent = Intent(requireContext(), MyStatusActivity::class.java)
                        startActivity(intent)

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
            3
        )
        binding.menuRecycler.setHasFixedSize(true)
        binding.menuRecycler.isNestedScrollingEnabled = false
        binding.menuRecycler.layoutManager = horizontalManager
        binding.menuRecycler.itemAnimator = DefaultItemAnimator()
        binding.menuRecycler.adapter = menuAdapter
    }
    private fun setSliderList()
    {
        sliderList.add(
            ModelSlider(1, "slider 1", R.drawable.fitflex)
        )
        sliderList.add(
            ModelSlider(2, "slider 2", R.drawable.jazz_mosafir)
        )

        sliderList.add(
            ModelSlider(4, "slider 4", R.drawable.farmtohome)
        )

        sliderList.add(
            ModelSlider(3, "slider 3", R.drawable.weatherwalay)
        )
    }

    override fun onSuccess(`object`: JsonObject, className: String)
    {
        when (className) {
            "SubscibeCall" -> {
                subModel = Gson().fromJson(`object`, SubModel::class.java)
                Timber.d("goal selection is ${Gson().toJson(subModel)}")

            }
            "UnSubscibeCall" -> {


            }
      }

   }


    override fun onFailure(errorMessage: String, className: String)
    {
        Timber.d("Failure is $errorMessage")

    }

    override fun onException(exception: String, className: String)
    {
        Timber.d("Exception is $exception")

    }
}