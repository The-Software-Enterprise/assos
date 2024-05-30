plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("com.google.gms.google-services")
  id("kotlin-kapt")
  id("com.google.dagger.hilt.android")
  id("com.ncorti.ktfmt.gradle") version "0.16.0"
  id("org.sonarqube") version "4.4.1.3373"
}

android {
  namespace = "com.swent.assos"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.swent.assos"
    minSdk = 29
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    // Replace com.example.android.dagger with your class path.
    testInstrumentationRunner = "com.swent.assos.CustomTestRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
    debug {
      enableUnitTestCoverage = true
      enableAndroidTestCoverage = true
    }
  }
  compileOptions {
    //coreLibraryDesugaringEnabled true
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.1"
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
      merges += "META-INF/LICENSE.md"
      merges += "META-INF/LICENSE-notice.md"
    }
  }
  testOptions {
    packagingOptions {
      jniLibs {
        useLegacyPackaging = true
      }
    }
  }
  configurations.all {
    resolutionStrategy {
      force("androidx.appcompat:appcompat:1.4.1")
      force("androidx.appcompat:appcompat-resources:1.4.1")
    }
  }
}

dependencies {
  implementation("androidx.core:core-ktx:1.12.0")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
  implementation("androidx.activity:activity-compose:1.8.2")
  implementation(platform("androidx.compose:compose-bom:2023.08.00"))
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-graphics")
  implementation("androidx.compose.ui:ui-tooling-preview")
  implementation("androidx.compose.material3:material3")
  implementation("com.google.firebase:firebase-functions-ktx:20.4.0")
  implementation("com.google.mlkit:vision-common:17.3.0")
  implementation("com.google.android.gms:play-services-mlkit-barcode-scanning:18.3.0")
    testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
  androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
  androidTestImplementation("androidx.compose.ui:ui-test-junit4")
  debugImplementation("androidx.compose.ui:ui-tooling")
  debugImplementation("androidx.compose.ui:ui-test-manifest")

  // Firebase
  implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
  implementation("com.google.firebase:firebase-firestore-ktx")
  implementation("com.google.firebase:firebase-auth-ktx")
  implementation("com.google.firebase:firebase-storage-ktx")

  // Hilt
  implementation("com.google.dagger:hilt-android:2.49")
  kapt("com.google.dagger:hilt-android-compiler:2.47")
  implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

  //Calendar
  implementation("androidx.compose.material:material-icons-extended:1.2.0")
  //coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.0.9")

  androidTestImplementation("com.kaspersky.android-components:kaspresso:1.4.3")
  // Allure support
  androidTestImplementation("com.kaspersky.android-components:kaspresso-allure-support:1.4.3")
  // Jetpack Compose support
  androidTestImplementation("com.kaspersky.android-components:kaspresso-compose-support:1.4.1")

  // Dependency for using Intents in instrumented tests
  androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")

  // Dependencies for using MockK in instrumented tests
  androidTestImplementation("io.mockk:mockk:1.13.7")
  androidTestImplementation("io.mockk:mockk-android:1.13.7")
  androidTestImplementation("io.mockk:mockk-agent:1.13.7")

  // Hilt Test Injection for instrumented tests.
  androidTestImplementation("com.google.dagger:hilt-android-testing:2.44")
  // ...with Kotlin.
  kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.44")
  // ...with Java.
  androidTestAnnotationProcessor("com.google.dagger:hilt-android-compiler:2.44")

  implementation ("com.journeyapps:zxing-android-embedded:4.1.0")
  implementation ("com.google.zxing:core:3.4.1")

  // Dependencies for unit tests
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
  testImplementation("io.mockk:mockk:1.12.0")
  testImplementation("io.mockk:mockk-android:1.13.7")
  testImplementation("io.mockk:mockk-agent:1.13.7")
  testImplementation("androidx.test.ext:junit:1.1.5")
  testImplementation("androidx.test.espresso:espresso-core:3.5.1")
  testImplementation("org.robolectric:robolectric:4.11.1")

  implementation("sh.calvin.reorderable:reorderable:1.5.2")
  implementation("com.maxkeppeler.sheets-compose-dialogs:core:1.1.1")
  implementation("com.maxkeppeler.sheets-compose-dialogs:date-time:1.1.1")

  // Coil
  implementation("io.coil-kt:coil-compose:2.1.0")
  implementation(kotlin("reflect"))

  //Images
  implementation ("io.coil-kt:coil-compose:2.1.0")

  // Lottie Animation
  implementation("com.airbnb.android:lottie-compose:4.0.0")

  // CameraX core library
  implementation("androidx.camera:camera-camera2:1.1.0-alpha06")
  implementation("androidx.camera:camera-lifecycle:1.1.0-alpha06")
  implementation("androidx.camera:camera-view:1.0.0-alpha27")

  // Compose-specific dependencies
  implementation("androidx.compose.ui:ui:1.3.0")
  // Si vous utilisez Kotlin coroutines pour le multithreading
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")

  // Generation QRCode
  implementation("com.google.zxing:core:3.5.1")

  testImplementation("org.mockito:mockito-core:3.12.4")

  // Local database
  implementation("androidx.room:room-runtime:2.6.1")
  kapt("androidx.room:room-compiler:2.6.1")
  implementation("androidx.room:room-ktx:2.6.1")

  // Bar code scanning
  implementation("com.google.mlkit:barcode-scanning:17.2.0")
  // Mock for instrumented tests
  androidTestImplementation("org.mockito:mockito-core:5.7.0")
  androidTestImplementation("org.powermock:powermock-api-mockito2:2.0.9")
  androidTestImplementation("org.powermock:powermock-module-junit4:2.0.9")
}

tasks.register("jacocoTestReport", JacocoReport::class) {
  mustRunAfter("testDebugUnitTest", "connectedDebugAndroidTest")

  reports {
    xml.required = true
    html.required = true
  }

  val fileFilter = listOf(
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*",
    "android/**/*.*",
  )
  val debugTree = fileTree("${project.buildDir}/tmp/kotlin-classes/debug") {
    exclude(fileFilter)
  }
  val mainSrc = "${project.projectDir}/src/main/java"

  sourceDirectories.setFrom(files(mainSrc))
  classDirectories.setFrom(files(debugTree))
  executionData.setFrom(fileTree(project.buildDir) {
    include("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
    include("outputs/code_coverage/debugAndroidTest/connected/*/coverage.ec")
  })
}

sonar {
  properties {
    property("sonar.projectKey", "The-Software-Enterprise_assos")
    property("sonar.projectName", "assos")
    property("sonar.organization", "the-software-enterprise")
    property("sonar.host.url", "https://sonarcloud.io")
    property("sonar.junit.reportPaths", "${project.layout.buildDirectory.get()}/test-results/testDebugUnitTest/")
    property("sonar.androidLint.reportPaths", "${project.layout.buildDirectory.get()}/reports/lint-results-debug.xml")
    property("sonar.coverage.jacoco.xmlReportPaths", "${project.layout.buildDirectory.get()}/reports/jacoco/jacocoTestReport/jacocoTestReport.xml")
  }
}