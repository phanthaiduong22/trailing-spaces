package com.github.phanthaiduong22.trailingspaces.ui.fixtures

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.data.RemoteComponent
import com.intellij.remoterobot.fixtures.ComponentFixture
import com.intellij.remoterobot.fixtures.ContainerFixture
import com.intellij.remoterobot.fixtures.DefaultXpath
import com.intellij.remoterobot.fixtures.FixtureName
import com.intellij.remoterobot.search.locators.byXpath
import com.intellij.remoterobot.stepsProcessing.step
import java.time.Duration

@DefaultXpath(by = "FlatWelcomeFrame type", xpath = "//div[@class='FlatWelcomeFrame']")
@FixtureName(name = "Welcome Frame")
class WelcomeFrameFixture(remoteRobot: RemoteRobot, remoteComponent: RemoteComponent) : ContainerFixture(remoteRobot, remoteComponent) {
    
    fun createNewProjectLink(): ComponentFixture {
        return find(ComponentFixture::class.java, byXpath("New Project", 
            "//div[(@class='MainButton' and @text='New Project') or " +
            "(@accessiblename='New Project' and @class='JButton')]"), Duration.ofSeconds(30))
    }
    
    fun getFromVcsLink(): ComponentFixture {
        return find(ComponentFixture::class.java, byXpath("Get from VCS", 
            "//div[(@defaulticon='fromVCSTab.svg') or " +
            "(@accessiblename='Get from VCS' and @class='JBOptionButton') or " +
            "(@accessiblename='Clone Repository' and @class='JBOptionButton' and @text='Clone Repository')]"), Duration.ofSeconds(30))
    }
    
    fun createNewProject() {
        step("Create New Project") {
            createNewProjectLink().click()
        }
    }
} 