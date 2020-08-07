package function

import android.content.Context
import android.content.pm.PackageManager
import com.example.warframeaccountant.AddActivity
import database.DatabaseConnection
import domin.Item
import domin.Type

class AddFunction(guiClassContext: Context)
{
	private var guiClass = guiClassContext
	private val itemRepo = DatabaseConnection(guiClass).getItemRepo()
	private val typeRepo = DatabaseConnection(guiClass).getTypeRepo()
	private val imageRepo = DatabaseConnection(guiClass).getImageRepo()


	fun getTypeList(): List<Type>
	{
		return typeRepo.getAllType()
	}

	fun getTargetType(typeName: String): Type
	{
		return typeRepo.getTypeByName(typeName)
	}

	fun getTargetItem(id: Int): Item
	{
		var targetItem = itemRepo.getItem(id)

		targetItem.imageString = imageRepo.getImage(targetItem.imageId).imageString

		return targetItem
	}
}