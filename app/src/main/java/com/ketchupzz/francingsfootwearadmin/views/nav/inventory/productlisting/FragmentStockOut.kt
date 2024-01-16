package com.ketchupzz.francingsfootwearadmin.views.nav.inventory.productlisting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ketchupzz.francingsfootwearadmin.utils.LoadingDialog
import com.ketchupzz.francingsfootwearadmin.databinding.FragmentStockOutBinding
import com.ketchupzz.francingsfootwearadmin.model.products.Size
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import com.ketchupzz.francingsfootwearadmin.viewmodels.VariationViewModel


class FragmentStockOut : Fragment() {

    private lateinit var binding : FragmentStockOutBinding
    private lateinit var loadingDialog: LoadingDialog
    private val variationViewModel by activityViewModels<VariationViewModel>()
        private val args by navArgs<FragmentStockOutArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStockOutBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    private fun goStockOut(productID: String,variationID : String,size: List<Size>) {
        variationViewModel.stockOut(productID,variationID,size){
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message, Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("removing products from ${args.variation.name}")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.data, Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }
}