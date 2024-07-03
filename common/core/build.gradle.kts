plugins {
    id("kotlin-library-convention")
    alias(libs.plugins.kapt)
}

dependencies {
    implementation(libs.coroutines.core)
    implementation(libs.dagger.android)
    kapt(libs.dagger.compiler)
}
