// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id ("com.android.application") version "7.4.1" apply false
    id ("com.android.library") version "7.4.1" apply false
    id ("org.jetbrains.kotlin.android") version "1.7.21" apply false
    id ("com.google.gms.google-services") version "4.3.13" apply false
}

tasks.register("clean", Delete::class){
    delete(rootProject.buildDir)
}