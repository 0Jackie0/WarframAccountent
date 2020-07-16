package server

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import domin.Item
import domin.Type
import function.HomeFunction
import function.MainFunction
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class Communication(guiContext: Context)
{
//	private val SERVER_URL = "http://10.0.2.2:28590/api/"
	private val SERVER_URL = "http://192.168.1.52:28590/api/"

	private val requestQueue = Volley.newRequestQueue(guiContext)


	fun getItemList(callerClass: MainFunction)
	{
		val itemListRequest =
			JsonArrayRequest(Request.Method.GET, "${SERVER_URL}item", null, Response.Listener { response ->
				try
				{
//					Log.i("Item List", "${response.toString()}")
					val itemArray = ArrayList<Item>()

					for (index in 0 until response.length())
					{
						val jsonObject = response.getJSONObject(index)
						val newItem = Item();

						newItem.itemId = jsonObject.getInt("itemId")
						newItem.type = jsonObject.getInt("type")
						newItem.number = jsonObject.getInt("quantity")
						newItem.name = jsonObject.getString("name")
						newItem.ePrice = jsonObject.getInt("eprice")
						newItem.bPrice = jsonObject.getInt("bprice")
						newItem.imageString = jsonObject.getString("imageString")

						itemArray.add(newItem)
					}

					callerClass.itemListCallback(itemArray)
				}
				catch (e: JSONException)
				{
					e.printStackTrace()
				}
			}, Response.ErrorListener { error -> error.printStackTrace() })

		requestQueue.add(itemListRequest)
	}

	fun getItemType(callerClass: MainFunction, firstLauch: Boolean)
	{
		val typeListRequest =
			JsonArrayRequest(Request.Method.GET, "${SERVER_URL}type", null, Response.Listener { response ->
				try
				{
//					Log.i("Type List", "${response.toString()}")

					val typeArray = ArrayList<Type>()

					for (index in 0 until response.length())
					{
						val jsonObject = response.getJSONObject(index)
						val newType = Type();

						newType.typeId = jsonObject.getInt("typeId")
						newType.name = jsonObject.getString("typeName")

						typeArray.add(newType)
					}

					if (firstLauch)
					{
						callerClass.itemTypeListCallback(typeArray, true)
					}
					else
					{
						callerClass.itemTypeUpdateCallback(true)
					}
				}
				catch (e: JSONException)
				{
					e.printStackTrace()
					if (firstLauch)
					{
						callerClass.itemTypeListCallback(ArrayList<Type>(), false)
					}
					else
					{
						callerClass.itemTypeUpdateCallback(false)
					}
				}
			}, Response.ErrorListener { error ->
				error.printStackTrace()
				if (firstLauch)
				{
					callerClass.itemTypeListCallback(ArrayList<Type>(), false)
				}
				else
				{
					callerClass.itemTypeUpdateCallback(false)
				}
			})

		requestQueue.add(typeListRequest)
	}

	fun addItem(callerClass: HomeFunction, newItem: Item)
	{
		val newJson = JSONObject()
		try
		{
			newJson.put("itemId", newItem.itemId)
			newJson.put("type", newItem.type)
			newJson.put("quantity", newItem.number)
			newJson.put("name", newItem.name)
			newJson.put("eprice", newItem.ePrice)
			newJson.put("bprice", newItem.bPrice)
			newJson.put("imageString", newItem.imageString)
		}
		catch (e: JSONException)
		{
			e.printStackTrace()
		}

		val addNewItemRequest =
			JsonObjectRequest(Request.Method.POST, "${SERVER_URL}item/new", newJson, Response.Listener { response ->
				try
				{
					newItem.itemId = response.getInt("itemId")
					callerClass.addItemCallback(newItem)
				}
				catch (e: JSONException)
				{
					e.printStackTrace()
				}
			},
				Response.ErrorListener { error -> error.printStackTrace() }
			)

		requestQueue.add(addNewItemRequest)
	}

	fun updateAllItem(callerClass: HomeFunction, ItemList: List<Item>)
	{
		val itemListJson = JSONObject()
		try
		{
			val itemListJsonArray = JSONArray()
			for (item in ItemList)
			{
				val newJson = JSONObject()
				newJson.put("itemId", item.itemId)
				newJson.put("type", item.type)
				newJson.put("quantity", item.number)
				newJson.put("name", item.name)
				newJson.put("eprice", item.ePrice)
				newJson.put("bprice", item.bPrice)
				newJson.put("imageString", item.imageString)

				itemListJsonArray.put(newJson)
			}

			itemListJson.put("itemList", itemListJsonArray)
		}
		catch (e: JSONException)
		{
			e.printStackTrace()
		}

		val updateAllItemRequest =
			JsonObjectRequest(Request.Method.PUT, "${SERVER_URL}item/all", itemListJson, Response.Listener { response ->
				if (response.getString("result").equals("success", false))
				{
					callerClass.updateAllItemCallback(true)
				}
				else
				{
					callerClass.updateAllItemCallback(false)
				}
			},
				Response.ErrorListener { error ->
					error.printStackTrace()
					callerClass.updateAllItemCallback(false)
				}
			)

		requestQueue.add(updateAllItemRequest)
	}

	fun removeItem(callerClass: HomeFunction, targetId: Int)
	{
		val removeItemRequest =
			JsonObjectRequest(Request.Method.DELETE, "${SERVER_URL}item/remove/$targetId", null, Response.Listener { response ->

				callerClass.deleteItemCallback(targetId)
			},
				Response.ErrorListener { error -> error.printStackTrace() })

		requestQueue.add(removeItemRequest)
	}

	fun pushAllItem(callerClass: MainFunction, ItemList: List<Item>)
	{
		val itemListJson = JSONObject()
		try
		{
			val itemListJsonArray = JSONArray()
			for (item in ItemList)
			{
				val newJson = JSONObject()
				newJson.put("itemId", item.itemId)
				newJson.put("type", item.type)
				newJson.put("quantity", item.number)
				newJson.put("name", item.name)
				newJson.put("eprice", item.ePrice)
				newJson.put("bprice", item.bPrice)
				newJson.put("imageString", item.imageString)

				itemListJsonArray.put(newJson)
			}

			itemListJson.put("itemList", itemListJsonArray)
		}
		catch (e: JSONException)
		{
			e.printStackTrace()
		}

		val updateAllItemRequest =
			JsonObjectRequest(Request.Method.PUT, "${SERVER_URL}item/all", itemListJson, Response.Listener { response ->
				if (response.getString("result").equals("success", false))
				{
					callerClass.pushAllItemCallback(true)
				}
				else
				{
					callerClass.pushAllItemCallback(false)
				}
			},
				Response.ErrorListener { error ->
					error.printStackTrace()
					callerClass.pushAllItemCallback(false)
				}
			)

		requestQueue.add(updateAllItemRequest)
	}
}