plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.artisan.browser"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.artisan.browser"
        minSdk = 33
        targetSdk = 37
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions.add("arch")
    productFlavors {
        create("universal") {
            dimension = "arch"
        }
        create("arm64") {
            dimension = "arch"
            ndk { abiFilters.addAll(mutableSetOf("arm64-v8a")) }
        }
        create("arm7") {
            dimension = "arch"
            ndk { abiFilters.addAll(mutableSetOf("armeabi-v7a")) }
        }
        create("x86") {
            dimension = "arch"
            ndk { abiFilters.addAll(mutableSetOf("x86_64")) }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        jniLibs {
            useLegacyPackaging = true
        }
    }

    bundle {
        abi {
            enableSplit = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    //noinspection KaptUsageInspection
    annotationProcessor(libs.androidx.room.compiler)
    implementation("io.github.kyant0:backdrop:2.0.0")
    implementation("io.github.kyant0:shapes:1.2.0")
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation("org.mozilla.geckoview:geckoview:150.0.20260511200624")
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}