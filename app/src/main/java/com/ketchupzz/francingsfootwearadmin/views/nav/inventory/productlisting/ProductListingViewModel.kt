package com.ketchupzz.francingsfootwearadmin.views.nav.inventory.productlisting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ketchupzz.francingsfootwearadmin.model.products.Variation

class ProductListingViewModel : ViewModel() {
    val variation = MutableLiveData<Variation>()

    fun getVariation() : LiveData<Variation> {
        return  variation;
    }
    fun setVariation(variations: Variation) {
        variation.value = variations;
    }
}