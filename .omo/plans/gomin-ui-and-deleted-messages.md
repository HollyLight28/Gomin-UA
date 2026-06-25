# gomin-ui-and-deleted-messages - Work Plan

## TL;DR (For humans)
<!-- Fill this LAST, after the detailed plan below is written, so it summarizes the REAL plan. -->
<!-- Plain English for a non-engineer: NO file paths, NO todo numbers, NO wave/agent/tool names. -->

**What you'll get:** Rounded card-style UI for Gomin settings + ability to preserve all messages when contact deletes conversation, with clear visual indicators showing which messages were deleted by the other party.

**Why this approach:** Using custom Drawable for rounded corners (cleanest, most maintainable) + adding database field for delete tracking (most robust solution) + combining faded text with trash icon for non-intrusive but clear indicators.

**What it will NOT do:** Modify core Telegram files beyond 10 lines each, use any Cherrygram code, add Firebase dependencies, or break existing functionality.

**Effort:** Medium
**Risk:** Low - Feature disabled by default, reversible, no breaking changes
**Decisions I made for you:** Using RoundedBackgroundDrawable for UI, is_deleted_by_other database field for message tracking, faded text + trash icon for indicators, Ukrainian localization for all new strings.

Your next move: Approve the plan to start execution, or run high-accuracy review for detailed analysis.

---

> TL;DR (machine): <1 line - effort, risk, deliverables>

## Scope
### Must have
1. **Rounded corners for Gomin settings** - Card-style UI with rounded corners for all settings sections
2. **Deleted messages preservation** - When contact deletes conversation, ALL messages (both sides) remain visible locally
3. **Visual indicator** - Clear but non-intrusive indicator showing which messages were deleted by the other party
4. **Database migration** - Safe migration to add `is_deleted_by_other` field to messages table
5. **Ukrainian localization** - All new strings in Ukrainian

### Must NOT have (guardrails, anti-slop, scope boundaries)
1. **NO modifications to core Telegram files** beyond 10 lines per file (Gomin architecture rule)
2. **NO Cherrygram code** - Must not use any `uz.unnarsx.*` packages
3. **NO Firebase Analytics/Crashlytics** dependencies
4. **NO breaking existing functionality** - Feature must be disabled by default and reversible
5. **NO performance degradation** - Message loading must remain fast

## Verification strategy
> Zero human intervention - all verification is agent-executed.
- Test decision: none (manual testing as per CLAUDE.md - Telegram test framework incompatible)
- Evidence: .omo/evidence/task-<N>-gomin-ui-and-deleted-messages.<ext>
- Build verification: `./gradlew assembleAfatStandalone` must succeed
- Manual QA: Install APK, test both features visually

## Execution strategy
### Parallel execution waves
> Target 5-8 todos per wave. Fewer than 3 (except the final) means you under-split.

**Wave 1 (Foundation):** Tasks 1, 2 - Create utility classes and database migration (parallel)
**Wave 2 (Core Logic):** Tasks 3, 4 - Implement delete interception and message loading (parallel, depends on Wave 1)
**Wave 3 (UI Implementation):** Tasks 5, 6 - Create indicator helper and apply rounded corners (parallel, depends on Wave 2)
**Wave 4 (Message Display):** Tasks 7, 8 - Add indicators to chat UI (sequential, depends on Wave 3)
**Wave 5 (Polish):** Tasks 9, 10 - Add strings and final verification (sequential)

### Dependency matrix
| Todo | Depends on | Blocks | Can parallelize with |
| --- | --- | --- | --- |
| 1. RoundedBackgroundDrawable | none | 6 | 2 |
| 2. DB migration | none | 3, 4, 5 | 1 |
| 3. Delete interception | 2 | 5 | 4 |
| 4. Message loading | 2 | 5 | 3 |
| 5. Indicator helper | 4 | 7 | 6 |
| 6. Settings UI | 1 | 8 | 5 |
| 7. ChatMessageCell | 5 | 9 | 8 |
| 8. Chat header status | 7 | 10 | 9 |
| 9. Ukrainian strings | 7, 8 | 10 | none |
| 10. Final test | 6, 8, 9 | none | none |

## Todos
> Implementation + Test = ONE todo. Never separate.
<!-- APPEND TASK BATCHES BELOW THIS LINE WITH edit/apply_patch - never rewrite the headers above. -->

