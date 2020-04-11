package function

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.warframeaccountant.HomeActivity
import com.example.warframeaccountant.R
import domin.Item

//: RecyclerView.Adapter<>
class RecycleAdapter(guiClassContext: Context, newItemList: ArrayList<Item>) : RecyclerView.Adapter<RecycleAdapter.ItemViewHolder>()
{
	val itemList = newItemList

	private val guiClass : Context = guiClassContext

	/**
	 * Called when RecyclerView needs a new [ViewHolder] of the given type to represent
	 * an item.
	 *
	 *
	 * This new ViewHolder should be constructed with a new View that can represent the items
	 * of the given type. You can either create a new View manually or inflate it from an XML
	 * layout file.
	 *
	 *
	 * The new ViewHolder will be used to display items of the adapter using
	 * [.onBindViewHolder]. Since it will be re-used to display
	 * different items in the data set, it is a good idea to cache references to sub views of
	 * the View to avoid unnecessary [View.findViewById] calls.
	 *
	 * @param parent The ViewGroup into which the new View will be added after it is bound to
	 * an adapter position.
	 * @param viewType The view type of the new View.
	 *
	 * @return A new ViewHolder that holds a View of the given view type.
	 * @see .getItemViewType
	 * @see .onBindViewHolder
	 */
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder
	{
		val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)

		return ItemViewHolder(view)
	}

	/**
	 * Returns the total number of items in the data set held by the adapter.
	 *
	 * @return The total number of items in this adapter.
	 */
	override fun getItemCount(): Int
	{
		return itemList.size
	}

	/**
	 * Called by RecyclerView to display the data at the specified position. This method should
	 * update the contents of the [ViewHolder.itemView] to reflect the item at the given
	 * position.
	 *
	 *
	 * Note that unlike [android.widget.ListView], RecyclerView will not call this method
	 * again if the position of the item changes in the data set unless the item itself is
	 * invalidated or the new position cannot be determined. For this reason, you should only
	 * use the `position` parameter while acquiring the related data item inside
	 * this method and should not keep a copy of it. If you need the position of an item later
	 * on (e.g. in a click listener), use [ViewHolder.getAdapterPosition] which will
	 * have the updated adapter position.
	 *
	 * Override [.onBindViewHolder] instead if Adapter can
	 * handle efficient partial bind.
	 *
	 * @param holder The ViewHolder which should be updated to represent the contents of the
	 * item at the given position in the data set.
	 * @param position The position of the item within the adapter's data set.
	 */
	override fun onBindViewHolder(holder: ItemViewHolder, position: Int)
	{
		holder.itemName.text = itemList[position].name
		holder.itemBPrice.text = itemList[position].bPrice.toString()
		holder.itemEPrice.text = itemList[position].ePrice.toString()
		holder.itemQuantity.text = itemList[position].number.toString()

		holder.itemAdd.setOnClickListener{
			addFunction(holder, position)
		}


		holder.itemRemove.setOnClickListener{
			removeFunction(holder, position)
		}
	}

	private fun addFunction (holder: ItemViewHolder, position: Int)
	{
		var quantity: Int = holder.itemQuantity.text.toString().toInt()
		quantity ++
		holder.itemQuantity.text = quantity.toString()

		itemList[position].number = quantity

		(guiClass as HomeActivity).updateSumPrice(itemList[position].ePrice)
		(guiClass as HomeActivity).updateSumQuantity(1)
	}

	private fun removeFunction (holder: ItemViewHolder, position: Int)
	{
		var quantity: Int = holder.itemQuantity.text.toString().toInt()
		if (quantity - 1 >= 0)
		{
			quantity --
			holder.itemQuantity.text = quantity.toString()

			itemList[position].number = quantity

			(guiClass as HomeActivity).updateSumPrice(itemList[position].ePrice)
			(guiClass as HomeActivity).updateSumQuantity(-1)
		}
		else
		{
			Toast.makeText(guiClass, "Empty!", Toast.LENGTH_SHORT).show()
		}
	}


	class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	{
		val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
		val itemName: TextView = itemView.findViewById(R.id.itemName)
		val itemQuantity: TextView = itemView.findViewById(R.id.quantityText)
		val itemEPrice: TextView = itemView.findViewById(R.id.expectedPriceText)
		val itemBPrice: TextView = itemView.findViewById(R.id.basePriceText)
		val itemAdd: Button = itemView.findViewById(R.id.increaseButton)
		val itemRemove: Button = itemView.findViewById(R.id.decreaseButton)
	}
}


