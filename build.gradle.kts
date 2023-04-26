import io.gitlab.arturbosch.detekt.extensions.DetektExtension.Companion.DEFAULT_SRC_DIR_JAVA
import io.gitlab.arturbosch.detekt.extensions.DetektExtension.Companion.DEFAULT_SRC_DIR_KOTLIN
import io.gitlab.arturbosch.detekt.extensions.DetektExtension.Companion.DEFAULT_TEST_SRC_DIR_JAVA
import io.gitlab.arturbosch.detekt.extensions.DetektExtension.Companion.DEFAULT_TEST_SRC_DIR_KOTLIN

plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("7.4.1").apply(false)
    id("com.android.library").version("7.4.1").apply(false)
    kotlin("android").version("1.8.0").apply(false)
    kotlin("multiplatform").version("1.8.0").apply(false)
    id("io.gitlab.arturbosch.detekt").version("1.22.0")
}

configureDetekt()

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

buildscript {

    repositories {
        gradlePluginPortal()
    }

    dependencies {
        classpath("dev.icerock.moko:resources-generator:0.21.2")
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.5")
    }
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config =
        files("$projectDir/config/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
    baseline =
        file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
}

// run gradle detekt
fun configureDetekt() {
    allprojects {
        apply(plugin = "io.gitlab.arturbosch.detekt")

        dependencies {
            detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.22.0")
        }

        detekt {
            source = objects.fileCollection()
                .from(
                    DEFAULT_SRC_DIR_JAVA,
                    DEFAULT_TEST_SRC_DIR_JAVA,
                    DEFAULT_SRC_DIR_KOTLIN,
                    DEFAULT_TEST_SRC_DIR_KOTLIN,
                    "src/commonMain/kotlin",
                    "src/commonTest/kotlin",
                    "src/androidTest/kotlin"
                )
            config = files("$rootDir/config/detekt/detekt.yml")
        }
    }
}