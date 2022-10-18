package com.icesi.fiufiu.consumer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.icesi.fiufiu.databinding.FragmentMarketProfileBinding
import com.icesi.fiufiu.model.*
import com.icesi.fiufiu.model.adapters.ProductAdapter
import com.icesi.fiufiu.util.Util

class ServiceProfileFragment : Fragment(), ConfirmPurchaseDialogFragment.ConfirmPurchaseObserver {

    /// View
    private lateinit var _binding: FragmentMarketProfileBinding
    private val binding get() = _binding!!
    var adapter = ProductAdapter()

    /// Objects
    private lateinit var currentService: Service
    private lateinit var currentUser: User
    private lateinit var shoppingCar: ShoppingCar

    /// Listeners
    lateinit var onProductObserver: ConsumerMainOverviewFragment.SellerObserver
    lateinit var onPurchaseObserver: BackButtonObserver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initListener()

        _binding = FragmentMarketProfileBinding.inflate(inflater,container,false)
        _binding.infoMarketProfile.text = currentService.marketDescription
        _binding.marketName.text = currentService.marketName

        Util.initRecycler(binding.productsMarketInConsumer, requireActivity(),LinearLayoutManager.HORIZONTAL).adapter = adapter
        Util.loadImage(currentService.imageID, binding.marketImageProfile,"market-image-profile" )

        loadProducts()

        _binding.sellerinfoBtn.setOnClickListener {
            onPurchaseObserver.askOrder(shoppingCar)
        }

        return _binding.root
    }

    private fun initListener(){
        adapter.onProductObserver = onProductObserver
        onProductObserver.sendMarketInfo(currentService)
        onProductObserver.sendShoppingInfo(currentUser.name, currentService)
        adapter.clear()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ServiceProfileFragment()
    }

    private fun loadProducts(){
        Firebase.firestore.collection("markets").document(currentService.id).collection("products").get()
            .addOnSuccessListener { task ->
            for(product in task.documents){
                adapter.addProduct(product.toObject(Product::class.java)!!)
            }
        }
    }

    override fun confirm() {
        onProductObserver.sendMessage(shoppingCar.sendMessage())
    }

    override fun discard() {
        onProductObserver.backToMarkets()
    }

    fun setUser(user: User){
        currentUser = user
    }

    fun setMarket(service: Service){
        currentService = service
    }

    fun setShoppingCar(shoppingCar: ShoppingCar){
        this.shoppingCar = shoppingCar
    }

    fun getShoppingCar(): ShoppingCar{
        return shoppingCar
    }

    interface BackButtonObserver{
        fun askOrder(shoppingCar: ShoppingCar)
    }
}