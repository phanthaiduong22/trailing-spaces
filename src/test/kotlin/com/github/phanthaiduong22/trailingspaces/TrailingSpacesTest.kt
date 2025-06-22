package com.github.phanthaiduong22.trailingspaces

import com.github.phanthaiduong22.trailingspaces.config.PluginConfig
import com.github.phanthaiduong22.trailingspaces.settings.TrailingSpacesSettings
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import java.util.regex.Pattern

class TrailingSpacesTest : BasePlatformTestCase() {

    fun testTrailingSpacePattern() {
        val pattern = Pattern.compile(PluginConfig.TRAILING_SPACE_PATTERN, PluginConfig.PATTERN_FLAGS)
        
        assertTrue(pattern.matcher("line with spaces   \n").find())
        assertTrue(pattern.matcher("line with tabs\t\t\n").find())
        assertTrue(pattern.matcher("mixed   \t \n").find())
        assertFalse(pattern.matcher("clean line\n").find())
        assertFalse(pattern.matcher("no trailing").find())
    }

    fun testTrailingSpacesSettings() {
        val settings = TrailingSpacesSettings.getInstance()
        assertNotNull(settings)
        
        val originalValue = settings.highlightCurrentLine
        
        settings.highlightCurrentLine = true
        assertTrue(settings.highlightCurrentLine)
        
        settings.highlightCurrentLine = false
        assertFalse(settings.highlightCurrentLine)
        
        settings.highlightCurrentLine = originalValue
    }

    fun testFileWithTrailingSpaces() {
        val fileContent = "public class Test {    \n" +
                "    private int value;   \n" +
                "\n" +
                "    public void method() {  \n" +
                "        System.out.println(\"test\");\n" +
                "    }   \n" +
                "}"
        
        val psiFile = myFixture.configureByText(PlainTextFileType.INSTANCE, fileContent)
        assertNotNull(psiFile)
        assertEquals(fileContent, psiFile.text)
        
        val pattern = Pattern.compile(PluginConfig.TRAILING_SPACE_PATTERN, PluginConfig.PATTERN_FLAGS)
        assertTrue(pattern.matcher(fileContent).find())
    }

    fun testFileWithoutTrailingSpaces() {
        val fileContent = "public class Clean {\n" +
                "    private int value;\n" +
                "\n" +
                "    public void method() {\n" +
                "        System.out.println(\"clean\");\n" +
                "    }\n" +
                "}"
        
        val psiFile = myFixture.configureByText(PlainTextFileType.INSTANCE, fileContent)
        assertNotNull(psiFile)
        
        val pattern = Pattern.compile(PluginConfig.TRAILING_SPACE_PATTERN, PluginConfig.PATTERN_FLAGS)
        assertFalse(pattern.matcher(fileContent).find())
    }
}
