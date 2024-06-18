package com.ls.kassify

import android.app.Application
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.configuration.AmplifyOutputs

class Kassify: Application() {
    override fun onCreate() {
        super.onCreate()
        // AWS Amplify Configuration
        try {
            // Adding Auth- and API-Plugin
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSApiPlugin())
            // Load configuration with amplify_outputs.json
            Amplify.configure(AmplifyOutputs.fromResource(R.raw.amplify_outputs), applicationContext)
            Log.i("Amplify", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("Amplify", "Could not initialize Amplify", error)
        }
    }
}