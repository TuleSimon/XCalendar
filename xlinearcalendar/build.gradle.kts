plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    `maven-publish`
}

android {
    namespace = "com.anonymous.xlinearcalendar"
    compileSdk = 36

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.bundles.android.lifecycle)
    implementation(libs.kotlinx.datetime)
}

val androidSourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    // Correct Kotlin DSL syntax to access Java source directories
    from(android.sourceSets.getByName("main").java.srcDirs)
}

// 2. Task to generate Javadoc documentation
val javadoc by tasks.registering(Javadoc::class) {
    // Point to the source files
    source(android.sourceSets.getByName("main").java.srcDirs)
    // Set the Javadoc classpath (crucial for Android Libraries)
    classpath += files(android.bootClasspath)
    // Add Kotlin sources for documentation
    options.apply {
        encoding = "UTF-8"
    }
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    // Ensure javadoc task runs first
    dependsOn(javadoc)
    // Package the output of the javadoc task
    from(javadoc.get().destinationDir)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = "com.anonymous"
                artifactId = "xlinearcalendar"
                version = "1.0.0"

                // Get the artifacts from the default 'release' build variant
                from(components["release"])

                // Attach the auxiliary artifacts using the registered tasks
                artifact(androidSourcesJar.get())
                artifact(javadocJar.get())

                pom {
                    name.set("XLinearCalendar")
                    description.set("A Jetpack Compose Linear + Grid Calendar library")
                    url.set("https://github.com/yourusername/xlinearcalendar") // update URL
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("yourid")
                            name.set("Tule Simon")
                            email.set("tulesimon98@gmail.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/yourusername/xlinearcalendar.git")
                        developerConnection.set("scm:git:ssh://github.com/yourusername/xlinearcalendar.git")
                        url.set("https://github.com/yourusername/xlinearcalendar")
                    }
                }
            }
        }
    }
}
