package com.ketchupzz.francingsfootwearadmin.views.nav.inventory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ketchupzz.francingsfootwear.utils.LoadingDialog
import com.ketchupzz.francingsfootwearadmin.R
import com.ketchupzz.francingsfootwearadmin.databinding.FragmentInventoryBinding
import com.ketchupzz.francingsfootwearadmin.model.products.Product
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import com.ketchupzz.francingsfootwearadmin.viewmodels.ProductViewModel
import com.ketchupzz.francingsfootwearadmin.views.nav.inventory.adapters.ProductAdapter
import com.ketchupzz.francingsfootwearadmin.views.nav.inventory.adapters.ProductAdapterClickListener


class InventoryFragment : Fragment() ,ProductAdapterClickListener{
    private lateinit var binding : FragmentInventoryBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var productsAdapter : ProductAdapter
    private val productViewModel by activityViewModels<ProductViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInventoryBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productViewModel.getAllProducts()
        binding.buttonCreateProduct.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_inventory_to_createProductFragment)
        }

        observers()
    }

    private fun observers() {
        productViewModel.product.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }

                is UiState.LOADING -> {
                    loadingDialog.showDialog("Getting All Products")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    productsAdapter = ProductAdapter(binding.root.context,it.data,this)
                    binding.recyclerviewProducts.apply {
                        layoutManager = GridLayoutManager(binding.root.context,2)
                        adapter = productsAdapter
                    }
                }
            }
        }
    }

    override fun onClick(product: Product) {
        val directions = InventoryFragmentDirections.actionNavigationInventoryToViewProductFragment(product)
        findNavController().navigate(directions)
    }


}