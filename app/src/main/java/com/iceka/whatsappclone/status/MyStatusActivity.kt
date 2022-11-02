package com.iceka.whatsappclone.status

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.iceka.whatsappclone.R
import com.iceka.whatsappclone.adapters.FeaturesAdapter
import com.iceka.whatsappclone.databinding.ActivityMyStatusBinding
import com.iceka.whatsappclone.databinding.SubscriptionDialogBinding
import com.iceka.whatsappclone.interfaces.NetworkInterfaceCalls
import com.iceka.whatsappclone.models.ModelFeatures
import com.iceka.whatsappclone.network.SubNetworkCall
import com.iceka.whatsappclone.network.UnsubscibeNetworkCall
import com.iceka.whatsappclone.networkmodels.SubModel
import com.iceka.whatsappclone.utils.CustomDialog
import com.iceka.whatsappclone.utils.SharedPrefs
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import timber.log.Timber


class MyStatusActivity : AppCompatActivity(), NetworkInterfaceCalls {
    private lateinit var binding: ActivityMyStatusBinding

    private var subNetworkCall: SubNetworkCall? = null
    private var UnsubNetworkCall: UnsubscibeNetworkCall? = null
    private var subModel: SubModel? = null
    private var sharedPref: SharedPrefs? = null
    private var alertDialog: AlertDialog? = null

    private var featuresList = mutableListOf<ModelFeatures>()
    private var featuresAdapter: FeaturesAdapter? = null

    val videoId = "azsQKBYn5rU";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFeaturesData()

        sharedPref = SharedPrefs(this)
        subNetworkCall = SubNetworkCall(this)
        UnsubNetworkCall = UnsubscibeNetworkCall(this)

        setupToolbar()
        lifecycle.addObserver(binding.youtubePlayerView);


        if (sharedPref?.getIsSub() == true) {
            binding.unsubscribe.visibility = View.VISIBLE
            binding.subscribe.visibility = View.GONE

            binding.featuresRecycler.visibility = View.VISIBLE
            binding.groupNo1.visibility = View.GONE
        } else {
            binding.subscribe.visibility = View.VISIBLE
            binding.unsubscribe.visibility = View.GONE

            binding.featuresRecycler.visibility = View.GONE
            binding.groupNo1.visibility = View.VISIBLE
        }

        setFeaturesAdapter()


        Glide.with(this).load("https://img.youtube.com/vi/$videoId/mqdefault.jpg")
            .into(binding.youtubeThumbnail);


        binding.subscribe.setOnClickListener {
            alertDialog = CustomDialog.setLoadingDialog(
                this,
                false
            )
            val requestObject = JsonObject()
            requestObject.addProperty("b_party", sharedPref?.getUserNumber())
            requestObject.addProperty("channel", "USSD")
            requestObject.addProperty("service_mode", "1")
            subNetworkCall?.subscribeUser(requestObject)
            //UnsubNetworkCall?.unSubscribeUser(sharedPref?.getUserNumber()!!)

        }

        binding.unsubscribe.setOnClickListener {
            alertDialog = CustomDialog.setLoadingDialog(
                this,
                false
            )
            UnsubNetworkCall?.unSubscribeUser(sharedPref?.getUserNumber()!!)
        }


