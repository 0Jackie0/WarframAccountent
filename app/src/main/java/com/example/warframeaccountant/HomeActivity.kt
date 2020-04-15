package com.example.warframeaccountant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import domin.Item
import function.HomeFunction
import function.RecycleAdapter
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity()
{
	private val ADD_PAGE_REQUEST_CODE = 1
	private val homeFunction: HomeFunction = HomeFunction(this)

	private lateinit var recycleListManager: RecycleAdapter

	fun addItem(addButton: View)
	{
		val newIntent = Intent(this, AddActivity::class.java)

		newIntent.putExtra("edit", false)
		startActivityForResult(newIntent, ADD_PAGE_REQUEST_CODE)
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_home)

		setup()
	}
	private fun setup ()
	{
		recycleListManager = RecycleAdapter(this, homeFunction.itemList)

		itemListArea.adapter = recycleListManager
		itemListArea.layoutManager = LinearLayoutManager(this)

		getSumPrice()
		getSumQuantity()
	}
	private fun getSumPrice ()
	{
		var sum = 0
		for (item in homeFunction.itemList)
		{
			sum += item.ePrice * item.number
		}
		totalPriceText.text = sum.toString()
	}
	private fun getSumQuantity ()
	{
		var sum = 0
		for (item in homeFunction.itemList)
		{
			sum += item.number
		}

		totalAmountText.text = sum.toString()
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	fun updateSumPrice(value: Int)
	{
		totalPriceText.text = (totalPriceText.text.toString().toInt() + value).toString()
	}
	fun updateSumQuantity (index: Int, flag: Int)
	{
		totalAmountText.text = (totalAmountText.text.toString().toInt() + flag).toString()

		homeFunction.changeItemQuantity(index, flag)
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	fun startEditItem (index: Int)
	{
		val newIntent = Intent(this, AddActivity::class.java)

		newIntent.putExtra("edit", true)
		newIntent.putExtra("index", index)

		newIntent.putExtra("name", homeFunction.itemList[index].name)
		newIntent.putExtra("quantity", homeFunction.itemList[index].number)
		newIntent.putExtra("ePrice", homeFunction.itemList[index].ePrice)
		newIntent.putExtra("bPrice", homeFunction.itemList[index].bPrice)

		startActivityForResult(newIntent, ADD_PAGE_REQUEST_CODE)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
	{
		super.onActivityResult(requestCode, resultCode, data)

		if (resultCode == Activity.RESULT_OK && data != null)
		{
			val action = data?.getStringExtra("action")
			if (action.equals("delete", true))
			{
				val itemIndex = data.getIntExtra("index", -1)
				homeFunction.removeItem(itemIndex)
				setup()
			}
			else if (action.equals("add", true))
			{
				val newItem: Item = Item()

				newItem.name = data.getStringExtra("name")
				newItem.number = data.getIntExtra("quantity", 0)
				newItem.ePrice = data.getIntExtra("ePrice", 0)
				newItem.bPrice = data.getIntExtra("bPrice", 0)

				homeFunction.addItem(newItem)

				findViewById<TextView>(R.id.totalPriceText).text = ( findViewById<TextView>(R.id.totalPriceText).text.toString().toInt() + data.getIntExtra("ePrice", 0) * data.getIntExtra("quantity", 0) ).toString()
				findViewById<TextView>(R.id.totalAmountText).text = ( findViewById<TextView>(R.id.totalAmountText).text.toString().toInt() + data.getIntExtra("quantity", 0) ).toString()
			}
			else if (action.equals("edit", true))
			{
				val targetItem: Item = Item()

				targetItem.name = data.getStringExtra("name")
				targetItem.number = data.getIntExtra("quantity", 0)
				targetItem.ePrice = data.getIntExtra("ePrice", 0)
				targetItem.bPrice = data.getIntExtra("bPrice", 0)

				homeFunction.editItem(targetItem, data.getIntExtra("index", -1))
				setup()
			}
		}
	}

	private fun createData ()
	{
		val temp = ArrayList<Item>()
		for (index in 0..9)
		{
			val name = "Item Name $index"
			val number: Int = ((Math.random() * ((50 - 1) + 1)) + 1).toInt()
			val ePrice: Int = ((Math.random() * ((500 - 5) + 1)) + 1).toInt()
			val bPrice: Int = ((Math.random() * ((250 - 2) + 1)) + 1).toInt()

			val newItem = Item(name, number, ePrice, bPrice)

			temp.add(newItem)
		}
//		homeFunction.itemList = temp
	}
}
