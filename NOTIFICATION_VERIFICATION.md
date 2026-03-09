# Notification System Verification - Issue #98

## Changes Made

### 1. Switched to Server-Side Notifications
- Set `USE_CLIENT_SIDE_NOTIFICATIONS = false` in `NotificationConfig.kt`
- OneSignal SDK now skips initialization (no more errors)
- All notifications now use Supabase Edge Function: `send-push-notification`

### 2. Fixed Duplicate DM Notifications
**Problem:** DM notifications were sent twice:
- Once by `SupabaseChatRepository` (shared module)
- Once by `ChatViewModel` (Android app)

**Solution:** Removed duplicate calls from `ChatViewModel`:
- Text messages
- Media messages (images, videos, audio)
- Edited messages

**Result:** Each DM now triggers exactly ONE Edge Function call from the repository.

### 3. Notification Flow (All Types)

#### Direct Messages (DMs)
```
User sends message
  ↓
ChatViewModel → SendMessageUseCase
  ↓
SupabaseChatRepository.sendMessage()
  ↓
Inserts message to DB
  ↓
Calls: dataSource.sendMessageNotification()
  ↓
Edge Function: send-push-notification
```

#### Other Notifications (Likes, Comments, Follows, Mentions)
```
User action (like/comment/follow)
  ↓
NotificationUtils.sendXXXNotification()
  ↓
NotificationHelper.sendNotification()
  ↓
Persists to notifications table
  ↓
Checks user preferences (DND, quiet hours)
  ↓
sendPushViaEdgeFunction()
  ↓
Edge Function: send-push-notification
```

### 4. Edge Function Responsibilities
The `send-push-notification` Edge Function should:
- Receive notification request with recipient_id, message, type, data
- Look up recipient's OneSignal player ID from users table
- Call OneSignal REST API to send push notification
- Handle errors gracefully

### 5. Configuration Status

**Current Setup:**
- ✅ Server-side notifications enabled
- ✅ OneSignal initialization skipped
- ✅ All notifications use Edge Function
- ✅ No duplicate notifications
- ✅ Proper error handling

**What's NOT Used:**
- ❌ Cloudflare Workers (legacy constant, never used)
- ❌ OneSignal client SDK for sending (only for receiving)
- ❌ Direct OneSignal REST API calls from app

### 6. Testing Checklist

To verify notifications work:

1. **DM Notifications:**
   - Send text message → Check Edge Function logs
   - Send image/video → Check Edge Function logs
   - Edit message → Should NOT trigger notification

2. **Social Notifications:**
   - Like a post → Check Edge Function logs
   - Comment on post → Check Edge Function logs
   - Follow user → Check Edge Function logs
   - Mention user → Check Edge Function logs

3. **User Preferences:**
   - Enable DND → Notifications should be suppressed
   - Set quiet hours → Notifications suppressed during window
   - Disable category → That type should be suppressed

4. **Edge Function Logs:**
   - Check Supabase Dashboard → Edge Functions → send-push-notification
   - Verify requests are received
   - Check for errors

### 7. Known Issues Fixed

- ✅ LazyColumn duplicate keys (Feed & Chat)
- ✅ OneSignal 403 Forbidden errors (SDK now skipped)
- ✅ Missing Google Project Number errors (SDK now skipped)
- ✅ Duplicate DM notifications
- ✅ Inverted notification logic

### 8. Files Modified

1. `app/src/main/java/com/synapse/social/studioasinc/core/config/NotificationConfig.kt`
   - Changed `USE_CLIENT_SIDE_NOTIFICATIONS` to `false`
   - Fixed notification system description

2. `app/src/main/java/com/synapse/social/studioasinc/core/SynapseApplication.kt`
   - Added check to skip OneSignal initialization when using Edge Functions

3. `app/src/main/java/com/synapse/social/studioasinc/core/util/NotificationHelper.kt`
   - Fixed inverted logic (client-side vs server-side)
   - Both modes now use Edge Function (correct behavior)

4. `app/src/main/java/com/synapse/social/studioasinc/feature/inbox/inbox/ChatViewModel.kt`
   - Removed duplicate notification calls for DMs

5. `app/src/main/java/com/synapse/social/studioasinc/domain/model/FeedItem.kt`
   - Added `uniqueKey` property to prevent duplicate keys

6. `app/src/main/java/com/synapse/social/studioasinc/feature/home/home/FeedScreen.kt`
   - Use `uniqueKey` instead of `id` for LazyColumn items

7. `app/src/main/java/com/synapse/social/studioasinc/feature/inbox/inbox/screens/ChatScreen.kt`
   - Simplified key to just use `message.id`

## Summary

The app now uses **server-side notifications exclusively** via Supabase Edge Functions. OneSignal SDK is not initialized, eliminating all OneSignal-related errors. All notification types (DMs, likes, comments, follows) go through a single, consistent path to the Edge Function, with no duplicates.
