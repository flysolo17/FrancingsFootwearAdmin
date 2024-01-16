package com.ketchupzz.francingsfootwearadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.firebase.auth.FirebaseAuth
import com.ketchupzz.francingsfootwearadmin.utils.LoadingDialog
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import com.ketchupzz.francingsfootwearadmin.databinding.ActivityMainBinding
import com.ketchupzz.francingsfootwearadmin.model.Users
import com.ketchupzz.francingsfootwearadmin.utils.getNewMessages
import com.ketchupzz.francingsfootwearadmin.viewmodels.AuthViewModel
import com.ketchupzz.francingsfootwearadmin.viewmodels.MessagesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@ExperimentalBadgeUtils
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val authViewModel : AuthViewModel by viewModels()

    private lateinit var binding : ActivityMainBinding
    private val loadingDialog = LoadingDialog(this)
    private var users : Users? = null
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val messagesViewModel by viewModels<MessagesViewModel>()
    private lateinit var messageBadge: BadgeDrawable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        messageBadge = BadgeDrawable.create(this)
        val toolbar = binding.appBarMain.toolbar
        navController = findNavController(R.id.nav_host_fragment_content_main)
        setSupportActionBar(toolbar)
        setUpNav()
        observers()
        CoroutineScope(Dispatchers.IO).run {
            FirebaseAuth.getInstance().currentUser?.let{
                authViewModel.getCustomerInfo(it.uid)
            }
        }
        messagesViewModel.getAllMessages()


    }
    private fun setUpNav() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
         appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard,
                R.id.navigation_transactions,
                R.id.navigation_customers,
                R.id.navigation_inventory,
                R.id.navigation_account
            ),binding.drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navigation.setupWithNavController(navController)
    }


    override fun onSupportNavigateUp(): Boolean {

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun observers() {
        authViewModel.users.observe(this) {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message, Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().signOut().also {
                        finish()
                    }
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Getting User Profile")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    this.users  = it.data
                }
            }
        }
        messagesViewModel.messages.observe(this) {
            when(it) {
                is UiState.FAILED -> {

                }
                is UiState.LOADING -> {

                }
                is UiState.SUCCESS -> {
                    if (users != null) {
                        messageBadge.number = it.data.getNewMessages(users?.id ?: "")
                        invalidateOptionsMenu()
                    }
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val currentDestinationId = navController.currentDestination?.id
        if (currentDestinationId != R.id.menu_messages && currentDestinationId != R.id.conversationFragment) {
            menuInflater.inflate(R.menu.menu_actions, menu)
            val messages = menu.findItem(R.id.menu_messages)
            BadgeUtils.attachBadgeDrawable(messageBadge, binding.appBarMain.toolbar, messages.itemId)
            invalidateOptionsMenu()
            return true
        }
        invalidateOptionsMenu()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_messages -> {
                navController.navigate(R.id.menu_messages)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}