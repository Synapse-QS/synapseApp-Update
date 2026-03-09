package com.synapse.social.studioasinc.core.di.ai

import com.synapse.social.studioasinc.BuildConfig
import com.synapse.social.studioasinc.data.repository.ai.AiRepositoryImpl
import com.synapse.social.studioasinc.domain.repository.ai.AiRepository
import com.synapse.social.studioasinc.domain.usecase.ai.GenerateSmartRepliesUseCase
import com.synapse.social.studioasinc.domain.usecase.ai.SummarizeChatUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AiModule {

    @Provides
    @Singleton
    fun provideAiRepository(): AiRepository {
        return AiRepositoryImpl(BuildConfig.GEMINI_API_KEY)
    }

    @Provides
    @Singleton
    fun provideGenerateSmartRepliesUseCase(
        aiRepository: AiRepository
    ): GenerateSmartRepliesUseCase {
        return GenerateSmartRepliesUseCase(aiRepository)
    }

    @Provides
    @Singleton
    fun provideSummarizeChatUseCase(
        aiRepository: AiRepository
    ): SummarizeChatUseCase {
        return SummarizeChatUseCase(aiRepository)
    }
}
