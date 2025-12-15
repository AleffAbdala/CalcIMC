// Top-level build file

plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false

    // ✅ KSP COMPATÍVEL COM KOTLIN 1.9.x
    id("com.google.devtools.ksp") version "1.9.24-1.0.20" apply false
}
