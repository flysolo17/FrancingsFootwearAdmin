package com.ketchupzz.francingsfootwearadmin.views.nav.messages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ketchupzz.francingsfootwear.utils.LoadingDialog
import com.ketchupzz.francingsfootwearadmin.R
import com.ketchupzz.francingsfootwearadmin.databinding.FragmentConversationBinding
import com.ketchupzz.francingsfootwearadmin.model.messages.Messages
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import com.ketchupzz.francingsfootwearadmin.viewmodels.MessagesViewModel


class ConversationFragment : Fragment() {

    private lateinit var binding :FragmentConversationBinding
    private lateinit var loadingDialog: LoadingDialog
    private val messagesViewModel by activityViewModels<MessagesViewModel>()
    private val args by navArgs<ConversationFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentConversationBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonMessages.setOnClickListener {
            val message = binding.inputMessages.text.toString()

            if (message.isEmpty()) {
                Toast.makeText(view.context,"enter message",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val messages = Messages(
                senderID =  args.users.id ?: "",
                receiverID = args.customer.id ?: "",
                message = message,
            )
            sendMessage(messages)
        }
        observers()
    }
    private fun observers() {
        messagesViewModel.messages.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.FAILED -> {

                }

                is UiState.LOADING -> {

                }

                is UiState.SUCCESS -> {
                    val messages = it.data.filter { message ->
                        (message.senderID == args.customer.id && message.receiverID == args.users.id) || (message.receiverID == args.customer.id && message.senderID == args.users.id)
                    }

                    val conversationAdapter = ConversationAdapter(binding.root.context,messages, args.users,args.customer)
                    val linearLayoutManager = LinearLayoutManager(binding.root.context)
                    linearLayoutManager.reverseLayout = true
                    linearLayoutManager.stackFromEnd = true
                    binding.recyclerviewMessages.apply {
                        layoutManager = linearLayoutManager
                        adapter = conversationAdapter
                    }
                }
            }
        }
    }
    private fun sendMessage(messages: Messages) {
        messagesViewModel.messagesRepository.sendMessage(messages) {
            when(it) {
                is UiState.FAILED -> {
                    binding.buttonMessages.isClickable = true
                    Toast.makeText(binding.root.context,it.message, Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    binding.buttonMessages.isClickable = false
                }
                is UiState.SUCCESS -> {
                    binding.buttonMessages.isClickable = true
                    binding.inputMessages.setText("")
                    Toast.makeText(binding.root.context,it.data, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}