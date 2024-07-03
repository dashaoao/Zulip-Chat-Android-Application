plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinParcelize)
    alias(libs.plugins.kapt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.coursework"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.coursework"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(project(":common:core"))
    implementation(project(":common:util"))
    implementation(project(":common:ui"))
    implementation(project(":common:data"))
    implementation(project(":features:users:data"))
    implementation(project(":features:users:domain"))
    implementation(project(":features:users:presentation"))
    implementation(project(":features:channels:presentation"))
    implementation(project(":features:channels:data"))
    implementation(project(":features:channels:domain"))
    implementation(project(":features:chat:domain"))
    implementation(project(":features:chat:data"))
    implementation(project(":features:chat:presentation"))

    implementation(libs.coil.compose)
    implementation(libs.material)
    implementation(libs.appcompat)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)

    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)

    implementation(libs.accompanist)
    implementation(libs.cicirone)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.elmslie.android)
    implementation(libs.elmslie.core)
    implementation(libs.elmslie.coroutines)
    implementation(libs.elmslie.compose)

    implementation(libs.dagger.android)
    kapt(libs.dagger.compiler)
    implementation(libs.javax)

//    Tests
    debugImplementation(libs.kotlinx.coroutines.test)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.converter.kotlinx.serialization)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.kotlin)


    androidTestImplementation(libs.hamcrest)
    androidTestImplementation(libs.kaspresso)
    androidTestImplementation(libs.androidx.espresso.intents)

    androidTestImplementation(libs.ui.test)
    androidTestImplementation(libs.kaspresso.compose)
    androidTestImplementation(libs.kakaocup)
    androidTestImplementation(libs.mockwebserver)
    androidTestUtil(libs.orchestrator)

    debugImplementation(libs.androidx.test.core)
    debugImplementation(libs.androidx.fragment.testing)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.httpclient.android)

    androidTestImplementation(libs.wiremock) {
        exclude(group = "org.apache.httpcomponents", module = "httpclient")
    }
}
