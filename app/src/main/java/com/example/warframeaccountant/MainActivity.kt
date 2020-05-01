package com.example.warframeaccountant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import database.DatabaseConnection
import domin.Type

class MainActivity : AppCompatActivity()
{
	fun gotoMainPage (image: View)
	{
		val newIntent = Intent(this, HomeActivity::class.java)
		startActivity(newIntent)

		this.finish()
	}

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		DatabaseConnection(this)
		setup()
	}

	private fun setup ()
	{
//		val typeRepo = DatabaseConnection(this).getTypeRepo()
//		typeRepo.cleanTable()
//
//		typeRepo.insert(Type(0,"Warframe - Blueprint"))
//		typeRepo.insert(Type(0,"Warframe - Parts"))
//		typeRepo.insert(Type(0,"Primary - Blueprint"))
//		typeRepo.insert(Type(0,"Primary - Parts"))
//		typeRepo.insert(Type(0,"Secondary - Blueprint"))
//		typeRepo.insert(Type(0,"Secondary - Parts"))
//		typeRepo.insert(Type(0,"Melee - Blueprint"))
//		typeRepo.insert(Type(0,"Melee - Parts"))
	}
}
