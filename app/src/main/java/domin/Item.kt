package domin

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "item")
@ForeignKey(entity = Type::class, parentColumns = ["typeId"], childColumns = ["type"], onDelete = CASCADE)
//@ForeignKey(entity = ItemImage::class, parentColumns = ["imageId"], childColumns = ["imageId"], onDelete = CASCADE)
class Item (
		@PrimaryKey(autoGenerate = false)
		var itemId: Int = 0,

		@ColumnInfo(name = "name")
		var name: String,

		@ColumnInfo(name = "type")
		var type: Int,

		@ColumnInfo(name = "quantity")
		var number: Int = 0,
		@ColumnInfo(name = "expected_price")
		var ePrice: Int = 0,
		@ColumnInfo(name = "bass_price")
		var bPrice: Int = 0,

//		@ColumnInfo(name = "image")
//		var imageString: String = "",

		@ColumnInfo(name = "imageId")
		var imageId: Int
	)
{
	var imageString: String = ""
	constructor(): this(0, "", 0, 0, 0, 0, 0)
}