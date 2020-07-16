package database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import domin.Item
import domin.ItemImage
import domin.Type

@Database(version = 1, entities = [Item::class, Type::class, ItemImage::class])
abstract class DatabaseConnection : RoomDatabase()
{
	abstract fun getItemRepo(): ItemRepo

	abstract fun getTypeRepo(): TypeRepo

	abstract fun getImageRepo(): ImageRepo

	companion object
	{
		@Volatile
		private var instance: DatabaseConnection? = null
		private val LOCK = Any()

		operator fun invoke(context: Context) = instance ?: synchronized(LOCK)
		{
			instance?: buildDatabase(context).also { instance = it }
		}

		private fun buildDatabase(context: Context) = Room.databaseBuilder(context, DatabaseConnection::class.java, "Warframe_accountant.db").allowMainThreadQueries().build()
	}
}