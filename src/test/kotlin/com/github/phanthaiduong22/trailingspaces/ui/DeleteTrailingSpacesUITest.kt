package com.github.phanthaiduong22.trailingspaces.ui

import com.github.phanthaiduong22.trailingspaces.ui.fixtures.WelcomeFrameFixture
import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.fixtures.ComponentFixture
import com.intellij.remoterobot.search.locators.byXpath
import com.intellij.remoterobot.stepsProcessing.step
import com.intellij.remoterobot.utils.waitFor
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.time.Duration

class DeleteTrailingSpacesUITest {

    @Test
    fun testOpenNewProject() {
        with(remoteRobot) {
            step("Open new project") {
                // Handle welcome screen
                find(WelcomeFrameFixture::class.java, Duration.ofSeconds(10)).apply {
                    createNewProjectLink().click()

                    // Wait for project creation dialog and create simple Java project
                    waitFor(Duration.ofSeconds(15)) {
                        try {
                            find(ComponentFixture::class.java,
                                byXpath("//div[@text='Create']"),
                                Duration.ofSeconds(2)
                            ).click()
                            true
                        } catch (e: Exception) {
                            println("Waiting for Create button: ${e.message}")
                            false
                        }
                    }
                }

                // Wait for IDE frame to appear
                waitFor(Duration.ofSeconds(30)) {
                    try {
                        find(ComponentFixture::class.java, 
                            byXpath("//div[@class='IdeFrameImpl']"), 
                            Duration.ofSeconds(2))
                        println("IDE frame found")
                        true
                    } catch (e: Exception) {
                        println("Waiting for IDE frame: ${e.message}")
                        false
                    }
                }

                println("Project opened successfully")
            }
        }
    }

    companion object {
        private lateinit var remoteRobot: RemoteRobot

        @BeforeAll
        @JvmStatic
        fun initRobot() {
            remoteRobot = RemoteRobot("http://127.0.0.1:8082")
        }

        @AfterAll
        @JvmStatic
        fun closeRobot() {
            // Clean up if needed
        }
    }
}