package com.icesi.fiufiu.seller

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.icesi.fiufiu.MainActivity
import com.icesi.fiufiu.model.Seller
import com.icesi.fiufiu.util.Util
import com.icesi.fiufiu.databinding.FragmentSellerProfileBinding
import com.icesi.fiufiu.model.Service

class SellerProfileFragment : Fragment() {

    /// View
    private lateinit var _binding: FragmentSellerProfileBinding
    private val binding get() = _binding!!

    /// Objects
    private lateinit var service: Service
    private lateinit var seller: Seller

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSellerProfileBinding.inflate(inflater, container, false)
        getMarketInfo()

        binding.logoutSellerBtn.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }

        binding.settingsSellerBtn.setOnClickListener {
            startActivity(
                Intent(requireActivity(), SellerEditProfileActivity::class.java).apply {
                    putExtra("currentSeller", Gson().toJson(seller))
                }
            )
        }

        return binding.root
    }

    private fun getMarketInfo(){
        Util.loadImage(service.imageID, binding.profilePhotoSeller, "market-image-profile")
        binding.nameMarket.text = service.marketName
        binding.emailSeller.text = seller.email
        binding.phoneSeller.text = service.phoneNumber
    }

    fun setUser(user: Seller) {
        seller = user
        Firebase.firestore.collection("markets").document(user.marketID).get()
            .addOnSuccessListener {
                service = it.toObject(Service::class.java)!!
            }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SellerProfileFragment()
    }
}