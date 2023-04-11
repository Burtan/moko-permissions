/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("dev.icerock.moko.gradle.multiplatform.mobile")
    id("dev.icerock.moko.gradle.publication")
    id("dev.icerock.moko.gradle.stub.javadoc")
    id("dev.icerock.moko.gradle.detekt")
}

android {
    namespace = "dev.icerock.moko.permissions"
}

kotlin {
    targets {
        js(IR)
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin-wrappers:kotlin-browser:1.0.0-pre.536")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-web:1.0.0-pre.536")
            }
        }
    }
}

dependencies {
    commonMainImplementation(libs.coroutines)
    androidMainImplementation(libs.appCompat)
    androidMainImplementation(libs.lifecycleRuntime)
}
