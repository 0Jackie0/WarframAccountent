package domin

class Item (itemId: Int, itemName: String, itemNumber: Int = 0, itemEPrice: Int = 0, itemBPrice: Int = 0)
{
	var id: Int = itemId
		private set(newId)
		{
			id = newId
		}

	var name: String = itemName
	var number: Int = itemNumber
	var ePrice: Int = itemEPrice
	var bPrice: Int = itemBPrice

	constructor() : this(0, "")

	constructor(newName: String) : this(0, newName)

	constructor(newName: String, itemNumber: Int, itemEPrice: Int, itemBPrice: Int) : this(0, newName, itemNumber, itemEPrice, itemBPrice)


	/**
	 * This method will check the input price is lower than base price or not
	 */
	fun checkPrice(targetPrice: Int) : Boolean
	{
		if (targetPrice < bPrice)
		{
			return false
		}
		return true
	}

}