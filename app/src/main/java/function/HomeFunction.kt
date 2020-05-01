package function

import android.content.ClipData
import android.content.Context
import android.util.Log
import androidx.room.Room
import database.DatabaseConnection
import database.ItemRepo
import domin.Item
import domin.Type

class HomeFunction(guiClassContext: Context, guiItemList: ArrayList<Item> = ArrayList())
{
	private var guiClass = guiClassContext
	private val itemRepo = DatabaseConnection(guiClass).getItemRepo()
	private val typeRepo = DatabaseConnection(guiClass).getTypeRepo()


	fun getAllItemList(order: String = ""): ArrayList<Item>
	{
		if (order.equals("name", true))
		{
			return itemRepo.getAllItemOrderName() as ArrayList<Item>
		}
		else if (order.equals("quantity", true))
		{
			return itemRepo.getAllItemOrderQuantity() as ArrayList<Item>
		}
		else
		{
			return itemRepo.getAllItem() as ArrayList<Item>
		}
	}

	fun getFilterItemList (typeName: String, order: String): ArrayList<Item>
	{
		if (order.equals("name", true))
		{
			return itemRepo.getFilterItemName(typeRepo.getTypeByName(typeName).typeId) as ArrayList<Item>
		}
		else if (order.equals("quantity", true))
		{
			return itemRepo.getFilterItemQuantity(typeRepo.getTypeByName(typeName).typeId) as ArrayList<Item>
		}
		else
		{
			return itemRepo.getAllItem() as ArrayList<Item>
		}
	}

	fun getTargetItem (id: Int): Item
	{
		return itemRepo.getItem(id)
	}

	fun changeItemQuantity (id: Int, amount: Int)
	{
		itemRepo.changeItemQuantity(id, amount)
	}

	fun removeItem(id: Int)
	{
		itemRepo.removeItem(id)
	}

	fun addItem (newItem: Item)
	{
		itemRepo.insert(newItem)
	}

	fun editItem(targetItem: Item)
	{
		itemRepo.update(targetItem)
	}

	fun getItemTypeList(): List<Type>
	{
		return typeRepo.getAllType()
	}
}