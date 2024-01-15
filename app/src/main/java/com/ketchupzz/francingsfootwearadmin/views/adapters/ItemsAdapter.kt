package com.ketchupzz.francingsfootwearadmin.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ketchupzz.francingsfootwear.models.products.Size

import com.ketchupzz.francingsfootwearadmin.R
import com.ketchupzz.francingsfootwearadmin.model.transaction.Items
import com.ketchupzz.francingsfootwearadmin.utils.toPHP

class ItemsAdapter(private val context: Context,private val items : List<Items>) :RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.adapter_items,parent,false)
        return ItemsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        val item = items[position]

        holder.textName.text= item.name
        holder.textVariation.text = item.variation
        Glide.with(context)
            .load(item.image)
            .error(R.drawable.product)
            .into(holder.imageProduct)
        holder.textQuantity.text = "x${item.quantity}"
        holder.textSize.text = item.size
        holder.textPrice.text = item.price.toPHP()
    }
    class ItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textName : TextView = itemView.findViewById(R.id.textProductName)
         val textVariation : TextView = itemView.findViewById(R.id.textProductVariation)
        val textSize : TextView = itemView.findViewById(R.id.textSizeName)
        val textPrice : TextView = itemView.findViewById(R.id.textPrice)
        val textQuantity : TextView = itemView.findViewById(R.id.textQuantity)
         val imageProduct : ImageView = itemView.findViewById(R.id.imageProduct)
        var size : Size? = null
//        fun getProductAndVariation(firestore: FirebaseFirestore, item : Items) {
//            firestore.collection(PRODUCT_COLLECTION)
//                .document(item.productID)
//                .get()
//                .addOnSuccessListener { productDoc ->
//                    if (productDoc.exists()) {
//                        val product = productDoc.toObject(Product::class.java)
//                        if (product != null) {
//                            textName.text = product.name
//                            firestore.collection(PRODUCT_COLLECTION)
//                                .document(item.productID)
//                                .collection(VARIATION_SUB_COLLECTION)
//                                .document(item.variationID)
//                                .get()
//                                .addOnSuccessListener { variationDoc ->
//                                    if (variationDoc.exists()) {
//                                        val variation = variationDoc.toObject(Variation::class.java)
//                                        if (variation != null) {
//                                            textVariation.text = variation.name
//                                            size = variation.sizes.find { it.size == item.size }
//                                            val price: Double = size?.price ?: 0.00
//                                            textPrice.text = formatPrice(price * item.quantity)
//                                            Glide.with(itemView.context)
//                                                .load(variation.image)
//                                                .error(R.drawable.product)
//                                                .into(imageProduct)
//                                        }
//                                    }
//                                }
//                        }
//                    }
//                }
//        }
    }
}