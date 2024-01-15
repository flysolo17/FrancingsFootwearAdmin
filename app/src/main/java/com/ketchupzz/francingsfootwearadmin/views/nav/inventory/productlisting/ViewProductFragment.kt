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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ketchupzz.francingsfootwear.utils.LoadingDialog
import com.ketchupzz.francingsfootwearadmin.R
import com.ketchupzz.francingsfootwearadmin.databinding.FragmentViewProductBinding
import com.ketchupzz.francingsfootwearadmin.model.products.Variation
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import com.ketchupzz.francingsfootwearadmin.utils.getEffectiveProductPrice
import com.ketchupzz.francingsfootwearadmin.viewmodels.VariationViewModel
import com.ketchupzz.francingsfootwearadmin.views.nav.inventory.adapters.ProductAdapter
import com.ketchupzz.francingsfootwearadmin.views.nav.inventory.adapters.VariationAdapter
import com.ketchupzz.francingsfootwearadmin.views.nav.inventory.adapters.VariationClickListener

class ViewProductFragment : Fragment() , VariationClickListener{

    private lateinit var binding : FragmentViewProductBinding;
    private lateinit var loadingDialog: LoadingDialog
    private val variationViewModel by activityViewModels<VariationViewModel>()
    private val args by navArgs<ViewProductFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewProductBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        Glide.with(binding.root.context).load(args.product.image).error(R.drawable.product).into(binding.imageProduct)
        binding.textName.text = args.product.name
        binding.textDescription.text = args.product.description
        binding.textCategory.text = args.product.category?.uppercase()
        variationViewModel.getVariationByProductID(args.product.id ?: "")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()

        binding.buttonCreateVariation.setOnClickListener {
            val directions = ViewProductFragmentDirections.actionViewProductFragmentToAddVariationFragment(args.product.id ?: "")
            findNavController().navigate(directions)
        }
    }

    private fun observer() {
        variationViewModel.variations.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message, Toast.LENGTH_SHORT).show()
                }

                is UiState.LOADING -> {
                    loadingDialog.showDialog("Getting Product Variations")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    binding.textPrice.text = getEffectiveProductPrice(it.data)
                    binding.recyclerviewVariations.apply {
                        layoutManager = LinearLayoutManager(binding.root.context)
                        adapter = VariationAdapter(binding.root.context,it.data,this@ViewProductFragment)
                    }
                }
            }
        }
    }

    override fun onVariationClicked(variation: Variation) {
        val directions = ViewProductFragmentDirections.actionViewProductFragmentToVariationBottomSheet(variation,args.product.id ?: "")
        findNavController().navigate(directions)
    }


}