package com.kady.muhammad.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension<*, *, *, *, *, *>, extensionType: ExtensionType
) {
    commonExtension.run {
        buildFeatures {
            buildConfig = true
        }
        val apiKey = gradleLocalProperties(rootDir, providers).getProperty("API_KEY")
        when (extensionType) {
            ExtensionType.APPLICATION -> configure<ApplicationExtension> {
                buildTypes {
                    debug {
                        configureDebugBuildType(apiKey)
                    }
                    release {
                        configureReleaseBuildType(commonExtension, apiKey)
                        signingConfig = signingConfigs.getByName("debug")
                    }
                }
            }

            ExtensionType.LIBRARY     -> configure<LibraryExtension> {
                buildTypes {
                    debug {
                        configureDebugBuildType(apiKey)
                    }
                    release {
                        configureReleaseBuildType(commonExtension, apiKey)
                        // Let app module minify the release build
                        // Check https://developer.android.com/build/releases/past-releases/agp-8-4-0-release-notes#library-classes-shrunk
                        isMinifyEnabled = false
                    }
                }
            }
        }
    }
}

private fun BuildType.configureDebugBuildType(apiKey: String) {
    buildConfigField("String", "API_KEY", "\"$apiKey\"")
    buildConfigField("String", "BASE_URL", "\"https://data.fixer.io/api/\"")
}


private fun BuildType.configureReleaseBuildType(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    apiKey: String,
) {
    buildConfigField("String", "API_KEY", "\"$apiKey\"")
    buildConfigField("String", "BASE_URL", "\"https://data.fixer.io/api/\"")
    isMinifyEnabled = true
    proguardFiles(
        commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )
}