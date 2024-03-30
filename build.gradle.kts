// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.3.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.android.library") version "8.3.0" apply false
    id("org.jetbrains.kotlin.jvm") version "1.9.0" apply false

    //fire base
    id("com.google.gms.google-services") version "4.4.1" apply false
    //fire base - crashlytics
    id("com.google.firebase.crashlytics") version "2.9.9" apply false
}