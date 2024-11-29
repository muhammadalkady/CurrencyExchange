plugins {
    alias(libs.plugins.currencyexchange.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.kady.muhammad.exchange.data"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(projects.core.domain)
    implementation(projects.exchange.domain)
    implementation(projects.exchange.network)
}