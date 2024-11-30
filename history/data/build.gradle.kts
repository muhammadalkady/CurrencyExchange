plugins {
    alias(libs.plugins.currencyexchange.android.library)
}

android {
    namespace = "com.kady.muhammad.history.data"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.history.domain)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}