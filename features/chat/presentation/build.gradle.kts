plugins {
    id("android-library-convention")
    id("compose-convention")
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.example.chat.presentation"
}

dependencies {
    implementation(project(":common:ui"))
    implementation(project(":common:core"))
    implementation(project(":common:util"))
    implementation(project(":features:chat:domain"))
    implementation(project(":features:users:domain"))
    implementation(project(":features:channels:presentation"))

    implementation(libs.viewmodel.compose)
    implementation(libs.material)
    implementation(libs.material.icons)
    implementation(libs.appcompat)
    implementation(libs.coroutines.core)
    implementation(libs.bundles.elmslie)
    implementation(libs.coil.compose)
    implementation(libs.accompanist)
    implementation(libs.cicirone)
    implementation(libs.dagger.android)
    implementation(libs.androidx.media3.common)
    implementation(project(":features:channels:domain"))
    kapt(libs.dagger.compiler)

    //Unit test
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.converter.kotlinx.serialization)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.kotlin)

    //UI test
    androidTestImplementation(libs.ui.test)
    androidTestImplementation(libs.kaspresso)
    androidTestImplementation(libs.kaspresso.compose)
    androidTestImplementation(libs.kakaocup)
    androidTestImplementation(libs.mockwebserver)
    androidTestUtil(libs.orchestrator)
}
