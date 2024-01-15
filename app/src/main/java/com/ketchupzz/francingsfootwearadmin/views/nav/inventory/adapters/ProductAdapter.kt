package com.ketchupzz.francingsfootwearadmin.views.nav.inventory.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ketchupzz.francingsfootwearadmin.R
import com.ketchupzz.francingsfootwearadmin.model.products.Product


interface ProductAdapterClickListener {
    fun onClick(product: Product)
}
class ProductAdapter(private val context: Context,private val products : List<Product>,private  val productAdapterClickListener: ProductAdapterClickListener) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_product,parent,false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.textProductName.text = product.name
        Glide.with(context)
            .load(product.image)
            .error(R.drawable.product)
            .into(holder.imageProduct)

        holder.itemView.setOnClickListener {
            productAdapterClickListener.onClick(product)
        }
    }
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textProductName : TextView = itemView.findViewById(R.id.textProductName)
        val imageProduct : ImageView = itemView.findViewById(R.id.imageProduct)
    }
}