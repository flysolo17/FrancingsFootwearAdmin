package com.ketchupzz.francingsfootwearadmin.views.nav.inventory.productlisting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ketchupzz.francingsfootwearadmin.utils.LoadingDialog
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import com.ketchupzz.francingsfootwearadmin.R
import com.ketchupzz.francingsfootwearadmin.databinding.FragmentCreateProductBinding
import com.ketchupzz.francingsfootwearadmin.model.products.Product
import com.ketchupzz.francingsfootwearadmin.utils.generateRandomNumber
import com.ketchupzz.francingsfootwearadmin.utils.getImageTypeFromUri

import com.ketchupzz.francingsfootwearadmin.viewmodels.ProductViewModel


class CreateProductFragment : Fragment() {
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding : FragmentCreateProductBinding
    private lateinit var loadingDialog: LoadingDialog
    private val productViewModel by activityViewModels<ProductViewModel>()
    private var selectedImage : Uri ? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateProductBinding.inflate(inflater, container, false)
        loadingDialog = LoadingDialog(binding.root.context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSaveProduct.setOnClickListener {
            val id = generateRandomNumber()
            val name = binding.inputProductName.text.toString()
            val category = binding.inputCategory.text.toString()
            val desc = binding.inputProductDesc.text.toString()
            if (selectedImage == null) {
                Toast.makeText(binding.root.context,"Please add image",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (name.isEmpty()) {
                binding.layoutProductName.error = "enter name"
                return@setOnClickListener
            }
            if (category.isEmpty()) {
                binding.layoutProductName.error = "enter category"
                return@setOnClickListener
            }
            if (desc.isEmpty()) {
                binding.layoutProductDescription.error = "enter description"
                return@setOnClickListener
            }
            val product = Product(id,image = selectedImage.toString(),name= name,category = category, description =  desc)
            uploadProductImage(product)
        }
        binding.buttonAddImage.setOnClickListener {
            pickImageFromGallery()
        }
        observer()
    }
    private fun uploadProductImage(product: Product) {
        val image = Uri.parse(product.image)
        val type = binding.root.context.getImageTypeFromUri(image)
        productViewModel.uploadProductImages(product.id!!, image,type ?: ".jpg") {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Uploading Product image...")
                }
                is UiState.SUCCESS -> {
                    product.image = it.data.toString()
                    loadingDialog.closeDialog()
                    saveProduct(product)
                }
            }
        }
    }
    private fun saveProduct(product: Product) {
        productViewModel.createProduct(product) {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Saving product...")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.data,Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }

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
    private fun observer() {
        productViewModel.product.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.FAILED -> {

                }
                is UiState.LOADING -> {

                }
                is UiState.SUCCESS -> {
                    val items =it.data.map { data->data.category?.uppercase() ?: "" }.distinct()
                    binding.inputCategory.apply {
                        setAdapter(ArrayAdapter(binding.root.context, R.layout.dropdown_category, items))
                    }

//                binding.inputCategory.setText(info.gender?.name ?: "", false)


                }
            }
        }
    }
}