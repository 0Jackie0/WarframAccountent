package com.example.warframeaccountant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import domin.Item
import function.RecycleAdapter
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity()
{
	var itemList: ArrayList<Item> = ArrayList()
	lateinit var recycleListManager: RecycleAdapter

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_home)

		setup()
	}

	private fun setup ()
	{
		createData()
		recycleListManager = RecycleAdapter(this, itemList)

		itemListArea.adapter = recycleListManager
		itemListArea.layoutManager = LinearLayoutManager(this)

		getSumPrice()
		getSumQuantity()
	}

	private fun getSumPrice ()
	{
		var sum = 0
		for (item in itemList)
		{
			sum += item.ePrice * item.number
		}
		totalPriceText.text = sum.toString()
	}

	private fun getSumQuantity ()
	{
		var sum = 0
		for (item in itemList)
		{
			sum += item.number
		}

		totalAmountText.text = sum.toString()
	}

	fun updateSumPrice(value: Int)
	{
		totalPriceText.text = (totalPriceText.text.toString().toInt() - value).toString()
	}
	fun updateSumQuantity (flag: Int)
	{
		totalAmountText.text = (totalAmountText.text.toString().toInt() + flag).toString()
	}

	private fun createData ()
	{
		for (index in 0..9)
		{
			val name = "Item Name $index"
			val number: Int = ((Math.random() * ((50 - 1) + 1)) + 1).toInt()
			val ePrice: Int = ((Math.random() * ((500 - 5) + 1)) + 1).toInt()
			val bPrice: Int = ((Math.random() * ((250 - 2) + 1)) + 1).toInt()

			val newItem = Item(name, number, ePrice, bPrice)

			itemList.add(newItem)
		}
	}
}
