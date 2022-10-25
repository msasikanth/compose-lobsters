/*
 * Copyright © 2014-2021 The Android Password Store Authors. All Rights Reserved.
 * SPDX-License-Identifier: GPL-3.0-only
 */

package dev.msfjarvis.aps.gradle

import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

@Suppress("Unused")
class SpotlessPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    if (project.rootProject != project) {
      throw GradleException("Spotless plugin must only be applied to the root project.")
    }
    project.pluginManager.apply(SpotlessPlugin::class)
    project.extensions.getByType<SpotlessExtension>().run {
      kotlin {
        ktfmt(KTFMT_VERSION).googleStyle()
        target("**/*.kt")
        targetExclude("**/build/", "/spotless/")
        licenseHeaderFile(project.file("spotless/license.kt"))
      }
      kotlinGradle {
        ktfmt(KTFMT_VERSION).googleStyle()
        target("**/*.kts")
        targetExclude("**/build/")
        licenseHeaderFile(project.file("spotless/license.kt"), "import|plugins|@file")
      }
      format("xml") {
        target("**/*.xml")
        targetExclude("**/build/", ".idea/")
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
      }
    }
  }

  private companion object {
    private const val KTFMT_VERSION = "0.41"
  }
}
