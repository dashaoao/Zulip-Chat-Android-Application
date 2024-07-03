plugins {
    id("android-library-convention")
    id("compose-convention")
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.example.users.presentation"
}

dependencies {
    implementation(project(":common:ui"))
    implementation(project(":common:core"))
    implementation(project(":common:util"))
    implementation(project(":features:users:domain"))

    implementation(libs.coroutines.core)
    implementation(libs.coil.compose)
    implementation(libs.bundles.elmslie)
    implementation(libs.accompanist)
    implementation(libs.cicirone)
    implementation(libs.appcompat)
    implementation(libs.dagger.android)
    kapt(libs.dagger.compiler)
}
