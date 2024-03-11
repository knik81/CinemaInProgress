package com.best.nikflix

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.best.nikflix.databinding.ActivityMainBinding
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar = supportActionBar
        actionBar?.hide()


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //настройка нижнего навигатора
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        //binding.navView.setupWithNavController(navController)


        //обработка боттом навигатора.
        //сложно потому, что мне нужно было изменить логику перехода
        // при нажатии на нижние кнопки + одтельно для HomeFragment
        binding.navView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    navController.currentBackStack.value.forEach {
                        if (it.destination.label != "Home"
                            && it.destination.label != "fragment_onboarding"
                            && it.destination.label != null
                        ) {
                            navController.popBackStack()
                        }
                    }
                }

                R.id.navigation_search -> {
                    navController.currentBackStack.value.forEach {
                        if (it.destination.label != "Home"
                            && it.destination.label != "fragment_onboarding"
                            && it.destination.label != null
                        )
                            navController.popBackStack()
                    }
                    navController.navigate(menuItem.itemId)
                }

                R.id.navigation_profile -> {
                    navController.currentBackStack.value.forEach {
                        if (it.destination.label != "Home"
                            && it.destination.label != "fragment_onboarding"
                            && it.destination.label != null
                        )
                            navController.popBackStack()
                    }
                    navController.navigate(menuItem.itemId)
                }
            }
            return@setOnItemSelectedListener true
        }



        binding.arrowBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        //Log.d("Nik", "POST_NOTIFICATIONS")


        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            //    Log.d("Nik", "${it.result}")
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("Nik", "Back button pressed")

                val label = navController.currentBackStackEntry?.destination?.label
                if (label != "Home")
                    navController.popBackStack()
            }
        })
    }

/*
    private fun createNotification() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        else
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, App.channelId)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("NikFlix 1")
            .setContentText("Информирование")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Nik", "POST_NOTIFICATIONS")
            return
        }
        NotificationManagerCompat.from(this).notify(notificationId, notification)
    }

 */

    companion object {
        var firstStart = true
        //private const val notificationId = 1000
    }


}