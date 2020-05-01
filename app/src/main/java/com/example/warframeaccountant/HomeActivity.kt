package com.example.warframeaccountant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
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
	private val ALL_ITEM_TEXT = "- - -"
	private val homeFunction: HomeFunction = HomeFunction(this)

	private var order: Array<String> = arrayOf("- - -", "Name")

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

		setupDropdownList()
		setup(homeFunction.getAllItemList())
	}

	private fun setup(itemList: ArrayList<Item>)
	{
		recycleListManager = RecycleAdapter(this, itemList)

		itemListArea.adapter = recycleListManager
		itemListArea.layoutManager = LinearLayoutManager(this)

		getSumPrice()
		getSumQuantity()
	}

	private fun getSumPrice()
	{
		var sum = 0
		for (item in homeFunction.getAllItemList())
		{
			sum += item.ePrice * item.number
		}
		totalPriceText.text = sum.toString()
	}

	private fun getSumQuantity()
	{
		var sum = 0
		for (item in homeFunction.getAllItemList())
		{
			sum += item.number
		}

		totalAmountText.text = sum.toString()
	}

	private fun setupDropdownList()
	{
		val filterTypeContent = ArrayList<String>()

		filterTypeContent.add(ALL_ITEM_TEXT)
		for (index in homeFunction.getItemTypeList())
		{
			filterTypeContent.add(index.name)
		}

		val filterDropdownList = findViewById<Spinner>(R.id.filterList)
		filterDropdownList.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, filterTypeContent)

		filterDropdownList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
		{
			override fun onNothingSelected(parent: AdapterView<*>?)
			{

			}

			override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
			{
				order[0] = filterTypeContent[position]
				if (position != 0)
				{
					setup(homeFunction.getFilterItemList(order[0], order[1]))
				}
				else
				{
					setup(homeFunction.getAllItemList(order[1]))
				}
			}
		}

		val orderContent = ArrayList<String>()
		orderContent.add("Name")
		orderContent.add("Quantity")

		val orderDropdownList = findViewById<Spinner>(R.id.sortList)
		orderDropdownList.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, orderContent)

		orderDropdownList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
		{
			override fun onNothingSelected(parent: AdapterView<*>?)
			{

			}

			override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
			{
				order[1] = orderContent[position]
				if (order[0].equals(ALL_ITEM_TEXT, false))
				{
					setup(homeFunction.getAllItemList(order[1]))
				}
				else
				{
					setup(homeFunction.getFilterItemList(order[0], order[1]))
				}
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	fun updateSumPrice(value: Int)
	{
		totalPriceText.text = (totalPriceText.text.toString().toInt() + value).toString()
	}

	fun updateSumQuantity(id: Int, flag: Int)
	{
		totalAmountText.text = (totalAmountText.text.toString().toInt() + flag).toString()

		homeFunction.changeItemQuantity(id, flag)
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	fun startEditItem(id: Int)
	{
		val newIntent = Intent(this, AddActivity::class.java)

		val targetItem = homeFunction.getTargetItem(id)
		newIntent.putExtra("edit", true)
		newIntent.putExtra("id", targetItem.itemId)

		newIntent.putExtra("name", targetItem.name)
		newIntent.putExtra("quantity", targetItem.number)
		newIntent.putExtra("ePrice", targetItem.ePrice)
		newIntent.putExtra("bPrice", targetItem.bPrice)
		newIntent.putExtra("type", targetItem.type)

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
				homeFunction.removeItem(data.getIntExtra("id", -1))
			}
			else if (action.equals("add", true))
			{
				val newItem: Item = Item()

				newItem.name = data.getStringExtra("name")
				newItem.number = data.getIntExtra("quantity", 0)
				newItem.ePrice = data.getIntExtra("ePrice", 0)
				newItem.bPrice = data.getIntExtra("bPrice", 0)
				newItem.type = data.getIntExtra("type", 0)

				homeFunction.addItem(newItem)

				findViewById<TextView>(R.id.totalPriceText).text =
					(findViewById<TextView>(R.id.totalPriceText).text.toString()
						.toInt() + data.getIntExtra("ePrice", 0) * data.getIntExtra("quantity", 0)).toString()

				findViewById<TextView>(R.id.totalAmountText).text =
					(findViewById<TextView>(R.id.totalAmountText).text.toString()
						.toInt() + data.getIntExtra("quantity", 0)).toString()
			}
			else if (action.equals("edit", true))
			{
				val targetItem: Item = Item()

				targetItem.itemId = data.getIntExtra("id", 0)
				targetItem.name = data.getStringExtra("name")
				targetItem.number = data.getIntExtra("quantity", 0)
				targetItem.ePrice = data.getIntExtra("ePrice", 0)
				targetItem.bPrice = data.getIntExtra("bPrice", 0)
				targetItem.type = data.getIntExtra("type", 0)

				homeFunction.editItem(targetItem)
			}

			if (order[0].equals(ALL_ITEM_TEXT, false))
			{
				setup(homeFunction.getAllItemList(order[1]))
			}
			else
			{
				setup(homeFunction.getFilterItemList(order[0], order[1]))
			}
		}
	}
}
