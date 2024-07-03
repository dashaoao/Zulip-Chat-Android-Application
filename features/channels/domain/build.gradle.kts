plugins {
    id("kotlin-library-convention")
}

dependencies {
    implementation(project(":common:core"))
    implementation(project(":features:chat:domain"))
    implementation(libs.coroutines.core)
}
