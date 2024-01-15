package com.ketchupzz.francingsfootwearadmin.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.ketchupzz.francingsfootwearadmin.R
import com.ketchupzz.francingsfootwearadmin.model.customer.Customer
import com.ketchupzz.francingsfootwearadmin.model.messages.CustomerWithMessage
import com.ketchupzz.francingsfootwearadmin.model.messages.Messages
import com.ketchupzz.francingsfootwearadmin.utils.toTimeAgoOrDateTimeFormat

interface MessageClickListener {
    fun onMessageClicked(customer: Customer)
}
class MessagesAdapter(private val context: Context,private val customerWithMessage: List<CustomerWithMessage>,private val messageClickListener: MessageClickListener): RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.adapter_messages,parent,false)
       return MessagesViewHolder(view);
    }

    override fun getItemCount(): Int {
        return customerWithMessage.size;
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        val customerWithMessage  = customerWithMessage[position]
        val customer = customerWithMessage.customer
        val messages = customerWithMessage.messages
        holder.textLatestMessage.text = messages[0].message
        if (messages[0].senderID != "P1t0Wwrtm1Vg40Mrx6TLp2t79Sl2") {
            holder.textLatestMessage.setTextColor(ContextCompat.getColor(context, R.color.black))
        }
        holder.textTimestamp.text = messages[0].createdAt.toTimeAgoOrDateTimeFormat()
        Glide.with(context)
            .load(customer?.profile)
            .error(R.drawable.profiles)
            .into(holder.imageCustomerProfile)
        holder.textCustomerName.text = customer?.name ?: "no name"
        holder.itemView.setOnClickListener {
            messageClickListener.onMessageClicked(customer)
        }
    }

    class MessagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageCustomerProfile : ImageView = itemView.findViewById(R.id.imageCustomerProfile)
        val textCustomerName : TextView = itemView.findViewById(R.id.textCustomerName)
        val textLatestMessage : TextView = itemView.findViewById(R.id.textLatestMessage)
        val textTimestamp : TextView = itemView.findViewById(R.id.textTimestamp)
    }

}