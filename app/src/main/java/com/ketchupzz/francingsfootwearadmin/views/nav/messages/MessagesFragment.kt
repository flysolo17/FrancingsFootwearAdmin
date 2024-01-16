package com.ketchupzz.francingsfootwearadmin.views.nav.messages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ketchupzz.francingsfootwearadmin.utils.LoadingDialog
import com.ketchupzz.francingsfootwearadmin.databinding.FragmentMessagesBinding
import com.ketchupzz.francingsfootwearadmin.model.Users
import com.ketchupzz.francingsfootwearadmin.model.customer.Customer
import com.ketchupzz.francingsfootwearadmin.model.messages.CustomerWithMessage
import com.ketchupzz.francingsfootwearadmin.model.messages.Messages
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import com.ketchupzz.francingsfootwearadmin.viewmodels.AuthViewModel
import com.ketchupzz.francingsfootwearadmin.viewmodels.CustomersViewModel
import com.ketchupzz.francingsfootwearadmin.viewmodels.MessagesViewModel
import com.ketchupzz.francingsfootwearadmin.views.adapters.CustomerChatAdapter
import com.ketchupzz.francingsfootwearadmin.views.adapters.CustomerChatClickLister
import com.ketchupzz.francingsfootwearadmin.views.adapters.MessageClickListener
import com.ketchupzz.francingsfootwearadmin.views.adapters.MessagesAdapter

class MessagesFragment : Fragment() ,MessageClickListener ,CustomerChatClickLister{
    private lateinit var binding : FragmentMessagesBinding
    private val customersViewModel: CustomersViewModel by activityViewModels()
    private val messagesViewModel by activityViewModels<MessagesViewModel>()
    private lateinit var loadingDialog: LoadingDialog
    private val authViewModel by activityViewModels<AuthViewModel>()
    private var users: Users ? = null
    private var messages = listOf<Messages>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessagesBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customersViewModel.getAllCustomers()
        observers()
    }
    private fun observers() {
        authViewModel.users.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.FAILED -> {
                }
                is UiState.LOADING -> {
                }
                is UiState.SUCCESS -> {
                    this.users  = it.data
                }
            }
        }
        messagesViewModel.messages.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.FAILED -> {

                }
                is UiState.LOADING -> {

                }
                is UiState.SUCCESS -> {
                    messages = it.data
                }
            }
        }
        customersViewModel.customers.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Getting All Customers")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    val customerWithMessage = mutableListOf<CustomerWithMessage>()
                    it.data.forEach { customer ->
                        val messages =  messages.filter { message ->
                            customer.id == message.senderID || customer.id == message.receiverID
                        }
                        if (messages.isNotEmpty()) {
                            customerWithMessage.add(CustomerWithMessage(customer, messages))
                        }
                    }
                    binding.recyclerviewMessages.apply {
                        layoutManager = LinearLayoutManager(binding.root.context)
                        adapter = MessagesAdapter(binding.root.context,customerWithMessage,this@MessagesFragment)
                        addItemDecoration(
                            DividerItemDecoration(
                                binding.root.context,
                                LinearLayoutManager.VERTICAL
                            )
                        )
                    }
                    binding.recyclerViewCustomers.apply {
                        adapter = CustomerChatAdapter(binding.root.context,it.data,this@MessagesFragment)
                    }
                }
            }
        }


    }

    override fun onMessageClicked(customer: Customer) {
        users?.let {
            val directions = MessagesFragmentDirections.actionMenuMessagesToConversationFragment(it, customer)
            findNavController().navigate(directions)
        }

    }

    override fun onCustomerClicked(customer: Customer) {
        users?.let {
            val directions = MessagesFragmentDirections.actionMenuMessagesToConversationFragment(it, customer)
            findNavController().navigate(directions)
        }
    }
}