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
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ketchupzz.francingsfootwearadmin.utils.LoadingDialog
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
        args.variation.sizes.mapIndexed { index, size ->
            displaySizeStocks(index, size = size)
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSaveNewStocks.setOnClickListener {
            val newStocksMap = mutableMapOf<String, Int>()
            for (i in 0 until binding.layoutSizes.childCount) {
                val view = binding.layoutSizes.getChildAt(i)
                if (view is ViewGroup) {
                    val textName = view.findViewById<TextView>(R.id.textName)
                    val inputStocks = view.findViewById<TextInputEditText>(R.id.inputStocks)
                    val sizeName = textName.text.toString()
                    val stocks = inputStocks.text.toString().toIntOrNull() ?: 0
                    newStocksMap[sizeName] = stocks
                    args.variation.sizes = args.variation.sizes.map { size ->
                        if (sizeName == size.size) {
                            size.copy(stock = size.stock + stocks)
                        } else {
                            size
                        }
                    }.toMutableList()

                }
            }

           goStockIn(args.productID,args.variation.id,args.variation.sizes)
        }
    }

    private fun displaySizeStocks(position : Int,size : Size) {
        val view : View = LayoutInflater.from(binding.root.context).inflate(R.layout.row_stock_in,null)
        val textname : TextView = view.findViewById(R.id.textName)
        val inputStocks: TextInputEditText = view.findViewById(R.id.inputStocks)
        val layoutStocks : TextInputLayout  = view.findViewById(R.id.layoutStocks)
        textname.text = size.size
        binding.layoutSizes.addView(view)
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


    private fun isSizeAlreadyExists(newSize: Size): Boolean {
        for (existingSize in args.variation.sizes) {
            if (existingSize.size.lowercase() == newSize.size.lowercase()) {
                return true
            }
        }
        return false
    }
}