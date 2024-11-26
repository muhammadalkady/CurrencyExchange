package com.kady.muhammad.convention

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

/**
 * Extension property to get the `libs` version catalog from the project.
 */
val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun VersionCatalog.findIntVersion(key: String): Int {
    return findVersion(key).get().toString().toInt()
}

fun VersionCatalog.findStringVersion(key: String): String {
    return findVersion(key).get().toString()
}
