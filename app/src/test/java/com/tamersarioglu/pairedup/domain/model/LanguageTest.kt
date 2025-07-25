package com.tamersarioglu.pairedup.domain.model

import org.junit.Test
import org.junit.Assert.*

class LanguageTest {

    @Test
    fun `fromCode returns correct language for valid codes`() {
        assertEquals(Language.ENGLISH, Language.fromCode("en"))
        assertEquals(Language.TURKISH, Language.fromCode("tr"))
    }

    @Test
    fun `fromCode returns English for invalid codes`() {
        assertEquals(Language.ENGLISH, Language.fromCode("invalid"))
        assertEquals(Language.ENGLISH, Language.fromCode(""))
        assertEquals(Language.ENGLISH, Language.fromCode("fr"))
    }

    @Test
    fun `language enum has correct properties`() {
        assertEquals("en", Language.ENGLISH.code)
        assertEquals("English", Language.ENGLISH.displayName)
        
        assertEquals("tr", Language.TURKISH.code)
        assertEquals("Türkçe", Language.TURKISH.displayName)
    }
}