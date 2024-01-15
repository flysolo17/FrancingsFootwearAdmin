package com.ketchupzz.francingsfootwearadmin.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ketchupzz.francingsfootwearadmin.R
import com.ketchupzz.francingsfootwearadmin.model.customer.Customer

interface CustomerChatClickLister {
    fun onCustomerClicked(customer: Customer)
}

class CustomerChatAdapter(private val context: Context,private val customers : List<Customer>,private val customerChatClickLister: CustomerChatClickLister): RecyclerView.Adapter<CustomerChatAdapter.CustomerChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerChatViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_customer_2,parent,false)
        return CustomerChatViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  customers.size
    }

    override fun onBindViewHolder(holder: CustomerChatViewHolder, position: Int) {
        val customer = customers[position]
        Glide.with(context)
            .load(customer.profile)
            .error(R.drawable.profiles)
            .into(holder.imageCustomerProfile)
        holder.textCustomerName.text = customer.name
        holder.itemView.setOnClickListener {
            customerChatClickLister.onCustomerClicked(customer)
        }
    }
    class CustomerChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageCustomerProfile : ImageView = itemView.findViewById(R.id.imageCustomerProfile)
        val textCustomerName : TextView = itemView.findViewById(R.id.textCustomerName)
    }
}