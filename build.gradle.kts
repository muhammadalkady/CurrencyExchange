// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.currencyexchange.android.application) apply false
    alias(libs.plugins.currencyexchange.android.application.compose) apply false
    alias(libs.plugins.currencyexchange.android.library) apply false
    alias(libs.plugins.currencyexchange.android.library.compose) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}