package com.iceka.whatsappclone.status

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.iceka.whatsappclone.R
import com.iceka.whatsappclone.databinding.ActivityMyStatusBinding
import com.iceka.whatsappclone.databinding.SubscriptionDialogBinding
import com.iceka.whatsappclone.interfaces.NetworkInterfaceCalls
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

    val videoId = "azsQKBYn5rU";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPrefs(this)
        subNetworkCall = SubNetworkCall(this)
        UnsubNetworkCall = UnsubscibeNetworkCall(this)

        setupToolbar()
        lifecycle.addObserver(binding.youtubePlayerView);

        Glide.with(this)
            .load("https://img.youtube.com/vi/$videoId/mqdefault.jpg")
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
        Timber.d("Failure is $errorMessage")
        Toast.makeText(this, "Already Subscribed", Toast.LENGTH_SHORT).show()

    }

    override fun onException(exception: String, className: String) {
        alertDialog?.dismiss()
        Timber.d("Exception is $exception")

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
}