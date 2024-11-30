plugins {
    alias(libs.plugins.currencyexchange.android.library)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
}

room {
    schemaDirectory("$projectDir/schemas")
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

    api(libs.room.runtime)
    ksp(libs.room.compiler)
    api(libs.room.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}