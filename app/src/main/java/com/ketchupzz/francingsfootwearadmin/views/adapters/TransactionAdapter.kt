package com.ketchupzz.francingsfootwearadmin.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.ketchupzz.francingsfootwearadmin.R
import com.ketchupzz.francingsfootwearadmin.model.transaction.PaymentMethods
import com.ketchupzz.francingsfootwearadmin.model.transaction.PaymentStatus
import com.ketchupzz.francingsfootwearadmin.model.transaction.TransactionStatus
import com.ketchupzz.francingsfootwearadmin.model.transaction.Transactions
import com.ketchupzz.francingsfootwearadmin.utils.toPHP

interface TransactionClickListener {
    fun declineTransaction(transactions: Transactions)
    fun acceptTransaction(transactions: Transactions)
}

class TransactionAdapter(private val context: Context,private val transactions : List<Transactions>,private val transactionClickListener: TransactionClickListener) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_transactions,parent,false)
        return TransactionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.textTransactionID.text = transaction.id
        holder.textTotal.text = transaction.payment.total.toPHP()
        holder.textTransactionStatus.text = transaction.status.name

        holder.recyclerviewItems.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ItemsAdapter(context,transaction.items)
        }
        holder.buttonAccept.setOnClickListener {
            transactionClickListener.acceptTransaction(transaction)
        }
        holder.buttonDecline.setOnClickListener {
            transactionClickListener.declineTransaction(transaction)
        }
    }
    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textTransactionID : TextView = itemView.findViewById(R.id.textTransactionID)
        val textTransactionStatus : TextView = itemView.findViewById(R.id.textStatus)
        val textTotal : TextView = itemView.findViewById(R.id.textTotal)
        val recyclerviewItems : RecyclerView = itemView.findViewById(R.id.recyclerviewItems)
        val buttonDecline : TextView = itemView.findViewById(R.id.buttonDecline)
        val buttonAccept : TextView = itemView.findViewById(R.id.buttonAccept)
    }

}