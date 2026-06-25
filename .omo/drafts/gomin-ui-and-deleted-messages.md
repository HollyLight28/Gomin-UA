# Draft: UI Rounded Corners + Deleted Messages Preservation

**Status:** awaiting-approval
**Pending action:** write .omo/plans/gomin-ui-and-deleted-messages.md
**Approach:** 5-wave parallel execution with 10 tasks + 4 final verification tasks

## Research Findings

### Issue 1: Settings UI - Rounded Corners

**Current State:**
- `GominSettingsEntry.java` extends `UniversalFragment`
- Uses `TextCheckCell` and `NotificationsCheckCell` for settings items
- These cells use standard Telegram styling with sharp corners
- Background is set via `Theme.getColor(Theme.key_windowBackgroundGray)`

**Problem:**
- Settings items have sharp corners and stick to screen edges
- User wants "card-like" appearance with rounded corners

**Solution Options:**
1. **Custom Cell Wrapper** - Create a wrapper view that adds rounded corners and margin to each cell
2. **RecyclerView ItemDecoration** - Add margins and background via ItemDecoration
3. **Custom Background Drawable** - Create a RoundedBackgroundDrawable that can be applied to cells

**Recommended:** Option 3 - Create a reusable `RoundedBackgroundDrawable` that can be applied to cells via a new helper method in `SettingsHelper`.

### Issue 2: Deleted Messages Preservation

**Current State:**
- Feature flag: `GominPrivacyConfig.getKeepDeletedMessages()`
- Hook: `GominFeatureHooks.shouldKeepDeleted()`
- Implementation in `MessagesStorage.java`:
  - Lines 13834-13857: When loading messages, if `shouldKeepDeleted()` is true and message is deleted (status == 0), it adds message to `messagesToGhost` list instead of deleting
  - Lines 14722-14736: When deleting messages from a dialog, if `shouldKeepDeleted()` is true, only outgoing messages are deleted (preserves incoming)
- Implementation in `MessagesController.java`:
  - Lines 19566-19569: When user is kicked from a chat, if `shouldKeepDeleted()` is true, dialog is NOT deleted
  - Lines 19609-19612: Same for group chats

**Problem:**
- Current implementation only handles:
  1. Being kicked from a group (keeps dialog)
  2. Deleting messages from channels (only deletes outgoing)
- Does NOT handle: When the OTHER person deletes the entire private chat conversation
- When contact deletes dialog:
  - Only their messages remain visible
  - User's messages disappear
  - This is because Telegram's server deletes the entire dialog for both sides

**Root Cause:**
- The current hook is placed in the wrong location
- Need to intercept the delete operation BEFORE it reaches the server
- Need to preserve ALL messages (both incoming and outgoing) when the other person deletes

**Solution:**
1. Add a new field to messages table: `is_deleted_by_other` (boolean)
2. When receiving a delete request from the other person:
   - Instead of deleting, mark messages as `is_deleted_by_other = 1`
   - Keep the dialog in the list
3. When displaying messages:
   - Show messages with `is_deleted_by_other = 1` with a special indicator
4. UI Indicator options:
   - Small trash icon next to the message
   - Faded/italic text style
   - "Deleted" badge in the corner
   - Status line in chat header: "This conversation was deleted by the other party"

**Recommended:** Combine approaches:
- Add `is_deleted_by_other` field to messages
- Show a subtle indicator (faded text + small trash icon) on deleted-by-other messages
- Add a status line in the chat: "Цей діалог було видалено співрозмовником"

## Decisions Made

1. **UI Rounded Corners:** Use `RoundedBackgroundDrawable` approach - cleanest, most maintainable
2. **Deleted Messages:** Add database field + UI indicator - most robust solution
3. **Visual Indicator:** Faded text + small trash icon - non-intrusive but clear
4. **Database Schema:** Add column to `messages_v2` table with migration

## Open Questions

None - this is UNCLEAR intent, so I'm adopting best-practice defaults.
