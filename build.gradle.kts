plugins {
    id("com.android.application")

    kotlin("android")
    kotlin("kapt")

    id("gradle-versions")
    id("signing-config")
    id("sentry-io")
    id("kotlin-android")
}

repositories {
    google()
    mavenCentral()
    jcenter()
    maven("https://jitpack.io")
}

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.3")

    signingConfigs {
        getByName("debug") {
            storeFile = file("keystore/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
        create("release") {
            val keystoreLocation: String by project
            val keystorePassword: String by project
            val storeKeyAlias: String by project
            val aliasKeyPassword: String by project

            storeFile = file(keystoreLocation)
            storePassword = keystorePassword
            keyAlias = storeKeyAlias
            keyPassword = aliasKeyPassword
        }
    }

    defaultConfig {
        applicationId = "com.softwareforgood.pridefestival"
        minSdkVersion(21)
        targetSdkVersion(29)

        versionCode = 124
        versionName = "1.6.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
            isShrinkResources = false
        }
        getByName("release") {
            applicationIdSuffix = ".release"
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.txt")
        }
    }

    packagingOptions {
        exclude("LICENSE.txt")
        exclude("META-INF/LICENSE.txt")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    // ignore javax packages in sentry.io jar
    lintOptions {
        disable = setOf("InvalidPackage")
    }

    buildFeatures.viewBinding = true
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.7")

    implementation("androidx.core:core-ktx:1.3.0")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.emoji:emoji-bundled:1.1.0")
    implementation("androidx.browser:browser:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")

    implementation("androidx.room:room-runtime:2.2.5")
    kapt("androidx.room:room-compiler:2.2.5")
    implementation("androidx.room:room-ktx:2.2.5")

    implementation("com.google.android.material:material:1.1.0")

    implementation("com.google.android.gms:play-services-maps:17.0.0")

    implementation("com.jakewharton:process-phoenix:2.0.0")
    implementation("com.jakewharton.threetenabp:threetenabp:1.2.4")
    implementation("com.jakewharton.timber:timber:4.7.1")

    implementation("com.squareup.moshi:moshi:1.9.3")

    implementation("com.google.dagger:dagger:2.28.1")
    kapt("com.google.dagger:dagger-compiler:2.28.1")

    implementation("io.reactivex.rxjava2:rxjava:2.2.19")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("com.jakewharton.rxbinding2:rxbinding-appcompat-v7:2.2.0")
    implementation("com.f2prateek.rx.preferences2:rx-preferences:2.0.0")

    implementation("com.github.parse-community.Parse-SDK-Android:parse:1.24.2")

    implementation("com.github.bumptech.glide:glide:4.11.0")
    kapt("com.github.bumptech.glide:compiler:4.11.0")

    implementation("com.github.rogues-dev:hoard:0.2.0")
    implementation("com.andrewreitz.velcro:velcro-betterviewanimator:1.0.1")
    implementation("ca.barrenechea.header-decor:header-decor:0.2.8")
    implementation("xyz.danoz:recyclerviewfastscroller:0.1.3")
    implementation("pub.devrel:easypermissions:3.0.0")

    releaseImplementation("io.sentry:sentry-android:1.7.16")
    releaseImplementation("org.slf4j:slf4j-nop:1.7.30")

    debugImplementation("com.facebook.stetho:stetho:1.5.1")

    androidTestImplementation(kotlin("test-junit"))
    androidTestImplementation("androidx.test:rules:1.2.0")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    kaptAndroidTest("com.google.dagger:dagger-compiler:2.28.1")

    testImplementation(kotlin("test-junit"))
    testImplementation("com.nhaarman:mockito-kotlin:1.6.0")
    testImplementation("org.amshove.kluent:kluent:1.61")

    testImplementation("org.threeten:threetenbp:1.4.0") { exclude(group = "com.jakewharton.threetenabp") }
}
