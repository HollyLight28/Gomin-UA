# search-back-button-fix - Work Plan

## TL;DR (For humans)

**What you'll get:** Виправлення багу, при якому жест "назад" та натискання на вкладку "Чати" не закривали пошук, відкритий через кнопку пошуку в нижньому барі.

**Why this approach:** Проблема у тому, що при відкритті пошуку через кнопку Search в нижньому барі, стан `searchIsExpanded` в `SearchTextWatcher` не синхронізується з `animatorSearchVisible`. Тому `toggleSearch(false)` при натисканні "назад" нічого не робить — він думає, що пошук вже закритий. Фікс: додати виклик `toggleSearch(true)` в метод `DialogsActivity.search()` після `showSearch()`.

**What it will NOT do:** Не змінює UI пошуку, анімації, або поведінку пошуку коли він відкривається з ActionBar. Не змінює MainTabsActivity.java.

**Effort:** Quick
**Risk:** Low - мінімальна зміна в одному методі DialogsActivity.search()
**Decisions to sanity-check:** Немає — кореневе причина зрозуміла, фікс детермінований.

Your next move: схвалити план, або запустити high-accuracy review.

---

> TL;DR (machine): Quick fix — add toggleSearch(true) in DialogsActivity.search() to sync SearchTextWatcher state. Single-file change in one method. Low risk.

## Scope
### Must have
- Жест "назад" закриває пошук, відкритий через кнопку Search в нижньому барі
- Натискання на вкладку "Чати" закриває пошук якщо вже на вкладці "Чати"
- `SearchTextWatcher.searchIsExpanded` синхронізується з `animatorSearchVisible` при відкритті пошуку через `search()` метод

### Must NOT have (guardrails, anti-slop, scope boundaries)
- Зміни UI пошуку або анімацій
- Зміни поведінки пошуку при відкритті з ActionBar search field (вже працює коректно)
- Зміни поведінки інших вкладок
- Зміни в MainTabsActivity.java
- Нові фічі або рефакторинг не по темі

## Verification strategy
> Zero human intervention - all verification is agent-executed.
- Test decision: none (автотести не пишуться через несумісність тестового фреймворку Telegram — дивись CLAUDE.md)
- Evidence: manual QA via build + install on device/emulator

## Execution strategy
### Parallel execution waves
> Single wave — one small targeted fix.

### Dependency matrix
| Todo | Depends on | Blocks | Can parallelize with |
| --- | --- | --- | --- |
| 1 | — | F1-F4 | — |

## Todos
> Implementation + Test = ONE todo. Never separate.
<!-- APPEND TASK BATCHES BELOW THIS LINE WITH edit/apply_patch - never rewrite the headers above. -->
- [ ] 1. Fix SearchTextWatcher state sync in DialogsActivity.search()
  What to do / Must NOT do:
  In `DialogsActivity.search()` (line 7340-7346), add a call to `fragmentSearchFieldWatcher.toggleSearch(true)` after `showSearch(true, false, animated)` to ensure the `SearchTextWatcher.searchIsExpanded` state is set to `true` when search opens.

  Current code (line 7340-7346):
  ```java
  public void search(String query, boolean animated) {
      showSearch(true, false, animated);
      if (fragmentSearchField != null) {
          fragmentSearchField.editText.setText(query);
          fragmentSearchField.editText.setSelection(query.length());
      }
  }
  ```

  Fixed code:
  ```java
  public void search(String query, boolean animated) {
      showSearch(true, false, animated);
      if (fragmentSearchField != null) {
          fragmentSearchField.editText.setText(query);
          fragmentSearchField.editText.setSelection(query.length());
      }
      /** Gomin start — sync SearchTextWatcher state so back button and tab switching can close search */
      if (fragmentSearchFieldWatcher != null) {
          fragmentSearchFieldWatcher.toggleSearch(true);
      }
      /** Gomin end */
  }
  ```

  Must NOT: modify any other files, change the search animation, or alter the back button handling logic.

  Parallelization: Wave 1 | Blocked by: — | Blocks: F1-F4
  References:
  - DialogsActivity.java:7340-7346 (search method — fix location)
  - DialogsActivity.java:7367-7368 (showSearch sets animatorSearchVisible)
  - DialogsActivity.java:7181-7191 (onBackPressed relies on toggleSearch)
  - DialogsActivity.java:7353-7360 (closeSearch relies on toggleSearch)
  - DialogsActivity.java:484 (fragmentSearchFieldWatcher field declaration)
  - DialogsActivity.java:3260-3303 (onSearchExpand callback)
  - DialogsActivity.java:3319-3336 (onSearchCollapse callback)
  - SearchTextWatcher.java:52-70 (toggleSearch guard: searchIsExpanded == expand)
  - SearchTextWatcher.java:34-46 (afterTextChanged sets searchIsExpanded)
  - MainTabsActivity.java:755-775 (onSearchButtonClick calls dialogsActivity.search)
  - MainTabsActivity.java:314-318 (Search tab click handler)
  Acceptance criteria (agent-executable):
  1. `./gradlew assembleAfatStandalone` succeeds without compilation errors
  2. Grep for `fragmentSearchFieldWatcher.toggleSearch(true)` in DialogsActivity.java returns exactly 1 match inside the search() method
  3. The `/** Gomin start */` / `/** Gomin end */` markers properly wrap the new code
  4. No changes in any file other than DialogsActivity.java
  QA scenarios:
  - Happy path: Build APK, install on device, tap Search tab → search opens → press back → search closes → tap Chats tab → stays on Chats without search
  - Happy path: Tap Search tab → search opens → tap Chats tab → search closes, stays on Chats
  - Happy path: Tap Search tab from Contacts tab → search opens on Chats → press back → returns to Contacts
  - Regression: Open search from ActionBar search field → type query → press back → search closes normally (existing behavior preserved)
  - Failure: If `fragmentSearchFieldWatcher` is null (shouldn't happen but guard it), no crash
  Evidence: .omo/evidence/task-1-search-back-button-fix.txt
  Commit: Y | fix(search): sync SearchTextWatcher state when opening search via bottom bar Search tab

## Final verification wave
> Runs in parallel after ALL todos. ALL must APPROVE. Surface results and wait for the user's explicit okay before declaring complete.
- [ ] F1. Plan compliance audit — verify the fix is in exactly one place (DialogsActivity.search()), no other files modified, Gomin markers present
- [ ] F2. Code quality review — verify no null pointer risks, proper guard on fragmentSearchFieldWatcher, code style matches surrounding code
- [ ] F3. Real manual QA — build standalone APK, install, test all 3 scenarios above on device/emulator
- [ ] F4. Scope fidelity — confirm no changes outside DialogsActivity.java, no new files, no unrelated modifications

## Commit strategy
Single atomic commit: `fix(search): sync SearchTextWatcher state when opening search via bottom bar Search tab`
File: `TMessagesProj/src/main/java/org/telegram/ui/DialogsActivity.java`

## Success criteria
1. Back gesture closes search opened via Search tab button
2. Tapping Chats tab while on Chats with search open closes search
3. Tapping Search tab from non-Chats tab opens search and returns to Chats
4. Build succeeds with `./gradlew assembleAfatStandalone`
5. No regressions in search behavior when opened from ActionBar search field
