import com.android.build.api.dsl.ApplicationExtension
import com.kady.muhammad.convention.configureAndroidCompose
import com.kady.muhammad.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin :Plugin<Project>{
    override fun apply(target: Project) {
        target.run {
            pluginManager.apply("currencyexchange.android.application")
            val composeCompilerPlugin = libs.findPlugin("kotlin-compose").get()
            val composeCompilerPluginId = composeCompilerPlugin.get().pluginId
            pluginManager.apply(composeCompilerPluginId)
            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
        }
    }
}