package com.iceka.whatsappclone.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iceka.whatsappclone.InputPhoneNumberActivity
import com.iceka.whatsappclone.R
import com.iceka.whatsappclone.models.CountryCallingCode

class AdapterCountryCallingCode(
    private val mContext: Context,
    private val countryCallingCodeList: List<CountryCallingCode>
) : RecyclerView.Adapter<AdapterCountryCallingCode.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_country_calling_code, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val countryCallingCode = countryCallingCodeList[position]
        val flagOffset = 0x1F1E6
        val asciiOffset = 0x41
        val codeName = countryCallingCode.code
        val firstChar = Character.codePointAt(codeName, 0) - asciiOffset + flagOffset
        val secondChar = Character.codePointAt(codeName, 1) - asciiOffset + flagOffset
        val flag = String(Character.toChars(firstChar)) + String(Character.toChars(secondChar))
        holder.countryFlag.text = flag
        holder.countryName.text = countryCallingCode.name
        holder.dialCode.text = countryCallingCode.dial_code
        if (countryCallingCode.local != null) {
            holder.countryLocalName.visibility = View.VISIBLE
            holder.countryLocalName.text = countryCallingCode.local
        } else {
            holder.countryLocalName.visibility = View.GONE
        }
        holder.layout.setOnClickListener {
            holder.selectedCountry.visibility = View.VISIBLE
            val intent = Intent(mContext, InputPhoneNumberActivity::class.java)
            intent.putExtra("countryName", countryCallingCode.name)
            intent.putExtra("countryDialCode", countryCallingCode.dial_code)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            (mContext as Activity).setResult(Activity.RESULT_OK, intent)
            mContext.finish()
        }
    }

    override fun getItemCount(): Int {
        return countryCallingCodeList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val countryFlag: TextView
        val countryName: TextView
        val countryLocalName: TextView
        val dialCode: TextView
        val selectedCountry: ImageView
        val layout: LinearLayout

        init {
            countryFlag = itemView.findViewById(R.id.country_flag)
            countryName = itemView.findViewById(R.id.tv_country_name)
            countryLocalName = itemView.findViewById(R.id.tv_country_local_name)
            dialCode = itemView.findViewById(R.id.tv_dial_code)
            selectedCountry = itemView.findViewById(R.id.img_country_code_selected)
            layout = itemView.findViewById(R.id.country_calling_code_layout)
        }
    }
}