pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        jcenter()
        maven {
            url = uri( "http://jitpack.io")
            isAllowInsecureProtocol = true
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven {
            url = uri( "http://jitpack.io")
            isAllowInsecureProtocol = true
        }
    }
}
rootProject.name = "PerformaFuboru"
include(":app")
 