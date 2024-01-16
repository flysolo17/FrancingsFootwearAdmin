package com.ketchupzz.francingsfootwearadmin.views.nav.inventory.productlisting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ketchupzz.francingsfootwearadmin.utils.LoadingDialog
import com.ketchupzz.francingsfootwearadmin.databinding.FragmentVariationBottomSheetBinding
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import com.ketchupzz.francingsfootwearadmin.viewmodels.VariationViewModel


class VariationBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentVariationBottomSheetBinding
    private val args by navArgs<VariationBottomSheetArgs>()
    private lateinit var loadingDialog: LoadingDialog
    private val variationViewModel by activityViewModels<VariationViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVariationBottomSheetBinding.inflate(inflater,container,false)
        binding.textVariationName.text = args.variation.name
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.actionEdit.setOnClickListener {
            val directions = VariationBottomSheetDirections.actionVariationBottomSheetToUpdateVariationFragment(args.variation,args.productID)
            findNavController().navigate(directions)
        }
        binding.actionDelete.setOnClickListener {
            MaterialAlertDialogBuilder(view.context)
                .setTitle("Delete ")
                .setMessage("Are you sure you want to delete ${args.variation.name}")
                .setPositiveButton("Yes") {dialog,_ ->
                    deleteAction().also {
                        dialog.dismiss()
                    }
                }
                .setNegativeButton("Cancel") { dialog,_ ->
                    dialog.dismiss()
                }
                .show()
        }
        binding.actionStockIn.setOnClickListener {
            val directions = VariationBottomSheetDirections.actionVariationBottomSheetToFragmentStockIn(args.variation,args.productID)
            findNavController().navigate(directions)
        }
        binding.actionStockOut.setOnClickListener {
            val directions = VariationBottomSheetDirections.actionVariationBottomSheetToFragmentStockOut(args.variation,args.productID)
            findNavController().navigate(directions)
        }
    }

    private fun deleteAction() {
        variationViewModel.deleteVariation(args.productID,args.variation.id,args.variation.image) {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message, Toast.LENGTH_SHORT).show()
                }

                is UiState.LOADING -> {
                    loadingDialog.showDialog("Deleting ${args.variation.name}..")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.data,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}