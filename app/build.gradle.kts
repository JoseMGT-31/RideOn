import org.gradle.testing.jacoco.tasks.JacocoReport
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import java.math.BigDecimal
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.kapt")
    id("jacoco")
}

// Cargar propiedades de clave (si existe key.properties en la raíz del proyecto)
val keyPropertiesFile = rootProject.file("key.properties")
val keyProperties = Properties()
if (keyPropertiesFile.exists()) {
    keyPropertiesFile.inputStream().use { stream ->
        keyProperties.load(stream)
    }
}

android {
    namespace = "com.example.rideon"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.rideon"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            // solo asignar si key.properties está presente
            if (keyPropertiesFile.exists()) {
                storeFile = file(keyProperties.getProperty("storeFile"))
                storePassword = keyProperties.getProperty("storePassword")
                keyAlias = keyProperties.getProperty("keyAlias")
                keyPassword = keyProperties.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // aplicar signingConfig release si está configurada
            if (keyPropertiesFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
        debug {
            // Habilitar cobertura para pruebas instrumentadas en debug (opcional, útil para CI/local)
            // Se puede desactivar si no se requiere cobertura androidTest
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
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

    // Incluir recursos de Android en unit tests y deshabilitar animaciones para pruebas reproducibles
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
        animationsDisabled = true
    }

    packagingOptions {
        resources {
            excludes += setOf("META-INF/*.kotlin_module", "META-INF/LICENSE*", "META-INF/DEPENDENCIES")
        }
    }
    buildToolsVersion = "36.1.0"
}

dependencies {
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    // Kotest + MockK para unit tests (JVM)
    testImplementation("io.kotest:kotest-runner-junit5:5.6.2")
    testImplementation("io.kotest:kotest-assertions-core:5.6.2")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.9.5")
    implementation("at.favre.lib:bcrypt:0.10.2")


    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")
}

// Ejecutar Kotest (y otros frameworks sobre JUnit Platform)
tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

// Task: reporte JaCoCo para unit tests (Debug)
tasks.register("jacocoTestReport", JacocoReport::class) {
    dependsOn("testDebugUnitTest")

    val fileFilter = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*_Factory.class",
        "**/*_MembersInjector*.*",
        "**/databinding/*",
        "**/androidx/*"
    )

    val javaClasses = fileTree("${project.buildDir}/intermediates/javac/debug/classes") {
        exclude(fileFilter)
    }
    val kotlinClasses = fileTree("${project.buildDir}/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }

    classDirectories.setFrom(files(javaClasses, kotlinClasses))
    sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))
    executionData.setFrom(fileTree(project.buildDir) { include("**/jacoco/*.exec", "**/jacoco.exec") })

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
}

// Task: verificación de cobertura (80%)
tasks.register("jacocoCoverageVerification", JacocoCoverageVerification::class) {
    dependsOn("jacocoTestReport")

    violationRules {
        rule {
            limit {
                minimum = BigDecimal.valueOf(0.80)
            }
        }
    }

    // reutilizamos los mismos classDirectories / executionData que el report
    val fileFilter = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*_Factory.class",
        "**/*_MembersInjector*.*",
        "**/databinding/*",
        "**/androidx/*"
    )

    val javaClasses = fileTree("${project.buildDir}/intermediates/javac/debug/classes") {
        exclude(fileFilter)
    }
    val kotlinClasses = fileTree("${project.buildDir}/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }
    classDirectories.setFrom(files(javaClasses, kotlinClasses))
    sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))
    executionData.setFrom(fileTree(project.buildDir) { include("**/jacoco/*.exec", "**/jacoco.exec") })
}
