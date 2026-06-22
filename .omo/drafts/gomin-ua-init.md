---
slug: gomin-ua-init
status: awaiting-approval
intent: clear
pending-action: write .omo/plans/gomin-ua-init.md
approach: Create CLAUDE.md constitution, scaffold Gomin package structure, implement settings menu as UniversalFragment, then port features one-by-one as add-ons
---

# Draft: gomin-ua-init

## Components (topology ledger)

| id | outcome | status | evidence path |
|----|---------|--------|---------------|
| CLAUDE.md | Project constitution that enforces add-on architecture, forbids Cherrygram | active | draft below |
| Gomin package structure | `ua.gomin.messenger.*` with configs, preferences, ai, alerts, etc. | active | Gomin-UA is clean Telegram 12.8.1 with zero Gomin code |
| Settings menu | UniversalFragment with all Homin feature sections | active | Old `CGPreferencesEntry.java` at `Gomin/TMessagesProj/.../cherrygram/preferences/CGPreferencesEntry.java` |
| Telegram entry hook | Minimal edits to ProfileActivity to add "Homin Settings" button | active | Old code at `Gomin/.../ProfileActivity.java:13698-13701` and `10552-10553` |
| Gomin AI (Gemini) | Chat + Live Voice + Mic Service + Shield | deferred | Catalog Part 1 |
| Air Alert | Polling + push + notifications | deferred | Catalog Part 1 |
| Ghost Mode | 4 privacy switches | deferred | Catalog Part 1 |
| Speed Engine | Download/Upload boost | deferred | Catalog Part 1 |
| Black Edition | OLED theme | deferred | Catalog Part 1 |
| Cherrygram features | Solar Icons, CameraX, Translator, etc. (selective) | deferred | Catalog Part 2 |

## Open assumptions (announced defaults)

| assumption | adopted default | rationale | reversible? |
|------------|----------------|-----------|-------------|
| All feature toggles use SharedPreferences via Config objects | Yes, same pattern as old Gomin/Cherrygram | Telegram uses this pattern everywhere, no DI framework | Yes |
| Settings screen uses UniversalFragment + UItem system | Yes | Its Telegram's own UI system for settings | Yes |
| Package name is `ua.gomin.messenger` | Yes | Matches the app package | No (build-critical) |
| Custom resources in `res-gomin/` (not `res-cherrygram/`) | Yes | Clean separation from Cherrygram | Yes |
| No Firebase/Crashlytics unless explicitly needed | Yes | Lightweight, no trackers | Yes |

## Findings (cited - path:lines)

1. Old Gomin is built on Cherrygram fork (`uz.unnarsx.cherrygram.*`) — `Gomin/CLAUDE.md:43-61`
2. Cherrygram code is deeply embedded in Telegram source: ProfileActivity.java (63+ refs), ActionBarLayout.java (15+ refs), BaseFragment.java (8+ refs)
3. Gomin-UA has ZERO cherrygram code — clean Telegram 12.8.1 base
4. Settings entry is injected in ProfileActivity.java at `Gomin/.../ProfileActivity.java:10552-10554` (row creation), `13698-13701` (render), `4329-4330` (onClick)
5. Features catalog at `GOMIN_FEATURES_CATALOG.md` lists all features with priority order
6. All settings screens in old code use `UniversalFragment` extending Telegram's UI framework
7. Main settings menu (`CGPreferencesEntry.java`) at `Gomin/TMessagesProj/.../cherrygram/preferences/CGPreferencesEntry.java` — 861+ lines

## Decisions (with rationale)

1. **Write CLAUDE.md first** — every AI must read it before touching code, prevents Cherrygram contamination
2. **Package `ua.gomin.messenger`** — clean namespace, no association with Cherrygram's `uz.unnarsx`
3. **All Telegram edits marked `/** Gomin start */` / `/** Gomin end */`** — easy merge conflict resolution
4. **Minimal Telegram edits** — only ProfileActivity to add settings button (others deferred)
5. **Feature configs as Kotlin objects over SharedPreferences** — same proven pattern as Telegram
6. **Settings UI as UniversalFragment** — Telegram's native settings framework, zero extra deps
7. **One feature = one commit** — clean git history, easy revert
8. **Order: infra → menu → Ghost Mode (simple) → Speed Engine (simple) → Black Edition (visual) → Air Alert (module) → Gomin AI (complex)**

## Scope IN

- Create CLAUDE.md as project constitution
- Scaffold `ua.gomin.messenger.*` package structure
- Implement Gomin settings menu as UniversalFragment
- Add minimal Telegram hooks (ProfileActivity) to access settings
- Port ALL features from Features Catalog Part 1 (Unique Homin Features)
- Selectively port features from Part 2 (Cherrygram features worth having)
- Unit tests for each ported feature

## Scope OUT (Must NOT have)

- NO Cherrygram code (`uz.unnarsx.cherrygram.*`) — EVER
- NO `res-cherrygram/` — use `res-gomin/`
- NO Firebase services unless user explicitly asks
- NO copypaste from old Gomin without rewriting to `ua.gomin.messenger.*`
- NO heavy Telegram source modifications (>10 lines per file)
- NO DI frameworks, NO coroutines unless necessary, NO Kotlin Flow/LiveData
- NO donation/badge system from Cherrygram unless user requests it
- NO Huawei module (already disabled)

## Open questions

- [RESOLVED] Should we port ALL Cherrygram features or only Homin's unique ones? → Only Part 1 (unique Homin) + selective Part 2. User confirmed this.
- [RESOLVED] How to handle merges from upstream Telegram? → Mark all Telegram edits with `/** Gomin */` comments.
- [RESOLVED] What package name? → `ua.gomin.messenger`

## Approval gate
status: awaiting-approval
<!-- CLAUDE.md content and plan ready for review. See presentation below. -->
