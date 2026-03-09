# Firebase Configuration for OneSignal Push Notifications

## Overview
OneSignal requires Firebase Cloud Messaging (FCM) for push notifications on Android. This guide explains how to set up the `google-services.json` file.

## Prerequisites
- A Firebase project (create one at [Firebase Console](https://console.firebase.google.com/))
- Access to the Synapse Social codebase

## Steps to Configure

### 1. Create/Access Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or select an existing one
3. Name it appropriately (e.g., "Synapse Social")

### 2. Add Android App to Firebase
1. In Firebase Console, click "Add app" and select Android
2. Enter the package name: `com.synapse.social.studioasinc`
3. (Optional) Add app nickname: "Synapse Social"
4. (Optional) Add SHA-1 certificate fingerprint for Google Sign-In
5. Click "Register app"

### 3. Download google-services.json
1. Download the `google-services.json` file from Firebase Console
2. Place it in the `app/` directory of the project:
   ```
   synapseApp/
   └── app/
       └── google-services.json  ← Place here
   ```

### 4. Get FCM Server Key for OneSignal
1. In Firebase Console, go to **Project Settings** → **Cloud Messaging**
2. Under "Cloud Messaging API (Legacy)", enable it if disabled
3. Copy the **Server Key**
4. Go to [OneSignal Dashboard](https://app.onesignal.com/)
5. Navigate to **App Settings** → **Platforms** → **Google Android (FCM)**
6. Paste the Firebase Server Key
7. Save the configuration

### 5. Verify Configuration
The `google-services.json` file should contain:
```json
{
  "project_info": {
    "project_number": "YOUR_PROJECT_NUMBER",
    "project_id": "your-project-id",
    "storage_bucket": "your-project.appspot.com"
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "1:YOUR_PROJECT_NUMBER:android:...",
        "android_client_info": {
          "package_name": "com.synapse.social.studioasinc"
        }
      }
    }
  ]
}
```

## Security Notes
- **NEVER commit `google-services.json` to version control** (already in `.gitignore`)
- Each developer needs their own copy for local development
- CI/CD uses GitHub Secrets to inject the file during builds

## Troubleshooting

### Error: "Default FirebaseApp failed to initialize"
- Ensure `google-services.json` is in the `app/` directory
- Verify the package name matches: `com.synapse.social.studioasinc`
- Clean and rebuild the project

### Error: "Missing Google Project number"
- Verify FCM Server Key is configured in OneSignal Dashboard
- Check that Cloud Messaging API is enabled in Firebase Console
- Ensure `google-services.json` contains valid `project_number`

## CI/CD Configuration
For GitHub Actions, the `google-services.json` is generated from the `GOOGLE_SERVICES_JSON` secret. See `AGENTS.md` for details.

## References
- [Firebase Console](https://console.firebase.google.com/)
- [OneSignal FCM Setup](https://documentation.onesignal.com/docs/android-sdk-setup)
- [Google Services Plugin](https://developers.google.com/android/guides/google-services-plugin)
