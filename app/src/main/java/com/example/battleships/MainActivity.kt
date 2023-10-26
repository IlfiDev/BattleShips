package com.example.battleships

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import com.example.battleships.ui.theme.BattleShipsTheme
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    var textToDisplay = ""
    var messageToSend = ""
    var liveData = MutableLiveData<String>()
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main()
        setContent {
            BattleShipsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var text by remember { mutableStateOf("Hello") }

                    liveData.observe(this) {
                        textToDisplay = it
                    }
                    Greeting("Android")
                    Column {
                        Text(text = textToDisplay)
                        TextField(value = text, onValueChange = {
                            text = it
                            messageToSend = it.toString()
                        } )

                    }
                }
            }
        }
    }

    suspend fun DefaultClientWebSocketSession.outputMessages() {
        try {
            for (message in incoming) {
                Log.i("Test", "Cock")
                message as? Frame.Text ?: continue
                liveData.postValue(message.readText())
            }
        } catch (e: Exception) {
            println("Error while receiving: " + e.localizedMessage)
        }
    }

    private suspend fun inputMessages() {
        while (true) {
            val message = "ping"
            Log.i("Message", messageToSend)
            if (message.equals("exit", true)) return
            try {

            } catch (e: Exception) {
                println("Error while sending: " + e.localizedMessage)
                return
            }
        }
    }


    fun main() {
        val client = HttpClient {
            install(WebSockets)
        }
        Log.i("Test", client.isActive.toString())
        CoroutineScope(Dispatchers.IO).launch {
            client.webSocket(method = HttpMethod.Get, host = "62.217.187.32", port = 8080, path = "/ws") {
                val messageOutputRoutine = launch { outputMessages() }
                val userInputRoutine = launch { inputMessages() }

                Log.i("Test", client.isActive.toString())
                userInputRoutine.join() // Wait for completion; either "exit" or error
                messageOutputRoutine.cancelAndJoin()
            }
            client.close()
        }
        println("Connection closed. Goodbye!")
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BattleShipsTheme {
        Greeting("Android")
    }
}