/*
 * Copyright © 2021-2023 Harsh Shandilya.
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
@file:Suppress("UnstableApiUsage")

import com.android.build.api.variant.HasUnitTestBuilder

plugins {
  id("dev.msfjarvis.claw.android-library")
  id("dev.msfjarvis.claw.kotlin-android")
  alias(libs.plugins.anvil)
  alias(libs.plugins.whetstone)
}

android {
  buildFeatures {
    androidResources = true
    compose = true
  }
  composeOptions {
    useLiveLiterals = false
    kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
  }
  namespace = "dev.msfjarvis.claw.common"
}

androidComponents { beforeVariants { (it as HasUnitTestBuilder).enableUnitTest = false } }

anvil { generateDaggerFactories.set(true) }

dependencies {
  implementation(platform(libs.androidx.compose.bom))
  api(libs.napier)
  implementation(projects.core)
  implementation(projects.database)
  implementation(projects.model)
  implementation(libs.androidx.browser)
  implementation(libs.androidx.compose.animation)
  implementation(libs.androidx.compose.foundation)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.runtime)
  implementation(libs.androidx.compose.ui.text)
  implementation(libs.androidx.compose.ui.tooling)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.coil.compose)
  implementation(libs.compose.richtext.markdown)
  implementation(libs.compose.richtext.material3)
  implementation(libs.compose.richtext.ui)
  implementation(libs.kotlinx.collections.immutable)
  implementation(libs.kotlinx.coroutines.core)
  testImplementation(kotlin("test-junit"))
  testImplementation(libs.testparameterinjector)
}
