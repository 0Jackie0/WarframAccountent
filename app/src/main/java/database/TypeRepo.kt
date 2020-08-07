package database

import androidx.room.*
import domin.Item
import domin.Type

@Dao
interface TypeRepo
{
	@Query("Select * from type")
	fun getAllType(): List<Type>

	@Query("Select * from type where typeId = :id")
	fun getTypeById(id: String): Type

	@Query("Select * from type where type_name = :name")
	fun getTypeByName(name: String): Type

	@Query("Delete from type")
	fun cleanTable()

	@Insert
	fun insert(vararg newType: Type)

	@Update
	fun update(vararg targetType: Type)

	@Delete
	fun delete(targetType: Type)
}