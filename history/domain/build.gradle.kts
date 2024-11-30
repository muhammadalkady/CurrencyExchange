plugins {
    alias(libs.plugins.currencyexchange.jvm.library)
}

dependencies {
    implementation(libs.coroutines)
    implementation(projects.core.domain)
}