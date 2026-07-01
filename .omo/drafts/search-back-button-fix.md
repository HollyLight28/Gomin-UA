---
slug: search-back-button-fix
status: awaiting-approval
intent: clear
pending-action: write .omo/plans/search-back-button-fix.md
approach: Fix state synchronization by adding toggleSearch(true) call inside DialogsActivity.search() after showSearch(). This is the natural fix location since search() is the method that opens search, and keeps the change inside one file without exposing private fields.
---

# Draft: search-back-button-fix

## Components (topology ledger)

| id | outcome | status | evidence path |
|----|---------|--------|---------------|
| C1 | Back gesture closes search panel | active | MainTabsActivity.java:685-710, DialogsActivity.java:7181-7191 |
| C2 | Chats tab tap closes search when already on Chats | active | MainTabsActivity.java:325-338 |
| C3 | Search opens correctly from Search tab button | active (works) | MainTabsActivity.java:755-775 |

## Findings (cited)

### Root Cause: State Synchronization Bug

Two independent search state mechanisms exist:
1. **`animatorSearchVisible`** (BoolAnimator in DialogsActivity, line 299) — controls visual search overlay
2. **`SearchTextWatcher.searchIsExpanded`** (SearchTextWatcher.java:26) — tracks text watcher expanded state

When search is opened via the Search tab button:
- `onSearchButtonClick()` (MainTabsActivity.java:755) calls `dialogsActivity.search("", true)`
- `search()` (DialogsActivity.java:7340) calls `showSearch(true, false, true)`
- `showSearch(true, ...)` (DialogsActivity.java:7367) sets `animatorSearchVisible.setValue(true, animated)` at line 7368
- **BUT it does NOT call `fragmentSearchFieldWatcher.toggleSearch(true)`**
- So `SearchTextWatcher.searchIsExpanded` remains `false`

When back is pressed:
- `DialogsActivity.onBackPressed()` (line 7181) checks `animatorSearchVisible.getValue()` → true
- Clears text field (line 7183-7185)
- Calls `fragmentSearchFieldWatcher.toggleSearch(false)` (line 7188)
- `toggleSearch(false)` (SearchTextWatcher.java:52) checks `searchIsExpanded == expand` → `false == false` → **returns false, does NOTHING**
- Search never closes

When Chats tab is tapped while on Chats with search open:
- Line 327 checks `dialogsActivity.isSearchVisible()` → true
- Calls `dialogsActivity.closeSearch()` (line 330)
- `closeSearch()` (DialogsActivity.java:7353) calls `fragmentSearchFieldWatcher.toggleSearch(false)`
- Same problem: `searchIsExpanded` is already false → toggle returns false → search stays open

### Key Code Paths

- `SearchTextWatcher.toggleSearch()` (SearchTextWatcher.java:52-70): Guards with `searchIsExpanded == expand` check
- `SearchTextWatcher.afterTextChanged()` (SearchTextWatcher.java:34-46): Sets `searchIsExpanded = true` only when text goes from empty to non-empty
- `DialogsActivity.onBackPressed()` (DialogsActivity.java:7181-7191): Relies on `toggleSearch(false)` to close search
- `DialogsActivity.search()` (DialogsActivity.java:7340-7346): Opens search via showSearch() without syncing watcher state
- `MainTabsActivity.onSearchButtonClick()` (MainTabsActivity.java:755-775): Calls dialogsActivity.search()

### Metis Findings

- `fragmentSearchFieldWatcher` is `private` in DialogsActivity (line 484) — cannot access from MainTabsActivity
- Recommended fix location: `DialogsActivity.search()` — add `toggleSearch(true)` after `showSearch()`
- This keeps the change inside one file and is the natural fix location

## Decisions

1. **Fix location**: `DialogsActivity.search()` (line 7340-7346) — add `fragmentSearchFieldWatcher.toggleSearch(true)` after `showSearch(true, false, animated)`
2. **Why this approach**: Most natural location — `search()` is the method that opens search. Keeps change inside one file. No private field access issues.
3. **Alternative considered**: Adding public getter in DialogsActivity — rejected as unnecessary indirection when the fix belongs in search() itself.
4. **Double-toggle safety**: `toggleSearch(true)` calls `onSearchExpand()` which calls `showSearch(true, ...)` again, but `searchIsShowed` guard (line 3297) prevents double-show.

## Scope IN

- Fix back gesture closing search when opened via Search tab button
- Fix Chats tab tap closing search when already on Chats tab
- Ensure `searchIsExpanded` state is synchronized when search opens via `search()` method

## Scope OUT (Must NOT have)

- Changes to the search panel UI or animation
- Changes to how search works when opened from the ActionBar search field (already works correctly)
- Changes to other tabs' behavior
- New features or unrelated refactoring
- Changes to MainTabsActivity.java (fix is in DialogsActivity only)

## Open questions

None — the root cause is clear and the fix is deterministic.

## Approval gate
status: awaiting-approval
