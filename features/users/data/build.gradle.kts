plugins {
    id("android-library-convention")
    alias(libs.plugins.ksp)
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.example.users.data"
}

dependencies {
    implementation(project(":common:core"))
    implementation(project(":features:users:domain"))

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.dagger.android)
    kapt(libs.dagger.compiler)
}
