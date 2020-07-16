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
import domin.ItemImage
import domin.Type
import server.Communication
import java.lang.Exception

class HomeFunction(guiClassContext: Context, appMode: Boolean)
{
	private var guiClass = guiClassContext
	private val mode = appMode
	private val itemRepo = DatabaseConnection(guiClass).getItemRepo()
	private val typeRepo = DatabaseConnection(guiClass).getTypeRepo()
	private val imageRepo = DatabaseConnection(guiClass).getImageRepo()
	private val serverCommunication = Communication(guiClassContext)

	fun getAllItemList(order: String = ""): ArrayList<Item>
	{
		var itemList: ArrayList<Item> =
			if (order.equals("name", true))
			{
				itemRepo.getAllItemOrderName() as ArrayList<Item>
			}
			else if (order.equals("quantity", true))
			{
				itemRepo.getAllItemOrderQuantity() as ArrayList<Item>
			}
			else
			{
				itemRepo.getAllItem() as ArrayList<Item>
			}

		for(item in itemList)
		{
			item.imageString = imageRepo.getImage(item.imageId).imageString
		}

		return itemList
	}

	fun getFilterItemList(typeName: String, order: String): ArrayList<Item>
	{
		var itemList: ArrayList<Item> =
			if (order.equals("name", true))
			{
				itemRepo.getFilterItemName(typeRepo.getTypeByName(typeName).typeId) as ArrayList<Item>
			}
			else if (order.equals("quantity", true))
			{
				itemRepo.getFilterItemQuantity(typeRepo.getTypeByName(typeName).typeId) as ArrayList<Item>
			}
			else
			{
				itemRepo.getAllItem() as ArrayList<Item>
			}

			for(item in itemList)
			{
				item.imageString = imageRepo.getImage(item.imageId).imageString
			}

		return itemList
	}

	fun getTargetItem(id: Int): Item
	{
		var targetItem = itemRepo.getItem(id)

		targetItem.imageString = imageRepo.getImage(targetItem.imageId).imageString

		return targetItem
	}

	fun searchItem(itemName: String, order: String): ArrayList<Item>
	{
		var itemList: ArrayList<Item> =
			if (order.equals("name", true))
			{
				itemRepo.searchItemOrderName("%${itemName}%") as ArrayList<Item>
			}
			else
			{
				itemRepo.searchItemOrderQuantity("%${itemName}%") as ArrayList<Item>
			}

			for(item in itemList)
			{
				item.imageString = imageRepo.getImage(item.imageId).imageString
			}

		return itemList

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
		val targetImage = ItemImage(targetItem.imageId, targetItem.imageString)
		imageRepo.update(targetImage)

		targetImage.imageString = ""
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
		newItem.imageId = imageRepo.insert(ItemImage(0, newItem.imageString)).toInt()
		newItem.imageString = ""

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
		val tempImageId = itemRepo.getItem(targetId).imageId

		itemRepo.removeItem(targetId)
		(guiClass as HomeActivity).changeItemCallback()

		try
		{
			if (imageRepo.getImage(tempImageId) == null)
			{
				Log.i("Cascade delete","----------------------- Success ==================")
			}
			else
			{
				Log.i("Cascade delete","----------------------- faill ==================")
			}

		}
		catch (e: Exception)
		{
			Log.i("Cascade delete","----------------------- error ==================")
		}
	}
}