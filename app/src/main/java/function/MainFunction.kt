package function

import android.content.Context
import com.example.warframeaccountant.MainActivity
import database.DatabaseConnection
import domin.Item
import domin.Type
import server.Communication

class MainFunction(guiContext: Context)
{
	private val guiCass = guiContext
	private val typeRepo = DatabaseConnection(guiContext).getTypeRepo()
	private val itemRepo = DatabaseConnection(guiContext).getItemRepo()

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
			serverCommunication.pushAllItem(this, itemRepo.getAllItem())
		}
	}

	fun itemListCallback(itemList: ArrayList<Item>)
	{
		for (item in itemList)
		{
			itemRepo.insert(item)
		}
		(guiCass as MainActivity).activeButton()
	}

	fun itemTypeListCallback(itemList: ArrayList<Type>, success: Boolean)
	{
		if(success)
		{
			for (type in itemList)
			{
				typeRepo.insert(type)
			}
			serverCommunication.getItemList(this)
		}
		else
		{
			(guiCass as MainActivity).failedToInit()
		}
	}

	fun itemTypeUpdateCallback(success: Boolean)
	{
		val serverCommunicationFile = guiCass.getSharedPreferences("com.example.warframeaccountant", Context.MODE_PRIVATE)
		serverCommunicationFile.edit().putBoolean("Mode", success).apply()
		(guiCass as MainActivity).activeButton()
	}

	fun pushAllItemCallback (success: Boolean)
	{
		val serverCommunicationFile = guiCass.getSharedPreferences("com.example.warframeaccountant", Context.MODE_PRIVATE)

		(guiCass as MainActivity).activeButton()

		if(success)
		{
			serverCommunicationFile.edit().putBoolean("Mode", true).apply()
		}
		else
		{
			serverCommunicationFile.edit().putBoolean("Mode", false).apply()
		}
	}
}