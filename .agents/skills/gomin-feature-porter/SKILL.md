---
name: gomin-feature-porter
description: "Use this skill when porting legacy features from the old Cherrygram-based Gomin (Telegram 12.5.0) to the new clean Gomin-UA (Telegram 12.8.1) codebase."
---

# Gomin-UA Feature Porting Guide

This skill guides the porting process to ensure clean, fast, and compilation-ready integration of legacy features without breaking the core Telegram functionality.

## 1. Locate Legacy Source Files
Look up the feature's legacy location (usually in the old `Gomin/` codebase, package `uz.unnarsx.cherrygram.*`). Key features are cataloged in [GOMIN_FEATURES_CATALOG.md](file:///g:/Code/Java/Gomin-UA/GOMIN_FEATURES_CATALOG.md).

## 2. Port and Translate Logic
- **Pure Java Enforcement**: All ported code must be translated/written in Java under the `ua.gomin.messenger.*` package in `TMessagesProj/src/main/java/ua/gomin/messenger/`.
- Do NOT use Kotlin plugins in `TMessagesProj`. Translate any Kotlin classes (e.g., helpers, configs) to equivalent Java structures.
- Do NOT copy imports pointing to `uz.unnarsx.cherrygram.*`. Replace all configs references with the new java configs in `ua.gomin.messenger.configs.*`.

## 3. Integration Gates (Hooks)
- Minimize hooks in `org.telegram.*` classes. Keep them under **10 lines** per file.
- Always wrap modifications in:
  ```java
  /** Gomin start */
  // Ported logic hook
  /** Gomin end */
  ```
- Use the Telegram event bus `NotificationCenter` if you need to dispatch global events, or invoke static helper methods in `ua.gomin.messenger`.

## 4. Resource Allocation
- Put all strings in `TMessagesProj/src/main/res-gomin/values-uk/gomin_strings.xml`.
- Put all visual assets (drawables, layouts, icons) under the custom `res-gomin/` resource directory (not `res-cherrygram/`).

## 5. Verification Cycle
1. Run compilation check: `./gradlew assembleAfatDebug`
2. If compilation fails due to Kotlin compiler errors (e.g., standard library mismatches), verify that no Kotlin files were introduced to `TMessagesProj`.
3. Test locally or verify static code integrity before confirming the task is complete.
