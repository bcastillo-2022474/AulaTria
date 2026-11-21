plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

// The API the Android app talks to. Default: the emulator's alias for the host machine. A real phone needs the
// computer's LAN address: put `apiBaseUrl=http://192.168.x.x:3000` in apps/app/local.properties (gitignored).
val apiBaseUrl: String = rootProject.file("local.properties").takeIf { it.exists() }
    ?.readLines()?.firstOrNull { it.trim().startsWith("apiBaseUrl=") }?.substringAfter("=")?.trim()
    ?: "http://10.0.2.2:3000"

android {
    namespace = "org.aula.app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "org.aula.app"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "0.1.0"
        buildConfigField("String", "API_BASE_URL", "\"$apiBaseUrl\"")
    }
    buildFeatures { buildConfig = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildTypes { release { isMinifyEnabled = false } }
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.androidx.activity.compose)
}
