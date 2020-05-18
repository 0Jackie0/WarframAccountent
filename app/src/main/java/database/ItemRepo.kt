package database

import androidx.room.*
import domin.Item

@Dao
interface ItemRepo
{
	@Query("Select * from item")
	fun getAllItem(): List<Item>
	@Query("Select * from item ORDER BY name")
	fun getAllItemOrderName(): List<Item>
	@Query("Select * from item ORDER BY quantity DESC")
	fun getAllItemOrderQuantity(): List<Item>

	@Query("Select * from item where name like :itemName ORDER BY name")
	fun searchItemOrderName(itemName: String): List<Item>
	@Query("Select * from item where name like :itemName ORDER BY quantity DESC")
	fun searchItemOrderQuantity(itemName: String): List<Item>

	@Query("Select * from item where itemId = :id")
	fun getItem(id: Int): Item

	@Query("Select * from item where type = :typeId ORDER BY name")
	fun getFilterItemName(typeId: Int): List<Item>
	@Query("Select * from item where type = :typeId ORDER BY quantity DESC")
	fun getFilterItemQuantity(typeId: Int): List<Item>

	@Query("Delete from item where itemId = :id")
	fun removeItem(id: Int)

	@Query("Update item set quantity=quantity+:amount where itemId = :id")
	fun changeItemQuantity(id: Int, amount: Int)

	@Insert
	fun insert(vararg newItem:Item)

	@Update
	fun update(vararg targetItem:Item)

	@Delete
	fun delete(vararg targetItem:Item)
}