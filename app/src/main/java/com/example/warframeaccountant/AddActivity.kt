package com.example.warframeaccountant

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import database.DatabaseConnection
import function.AddFunction
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class AddActivity : AppCompatActivity()
{
	val REQUEST_IMAGE_CAPTURE = 1
	private var edit: Boolean = false
	private var id: Int = -1
	private var imageString: String = ""

	private lateinit var addFunction: AddFunction

	fun saveItem (saveButton: View)
	{
		val targetType = addFunction.getTargetType(findViewById<Spinner>(R.id.itemTypeDropdown).selectedItem.toString())

		val returnIntent = Intent()
		if (edit)
		{
			returnIntent.putExtra("id", id)
			returnIntent.putExtra("action", "edit")
		}
		else
		{
			returnIntent.putExtra("action", "add")
		}

		returnIntent.putExtra("name", findViewById<TextView>(R.id.itemNameText).text.toString())
		returnIntent.putExtra("quantity", findViewById<TextView>(R.id.quantityText).text.toString().toInt())
		returnIntent.putExtra("ePrice", findViewById<TextView>(R.id.expectedPriceText).text.toString().toInt())
		returnIntent.putExtra("bPrice", findViewById<TextView>(R.id.basePriceText).text.toString().toInt())
		returnIntent.putExtra("type", targetType.typeId)
		returnIntent.putExtra("imageString", imageString)

		findViewById<ImageView>(R.id.itemImage)


		setResult(Activity.RESULT_OK, returnIntent)

		this.finish()
	}

	fun cancel(cancleButton: View)
	{
		this.finish()
	}

	fun remove(removeButton: View)
	{
		val returnIntent = Intent()
		returnIntent.putExtra("id", id)
		returnIntent.putExtra("action", "delete")

		setResult(Activity.RESULT_OK, returnIntent)

		this.finish()
	}

	fun takeImage(imageButton: View)
	{
		dispatchTakePictureIntent()
	}

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_add)

		addFunction = AddFunction(this)

		val intentThis = intent

		var targetIndex =  setupItemType(intentThis.getIntExtra("type", -1))

		if (intentThis.getBooleanExtra("edit", false))//Edit item
		{
			val targetItem = addFunction.getTargetItem(intentThis.getIntExtra("id", -1))

			findViewById<Button>(R.id.removeButton).visibility = View.VISIBLE
			edit = true
			id = targetItem.itemId

			if(targetItem.imageString != "" && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
			{
				val byteImage = Base64.getDecoder().decode( targetItem.imageString )
				findViewById<ImageView>(R.id.itemImage).setImageBitmap(BitmapFactory.decodeStream(ByteArrayInputStream( byteImage )))
			}

			findViewById<EditText>(R.id.itemNameText).setText(targetItem.name)
			findViewById<EditText>(R.id.quantityText).setText(targetItem.number.toString())
			findViewById<EditText>(R.id.expectedPriceText).setText(targetItem.ePrice.toString())
			findViewById<EditText>(R.id.basePriceText).setText(targetItem.bPrice.toString())

			if (targetIndex != -1)
			{
				findViewById<Spinner>(R.id.itemTypeDropdown).setSelection(targetIndex)
			}
		}
		else//Add item
		{
			findViewById<Button>(R.id.removeButton).visibility = View.INVISIBLE

			findViewById<EditText>(R.id.quantityText).setText("0")
			findViewById<EditText>(R.id.expectedPriceText).setText("0")
			findViewById<EditText>(R.id.basePriceText).setText("0")

			edit = false
		}
	}

	private fun setupItemType (target: Int): Int
	{
		val itemTypeContent = ArrayList<String>()
		val allTypeList = addFunction.getTypeList()
		var targetIndex: Int = -1

		for (index in allTypeList.indices)
		{
			itemTypeContent.add(allTypeList[index].name)

			if (target != -1 && target == allTypeList[index].typeId)
			{
				targetIndex = index
			}
		}

		val dropdownList = findViewById<Spinner>(R.id.itemTypeDropdown)
		dropdownList.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,itemTypeContent)

		return targetIndex
	}

	private fun dispatchTakePictureIntent()
	{
		Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
			takePictureIntent.resolveActivity(packageManager)?.also {
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
			}
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null)
		{
			val imageBitmap = data.extras?.get("data") as Bitmap

			val byteOutputStream = ByteArrayOutputStream()
			imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, byteOutputStream)

			val imageByte = byteOutputStream.toByteArray()

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
			{
				imageString = Base64.getEncoder().encodeToString(imageByte)
			}
			else
			{
				Log.i("Convert image", "Error version lower than 26")
			}

			findViewById<ImageView>(R.id.itemImage).setImageBitmap(imageBitmap)
		}
	}
}

