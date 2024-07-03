plugins {
    id("android-library-convention")
    alias(libs.plugins.ksp)
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.example.chat.data"
}

dependencies {
    implementation(project(":common:core"))
    implementation(project(":common:util"))
    implementation(project(":common:data"))
    implementation(project(":features:chat:domain"))

    implementation(project(":features:users:domain"))
    implementation(project(":features:users:data"))
    implementation(project(":common:data"))

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.dagger.android)
    kapt(libs.dagger.compiler)

}
