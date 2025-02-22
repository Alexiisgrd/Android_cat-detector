package com.example.myapplicationofdetectioncat

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import fi.iki.elonen.NanoHTTPD

class MainActivity : AppCompatActivity() {

    private var httpServer: MyHttpServer? = null
    private lateinit var statusTextView: TextView
    private lateinit var statusTextViewStatusChat: TextView

    private lateinit var controlButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusTextView = findViewById(R.id.statusTextView)
        statusTextViewStatusChat = findViewById(R.id.statusTextViewChatStatus)
        controlButton = findViewById(R.id.controlButton)

        updateServerStatus(isRunning = false)

        controlButton.setOnClickListener {
            if (httpServer == null) {
                httpServer = MyHttpServer(8080)
                httpServer?.startServer()
                updateServerStatus(isRunning = true)
            } else {
                httpServer?.stop()
                httpServer = null
                updateServerStatus(isRunning = false)
            }
        }
    }

    private fun updateServerStatus(isRunning: Boolean) {
        val statusText = if (isRunning) "Serveur démarré" else "Serveur arrêté"
        statusTextView.text = statusText
        controlButton.text = if (isRunning) "Arrêter le serveur" else "Démarrer le serveur"
    }

    inner class MyHttpServer(private val port: Int) : NanoHTTPD(port) {

        override fun serve(session: IHTTPSession): Response {
            if (session.method == Method.POST && session.uri == "/detect") {
                runOnUiThread {
                    statusTextViewStatusChat.text = "Chat détecté !"
                }
                return newFixedLengthResponse("Chat detected!")
            }
            return newFixedLengthResponse("Not Found")
        }

        fun startServer() {
            start()
        }
    }
}
