package de.stuttgart.syzl3000.movies

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.imageFromResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import com.google.gson.GsonBuilder
import de.stuttgart.syzl3000.R
import de.stuttgart.syzl3000.network.MovieService
import de.stuttgart.syzl3000.network.UserService
import de.stuttgart.syzl3000.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MovieListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        val service = Retrofit.Builder()
                .baseUrl(Constants.BRUDDAAL_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(UserService::class.java)

        CoroutineScope(IO).launch {
            val response = service.get(
                token = "eyJraWQiOiJlK1wvdzFUN0t2QVVjMEtKRnhvNWYxRU9xWkdpazRETGd4MmlVNE5GY3NBaz0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIyYjBlNzdlNS0zMDg1LTRlNmUtYTgzMi1iZjYxYTZiY2Q1OGQiLCJhdWQiOiIzNTltNWFkdTN0MWMxYjJ1MjAxZ2FnM3U2YSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJldmVudF9pZCI6ImE4YzVkZDc4LTA4OTYtNDgwMS1iNmIwLTY2MGFlMTVhM2NhZSIsInRva2VuX3VzZSI6ImlkIiwiYXV0aF90aW1lIjoxNjA5NzAxMzAwLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAuZXUtY2VudHJhbC0xLmFtYXpvbmF3cy5jb21cL2V1LWNlbnRyYWwtMV9xMHpTSFZTbFoiLCJjb2duaXRvOnVzZXJuYW1lIjoiMmIwZTc3ZTUtMzA4NS00ZTZlLWE4MzItYmY2MWE2YmNkNThkIiwiZXhwIjoxNjA5NzA0OTAwLCJpYXQiOjE2MDk3MDEzMDAsImVtYWlsIjoiZ3IuZ2tyb0BnbWFpbC5jb20ifQ.J4_S6WPaHVJiQrG-hrMfZzdfplDpBl47Rwtp1uJ0rt-Y4PV-kXyRFXGyxLJ1NyP1Oc2ZsaYRw9y0qx_QHOYhhIwI1T9U8LgU50l3F5KAhgoHnQfMiZcfvVMw8LvWocAe0D8G7EqpcUoSTI6KG3oZnYC0pKIbJDrYYRrUMBN3r-Lz8ZYYcQlv6k9zgcDedXxHPyf36KyJdNmvrgI_st7eycBbg-noDgZKtbAy9Mt6rR3FcASpNZ5WwTxpwp45777nsIm32dDYUR05FN8CqSHrkUjbkopGYlS9JJs06I7ZxGin1wPGN7T9D0MF-w_2wFOuKhL_pBdCOyqFzdJmOZSHpw",
                id = "2b0e77e5-3085-4e6e-a832-bf61a6bcd58d"
            )
            Log.i("my response", "onResponse: ${response}")
            Log.i("my response", "onResponse: ${response.user}")
            Log.i("my response", "onResponse: ${response.user.username}")
            Log.i("my response", "onResponse: TEEEST")
            Log.i("my response", "onResponse: ${response.user.id}")
            Log.i("my response", "onResponse: ${response.user.email}")
            Log.i("my response", "onResponse: ${response.user.picture}")
        }
    }
}