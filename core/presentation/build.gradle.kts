plugins {
    alias(libs.plugins.currencyexchange.android.library.compose)
}

android {
    namespace = "com.kady.muhammad.core.presentation"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}