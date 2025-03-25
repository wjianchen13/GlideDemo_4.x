plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.glidedemo_4x"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.glidedemo_4x"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation("com.github.bumptech.glide:glide:4.13.2") // Glide 依赖
    annotationProcessor("com.github.bumptech.glide:compiler:4.13.2") // Glide 编译器依赖
    implementation("com.github.zjupure:webpdecoder:2.6.4.16.0")
    // SVGAPlayer
    implementation("com.github.yyued:SVGAPlayer-Android:2.6.1")

    // integration for them
    implementation("com.github.YvesCheung:SVGAGlidePlugin:4.13.3")
}