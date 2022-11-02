package com.icesi.fiufiu.seller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.icesi.fiufiu.databinding.ActivitySellerEditProfileBinding
import com.icesi.fiufiu.model.Service
import com.icesi.fiufiu.model.Seller

class SellerEditProfileActivity : AppCompatActivity() {

    /// View
    private lateinit var binding: ActivitySellerEditProfileBinding

    /// Objects
    private lateinit var seller: Seller
    private lateinit var service: Service

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerEditProfileBinding.inflate(layoutInflater)
        seller = Gson().fromJson(intent.extras!!.getString("currentSeller", ""), Seller::class.java)

        Firebase.firestore.collection("markets").document(seller.marketID).get()
            .addOnSuccessListener {
                service = it.toObject(Service::class.java)!!
                loadInfo()
            }

        binding.editMarketDoneBtn.setOnClickListener {
           editInfo()
        }

        setContentView(binding.root)
    }

    private fun editInfo(){
        service.marketName = binding.nameMarketEdit.text.toString()
        service.phoneNumber = binding.phoneMarketEdit.text.toString()
        service.marketDescription = binding.descriptionMarketEdit.text.toString()

        Firebase.firestore.collection("markets").document(seller.marketID).set(service)

        startActivity(
            Intent(this, SellerHomeActivity::class.java).apply {
                putExtra("currentUser", Gson().toJson(seller))
            })
    }

    private fun loadInfo() {
        binding.nameMarketEdit.setText(service.marketName)
        binding.descriptionMarketEdit.setText(service.marketDescription)

        binding.phoneMarketEdit.setText(service.phoneNumber)
    }
}