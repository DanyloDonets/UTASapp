plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-android")


}




android {
    namespace = "donets.danylo.android.utasapp"
    compileSdk = 34
    viewBinding.enable = true

    defaultConfig {
        applicationId = "donets.danylo.android.utasapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    packagingOptions {
        resources.excludes.add("META-INF/*")
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation ("com.firebaseui:firebase-ui-database:8.0.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation ("androidx.browser:browser:1.8.0")

    //бібліотеки для дизайну
    implementation ("com.mikepenz:materialdrawer:7.0.0")
    implementation ("com.mikepenz:materialdrawer-nav:7.0.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.vanniktech:android-image-cropper:4.5.0")
    implementation ("com.github.yalantis:ucrop:2.2.8")
    implementation ("com.github.yalantis:ucrop:2.2.8-native")
    //api ("com.theartofdev.edmodo:android-image-cropper:2.8.0")
    implementation ("com.github.dhaval2404:imagepicker:2.1")
    implementation ("com.squareup.picasso:picasso:2.8")


    implementation ("com.google.api-client:google-api-client:2.0.0")
    implementation ("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation ("com.google.apis:google-api-services-sheets:v4-rev20220927-2.0.0")
    implementation ("com.google.http-client:google-http-client-gson:1.42.3")
    implementation ("com.google.api-client:google-api-client-jackson2:1.20.0")

}

allprojects{
    repositories{
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

