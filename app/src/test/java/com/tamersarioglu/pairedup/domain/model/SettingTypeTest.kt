package com.tamersarioglu.pairedup.domain.model

import org.junit.Assert.*
import org.junit.Test

class SettingTypeTest {

    @Test
    fun `enum should contain all expected values`() {
        val expectedValues = setOf(
            SettingType.DARK_THEME,
            SettingType.TIMER_ENABLED,
            SettingType.SOUND_ENABLED,
            SettingType.VIBRATION_ENABLED
        )
        
        val actualValues = SettingType.values().toSet()
        
        assertEquals(expectedValues, actualValues)
        assertEquals(4, SettingType.values().size)
    }

    @Test
    fun `enum values should have correct names`() {
        assertEquals("DARK_THEME", SettingType.DARK_THEME.name)
        assertEquals("TIMER_ENABLED", SettingType.TIMER_ENABLED.name)
        assertEquals("SOUND_ENABLED", SettingType.SOUND_ENABLED.name)
        assertEquals("VIBRATION_ENABLED", SettingType.VIBRATION_ENABLED.name)
    }

    @Test
    fun `enum values should have correct ordinals`() {
        assertEquals(0, SettingType.DARK_THEME.ordinal)
        assertEquals(1, SettingType.TIMER_ENABLED.ordinal)
        assertEquals(2, SettingType.SOUND_ENABLED.ordinal)
        assertEquals(3, SettingType.VIBRATION_ENABLED.ordinal)
    }

    @Test
    fun `valueOf should work correctly`() {
        assertEquals(SettingType.DARK_THEME, SettingType.valueOf("DARK_THEME"))
        assertEquals(SettingType.TIMER_ENABLED, SettingType.valueOf("TIMER_ENABLED"))
        assertEquals(SettingType.SOUND_ENABLED, SettingType.valueOf("SOUND_ENABLED"))
        assertEquals(SettingType.VIBRATION_ENABLED, SettingType.valueOf("VIBRATION_ENABLED"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `valueOf should throw exception for invalid value`() {
        SettingType.valueOf("INVALID_SETTING")
    }

    @Test
    fun `enum should be serializable`() {
        // Test that enum values can be converted to string and back
        for (settingType in SettingType.values()) {
            val serialized = settingType.toString()
            val deserialized = SettingType.valueOf(serialized)
            assertEquals(settingType, deserialized)
        }
    }
}