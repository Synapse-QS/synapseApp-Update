# OneSignal DM Notifications Implementation

## Summary
Successfully implemented OneSignal push notifications for Direct Messages (DM) in Synapse Social. The implementation follows Clean Architecture principles and integrates seamlessly with the existing notification system.

## Changes Made

### 1. NotificationConfig.kt
**Path:** `app/src/main/java/com/synapse/social/studioasinc/core/config/NotificationConfig.kt`

**Added:**
- `NOTIFICATION_TYPE_NEW_MESSAGE = "NEW_MESSAGE"` - Notification type constant for DMs
- `NOTIFICATION_TITLE_NEW_MESSAGE = "New Message"` - Title for DM notifications
- `TAG_MESSAGES = "messages"` - OneSignal tag for message notification preferences
- Updated `getTitleForNotificationType()` to handle `NEW_MESSAGE` type

### 2. NotificationHelper.kt
**Path:** `app/src/main/java/com/synapse/social/studioasinc/core/util/NotificationHelper.kt`

**Modified:**
- `sendMessageAndNotifyIfNeeded()` - Now uses `NOTIFICATION_TYPE_NEW_MESSAGE` constant instead of hardcoded "chat_message"
- Removed exclusion that prevented DM notifications from being persisted to database
- Enhanced `shouldSuppressPush()` to accept notification type parameter and check category-specific preferences
- Added logic to respect `messagesEnabled` preference for DM notifications

**Key Features:**
- DM notifications now respect user preferences (messagesEnabled)
- Honors Do Not Disturb (DND) mode
- Respects Quiet Hours settings
- Persists notifications to database for notification history
- Suppresses notifications when user is online/active

### 3. NotificationSettingsViewModel.kt
**Path:** `app/src/main/java/com/synapse/social/studioasinc/ui/settings/NotificationSettingsViewModel.kt`

**Modified:**
- `updateOneSignalTags()` - Added `TAG_MESSAGES` to OneSignal tag synchronization
- Ensures DM notification preferences are synced with OneSignal for server-side filtering

## How It Works

### Flow Diagram
```
User sends DM
    ↓
ChatViewModel.performSendMessage()
    ↓
NotificationHelper.sendMessageAndNotifyIfNeeded()
    ↓
NotificationHelper.sendNotification()
    ↓
Check preferences (DND, Quiet Hours, messagesEnabled)
    ↓
Persist to database (notifications table)
    ↓
Send push via Edge Function (if not suppressed)
    ↓
OneSignal delivers notification to recipient
```

### Notification Suppression Logic
Notifications are suppressed when:
1. **Do Not Disturb** is enabled
2. **Quiet Hours** are active (time-based)
3. **Messages notifications** are disabled in preferences
4. **User is online** (status = "online")
5. **Recipient and sender are the same** (self-messaging)

### Database Persistence
DM notifications are now persisted to the `notifications` table with:
- `type`: "NEW_MESSAGE"
- `recipient_id`: User receiving the notification
- `sender_id`: User who sent the message
- `data`: Contains `chat_id` and `message` content
- `is_read`: false (initially)
- `created_at`: Timestamp

## User Preferences Integration

The implementation respects the existing `NotificationPreferences` model:
- `messagesEnabled: Boolean` - Controls whether DM notifications are sent
- `doNotDisturb: Boolean` - Global DND toggle
- `quietHoursEnabled: Boolean` - Time-based notification suppression
- `quietHoursStart/End: String` - Time window for quiet hours

Users can toggle DM notifications in Settings → Notifications, and the preference is automatically synced with OneSignal tags.

## OneSignal Tags
The following tag is now synchronized:
- `messages`: "true" or "false" based on user preference

This enables server-side filtering of notifications before they're sent.

## Testing Checklist

- [ ] Send a DM and verify notification is received
- [ ] Enable DND and verify DM notifications are suppressed
- [ ] Set Quiet Hours and verify notifications are suppressed during that window
- [ ] Disable "Messages" in notification settings and verify no notifications are sent
- [ ] Verify notifications are persisted to database
- [ ] Check that notification title shows "New Message"
- [ ] Verify OneSignal tags are updated when toggling message preferences
- [ ] Test that online users don't receive notifications
- [ ] Verify deep linking works (tapping notification opens the chat)

## Edge Function Requirements

Ensure your Supabase Edge Function (`send-push-notification`) handles the `NEW_MESSAGE` notification type:

```typescript
if (notificationType === "NEW_MESSAGE") {
  // Send push notification via OneSignal REST API
  // Use recipient's one_signal_player_id from users table
  // Include chat_id in data payload for deep linking
}
```

## Configuration

No additional configuration needed. The implementation uses existing:
- `ONESIGNAL_APP_ID` from BuildConfig
- `EDGE_FUNCTION_SEND_PUSH` endpoint
- Existing notification preferences table structure

## Architecture Compliance

✅ **Clean Architecture** - Business logic in Domain layer, implementation in Data layer
✅ **No Hardcoded Values** - Uses constants from NotificationConfig
✅ **Repository Pattern** - Uses SupabaseDatabaseService for data access
✅ **Separation of Concerns** - Notification logic isolated in NotificationHelper
✅ **Existing Patterns** - Follows same pattern as other notification types

## Future Enhancements

Potential improvements:
1. Add notification sound customization for DMs
2. Implement notification grouping for multiple messages from same chat
3. Add "Mark as Read" action in notification
4. Support for rich media previews in notifications
5. Add notification badges for unread message count

## Related Files

- `shared/src/commonMain/kotlin/com/synapse/social/studioasinc/shared/domain/model/chat/Message.kt`
- `app/src/main/java/com/synapse/social/studioasinc/feature/inbox/inbox/ChatViewModel.kt`
- `app/src/main/java/com/synapse/social/studioasinc/ui/settings/SettingsModels.kt`
- `shared/src/commonMain/kotlin/com/synapse/social/studioasinc/shared/domain/repository/NotificationRepository.kt`

---

**Build Status:** ✅ Successful
**Compilation:** ✅ No errors
**Architecture:** ✅ Compliant with AGENTS.md guidelines
