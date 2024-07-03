plugins {
    id("kotlin-library-convention")
}

dependencies {
    implementation(project(":common:core"))
    implementation(libs.coroutines.core)
}
