package com.ketchupzz.francingsfootwearadmin.views.nav.inventory.productlisting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.button.MaterialButton
import com.ketchupzz.francingsfootwear.utils.LoadingDialog
import com.ketchupzz.francingsfootwearadmin.R
import com.ketchupzz.francingsfootwearadmin.databinding.FragmentStockInBinding
import com.ketchupzz.francingsfootwearadmin.model.products.Size
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import com.ketchupzz.francingsfootwearadmin.viewmodels.VariationViewModel


class FragmentStockIn : Fragment() {
    private lateinit var binding : FragmentStockInBinding
    private lateinit var loadingDialog: LoadingDialog
    private val variationViewModel by activityViewModels<VariationViewModel>()
    private val args by navArgs<FragmentStockInArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStockInBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        args.variation.sizes.forEach {
            addTextViewToRow(it)
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun goStockIn(productID: String,variationID : String,size: List<Size>) {
        variationViewModel.stockIn(productID,variationID,size){
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Add products to ${args.variation.name}")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.data,Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun addTextViewToRow(size: Size) {
        val view : View = LayoutInflater.from(binding.root.context).inflate(R.layout.row_sizes,null)
        val textname : TextView = view.findViewById(R.id.textName)
        val textcost : TextView = view.findViewById(R.id.textCost)
        val textprice : TextView = view.findViewById(R.id.textPrice)
        val textStocks: TextView = view.findViewById(R.id.textStocks)
        val buttonDelete : MaterialButton = view.findViewById(R.id.buttonDelete)
        textname.text = size.size
        textcost.text = size.cost.toString()
        textprice.text = size.price.toString()
        textStocks.text = size.stock.toString()
        buttonDelete.setOnClickListener {
            binding.layoutSizes.removeView(view)
        }
        binding.layoutSizes.addView(view)
    }

    private fun isSizeAlreadyExists(newSize: Size): Boolean {
        for (existingSize in args.variation.sizes) {
            if (existingSize.size.lowercase() == newSize.size.lowercase()) {
                return true
            }
        }
        return false
    }
}