        loadYoutubeVideo()

    }

    private fun setupToolbar() {
        binding.toolbar.title = "My Status"
        setSupportActionBar(binding.toolbar)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


    private fun loadYoutubeVideo() {
        binding.youtubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                binding.relativeLayout.visibility = View.GONE
                youTubePlayer.loadVideo(videoId = videoId, 0f)
            }
        })
    }

    override fun onSuccess(`object`: JsonObject, className: String) {
        when (className) {
            "SubscibeCall" -> {
                alertDialog?.dismiss()
                subModel = Gson().fromJson(`object`, SubModel::class.java)
                Timber.d("goal selection is ${Gson().toJson(subModel)}")
                subDialog()
                if (subModel?.status == 200) {
                    binding.unsubscribe.visibility = View.VISIBLE
                } else {
                    binding.subscribe.visibility = View.GONE
                }
            }
            "UnSubscibeCall" -> {
                alertDialog?.dismiss()
                Toast.makeText(this, "Unsubscribed Successfully",
                    Toast.LENGTH_SHORT).show()
                if (subModel?.status == 200) {
                    binding.subscribe.visibility = View.VISIBLE
                } else {
                    binding.unsubscribe.visibility = View.GONE
                }
            }
        }

    }


    override fun onFailure(errorMessage: String, className: String) {
        alertDialog?.dismiss()
        when (className) {
            "SubscibeCall" -> {
                sharedPref?.isSubscribed(true)
                Timber.d("Failure is $errorMessage $className")
            }
            "UnSubscibeCall" -> {
                sharedPref?.isSubscribed(false)
                Timber.d("Failure is $errorMessage $className")
            }
        }

        if (sharedPref?.getIsSub() == true) {
            binding.unsubscribe.visibility = View.VISIBLE
            binding.subscribe.visibility = View.GONE

            binding.featuresRecycler.visibility = View.VISIBLE
            binding.groupNo1.visibility = View.GONE
        } else {
            binding.subscribe.visibility = View.VISIBLE
            binding.unsubscribe.visibility = View.GONE

            binding.featuresRecycler.visibility = View.GONE
            binding.groupNo1.visibility = View.VISIBLE
        }


    }

    override fun onException(exception: String, className: String) {
        alertDialog?.dismiss()

        when (className) {
            "SubscibeCall" -> {
                sharedPref?.isSubscribed(true)
                Timber.d("Exception is $exception $className")
            }
            "UnSubscibeCall" -> {
                sharedPref?.isSubscribed(false)
                Timber.d("Exception is $exception $className")
            }
        }




        if (sharedPref?.getIsSub() == true) {
            binding.unsubscribe.visibility = View.VISIBLE
            binding.subscribe.visibility = View.GONE

            binding.featuresRecycler.visibility = View.VISIBLE
            binding.groupNo1.visibility = View.GONE
        } else {
            binding.subscribe.visibility = View.VISIBLE
            binding.unsubscribe.visibility = View.GONE

            binding.featuresRecycler.visibility = View.GONE
            binding.groupNo1.visibility = View.VISIBLE
        }


    }

    private fun subDialog() {
        val dialogBuilder: AlertDialog.Builder =
            AlertDialog.Builder(this, R.style.CustomAlertDialog)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.subscription_dialog, null)

        val dialogBinding = SubscriptionDialogBinding.bind(dialogView);

        dialogBuilder.setView(dialogBinding.root)

        val alertDialog = dialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()

        dialogBinding.btnClose.setOnClickListener {
            alertDialog?.dismiss()
        }


    }

    private fun setFeaturesData() {
        featuresList.add(
            ModelFeatures(
                1,
                "Set status",
                false
            )
        )

        featuresList.add(
            ModelFeatures(
                2,
                "Change status",
                false
            )
        )

        featuresList.add(
            ModelFeatures(
                3,
                "Set status for specific number",
                false
            )
        )

        featuresList.add(
            ModelFeatures(
                4,
                "Set status for a group",
                false
            )
        )

        featuresList.add(
            ModelFeatures(
                5,
                "Delete status for specific number",
                false
            )
        )

        featuresList.add(
            ModelFeatures(
                6,
                "Add number to blacklist",
                false
            )
        )

        featuresList.add(
            ModelFeatures(
                7,
                "Delete number from blacklist",
                false
            )
        )
    }


    private fun setFeaturesAdapter() {
        featuresAdapter = FeaturesAdapter(
            this,
            featuresList
        )
        binding.featuresRecycler.layoutManager = LinearLayoutManager(this)
        binding.featuresRecycler.adapter = featuresAdapter
    }
}