package de.stuttgart.syzl3000.movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import de.stuttgart.syzl3000.R

class MovieListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, MovieListFragment())
                .commit()
//        setContentView(R.layout.activity_movie_list)
//
//        setContent {
//            ScrollableColumn(
//                    modifier = Modifier
//                            .fillMaxSize()
//                            .background(color = Color(0xFFF2F2F2))
//            ) {
//                Image(
//                        bitmap = imageFromResource(
//                                res = resources,
//                                resId = R.drawable.happy_meal_small
//                        ),
//                        modifier = Modifier.height(300.dp),
//                        contentScale = ContentScale.Crop,
//                )
//                Column(
//                        modifier = Modifier.padding(16.dp),
//                ) {
//                    Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Text(
//                                text = "Happy Meal",
//                                style = TextStyle(
//                                        fontSize = TextUnit.Companion.Sp(26)
//                                )
//                        )
//                        Text(
//                                text = "$5.99",
//                                style = TextStyle(
//                                        color = Color(0xFF85bb65),
//                                        fontSize = TextUnit.Companion.Sp(17)
//                                ),
//                                modifier = Modifier.align(Alignment.CenterVertically)
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.padding(top = 10.dp))
//                    Text(
//                            text = "800 Calories",
//                            style = TextStyle(
//                                    fontSize = TextUnit.Companion.Sp(17)
//                            ))
//                    Spacer(modifier = Modifier.padding(top = 10.dp))
//                    Button(
//                            onClick = {},
//                            modifier = Modifier.align(Alignment.CenterHorizontally)
//                    ) {
//                            Text(text = "Order Now")
//                    }
//                }
//            }
//
//        }
    }
}