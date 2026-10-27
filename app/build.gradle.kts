import com.android.build.api.variant.impl.VariantOutputImpl

plugins {
    alias(libs.plugins.android.application)
    id("com.google.devtools.ksp")
}

android {
    namespace = "sgolovanov.childrenpiramid"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "sgolovanov.childrenpiramid"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
            buildConfigField("String", "API_URL", "\"http://46.39.239.14:3111/knight_move/\"")
        }
        debug {
            buildConfigField("String", "API_URL", "\"http://46.39.239.14:3111/knight_move/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

androidComponents {
    onVariants { variant ->
        variant.outputs.forEach { output ->
            if (output is VariantOutputImpl) {
                output.outputFileName = "children-piramid-${variant.name}-${android.defaultConfig.versionName}.apk"
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.mobileads)

    implementation(libs.androidsvg)

    implementation (libs.retrofit2)
    implementation (libs.retrofit2.adapter.rxjava2)
    implementation (libs.retrofit2.converter.gson)
    implementation (libs.curlloggerinterceptor)
    implementation (libs.okhttp3.logging.interceptor)

    testImplementation(libs.junit)
}