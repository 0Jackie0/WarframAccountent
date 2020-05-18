package com.example.warframeaccountant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import database.DatabaseConnection
import domin.Type
import function.MainFunction

class MainActivity : AppCompatActivity()
{
	private lateinit var mainFunction: MainFunction
	private var dataChecked = false

	fun gotoMainPage (image: View)
	{
		if(dataChecked)
		{
			val newIntent = Intent(this, HomeActivity::class.java)
			startActivity(newIntent)

			this.finish()
		}
		else
		{
			Toast.makeText(this, "--Please wait--", Toast.LENGTH_SHORT).show()
		}
	}

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		mainFunction = MainFunction(this)

		setupFromServer()
	}

	private fun setupFromServer ()
	{
		mainFunction.setupLocalDatabase()
	}

	fun activeButton ()
	{
		dataChecked = true
	}

	fun failedToInit()
	{
		Toast.makeText(this, "--Failed to start the app--", Toast.LENGTH_LONG).show()
		this.finish()
	}

//	private fun setup ()
//	{
//		val typeRepo = DatabaseConnection(this).getTypeRepo()
//		if (typeRepo.getAllType().isEmpty())
//		{
//			typeRepo.insert(Type(0,"Warframe - Blueprint"))
//			typeRepo.insert(Type(0,"Warframe - Parts"))
//			typeRepo.insert(Type(0,"Primary - Blueprint"))
//			typeRepo.insert(Type(0,"Primary - Parts"))
//			typeRepo.insert(Type(0,"Secondary - Blueprint"))
//			typeRepo.insert(Type(0,"Secondary - Parts"))
//			typeRepo.insert(Type(0,"Melee - Blueprint"))
//			typeRepo.insert(Type(0,"Melee - Parts"))
//		}
//	}
}
