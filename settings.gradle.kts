dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
//        jcenter() // Warning: this repository is going to shut down soon
    }
}
rootProject.name = "Neighbors"
include(":app")
include(":animatedrecyclerview")
