package domin

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "itemImage")
@ForeignKey(entity = Item::class, parentColumns = ["imageId"], childColumns = ["imageId"], onDelete = ForeignKey.CASCADE)
class ItemImage (
		@PrimaryKey(autoGenerate = true)
		var imageId: Int = 0,

		@ColumnInfo(name = "imageString")
		var imageString: String = ""
)
{

}