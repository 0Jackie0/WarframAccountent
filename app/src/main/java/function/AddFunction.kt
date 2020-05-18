package function

import android.content.Context
import android.content.pm.PackageManager
import com.example.warframeaccountant.AddActivity
import database.DatabaseConnection
import domin.Type

class AddFunction(guiClassContext: Context)
{
	private var guiClass = guiClassContext
	private val typeRepo = DatabaseConnection(guiClass).getTypeRepo()

	fun getTypeList(): List<Type>
	{
		return typeRepo.getAllType()
	}

	fun getTargetType(typeName: String): Type
	{
		return typeRepo.getTypeByName(typeName)
	}
}