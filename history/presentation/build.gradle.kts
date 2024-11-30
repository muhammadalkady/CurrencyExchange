plugins {
    alias(libs.plugins.currencyexchange.android.library.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.kady.muhammad.history.presentation"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.kotlinxImmutableCollections)

    implementation(libs.hiltAndroid)
    implementation(libs.hiltNavigationCompose)
    implementation(libs.hiltLifeCycleViewModelCompose)
    ksp(libs.hiltAndroidCompiler)

    implementation(projects.core.domain)
    implementation(projects.core.presentation)
    implementation(projects.history.domain)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}