pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "weatherly"

include(":app")
include(":core:ui")
include(":features:weather")
include(":features:packing")
include(":core:network")
include(":core:data")
include(":core:model")
include(":core:common")
