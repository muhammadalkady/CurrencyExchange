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

    implementation(libs.coroutinesAndroid)

    api(libs.retrofit)
    api(libs.retrofitSerializationConverter)
    api(libs.serializationJson)

    implementation(projects.core.domain)
    implementation(projects.exchange.domain)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}