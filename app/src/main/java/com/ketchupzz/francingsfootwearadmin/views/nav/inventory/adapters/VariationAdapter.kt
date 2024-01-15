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
import com.ketchupzz.francingsfootwearadmin.model.products.Variation
import org.w3c.dom.Text

interface VariationClickListener {
    fun onVariationClicked(variation: Variation)
}
class VariationAdapter(private val context: Context,private val variations : List<Variation>,private val variationClickListener: VariationClickListener) : RecyclerView.Adapter<VariationAdapter.VariationViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VariationAdapter.VariationViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_variations,parent,false)
        return VariationViewHolder(view)
    }

    override fun onBindViewHolder(holder: VariationAdapter.VariationViewHolder, position: Int) {
        val variation = variations[position]
        holder.textVariationName.text = variation.name
        holder.textVariationPrice.text = variation.getEffectiveVariationPrice()
        Glide.with(context).load(variation.image).error(R.drawable.product).into(holder.imageVariation)
        holder.textSizes.text = variation.getSizeNames()
        holder.itemView.setOnClickListener {
            variationClickListener.onVariationClicked(variation)
        }
    }

    override fun getItemCount(): Int {
        return variations.size
    }
    class VariationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageVariation : ImageView = itemView.findViewById(R.id.imageVariation)
        val textVariationName : TextView = itemView.findViewById(R.id.textVariationName)
        val textVariationPrice : TextView = itemView.findViewById(R.id.textVariationPrice)
        val textSizes : TextView  = itemView.findViewById(R.id.textSizes)
    }
}