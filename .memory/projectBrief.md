# Project Brief: Гомін.UA (The Next-Gen Ukrainian Telegram Client)

## 1. Core Vision & Product Goals
**Гомін.UA (Gomin-UA)** is a clean, premium, and independent fork of the official Telegram for Android client (v12.8.1, build 6916), specifically tailored for the Ukrainian community. The objective is to port the custom features from the legacy Cherrygram-based Gomin client (v12.5.0) into a clean upstream fork of DrKLO/Telegram without inheriting Cherrygram's package clutter.

### Key Strategy: The Superstructure Architecture
- **Isolation**: All custom code must reside strictly within the `ua.gomin.messenger` package namespace.
- **Minimal Intervention**: Modifications to original Telegram classes (`org.telegram.*`) must act only as minimal "gates" (hooks) to invoke the Gomin subsystem. Code changes in original Telegram files should not exceed 10 lines per file.
- **No Upstream Pollution**: Absolute avoidance of third-party frameworks/packages like `uz.unnarsx.cherrygram` to prevent package collision during future upstream merges.
- **No Kotlin in TMessagesProj**: As a strict technical constraint, all files inside `:TMessagesProj` must be written in pure Java (Kotlin compilation is not supported by the module's toolchain).

## 2. Branding & Configuration Constants
- **Application Package**: `ua.gomin.messenger`
- **Application API ID**: 35162000
- **Application API Hash**: `8686113844de267311e15037880ae97b`
- **Telegram Base Version**: 12.8.1
- **UI Branding Title**: "Гомін"
- **OLED Accent Color**: `0xff000000` (Pure AMOLED black theme accent)
- **Application Logo / Vector Icon**: `bird.xml` (rendered from `bird.svg` into `res-gomin/drawable/bird.xml`)

## 3. High-Level Feature Spectrum
Ported from [GOMIN_FEATURES_CATALOG.md](file:///g:/Code/Java/Gomin-UA/GOMIN_FEATURES_CATALOG.md):
- **Gomin AI (Gemini Integration)**: Multi-turn chat assistant, voice transcriptions, live call modes via WebSockets (`gemini-3.1-flash-live-preview`).
- **Air Raid Alerts (Повітряні тривоги)**: Region-based notifications, real-time API polling, push-subscriptions, and audio alert overrides (sirens).
- **Gomin Shield**: Manipulation analysis of chat transcripts using Google Gemini models.
- **Ghost Mode**: Invisible reading status, typing status hiding, stories visual hiding, online status spoofing, and biometrics.
- **Speed Engine**: Download boost using parallel TCP streams and upload boost using buffer expansion.
- **Black Edition / Карбон**: Monet-based Monet AMOLED theme layouts.

## 4. Workspaces & Git Remotes
- **Workspace Root**: `g:\Code\Java\Gomin-UA`
- **Origin Remote**: `https://github.com/HollyLight28/Gomin-UA.git`
- **Upstream Target**: `https://github.com/DrKLO/Telegram.git`
