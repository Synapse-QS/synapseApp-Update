package com.synapse.social.studioasinc.shared.domain.service

import kotlinx.cinterop.*
import platform.AudioToolbox.*
import platform.CoreAudio.*
import platform.Foundation.*
import platform.posix.*

class AudioRecordState {
    var file: CPointer<FILE>? = null
    var isRecording: Boolean = false
}

val audioQueueInputCallback: AudioQueueInputCallback = staticCFunction { inUserData, inAQ, inBuffer, inStartTime, inNumberPacketDescriptions, inPacketDescs ->
    if (inUserData == null || inBuffer == null || inAQ == null) return@staticCFunction
    val state = inUserData.asStableRef<AudioRecordState>().get()

    if (!state.isRecording) return@staticCFunction

    val buffer = inBuffer.pointed
    val bytes = buffer.mAudioData
    val bytesSize = buffer.mAudioDataByteSize

    if (bytes != null && bytesSize > 0u && state.file != null) {
        fwrite(bytes, 1u, bytesSize.toULong(), state.file)
    }

    if (state.isRecording) {
        AudioQueueEnqueueBuffer(inAQ, inBuffer, 0u, null)
    }
}

actual class AudioRecorder {
    private var queue: AudioQueueRef? = null
    private var stateRef: StableRef<AudioRecordState>? = null
    private val numBuffers = 3
    private var allocatedBuffers = mutableListOf<AudioQueueBufferRef>()
    private var outputPath: String? = null

    actual fun startRecording(outputPath: String) {
        this.outputPath = outputPath

        val state = AudioRecordState().apply {
            file = fopen(outputPath, "wb")
            isRecording = true
        }
        stateRef = StableRef.create(state)

        memScoped {
            val format = alloc<AudioStreamBasicDescription>().apply {
                mSampleRate = 44100.0
                mFormatID = kAudioFormatLinearPCM
                mFormatFlags = kLinearPCMFormatFlagIsSignedInteger or kLinearPCMFormatFlagIsPacked
                mFramesPerPacket = 1u
                mChannelsPerFrame = 1u
                mBitsPerChannel = 16u
                mBytesPerPacket = 2u
                mBytesPerFrame = 2u
            }

            val queueVar = alloc<AudioQueueRefVar>()
            val status = AudioQueueNewInput(
                format.ptr,
                audioQueueInputCallback,
                stateRef!!.asCPointer(),
                null,
                null,
                0u,
                queueVar.ptr
            )

            if (status != 0) {
                stateRef?.dispose()
                stateRef = null
                if (state.file != null) fclose(state.file)
                return
            }

            val createdQueue = queueVar.value
            queue = createdQueue

            val bufferByteSize = 4096u
            for (i in 0 until numBuffers) {
                val bufferRef = alloc<AudioQueueBufferRefVar>()
                AudioQueueAllocateBuffer(createdQueue, bufferByteSize, bufferRef.ptr)
                bufferRef.value?.let {
                    allocatedBuffers.add(it)
                    AudioQueueEnqueueBuffer(createdQueue, it, 0u, null)
                }
            }

            AudioQueueStart(createdQueue, null)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun stopRecording(): ByteArray? {
        var result: ByteArray? = null
        queue?.let { q ->
            AudioQueueStop(q, true)
            AudioQueueDispose(q, true)
        }
        queue = null

        stateRef?.let { ref ->
            val state = ref.get()
            state.isRecording = false
            if (state.file != null) {
                fclose(state.file)
                state.file = null
            }
            ref.dispose()
        }
        stateRef = null
        allocatedBuffers.clear()

        outputPath?.let { path ->
            val data = NSData.dataWithContentsOfFile(path)
            if (data != null) {
                val bytes = data.bytes
                val length = data.length.toInt()
                if (bytes != null && length > 0) {
                    result = ByteArray(length)
                    result!!.usePinned { pinned ->
                        memcpy(pinned.addressOf(0), bytes, length.toULong())
                    }
                }
            }
        }

        return result
    }

    actual fun isRecording(): Boolean = stateRef?.get()?.isRecording == true
}
