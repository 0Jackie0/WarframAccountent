package domin

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "type")
class Type (
		@PrimaryKey(autoGenerate = true)
		var typeId: Int,

		@ColumnInfo(name = "type_name")
		var name: String
	)
{
}