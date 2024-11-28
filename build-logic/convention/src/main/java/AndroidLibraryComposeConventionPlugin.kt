import com.android.build.api.dsl.LibraryExtension
import com.kady.muhammad.convention.configureAndroidCompose
import com.kady.muhammad.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                val currencyExchangeAndroidLibraryPlugin =
                    libs.findPlugin("currencyexchange-android-library").get()
                apply(currencyExchangeAndroidLibraryPlugin.get().pluginId)
                val composeCompilerPlugin = libs.findPlugin("kotlin-compose").get()
                val composeCompilerPluginId = composeCompilerPlugin.get().pluginId
                pluginManager.apply(composeCompilerPluginId)
            }
            extensions.configure<LibraryExtension> {
                configureAndroidCompose(this)
            }
        }
    }
}