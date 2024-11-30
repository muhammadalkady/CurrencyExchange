plugins {
    alias(libs.plugins.currencyexchange.jvm.library)
}

dependencies {
    implementation(projects.core.domain)
}