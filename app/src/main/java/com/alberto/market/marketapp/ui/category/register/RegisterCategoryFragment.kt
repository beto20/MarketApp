package com.alberto.market.marketapp.ui.category.register

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.alberto.market.marketapp.R
import com.alberto.market.marketapp.data.server.RegisterRequestCategory
import com.alberto.market.marketapp.databinding.FragmentRegisterCategoryBinding
import com.alberto.market.marketapp.util.toast
import com.alberto.market.marketapp.ui.common.gone
import com.alberto.market.marketapp.ui.common.visible
import com.alberto.market.marketapp.util.Constants
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


@AndroidEntryPoint
class RegisterCategoryFragment : Fragment(R.layout.fragment_register_category) {

    private lateinit var binding: FragmentRegisterCategoryBinding
    private val viewModel: RegisterCategoryViewModel by viewModels()

    private var imageBase64 = ""

    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>

    private val cropResultContract = object: ActivityResultContract<Any?, Uri?>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity().setAspectRatio(16, 9).getIntent(requireContext())
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }
    }

    var cameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        when {
            isGranted -> startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
            else -> Toast.makeText(requireContext(), "Denied", Toast.LENGTH_SHORT).show()
        }
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val imageBitMap = data?.extras?.get("data") as Bitmap

            val uri = getImageUriFromBitMap(requireContext(), imageBitMap)
            cropImage(uri)
        }
    }

    private fun getImageUriFromBitMap(context: Context, imageBitMap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        imageBitMap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, imageBitMap, "Image", null)
        return Uri.parse(path.toString())
    }

    private fun cropImage(uri: Uri?) {
        CropImage.activity(uri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(16, 9)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .start(requireContext(), this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val uri = CropImage.getActivityResult(data).uri
            val inputStream = uri?.let {
                requireContext().contentResolver.openInputStream(it)
            }

            val imageBitMap = BitmapFactory.decodeStream(inputStream)

            binding.imgAddCategory.setImageBitmap(imageBitMap)

            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    converterBase64(imageBitMap)
                }
            }

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterCategoryBinding.bind(view)

        cropActivityResultLauncher = registerForActivityResult(cropResultContract) {
            it?.let { uri ->
                val inputStream = uri?.let {
                    requireContext().contentResolver.openInputStream(it)
                }
                val imageBitMap = BitmapFactory.decodeStream(inputStream)

                binding.imgAddCategory.setImageURI(uri)

                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        converterBase64(imageBitMap)
                    }
                }
            }
        }
        events()
        setUpObservable()
    }

    private fun setUpObservable() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    updateUI(state)
                }
            }
        }
    }

    private fun updateUI(state: RegisterCategoryViewModel.CategoryCreateState) {
        when(state) {
            RegisterCategoryViewModel.CategoryCreateState.Init -> Unit
            is RegisterCategoryViewModel.CategoryCreateState.Error -> requireContext().toast(state.message)
            is RegisterCategoryViewModel.CategoryCreateState.IsLoading -> handleLoading(state.isLoading)
            is RegisterCategoryViewModel.CategoryCreateState.Success -> requireContext().toast(state.response)
        }
    }

    private fun handleLoading(isLoading: Boolean) = with(binding) {
        if (isLoading) progressBar.visible()
        else progressBar.gone()
    }


    private fun events() = with(binding) {
        btnCamera.setOnClickListener {
            cameraPermission.launch(Manifest.permission.CAMERA)
        }

        btnGallery.setOnClickListener {
            cropActivityResultLauncher.launch(null)
        }

        btnUpload.setOnClickListener {
            val name = edtNameCategory.text.toString()
            viewModel.saveCategory(RegisterRequestCategory(name, "${Constants.FORMAT_BASE_64}${imageBase64}"))

        }
    }

    private fun converterBase64(imageBitMap: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        imageBitMap?.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        val bytes = byteArrayOutputStream.toByteArray()
        imageBase64 = Base64.encodeToString(bytes, Base64.DEFAULT)
    }

}