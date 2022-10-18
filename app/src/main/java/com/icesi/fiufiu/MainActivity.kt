package com.icesi.fiufiu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.icesi.fiufiu.consumer.ConsumerHomeActivity
import com.icesi.fiufiu.consumer.ConsumerLoginActivity
import com.icesi.fiufiu.databinding.ActivityMainBinding
import com.icesi.fiufiu.model.Seller
import com.icesi.fiufiu.seller.SellerHomeActivity
import com.icesi.fiufiu.seller.SellerLoginActivity

class MainActivity : AppCompatActivity() {

    /// View
    private lateinit var binding: ActivityMainBinding
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),::onResult)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUser = Firebase.auth.currentUser


        currentUser?.let {
            Firebase.firestore.collection("users").document(currentUser!!.uid).get()
                .addOnSuccessListener {
                    val user = it.toObject(Seller::class.java)

                    binding.selectConsumerBtn.setOnClickListener {
                        if(currentUser!=null){
                            var intent= Intent(this, ConsumerHomeActivity::class.java)
                            intent.apply {putExtra("currentUser",Gson().toJson(user) )}
                            launcher.launch(intent)
                        }else{
                            changeActivity("consumer")
                        }
                    }

                    binding.selectSellerBtn.setOnClickListener {
                        if(currentUser!=null){
                            var intent = Intent(this, SellerHomeActivity::class.java)
                            intent.apply {putExtra("currentUser",Gson().toJson(user) )}

                            launcher.launch(intent)
                        }else{
                            changeActivity("seller")
                        }
                    }


                }.addOnFailureListener {
                    Toast.makeText(this.baseContext, it.message, Toast.LENGTH_LONG).show()
                }
        }

        binding.selectSellerBtn.setOnClickListener {
                changeActivity("seller")
        }

        binding.selectSellerBtn.setOnClickListener {
                changeActivity("seller")
        }

    }

    private fun changeActivity(userType: String){
        var intent= Intent()
        when(userType){
            "consumer"-> intent = Intent(this, ConsumerLoginActivity::class.java)
            "seller"->intent = Intent(this, SellerLoginActivity::class.java)
        }
        intent.apply {putExtra("userType", userType)}
        launcher.launch(intent)
    }

    fun onResult(activityResult: ActivityResult?) {}
}

