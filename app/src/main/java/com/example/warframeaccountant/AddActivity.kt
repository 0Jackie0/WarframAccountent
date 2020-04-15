package com.example.warframeaccountant

import android.app.Activity
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class AddActivity : AppCompatActivity()
{
	private var edit: Boolean = false
	private var index: Int = -1

	fun saveItem (saveButton: View)
	{
		val returnIntent = Intent()
		if (edit)
		{
			returnIntent.putExtra("index", index)
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
		returnIntent.putExtra("index", index)
		returnIntent.putExtra("action", "delete")

		setResult(Activity.RESULT_OK, returnIntent)

		this.finish()
	}

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_add)

		val intentThis = intent

		if (intentThis.getBooleanExtra("edit", false))
		{
			findViewById<Button>(R.id.removeButton).visibility = View.VISIBLE
			edit = true
			index = intentThis.getIntExtra("index", -1)

			findViewById<TextView>(R.id.itemNameText).text = intentThis.getStringExtra("name")
			findViewById<TextView>(R.id.quantityText).text = intentThis.getIntExtra("quantity", 0).toString()
			findViewById<TextView>(R.id.expectedPriceText).text = intentThis.getIntExtra("ePrice", 0).toString()
			findViewById<TextView>(R.id.basePriceText).text = intentThis.getIntExtra("bPrice", 0).toString()
		}
		else
		{
			findViewById<Button>(R.id.removeButton).visibility = View.INVISIBLE
			edit = false
		}
	}
}
