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
        maven("https://developer.huawei.com/repo/")

        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        mavenCentral()
        google()

        maven("http://maven.aliyun.com/nexus/content/groups/public/"){
            isAllowInsecureProtocol=true
        }

        maven ("https://maven.aliyun.com/repository/google/")
        maven ("https://maven.aliyun.com/repository/public/")
        maven ("https://maven.aliyun.com/repository/gradle-plugin/")

        maven ("https://jitpack.io")
        maven ("https://mirrors.huaweicloud.com/repository/maven/")
        maven ("http://maven.faceunity.com/repository/maven-public/") {
            isAllowInsecureProtocol=true}
        maven ("https://s01.oss.sonatype.org/content/groups/public")
    }
}

rootProject.name = "GlideDemo_4.x"
include(":app")
 