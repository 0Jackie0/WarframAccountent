package function

import android.content.ClipData
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import com.example.warframeaccountant.HomeActivity
import database.DatabaseConnection
import database.ItemRepo
import domin.Item
import domin.Type
import server.Communication

class HomeFunction(guiClassContext: Context, appMode: Boolean)
{
	private var guiClass = guiClassContext
	private val mode = appMode
	private val itemRepo = DatabaseConnection(guiClass).getItemRepo()
	private val typeRepo = DatabaseConnection(guiClass).getTypeRepo()
	private val serverCommunication = Communication(guiClassContext)

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

	fun getFilterItemList(typeName: String, order: String): ArrayList<Item>
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

	fun getTargetItem(id: Int): Item
	{
		return itemRepo.getItem(id)
	}

	fun searchItem(itemName: String, order: String): ArrayList<Item>
	{
		if (order.equals("name", true))
		{
			return itemRepo.searchItemOrderName("%${itemName}%") as ArrayList<Item>
		}
		else
		{
			return itemRepo.searchItemOrderQuantity("%${itemName}%") as ArrayList<Item>
		}

	}

	fun changeItemQuantity(id: Int, amount: Int)
	{
		itemRepo.changeItemQuantity(id, amount)
	}

	fun removeItem(id: Int)
	{
		if(mode)
		{
			serverCommunication.removeItem(this, id)
		}
		else
		{
			deleteItemCallback(id)
		}
	}

	fun addItem (newItem: Item)
	{
		if(mode)
		{
			serverCommunication.addItem(this, newItem)
		}
		else
		{
			addItemCallback(newItem)
		}


	}

	fun editItem(targetItem: Item)
	{
		itemRepo.update(targetItem)
	}

	fun getItemTypeList(): List<Type>
	{
		return typeRepo.getAllType()
	}

	fun updateAllItemToServer()
	{
		serverCommunication.updateAllItem(this, itemRepo.getAllItem())
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	fun addItemCallback (newItem: Item)
	{
		itemRepo.insert(newItem)
		(guiClass as HomeActivity).changeItemCallback()
	}

	fun updateAllItemCallback(success: Boolean)
	{
		val serverCommunicationFile = guiClass.getSharedPreferences("com.example.warframeaccountant", Context.MODE_PRIVATE)
		serverCommunicationFile.edit().putBoolean("sync", success).apply()

		(guiClass as HomeActivity).updateAllItemCallback(success)
	}

	fun deleteItemCallback (targetId: Int)
	{
		itemRepo.removeItem(targetId)
		(guiClass as HomeActivity).changeItemCallback()
	}
}