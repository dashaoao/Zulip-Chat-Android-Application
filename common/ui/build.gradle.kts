plugins {
    id("android-library-convention")
    id("compose-convention")
    alias(libs.plugins.kotlinParcelize)
}

android {
    namespace = "com.example.common.ui"
}

dependencies {
    implementation(project(":common:core"))
    implementation(libs.material)
    implementation(libs.elmslie.compose)
    implementation(libs.elmslie.core)
    implementation(libs.viewmodel.compose)
}
