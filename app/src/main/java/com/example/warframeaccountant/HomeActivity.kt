package com.example.warframeaccountant

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import domin.Item
import function.HomeFunction
import function.RecycleAdapter
import kotlinx.android.synthetic.main.activity_home.*
import kotlin.math.max
import kotlin.math.min


class HomeActivity : AppCompatActivity()
{
	//Add page request code
	private val ADD_PAGE_REQUEST_CODE = 1
	//Float button drag base length
	private val CLICK_DRAG_TOLERANCE = 10f
	private val BACK_BUTTON_DURATION = 2 * 1000

	//Float button location
	private var downRawX = 0f
	private var downRawY: Float = 0f
	private var dX = 0f
	private var dY: Float = 0f

	//All Item text
	private val ALL_ITEM_TEXT = "- - -"
	private lateinit var homeFunction: HomeFunction

	private var order: Array<String> = arrayOf("- - -", "Name")

	private lateinit var recycleListManager: RecycleAdapter

	private var backButtonPressTime: Long = 0L
	private lateinit var backButtonMessage: Toast

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	override fun onBackPressed()
	{
		if (backButtonPressTime + BACK_BUTTON_DURATION > System.currentTimeMillis())
		{
			backButtonMessage.cancel()
			homeFunction.updateAllItemToServer()
		}
		else
		{
			backButtonPressTime = System.currentTimeMillis()
			backButtonMessage = Toast.makeText(this, "Press back again to save and exit", Toast.LENGTH_SHORT)
			backButtonMessage.show()
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private fun addItem(addButton: View)
	{
		val newIntent = Intent(this, AddActivity::class.java)

		newIntent.putExtra("edit", false)
		startActivityForResult(newIntent, ADD_PAGE_REQUEST_CODE)
	}

	fun searchItem(searchButton: View)
	{
		val textBox = findViewById<EditText>(R.id.searchInput)

		setup(homeFunction.searchItem(textBox.text.toString(), order[1]))

		textBox.setText("")
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_home)

		val serverCommunicationFile = this.getSharedPreferences("com.example.warframeaccountant", Context.MODE_PRIVATE)
		homeFunction = HomeFunction(this, serverCommunicationFile.getBoolean("Mode", false))
		Log.i("App Mode", "---------${serverCommunicationFile.getBoolean("Mode", false)}--------")

		setupDropdownList()
		setupFloatingButton()
		setup(homeFunction.getAllItemList())
	}

	private fun setup(itemList: ArrayList<Item>)
	{
		recycleListManager = RecycleAdapter(this, itemList)

		itemListArea.adapter = recycleListManager
		itemListArea.layoutManager = LinearLayoutManager(this)

		getSumPrice(itemList)
		getSumQuantity(itemList)
	}

	private fun getSumPrice(itemList: ArrayList<Item>)
	{
		var sum = 0
		for (item in itemList)
		{
			sum += item.ePrice * item.number
		}
		totalPriceText.text = sum.toString()
	}

	private fun getSumQuantity(itemList: ArrayList<Item>)
	{
		var sum = 0
		for (item in itemList)
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
		filterDropdownList.adapter = ArrayAdapter<String>(
			this,
			android.R.layout.simple_spinner_dropdown_item,
			filterTypeContent
		)

		filterDropdownList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
		{
			override fun onNothingSelected(parent: AdapterView<*>?)
			{

			}

			override fun onItemSelected(
					parent: AdapterView<*>?,
					view: View?,
					position: Int,
					id: Long
			)
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
		orderDropdownList.adapter =
			ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, orderContent)

		orderDropdownList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
		{
			override fun onNothingSelected(parent: AdapterView<*>?)
			{

			}

			override fun onItemSelected(
					parent: AdapterView<*>?,
					view: View?,
					position: Int,
					id: Long
			)
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

	private fun setupFloatingButton()
	{
		val floatingButton = findViewById<FloatingActionButton>(R.id.addItemButton)

		floatingButton.setOnTouchListener { view: View, motionEvent: MotionEvent ->
			val layoutParams: ViewGroup.MarginLayoutParams =
				view.layoutParams as ViewGroup.MarginLayoutParams
			val action: Int = motionEvent.action
			if (action == MotionEvent.ACTION_DOWN)
			{
				downRawX = motionEvent.rawX
				downRawY = motionEvent.rawY
				dX = view.x - downRawX
				dY = view.y - downRawY
				true // Consumed
			}
			else if (action == MotionEvent.ACTION_MOVE)
			{
				val viewWidth: Int = view.width
				val viewHeight: Int = view.height
				val viewParent = view.parent as View
				val parentWidth = viewParent.width
				val parentHeight = viewParent.height
				var newX: Float = motionEvent.rawX + dX

				newX = Math.max(
					layoutParams.leftMargin.toFloat(),
					newX
				) // Don't allow the FAB past the left hand side of the parent
				newX = Math.min(
					parentWidth - viewWidth - layoutParams.rightMargin.toFloat(),
					newX
				) // Don't allow the FAB past the right hand side of the parent

				var newY: Float = motionEvent.rawY + dY
				newY = max(
					layoutParams.topMargin.toFloat(),
					newY
				) // Don't allow the FAB past the top of the parent
				newY = min(
					parentHeight - viewHeight - layoutParams.bottomMargin.toFloat(),
					newY
				) // Don't allow the FAB past the bottom of the parent
				view.animate().x(newX).y(newY).setDuration(0).start()
				true // Consumed
			}
			else if (action == MotionEvent.ACTION_UP)
			{
				val upRawX: Float = motionEvent.getRawX()
				val upRawY: Float = motionEvent.getRawY()
				val upDX: Float = upRawX - downRawX
				val upDY: Float = upRawY - downRawY
				if (Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE)
				{ // A click
					addItem(view)
					true
				}
				else
				{ // A drag
					true // Consumed
				}
			}
			else
			{
				super.onTouchEvent(motionEvent)
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
				newItem.imageString = data.getStringExtra("imageString")

//				Log.i("Image String", newItem.imageString)

				homeFunction.addItem(newItem)
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
				targetItem.imageString = data.getStringExtra("imageString")

				homeFunction.editItem(targetItem)

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

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	fun changeItemCallback()
	{
		if (order[0].equals(ALL_ITEM_TEXT, false))
		{
			setup(homeFunction.getAllItemList(order[1]))
		}
		else
		{
			setup(homeFunction.getFilterItemList(order[0], order[1]))
		}
	}

	fun updateAllItemCallback (success: Boolean)
	{
		if (success)
		{
			super.onBackPressed()
		}
		else
		{
			Toast.makeText(this, "Upload to server failed, all data saved in local file", Toast.LENGTH_LONG).show()
			super.onBackPressed()
		}
	}
}
