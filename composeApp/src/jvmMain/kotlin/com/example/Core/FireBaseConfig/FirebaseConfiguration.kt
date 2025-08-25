package com.example.Core.FireBaseConfig

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import java.io.InputStream

fun initializeFirebase() {
    try {
        val serviceAccount: InputStream? = Thread.currentThread().contextClassLoader.getResourceAsStream("google-services.json")

        if (serviceAccount == null) {
            println("Error: google-services.json not found in resources.")
            return
        }

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options)
            println("Firebase has been initialized successfully.")
        } else {
            println("Firebase has already been initialized.")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
