package com.example.warframeaccountant

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import database.DatabaseConnection
import function.AddFunction

class AddActivity : AppCompatActivity()
{
	val REQUEST_IMAGE_CAPTURE = 1
	private var edit: Boolean = false
	private var id: Int = -1

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

		if (intentThis.getBooleanExtra("edit", false))
		{
			findViewById<Button>(R.id.removeButton).visibility = View.VISIBLE
			edit = true
			id = intentThis.getIntExtra("id", -1)

			findViewById<EditText>(R.id.itemNameText).setText(intentThis.getStringExtra("name"))
			findViewById<EditText>(R.id.quantityText).setText(intentThis.getIntExtra("quantity", 0).toString())
			findViewById<EditText>(R.id.expectedPriceText).setText(intentThis.getIntExtra("ePrice", 0).toString())
			findViewById<EditText>(R.id.basePriceText).setText(intentThis.getIntExtra("bPrice", 0).toString())
			if (targetIndex != -1)
			{
				findViewById<Spinner>(R.id.itemTypeDropdown).setSelection(targetIndex)
			}
		}
		else
		{
			findViewById<Button>(R.id.removeButton).visibility = View.INVISIBLE
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



	private fun dispatchTakePictureIntent() {
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

			findViewById<ImageView>(R.id.itemImage).setImageBitmap(imageBitmap)
		}
	}
}

