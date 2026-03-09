# Implement E2EE with Signal Protocol Encryption

Based on the prompt "Implement E2EE with signal protocol encryption" and my investigation of the codebase:

## Background
- [AndroidSignalProtocolManager](file:///c:/Users/Ashik/Documents/synapseApp/shared/src/androidMain/kotlin/com/synapse/social/studioasinc/shared/data/crypto/AndroidSignalProtocolManager.kt#22-115) is already fully implemented using the official Java library `org.whispersystems:signal-protocol-android`.
- [IosSignalProtocolManager](file:///c:/Users/Ashik/Documents/synapseApp/shared/src/iosMain/kotlin/com/synapse/social/studioasinc/shared/data/crypto/IosSignalProtocolManager.kt#8-41) throws `NotImplementedError` for all methods.
- The project is a KMP project, and the rules explicitly state: "*Crypto: Use CoreCrypto (C API) via Kotlin/Native cinterop. Avoid CryptoKit bridges.*"

## User Review Required
> [!WARNING]
> Implementing the precise Signal Protocol (X3DH, Double Ratchet, Protobuf message packing) strictly using **CoreCrypto primitives** (AES, HMAC, ECDH) in Kotlin from scratch to perfectly interoperate with the Android `org.whispersystems` library represents an enormous amount of highly sensitive security code. 
> 
> **Questions for the user:**
> 1. Do you want me to write a custom, pure Kotlin implementation of the Double Ratchet and X3DH protocols that uses `CoreCrypto` exclusively for the raw primitives (AES/HMAC/Curve25519) on iOS?
> 2. Alternatively, would you prefer generating a `.def` cinterop file for a compiled C/C++ library like `libsignal-client` (the official Signal FFI), rather than building the protocol from scratch using `CoreCrypto`?

## Proposed Changes

### iOS Native Crypto Primitives
If proceeding with a custom implementation using CoreCrypto primitives:
- Create `cinterop` definitions for `corecrypto`.
- Implement ECDH (Curve25519), AES-256-GCM, and HMAC-SHA256 wrappers using CoreCrypto C functions within `iosMain`.

### IosSignalProtocolManager Implementation
- Implement [generateIdentityAndKeys()](file:///c:/Users/Ashik/Documents/synapseApp/shared/src/commonMain/kotlin/com/synapse/social/studioasinc/shared/data/crypto/SignalProtocolManager.kt#9-10) to create identity keypairs.
- Implement [processPreKeyBundle()](file:///c:/Users/Ashik/Documents/synapseApp/shared/src/commonMain/kotlin/com/synapse/social/studioasinc/shared/data/crypto/SignalProtocolManager.kt#12-13) to handle X3DH using the CoreCrypto primitives.
- Implement [encryptMessage()](file:///c:/Users/Ashik/Documents/synapseApp/shared/src/commonMain/kotlin/com/synapse/social/studioasinc/shared/data/crypto/SignalProtocolManager.kt#15-16) and [decryptMessage()](file:///c:/Users/Ashik/Documents/synapseApp/shared/src/androidMain/kotlin/com/synapse/social/studioasinc/shared/data/crypto/AndroidSignalProtocolManager.kt#94-106) to handle the Double Ratchet state machine.
- Maintain session state in a local key store (similar to `AndroidSignalProtocolStore`).

## Verification Plan
### Automated testing
- Add common tests ensuring Android and iOS Signal Managers can exchange and encrypt/decrypt messages with each other.

### Manual Verification
- Deploy to an iOS simulator and an Android emulator/device.
- Start a chat between the two clients and verify E2EE messages are correctly encrypted and decrypted on both ends.
