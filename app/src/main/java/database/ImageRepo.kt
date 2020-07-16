package database

import androidx.room.*
import domin.ItemImage

@Dao
interface ImageRepo
{
	@Query("Select * from itemImage Where imageId=:imageId")
	fun getImage (imageId: Int): ItemImage

	@Query("Delete from itemImage where imageId = :id")
	fun removeImage(id: Int)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(newImage: ItemImage): Long

	@Update
	fun update(vararg targetImage: ItemImage)

	@Delete
	fun delete(vararg targetImage: ItemImage)
}