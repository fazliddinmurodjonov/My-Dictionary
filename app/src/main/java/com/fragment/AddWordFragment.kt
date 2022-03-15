package com.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adapter.SpinnerAdapter
import com.example.androiddatabaselesson9.BuildConfig
import com.example.androiddatabaselesson9.R
import com.example.androiddatabaselesson9.databinding.CustomPermissionDialogBinding
import com.example.androiddatabaselesson9.databinding.FragmentAddWordBinding
import com.room.database.DictionaryDatabase
import com.room.entity.Category
import com.room.entity.Words
import com.util.Empty
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddWordFragment : Fragment(R.layout.fragment_add_word) {
    private val binding: FragmentAddWordBinding by viewBinding()
    lateinit var dictionaryDatabase: DictionaryDatabase
    lateinit var spinnerAdapter: SpinnerAdapter
    lateinit var list: ArrayList<Category>
    var imagePath: String? = null
    var photoURI: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            dictionaryDatabase = DictionaryDatabase.getInstance(requireContext())
            loadActionBar()
            list = ArrayList()
            val category = Category()
            category.categoryName = "Select category"
            list.add(category)
            list.addAll(dictionaryDatabase.categoryDao().getAllCategory())
            spinnerAdapter = SpinnerAdapter(list, requireContext())
            categorySpinner.adapter = spinnerAdapter
            wordImage.setOnClickListener {
                val dialog = Dialog(requireActivity())
                val dialogView =
                    CustomPermissionDialogBinding.inflate(LayoutInflater.from(requireContext()),
                        null,
                        false)
                dialog.setContentView(dialogView.root)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialogView.camera.setOnClickListener {
                    val imageFile = createImageFile()
                    photoURI =
                        FileProvider.getUriForFile(requireContext(),
                            BuildConfig.APPLICATION_ID,
                            imageFile)
                    getTakeImageContent.launch(photoURI)
                    dialog.dismiss()
                }
                dialogView.gallery.setOnClickListener {
                    pickImageFromGalleryNew()
                    dialog.dismiss()
                }
                dialog.show()
            }
            cancelButton.setOnClickListener {
                findNavController().popBackStack()
            }
            saveButton.setOnClickListener {
                val word = word.text.toString()
                val wordTranslation = wordTranslation.text.toString()
                val wordBol = Empty.empty(word)
                val wordTransBol = Empty.empty(wordTranslation)
                val wordSpace = Empty.space(word)
                val wordTransSpace = Empty.space(wordTranslation)
                val categoryPosition = categorySpinner.selectedItemPosition
                val categoryId = list[categoryPosition].categoryId
                val bol = wordBol && wordTransBol
                val space = wordSpace && wordTransSpace
                if (bol && space && categoryPosition != 0) {
                    val wordObj = Words()
                    wordObj.wordName = word
                    wordObj.wordTranslation = wordTranslation
                    wordObj.wordFavourite = 0
                    wordObj.wordCategoryId = categoryId
                    wordObj.wordImage = imagePath
                    dictionaryDatabase.wordsDao().insertWords(wordObj)
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun loadActionBar() {
        (activity as AppCompatActivity).supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val externalFilesDir: File? =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", externalFilesDir).apply {

        }
    }

    private val getTakeImageContent =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                binding.wordImage.setImageURI(photoURI)
                val openInputStream = requireActivity().contentResolver?.openInputStream(photoURI!!)
                var numberText = "1234567890"
                val toCharArray = numberText.toCharArray()
                toCharArray.shuffle()
                var imageName: String = ""
                for (c in toCharArray) {
                    imageName += c
                }
                val file = File(requireActivity().filesDir, "$imageName.jpg")
                val fileOutputStream = FileOutputStream(file)
                openInputStream?.copyTo(fileOutputStream)
                openInputStream?.close()
                fileOutputStream.close()
                imagePath = file.absolutePath
            }
        }

    private fun pickImageFromGalleryNew() {
        getImageContent.launch("image/*")
    }

    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri ?: return@registerForActivityResult
            binding.wordImage.setImageURI(uri)
            val openInputStream = requireActivity().contentResolver?.openInputStream(uri)
            var numberText = "1234567890"
            val toCharArray = numberText.toCharArray()
            toCharArray.shuffle()

            var imageName: String = ""
            for (c in toCharArray) {
                imageName += c
            }
            val file = File(requireActivity().filesDir, "$imageName.jpg")
            val fileOutputStream = FileOutputStream(file)
            openInputStream?.copyTo(fileOutputStream)
            openInputStream?.close()
            fileOutputStream.close()
            val absolutePath = file.absolutePath
            imagePath = absolutePath
        }
}