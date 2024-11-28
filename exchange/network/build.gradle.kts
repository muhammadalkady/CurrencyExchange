plugins {
    alias(libs.plugins.currencyexchange.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.kady.muhammad.exchange.network"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}

dependencies {
    implementation(libs.coroutinesAndroid)
}

dependencies {
    api(libs.retrofit)
    api(libs.retrofitSerializationConverter)
    api(libs.serializationJson)
}

dependencies {
    implementation(projects.core.domain)
}


dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}