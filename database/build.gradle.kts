/*
 * Copyright © 2021-2023 Harsh Shandilya.
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
plugins {
  id("dev.msfjarvis.claw.android-library")
  id("dev.msfjarvis.claw.kotlin-android")
  alias(libs.plugins.anvil)
  alias(libs.plugins.sqldelight)
  alias(libs.plugins.whetstone)
}

android { namespace = "dev.msfjarvis.claw.database" }

anvil { generateDaggerFactories.set(true) }

sqldelight {
  databases {
    create("LobstersDatabase") {
      packageName.set("dev.msfjarvis.claw.database")
      sourceFolders.set(listOf("sqldelight"))
      schemaOutputDirectory.set(file("src/main/sqldelight/databases"))
      verifyMigrations.set(true)
    }
  }
}

dependencies {
  implementation(libs.dagger)
  implementation(libs.sqldelight.androidDriver)
  implementation(libs.sqldelight.primitiveAdapters)
  implementation(projects.core)

  testImplementation(libs.kotest.assertions.core)
  testImplementation(libs.kotest.runner.junit5)
  testImplementation(libs.kotlinx.coroutines.core)
  testImplementation(libs.sqldelight.jvmDriver)
}
