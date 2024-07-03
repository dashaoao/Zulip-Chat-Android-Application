plugins {
    id("android-library-convention")
    alias(libs.plugins.ksp)
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.example.channels.data"
}

dependencies {
    implementation(project(":common:core"))
    implementation(project(":common:data"))
    implementation(project(":features:channels:domain"))

    implementation(project(":common:data"))
    implementation(project(":features:chat:data"))
    implementation(project(":features:chat:domain"))

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(project(":common:util"))
    ksp(libs.room.compiler)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.dagger.android)
    kapt(libs.dagger.compiler)
}
