import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.JavaPluginExtension

fun DependencyHandler.implementation(dependency: String) {
    add("implementation", dependency)
}

fun DependencyHandler.debugImplementation(dependency: String) {
    add("debugImplementation", dependency)
}

fun DependencyHandler.test(dependency: String) {
    add("test", dependency)
}

fun DependencyHandler.androidTest(dependency: String) {
    add("androidTest", dependency)
}

fun DependencyHandler.ksp(dependency: String) {
    add("ksp", dependency)
}

fun DependencyHandler.implementation(dependency: Dependency) {
    add("implementation", dependency)
}

internal fun Project.android(configure: org.gradle.api.Action<LibraryExtension>): Unit = extensions.configure("android", configure)

internal val Project.libs: org.gradle.accessors.dm.LibrariesForLibs get() = extensions.getByName("libs") as org.gradle.accessors.dm.LibrariesForLibs

internal fun Project.java(): JavaPluginExtension = extensions.getByType(JavaPluginExtension::class.java)