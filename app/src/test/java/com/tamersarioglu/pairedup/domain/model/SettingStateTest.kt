package com.tamersarioglu.pairedup.domain.model

import org.junit.Assert.*
import org.junit.Test

class SettingStateTest {

    @Test
    fun `default SettingState should be in idle state`() {
        val settingState = SettingState()
        
        assertFalse(settingState.isLoading)
        assertFalse(settingState.hasError)
        assertFalse(settingState.showSuccess)
        assertTrue(settingState.isIdle)
        assertFalse(settingState.hasActiveFeedback)
    }

    @Test
    fun `loading state should not be idle and should have active feedback`() {
        val settingState = SettingState(isLoading = true)
        
        assertTrue(settingState.isLoading)
        assertFalse(settingState.hasError)
        assertFalse(settingState.showSuccess)
        assertFalse(settingState.isIdle)
        assertTrue(settingState.hasActiveFeedback)
    }

    @Test
    fun `error state should not be idle and should have active feedback`() {
        val settingState = SettingState(hasError = true)
        
        assertFalse(settingState.isLoading)
        assertTrue(settingState.hasError)
        assertFalse(settingState.showSuccess)
        assertFalse(settingState.isIdle)
        assertTrue(settingState.hasActiveFeedback)
    }

    @Test
    fun `success state should not be idle and should have active feedback`() {
        val settingState = SettingState(showSuccess = true)
        
        assertFalse(settingState.isLoading)
        assertFalse(settingState.hasError)
        assertTrue(settingState.showSuccess)
        assertFalse(settingState.isIdle)
        assertTrue(settingState.hasActiveFeedback)
    }

    @Test
    fun `multiple active states should not be idle and should have active feedback`() {
        val settingState = SettingState(
            isLoading = true,
            hasError = true,
            showSuccess = true
        )
        
        assertTrue(settingState.isLoading)
        assertTrue(settingState.hasError)
        assertTrue(settingState.showSuccess)
        assertFalse(settingState.isIdle)
        assertTrue(settingState.hasActiveFeedback)
    }

    @Test
    fun `copy function should work correctly`() {
        val originalState = SettingState(isLoading = true)
        val copiedState = originalState.copy(isLoading = false, hasError = true)
        
        assertFalse(copiedState.isLoading)
        assertTrue(copiedState.hasError)
        assertFalse(copiedState.showSuccess)
        assertFalse(copiedState.isIdle)
        assertTrue(copiedState.hasActiveFeedback)
    }

    @Test
    fun `equals should work correctly for data class`() {
        val state1 = SettingState(isLoading = true, hasError = false, showSuccess = false)
        val state2 = SettingState(isLoading = true, hasError = false, showSuccess = false)
        val state3 = SettingState(isLoading = false, hasError = true, showSuccess = false)
        
        assertEquals(state1, state2)
        assertNotEquals(state1, state3)
    }

    @Test
    fun `hashCode should work correctly for data class`() {
        val state1 = SettingState(isLoading = true, hasError = false, showSuccess = false)
        val state2 = SettingState(isLoading = true, hasError = false, showSuccess = false)
        
        assertEquals(state1.hashCode(), state2.hashCode())
    }
}