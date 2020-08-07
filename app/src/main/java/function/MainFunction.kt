package function

import android.content.Context
import com.example.warframeaccountant.MainActivity
import database.DatabaseConnection
import domin.Item
import domin.ItemImage
import domin.Type
import server.Communication

class MainFunction(guiContext: Context)
{
	private val guiCass = guiContext
	private val typeRepo = DatabaseConnection(guiContext).getTypeRepo()
	private val itemRepo = DatabaseConnection(guiContext).getItemRepo()
	private val imageRepo = DatabaseConnection(guiContext).getImageRepo()

	private val serverCommunication = Communication(guiContext)

	fun setupLocalDatabase()
	{
		val serverCommunicationFile = guiCass.getSharedPreferences("com.example.warframeaccountant", Context.MODE_PRIVATE)


		if (serverCommunicationFile.getBoolean("sync", true))
		{
			if(typeRepo.getAllType().isEmpty())
			{
				serverCommunication.getItemType(this, true)
			}
			else
			{
				serverCommunication.getItemType(this, false)
			}
		}
		else
		{
			serverCommunication.pushAllItem(this, getAllItemList())
		}
	}
	private fun getAllItemList(order: String = ""): ArrayList<Item>
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

	fun itemListCallback(itemList: ArrayList<Item>, isFirstLaugh: Boolean)
	{
		if(!isFirstLaugh)
		{
			imageRepo.clearTable()
			itemRepo.clearTable()
		}

		for (item in itemList)
		{
			item.imageId = imageRepo.insert(ItemImage(0, item.imageString)).toInt()
			item.imageString = ""

			itemRepo.insert(item)
		}
		(guiCass as MainActivity).activeButton()
	}
	fun itemTypeListCallback(itemList: ArrayList<Type>, success: Boolean)
	{
		val serverCommunicationFile = guiCass.getSharedPreferences("com.example.warframeaccountant", Context.MODE_PRIVATE)
		serverCommunicationFile.edit().putBoolean("Mode", success).apply()

		if(success)
		{
			for (type in itemList)
			{
				typeRepo.insert(type)
			}
			serverCommunication.getItemList(this, true)
		}
		else
		{
			(guiCass as MainActivity).failedToInit()
		}
	}

	fun itemTypeUpdateCallback(newTypeLise: ArrayList<Type>,success: Boolean)
	{
		val serverCommunicationFile = guiCass.getSharedPreferences("com.example.warframeaccountant", Context.MODE_PRIVATE)
		serverCommunicationFile.edit().putBoolean("Mode", success).apply()

		if(success)
		{
			for(newType in newTypeLise)
			{
				val oldType = typeRepo.getTypeById(newType.typeId.toString())

				if(oldType == null)
				{
					typeRepo.insert(newType)
				}
				else
				{
					oldType.name = newType.name
					typeRepo.update(oldType)
				}
			}
			serverCommunication.getItemList(this, false)
		}
	}

	fun pushAllItemCallback (success: Boolean)
	{
		val serverCommunicationFile = guiCass.getSharedPreferences("com.example.warframeaccountant", Context.MODE_PRIVATE)

		if(success)
		{
			serverCommunicationFile.edit().putBoolean("Mode", true).apply()
		}
		else
		{
			serverCommunicationFile.edit().putBoolean("Mode", false).apply()
		}

		(guiCass as MainActivity).activeButton()
	}
}