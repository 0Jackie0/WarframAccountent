package com.example.warframeaccountant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

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

//		Log.i("error mark", "-+-+-+-+-+-+-+-+-+-+-+-+-")
	}
}
