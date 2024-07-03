plugins {
    id("android-library-convention")
    id("compose-convention")
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.example.channels.presentation"
}

dependencies {
    implementation(project(":common:ui"))
    implementation(project(":common:core"))
    implementation(project(":common:util"))
    implementation(project(":features:channels:domain"))

    implementation(project(":features:chat:domain"))

    implementation(libs.viewmodel.compose)
    implementation(libs.appcompat)
    implementation(libs.coil.compose)
    implementation(libs.coroutines.core)
    implementation(libs.bundles.elmslie)
    implementation(libs.accompanist)
    implementation(libs.cicirone)
    implementation(libs.dagger.android)
    implementation(libs.androidx.media3.common)
    kapt(libs.dagger.compiler)
}
