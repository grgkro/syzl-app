package de.stuttgart.syzl3000.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import de.stuttgart.syzl3000.R
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){
    
    @Inject
    lateinit var app: BaseApplication

    @Inject
    lateinit var randomString: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        println("onCreate: the app context: ${app}")
        println("onCreate: ${randomString}")

    }


}






















