import com.android.build.api.dsl.ApplicationExtension
import com.kady.muhammad.convention.ExtensionType
import com.kady.muhammad.convention.configureBuildTypes
import com.kady.muhammad.convention.configureKotlinAndroid
import com.kady.muhammad.convention.findIntVersion
import com.kady.muhammad.convention.findStringVersion
import com.kady.muhammad.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")

            }
            extensions.configure<ApplicationExtension> {
                defaultConfig {
                    applicationId = libs.findStringVersion("projectApplicationId")
                    targetSdk = libs.findIntVersion("projectTargetSdkVersion")
                    versionCode = libs.findIntVersion("projectVersionCode")
                    versionName = libs.findStringVersion("projectVersionName")
                }
                configureKotlinAndroid(this)
                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.APPLICATION
                )
            }
        }
    }
}