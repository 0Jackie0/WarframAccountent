package function

import android.content.ClipData
import android.content.Context
import android.util.Log
import domin.Item

class HomeFunction(guiClassContext: Context, guiItemList: ArrayList<Item> = ArrayList())
{
	private var guiClass = guiClassContext
	var itemList: ArrayList<Item> = guiItemList
		private set

	fun changeItemQuantity (index: Int, amount: Int)
	{
		itemList[index].number += amount
	}

	fun removeItem(index: Int)
	{
		itemList.removeAt(index)
	}

	fun addItem (newItem: Item)
	{
		itemList.add(newItem)
	}

	fun editItem(targetItem: Item, index: Int)
	{
		itemList[index].name = targetItem.name
		itemList[index].number = targetItem.number
		itemList[index].ePrice = targetItem.ePrice
		itemList[index].bPrice = targetItem.bPrice
	}
}