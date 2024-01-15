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
import com.ketchupzz.francingsfootwearadmin.model.transaction.Transactions

enum class OrderType {
    BY_TOTAL_ORDERS_ASC,
    BY_TOTAL_ORDERS_DESC
}

class CustomersAdapter(private val context : Context ,private var customers : List<Customer>,private var transactions : List<Transactions>): RecyclerView.Adapter<CustomersAdapter.CustomersViewHolder>() {

    private var currentOrderType: OrderType = OrderType.BY_TOTAL_ORDERS_ASC
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomersViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_customer,parent,false)

        return CustomersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return customers.size
    }

    override fun onBindViewHolder(holder: CustomersViewHolder, position: Int) {
        val customer = customers[position]
        Glide.with(context)
            .load(customer.profile)
            .error(R.drawable.profiles)
            .into(holder.imageProfile)
        holder.textFullname.text = customer.name
        holder.textEmail.text = customer.email
        holder.textTotalOrders.text = "Total orders : ${transactions.filter { it.customerID == customer.id }.size}"
    }

    fun updateCustomers(customers: List<Customer>) {
        this.customers  = customers
        notifyDataSetChanged()
    }
    fun updateTransaction(transactions: List<Transactions>) {
        this.transactions = transactions
        notifyDataSetChanged()
    }

    class CustomersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textFullname : TextView = itemView.findViewById(R.id.textFullname)
        val textEmail : TextView = itemView.findViewById(R.id.textEmail)
        val imageProfile : ImageView = itemView.findViewById(R.id.imageProfile)
        val textTotalOrders : TextView = itemView.findViewById(R.id.textTotalOrders)
    }


}