### Wave 1: Foundation (Parallel)
- [ ] 1. Create RoundedBackgroundDrawable utility
  What to do / Must NOT do: Create `ua.gomin.messenger.helpers.ui.RoundedBackgroundDrawable.java` - a custom Drawable that draws a rounded rectangle background. Must accept corner radius, background color, and margin/padding parameters. Must NOT modify any Telegram files.
  Parallelization: Wave 1 | Blocked by: none | Blocks: 2
  References: ua.gomin.messenger.helpers.ui.PopupHelper.java (for pattern), Android Canvas API
  Acceptance criteria: File compiles, can be instantiated with parameters, draw() produces rounded rectangle
  QA scenarios: Create instance, verify draw() calls canvas.drawRoundRect(). Evidence .omo/evidence/task-1-rounded-bg.xml
  Commit: Y | feat(helpers): add RoundedBackgroundDrawable for card-style UI

- [ ] 2. Create Database migration for is_deleted_by_other field
  What to do / Must NOT do: Add `is_deleted_by_other` INTEGER DEFAULT 0 column to `messages_v2` table in MessagesStorage.java. Create migration method that runs on app start. Must NOT break existing data or schema.
  Parallelization: Wave 1 | Blocked by: none | Blocks: 3, 4, 5
  References: org/telegram/messenger/MessagesStorage.java (database schema methods)
  Acceptance criteria: Migration runs without error, existing messages preserved, new field accessible
  QA scenarios: Run migration on existing database, verify column exists, verify data intact. Evidence .omo/evidence/task-2-db-migration.xml
  Commit: Y | feat(storage): add is_deleted_by_other column to messages table

### Wave 2: Core Logic (Parallel, depends on Wave 1)
- [ ] 3. Implement delete interception in MessagesController
  What to do / Must NOT do: Modify the delete dialog/message handling in MessagesController.java to check `shouldKeepDeleted()`. When true, instead of deleting, set `is_deleted_by_other = 1` on affected messages. Must NOT change behavior when feature is disabled.
  Parallelization: Wave 2 | Blocked by: 2 | Blocks: 5
  References: org/telegram/messenger/MessagesController.java (delete methods), ua/gomin/messenger/hooks/GominFeatureHooks.java
  Acceptance criteria: When feature enabled, delete request marks messages instead of removing them
  QA scenarios: Simulate delete from other user, verify messages marked but not removed. Evidence .omo/evidence/task-3-delete-interception.xml
  Commit: Y | feat(messages): intercept delete to preserve messages when feature enabled

- [ ] 4. Implement is_deleted_by_other query in MessagesStorage
  What to do / Must NOT do: Add method to load messages with `is_deleted_by_other` flag. Modify message loading queries to include this field. Must NOT affect performance of normal message loading.
  Parallelization: Wave 2 | Blocked by: 2 | Blocks: 5
  References: org/telegram/messenger/MessagesStorage.java (message loading methods)
  Acceptance criteria: Messages with is_deleted_by_other=1 are loaded and flagged correctly
  QA scenarios: Insert test message with flag=1, load it, verify flag is set. Evidence .omo/evidence/task-4-message-loading.xml
  Commit: Y | feat(storage): load is_deleted_by_other flag with messages

### Wave 3: UI Implementation (Parallel, depends on Wave 2)
- [ ] 5. Create GominDeletedMessageIndicator helper
  What to do / Must NOT do: Create `ua.gomin.messenger.helpers.ui.GominDeletedMessageIndicator.java` - utility class that provides methods to mark messages as deleted-by-other and check the flag. Must NOT modify MessageObject directly.
  Parallelization: Wave 3 | Blocked by: 4 | Blocks: 7
  References: ua/gomin/messenger/helpers/, org/telegram/messenger/MessageObject.java
  Acceptance criteria: Utility methods work correctly, can check/set flag on message objects
  QA scenarios: Create helper, test methods with mock messages. Evidence .omo/evidence/task-5-indicator-helper.xml
  Commit: Y | feat(helpers): add GominDeletedMessageIndicator utility

- [ ] 6. Apply rounded corners to GominSettingsEntry
  What to do / Must NOT do: Modify GominSettingsEntry.java to apply RoundedBackgroundDrawable to settings sections. Group items into card-like sections with rounded corners and proper margins. Must NOT break existing functionality.
  Parallelization: Wave 3 | Blocked by: 1 | Blocks: 8
  References: ua/gomin/messenger/preferences/GominSettingsEntry.java, ua/gomin/messenger/helpers/ui/RoundedBackgroundDrawable.java
  Acceptance criteria: Settings sections display with rounded corners and card appearance
  QA scenarios: Build and run, verify UI shows rounded corners on all sections. Evidence .omo/evidence/task-6-settings-ui.xml
  Commit: Y | feat(settings): apply card-style rounded corners to Gomin settings

