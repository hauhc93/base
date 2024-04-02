package jp.co.skylark.digital_gusto

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

interface DigitalGustoApp {

    companion object {

        fun initializeApp(context: Context): DigitalGustoApp {
            // TODO
            val builder = FirebaseOptions.Builder()
                .setApplicationId("1:799130995601:android:2a2bea51222524dd5be830")
                .setApiKey("AIzaSyD2qdLrINctO-6xC8N1pfqSac3dKKkWROQ")
                .setDatabaseUrl("https://dmb-app-ay.firebaseio.com")
                .setStorageBucket("dmb-app-ay.appspot.com")
                .setProjectId("dmb-app-ay")

            val firebaseApp = FirebaseApp.initializeApp(context, builder.build(), "dmb-app-ay")

            return DigitalGustoAppImpl(
                firebaseApp = firebaseApp,
                baseUrl = "https://dg-api-server-53kj4fhbxa-an.a.run.app/",
            )
        }
    }
}