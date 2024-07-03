plugins {
    id("android-library-convention")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.common.data"
}

dependencies {
    implementation(project(":common:core"))
    implementation(project(":common:util"))

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.javax)
}
