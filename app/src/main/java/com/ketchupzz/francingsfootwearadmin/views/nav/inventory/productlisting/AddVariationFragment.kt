package com.ketchupzz.francingsfootwearadmin.views.nav.inventory.productlisting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.button.MaterialButton
import com.ketchupzz.francingsfootwearadmin.utils.LoadingDialog
import com.ketchupzz.francingsfootwearadmin.R
import com.ketchupzz.francingsfootwearadmin.databinding.FragmentAddVariationBinding
import com.ketchupzz.francingsfootwearadmin.model.products.Size
import com.ketchupzz.francingsfootwearadmin.model.products.Variation
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import com.ketchupzz.francingsfootwearadmin.utils.generateRandomNumber
import com.ketchupzz.francingsfootwearadmin.utils.getConsonants
import com.ketchupzz.francingsfootwearadmin.utils.getImageTypeFromUri
import com.ketchupzz.francingsfootwearadmin.viewmodels.VariationViewModel


class AddVariationFragment : Fragment() {
    private lateinit var binding : FragmentAddVariationBinding
    private val sizes = mutableListOf<Size>()
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private var selectedImage : Uri ?  = null
    private val variationViewModel by activityViewModels<VariationViewModel>()
    private lateinit var loadingDialog : LoadingDialog
    private val args by navArgs<AddVariationFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddVariationBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonAddSize.setOnClickListener {
            binding.cardAddSize.visibility = if (binding.cardAddSize.visibility == View.VISIBLE) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        binding.buttonSaveSize.setOnClickListener {
            val name = binding.inputSize.text.toString()
            val stocks = binding.inputStocks.text.toString()
            val cost = binding.inputCost.text.toString()
            val price = binding.inputPrice.text.toString()

            if (name.isNotEmpty() && stocks.isNotEmpty() && cost.isNotEmpty() && price.isNotEmpty()) {
                val stocksValue = stocks.toInt()
                val costValue = cost.toDouble()
                val priceValue = price.toDouble()
                val size = Size(name,stocksValue,costValue,priceValue)
                if (costValue >= priceValue) {
                    Toast.makeText(view.context, "Cost should be higher or equal to price value", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (isSizeAlreadyExists(size)) {
                    Toast.makeText(view.context, "Size exists", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                sizes.add(size)
                addTextViewToRow(size)
                binding.cardAddSize.visibility = View.GONE
            } else {
                Toast.makeText(view.context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonAddImage.setOnClickListener {
            pickImageFromGallery()
        }
        binding.buttonSaveVariation.setOnClickListener {
            val variationName = binding.inputVariationName.text.toString()
            if (variationName.isEmpty()) {
                binding.layoutVariationName.error = "enter variation name"
                return@setOnClickListener
            }
            if (selectedImage == null) {
                Toast.makeText(view.context,"Please add image",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (sizes.isEmpty()) {
                Toast.makeText(view.context,"Please add sizes",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val variation = Variation(id = generateRandomNumber() +"-"+ getConsonants(variationName), image = selectedImage.toString(),name = variationName,sizes)
            uploadVariationImage(args.productID,variation)
        }
    }

    private fun uploadVariationImage(productID: String ,variation: Variation) {
        val image = Uri.parse(variation.image)
        val type = binding.root.context.getImageTypeFromUri(image)
        variationViewModel.uploadVariationImage(productID, image,type ?: ".jpg") {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Uploading Product image...")
                }
                is UiState.SUCCESS -> {
                    variation.image = it.data.toString()
                    loadingDialog.closeDialog()
                    saveVariation(productID, variation)
                }
            }
        }
    }
    private fun saveVariation(productID : String,variation: Variation){
        variationViewModel.createVariation(productID,variation) {
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
        for (existingSize in sizes) {
            if (existingSize.size.lowercase() == newSize.size.lowercase()) {
                return true
            }
        }
        return false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        pickImageLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImageUri: Uri? = result.data?.data
                if (selectedImageUri != null) {
                    selectedImage = selectedImageUri
                    binding.buttonAddImage.setImageURI(selectedImageUri)
                } else {
                    Toast.makeText(binding.root.context,"Unknown error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }

}