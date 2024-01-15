package com.ketchupzz.francingsfootwearadmin.views.nav.inventory.adapters

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ketchupzz.francingsfootwear.models.products.Size
import com.ketchupzz.francingsfootwearadmin.R
import com.ketchupzz.francingsfootwearadmin.utils.toPHP

interface StocksClickListener {
    fun onStockClicked(size: Size)
}
class StockAdapter(private val context: Context,private val sizes : List<Size>,private val stocksClickListener: StocksClickListener) : RecyclerView.Adapter<StockAdapter.StockViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.row_stock_in,parent,false)
        return StockViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sizes.size
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val size = sizes[position]
        holder.textname.text = size.size
        holder.textcost.text = size.cost.toPHP()
        holder.textprice.text = size.price.toPHP()
        holder.textStocks.text = size.stock.toString()
        holder.itemView.setOnClickListener {
            stocksClickListener.onStockClicked(size)
        }
    }

    class StockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textname : TextView = itemView.findViewById(R.id.textName)
        val textcost : TextView = itemView.findViewById(R.id.textCost)
        val textprice : TextView = itemView.findViewById(R.id.textPrice)
        val textStocks: TextView = itemView.findViewById(R.id.textStocks)

    }
}