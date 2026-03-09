package com.synapse.social.studioasinc.core.util

import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor
import androidx.core.content.ContextCompat
import com.synapse.social.studioasinc.data.preferences.SettingsPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatLockManager @Inject constructor(
    private val settingsPreferences: SettingsPreferences
) {
    fun isChatLocked(chatId: String): Boolean {
        return settingsPreferences.getLockedChatIds().contains(chatId)
    }

    fun lockChat(chatId: String) {
        val currentLocked = settingsPreferences.getLockedChatIds().toMutableSet()
        currentLocked.add(chatId)
        settingsPreferences.setLockedChatIds(currentLocked)
    }

    fun unlockChat(chatId: String) {
        val currentLocked = settingsPreferences.getLockedChatIds().toMutableSet()
        currentLocked.remove(chatId)
        settingsPreferences.setLockedChatIds(currentLocked)
    }

    fun authenticate(
        activity: FragmentActivity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(activity)
        val biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errString.toString())
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onError("Authentication failed")
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Chat locked")
            .setSubtitle("Use your biometric credential to unlock this chat")
            .setNegativeButtonText("Cancel")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
