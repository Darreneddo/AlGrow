package com.thebeandean.algrow

import android.app.*
import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import com.thebeandean.algrow.databinding.CultureMainBinding
import java.util.*
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL


class CultureActivity : AppCompatActivity() {
    private lateinit var binding: CultureMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setting content view using XML file
        setContentView(R.layout.culture_main)
        binding = CultureMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //if android version of mobile is equal to or newer than version "Oreo"
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        //finding relative objects in corresponding XML file using ID's and assigning them to variables
        val returnButton = findViewById<Button>(R.id.returnButton2)
        val pHseekBar = findViewById<SeekBar>(R.id.PHSeekBar)
        val turbSeekBar = findViewById<SeekBar>(R.id.turbSeekBar)
        val tempSeekBar = findViewById<SeekBar>(R.id.tempSeekBar)

        //seekBars will only process int values, therefor all values get multiplied by 100 before converted from double to int to avoid any loss or inaccuracy of data
        //URLs for each files location on github are assigned to variables and connections opened
        val pHurl = URL("https://api.github.com/repos/Darreneddo/Diss_Proto_Sens/pHFile.txt")
        val pHconnection = pHurl.openConnection()
        //BufferedReader is called to get the input stream from the file and read it to the app to be assigned to the slider progress.
        BufferedReader(InputStreamReader(pHconnection.getInputStream())).use { inp ->
            var line: String?
            while (inp.readLine().also { line = it } != null) {
                val pHDouble = (line?.toDoubleOrNull()?.times(100))
                pHseekBar.progress = pHDouble!!.toInt()
            }
        }
        //seekBar will only process int values, therefor all values get multiplied by 100 before converted from double to int to avoid any loss or inaccuracy of data
        val turbUrl = URL("https://github.com/Darreneddo/Diss_Proto_Sens/blob/ad5d2630e82a02c50b547fbb22fad1c459111d04/turbFile.txt")
        val turbConnection = turbUrl.openConnection()
        BufferedReader(InputStreamReader(turbConnection.getInputStream())).use { inp ->
            var lin: String?
            while (inp.readLine().also { lin = it } != null) {
                val turbDouble = (lin?.toDoubleOrNull()?.times(100))
                turbSeekBar.progress = turbDouble!!.toInt()
            }
        }
        //seekBar will only process int values, therefor all values get multiplied by 100 before converted from double to int to avoid any loss or inaccuracy of data
        val tempUrl = URL("https://github.com/Darreneddo/Diss_Proto_Sens/blob/ad5d2630e82a02c50b547fbb22fad1c459111d04/tempFile.txt")
        val tempConnection = tempUrl.openConnection()
        BufferedReader(InputStreamReader(tempConnection.getInputStream())).use { inp ->
            var l: String?
            while (inp.readLine().also { l = it } != null) {
                val tempDouble = (l?.toDoubleOrNull()?.times(100))
                tempSeekBar.progress = tempDouble!!.toInt().and(127)
            }
        }


        //Pushes alert and notification for turbidity issue
        if (turbSeekBar.progress >= 100000)
        {
            val intent = Intent(applicationContext, Notification::class.java)
            val title = binding.textView9.text.toString()
            val message = "Culture Medium turbidity level too high. If due to algal growth please harvest algae from culture from area of growth origin. If due to over-addition of nutrient solution, please remove some of the medium and add fresh water to replace and dilute."
            intent.putExtra(titleExtra, title)
            intent.putExtra(messageExtra, message)
            showAlert(title, message)
            showNotification(title, message)
        }

        //Pushes alert and notification for temperature issues
        if (tempSeekBar.progress >= 2150 || tempSeekBar.progress <= 1600)
        {
            val intent = Intent(applicationContext, Notification::class.java)
            val title = binding.textView8.text.toString()
            val message = "Culture Medium temperature outside of recommended safe range. Please check temperature of culture medium and adjust environment accordingly."
            intent.putExtra(titleExtra, title)
            intent.putExtra(messageExtra, message)
            showAlert(title, message)
            showNotification(title, message)
        }

        //Pushes alert and notification for pH issues
        if (pHseekBar.progress >= 850 || pHseekBar.progress <= 550)
        {
            val intent = Intent(applicationContext, Notification::class.java)
            val title = binding.textView7.text.toString()
            val message = "Culture Medium pH level outside of recommended safe range. Please separate culture into new medium asap."
            intent.putExtra(titleExtra, title)
            intent.putExtra(messageExtra, message)
            showAlert(title, message)
            showNotification(title, message)
        }

        //Set actions for return button on screen
        returnButton.setOnClickListener{
            finishAndRemoveTask()
            startActivity(Intent(this, TitleActivity::class.java))
        }


        val algae = resources.getStringArray(R.array.algae_list)
        //val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, algae)
        //binding.autoCompleteTxt.setAdapter(arrayAdapter)

    }

    //Method to create alert dialog and push to users screen
    private fun showAlert(title: String, message: String)
    {
        AlertDialog.Builder(this)
            .setTitle("Problem With Culture Medium")
            .setMessage(
                "Title: " + title +
                "\nMessage: " + message)
            .show()
    }

    //Method to create notification dialog and push to user notification bar
    private fun showNotification(title: String, message: String)
    {
        val builder = NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.drawable.ic_alert)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(this)){
            notify(notificationID, builder.build())
        }
    }

    //method to create notification channel for android versions of Oreo and newer
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel()
    {
        val name = "Notif Channel"
        val desc = "Channel for pushing notifications"
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }
}