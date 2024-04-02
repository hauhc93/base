package jp.co.skylark.digital_gusto

import com.google.firebase.FirebaseApp

internal class DigitalGustoAppImpl(
    val firebaseApp: FirebaseApp,
    val baseUrl: String,
) : DigitalGustoApp