### Wave 4: Message Display (Sequential, depends on Wave 3)
- [ ] 7. Add deleted-by-other indicator to ChatMessageCell
  What to do / Must NOT do: Modify ChatMessageCell.java to detect `is_deleted_by_other` flag and render indicator (faded text + small trash icon). Must NOT break normal message display. Mark changes with Gomin start/end comments.
  Parallelization: Wave 4 | Blocked by: 5 | Blocks: 9
  References: org/telegram/ui/Cells/ChatMessageCell.java, ua/gomin/messenger/helpers/ui/GominDeletedMessageIndicator.java
  Acceptance criteria: Messages with flag show faded text and trash icon
  QA scenarios: Display message with flag=1, verify visual indicator appears. Evidence .omo/evidence/task-7-message-indicator.xml
  Commit: Y | feat(chat): add deleted-by-other indicator to message cells

- [ ] 8. Add chat header status for deleted conversations
  What to do / Must NOT do: Modify ChatActivity.java to show status line "Цей діалог було видалено співрозмовником" when all messages in conversation have `is_deleted_by_other` flag. Must NOT show when feature disabled.
  Parallelization: Wave 4 | Blocked by: 7 | Blocks: 10
  References: org/telegram/ui/ChatActivity.java, ua/gomin/messenger/hooks/GominFeatureHooks.java
  Acceptance criteria: Status line appears when appropriate, hidden when feature disabled
  QA scenarios: Open deleted conversation, verify status line visible. Evidence .omo/evidence/task-8-chat-status.xml
  Commit: Y | feat(chat): add deleted conversation status indicator

### Wave 5: Polish & Verification (Sequential)
- [ ] 9. Add Ukrainian strings for new UI elements
  What to do / Must NOT do: Add strings to res-gomin/values-uk/gomin_strings.xml for: "Цей діалог було видалено співрозмовником", "Видалено", trash icon content description. Must NOT modify existing strings.
  Parallelization: Wave 5 | Blocked by: 7, 8 | Blocks: 10
  References: res-gomin/values-uk/gomin_strings.xml
  Acceptance criteria: All new strings present and properly formatted
  QA scenarios: Build app, verify strings appear correctly in Ukrainian. Evidence .omo/evidence/task-9-strings.xml
  Commit: Y | feat(i18n): add Ukrainian strings for deleted message indicators

- [ ] 10. Final integration test and cleanup
  What to do / Must NOT do: Run full build, test both features end-to-end, remove any debug code, verify all Gomin start/end comments present. Must NOT leave TODO comments or incomplete implementations.
  Parallelization: Wave 5 | Blocked by: 6, 8, 9 | Blocks: none
  References: All modified files
  Acceptance criteria: App builds successfully, both features work as expected, no compilation warnings
  QA scenarios: Build with `./gradlew assembleAfatStandalone`, install and test both features manually. Evidence .omo/evidence/task-10-final-test.xml
  Commit: Y | chore: final cleanup and integration verification

## Final verification wave
> Runs in parallel after ALL todos. ALL must APPROVE. Surface results and wait for the user's explicit okay before declaring complete.
- [ ] F1. Plan compliance audit - Verify all modifications follow Gomin architecture rules (max 10 lines per Telegram file, Gomin start/end comments)
- [ ] F2. Code quality review - Check for code smells, performance issues, and proper error handling
- [ ] F3. Real manual QA - Build and install APK, test both features on real device
- [ ] F4. Scope fidelity - Verify no Cherrygram code, no Firebase dependencies, no breaking changes

## Commit strategy

## Success criteria
1. **Settings UI:** All Gomin settings sections display with rounded corners and card-like appearance
2. **Deleted Messages:** When contact deletes conversation, ALL messages (both sides) remain visible locally
3. **Visual Indicator:** Deleted-by-other messages show faded text and trash icon
4. **Chat Status:** Status line "Цей діалог було видалено співрозмовником" appears in deleted conversations
5. **Performance:** No noticeable lag in message loading or settings navigation
6. **Build:** `./gradlew assembleAfatStandalone` succeeds without errors
7. **Architecture:** All modifications follow Gomin rules (max 10 lines per Telegram file, Gomin start/end comments)
