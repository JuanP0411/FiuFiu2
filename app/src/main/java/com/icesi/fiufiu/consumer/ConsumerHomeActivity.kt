package com.icesi.fiufiu.consumer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.icesi.fiufiu.R
import com.icesi.fiufiu.databinding.ActivityConsumerHomeBinding
import com.icesi.fiufiu.model.*

class ConsumerHomeActivity : AppCompatActivity(), ConsumerMainOverviewFragment.SellerObserver,
    ServiceProfileFragment.BackButtonObserver{

    /// View
    private lateinit var binding: ActivityConsumerHomeBinding
    private lateinit var menuConsumer:BottomNavigationView

    /// Objects
    private lateinit var currentUser: User
    private var shoppingCar = ShoppingCar()

    ///Flags
    private var isInProductView: Boolean = false

    /// Fragments
    private  var consumerMainOverviewFragment = ConsumerMainOverviewFragment.newInstance()
    private  var consumerProfileFragment = ConsumerProfileFragment.newInstance()
    private  var consumerShoppingFragment= ConsumerShoppingFragment.newInstance()
    private  var consumerServiceProfileFragment= ServiceProfileFragment.newInstance()
    private  var productFragment = ProductFragment.newInstance()

    /// Dialog Fragments
    var dialogFragment = ConfirmPurchaseDialogFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsumerHomeBinding.inflate(layoutInflater)
        menuConsumer = binding.menuConsumer
        setContentView(binding.root)

        currentUser = Gson().fromJson(intent.extras?.getString("currentUser",""), User::class.java)


        loadUserInFragments()
        loadListeners()

        showFragment(consumerMainOverviewFragment, true)
        menuConsumer.setOnItemSelectedListener { menuItem->
            when(menuItem.itemId){
                R.id.homeItem -> showFragment(consumerMainOverviewFragment, true)
                R.id.profileItem -> showFragment(consumerProfileFragment, true)
                R.id.ordersItem -> showFragment(consumerShoppingFragment, true)
            }
            true
        }
    }

    private fun loadListeners(){
        /// SellerObserver
        consumerMainOverviewFragment.onSellerObserver = this

        /// ProductObserver
        consumerServiceProfileFragment.onProductObserver = this
        consumerServiceProfileFragment.onPurchaseObserver = this

        /// OrderObserver
        productFragment.onOrderObserver = this

        /// ConfirmPurchaseObserver
        dialogFragment.onConfirmPurchaseObserver = consumerServiceProfileFragment
    }

    private fun loadUserInFragments(){
        productFragment.setUser(currentUser)
        consumerProfileFragment.setUser(currentUser)
        consumerMainOverviewFragment.setUser(currentUser)
        consumerServiceProfileFragment.setUser(currentUser)
        consumerShoppingFragment.setUser(currentUser)
    }

    private fun showFragment(fragment: Fragment, blockMenu: Boolean){
        binding.menuConsumer.isVisible = blockMenu
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        consumerShoppingFragment.adapter.clear()
        transaction.commit()
    }

    override fun sendMarket(service: Service) {
        consumerServiceProfileFragment.setMarket(service)
        showFragment(consumerServiceProfileFragment, false)
    }

    override fun sendProduct(product: Product) {
        productFragment.setProduct(product)
        isInProductView = true
        showFragment(productFragment, false)
    }

    override fun sendMarketInfo(service: Service){
        productFragment.setMarket(service)
    }

    override fun sendShoppingInfo(name: String, service: Service) {
        shoppingCar.consumerName = name
        shoppingCar.currentService = service
    }

    override fun sendMessage(intent: Intent) {
        backToMarkets()
        startActivity(intent)
    }

    override fun backToTheMainMarket(){
        isInProductView = false
        showFragment(consumerServiceProfileFragment, false)
    }

    override fun backToMarketBlank(){
        isInProductView = false
        showFragment(consumerMainOverviewFragment, false)
    }

    override fun backToMarkets() {
        shoppingCar = ShoppingCar()
        consumerServiceProfileFragment.setShoppingCar(shoppingCar)
        showFragment(consumerMainOverviewFragment, true)
    }

    override fun loadOrder(order: Order) {
        shoppingCar.loadOrder(order)
        consumerServiceProfileFragment.setShoppingCar(shoppingCar)
        showFragment(consumerServiceProfileFragment, false)
    }

    override fun askOrder(shoppingCar: ShoppingCar) {
        if(shoppingCar.getAmountOfOrders() > 0) {
            val orderText = shoppingCar.generateConfirmText()
            dialogFragment.orderText = orderText
            dialogFragment.show(supportFragmentManager, "PurchaseConfirmationDialog")
        }else{
            backToMarkets()
        }
    }

    override fun onBackPressed() {
        when(isInProductView){
            true ->  backToTheMainMarket()
            false -> askOrder(consumerServiceProfileFragment.getShoppingCar())
        }
    }
}