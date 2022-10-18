package com.icesi.fiufiu.consumer

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.icesi.fiufiu.databinding.FragmentConsumerMainOverviewBinding
import com.icesi.fiufiu.model.*
import com.icesi.fiufiu.model.adapters.MarketAdapter
import com.icesi.fiufiu.util.Util

class ConsumerMainOverviewFragment : Fragment() {

    /// View
    private var _binding: FragmentConsumerMainOverviewBinding? = null
    private val binding get() = _binding!!
    private val adapter = MarketAdapter()

    /// Objects
    private lateinit var currentUser: User

    /// Listeners
    lateinit var onSellerObserver: SellerObserver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConsumerMainOverviewBinding.inflate(inflater,container,false)
        adapter.onSellerObserver = onSellerObserver
        getMarkets()
        getUserData()
        Util.initRecycler(binding.recyclerView, requireActivity(), LinearLayoutManager.VERTICAL,).adapter = adapter
        return binding.root
    }


    private fun getUserData(){
        binding.consumerName.text = currentUser.name
        Util.loadImage(currentUser.img,binding.consumerProfile,"profile" )
    }

    private fun getMarkets() {
        Firebase.firestore.collection("markets").get()
            .addOnCompleteListener { market ->
                adapter.clear()
                for (doc in market.result!!) {
                    adapter.addMarket(doc.toObject(Service::class.java))
                }
            }
    }

    fun setUser(user: User){
        currentUser = user
    }

    companion object {
        @JvmStatic
        fun newInstance() = ConsumerMainOverviewFragment()
    }

    interface SellerObserver{
        fun sendMarket(service: Service)
        fun sendProduct(product: Product)
        fun loadOrder(order: Order)
        fun sendShoppingInfo(name:String, service:Service)
        fun sendMessage(intent: Intent)
        fun backToMarkets()
        fun backToMarketBlank()
        fun backToTheMainMarket()
        fun sendMarketInfo(service: Service)
    }
}