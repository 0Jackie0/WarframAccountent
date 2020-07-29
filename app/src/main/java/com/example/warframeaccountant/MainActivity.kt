package com.example.warframeaccountant

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import function.MainFunction

class MainActivity : AppCompatActivity()
{
	private val WELLCOME_VIDEO_URL = "http://192.168.1.52:28590/api/video/resource"

	private lateinit var mainFunction: MainFunction
	private var dataChecked = false

	private lateinit var videoContainer: VideoView
	private lateinit var videoController: MediaController

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


		setupVideo();
		setupFromServer()
	}

	private fun setupVideo ()
	{
		videoController = MediaController(this)

		videoContainer = findViewById(R.id.wellcomVIdioContainer)
		videoController.setAnchorView(videoContainer)


		videoContainer.setVideoURI(Uri.parse(WELLCOME_VIDEO_URL))
		videoContainer.setMediaController(videoController)


		videoContainer.setOnPreparedListener {mp: MediaPlayer ->
			mp.isLooping = true
		}

		videoContainer.start()
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

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	override fun onStop()
	{
		videoContainer.stopPlayback();

		super.onStop()
	}
}
