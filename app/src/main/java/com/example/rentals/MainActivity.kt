package com.example.rentals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentals.ui.theme.RentalsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Login()
        }
    }
}

@Composable
fun Login() {
    Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Column (modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            Card(modifier = Modifier
                .wrapContentSize()
                .padding(30.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize(), 
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                    var email  by remember { mutableStateOf("") }
                    var password  by remember { mutableStateOf("") }
                    var role  by remember { mutableStateOf("") }
                    val expanded by remember { mutableStateOf(false) }
                    var list = listOf("landlord", "caretaker")
                    var icon = if (expanded){
                        Icon(painterResource(id = R.drawable.baseline_arrow_drop_down_24), contentDescription = null)
                    } else{
                        Icon(painterResource(id = R.drawable.baseline_arrow_drop_up_24), contentDescription = null)
                    }
                    val context = LocalContext.current
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(text = "Loin")
                    Spacer(modifier = Modifier.size(20.dp))
                    OutlinedTextField(value = email, onValueChange = {email=it},
                        label = { Text(text = "Email")},
                        leadingIcon = { Icon(painterResource(id = R.drawable.baseline_email_24), contentDescription =null)})
                    
                    
                }

            }

        }

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
   Login()
}