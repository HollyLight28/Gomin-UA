# Technical Context (Gomin-UA)

## 1. Compiler & Platform Toolchain
- **JDK Target**: Java Development Kit 17 (Must be configured in local IDE settings and Gradle runner context).
- **Target Android SDK**: API Level 36.
- **Min Android SDK**: API Level 23 (Android 6.0).
- **NDK Version**: 21.4.7075529 (Required to compile C++ JNI code for Telegram database wrappers, OpenAL, Opus, and WebRTC protocols).
- **Gradle Version**: Enforced via `gradle/wrapper/gradle-wrapper.properties` version mappings.

## 2. Main Compilation Constraints: The Kotlin Prohibition
- **The Issue**: In previous versions, incorporating Kotlin plugins (`kotlin-android`) within `TMessagesProj` caused signature resolution errors: `Class kotlin.Unit compiled with an incompatible version of Kotlin` during Gradle sync and assembly. This stems from Telegram's custom Gradle settings, pre-compiled C++ toolchains, and varying Kotlin standard library dependencies inside transitives.
- **The Resolution**: All code within the main module `TMessagesProj` must be written in **pure Java**. Kotlin file compilation is disabled.
- **Implication for Porting**: Any files ported from the legacy Cherrygram repository (which uses Kotlin) must be translated into equivalent Java structures prior to inclusion in Gomin-UA.

## 3. Project Directory Map & Custom Assets
- **Project Location**: `g:\Code\Java\Gomin-UA`
- **Main Code Module**: `TMessagesProj/`
- **Custom Code Package**: `TMessagesProj/src/main/java/ua/gomin/messenger/`
- **Custom Resources Directory**: `TMessagesProj/src/main/res-gomin/` (Configured in Gradle source sets to merge custom resources directly into the main build output).
  - Main Ukrainian localization strings: `TMessagesProj/src/main/res-gomin/values-uk/gomin_strings.xml`.
  - Mascot and branding drawables: `TMessagesProj/src/main/res-gomin/drawable/`.

## 4. Build Targets & Flavors
The project uses Gradle flavors to manage distribution variants.
- **Build Command**: `./gradlew assembleAfatDebug`
  - Outputs compilation artifacts to: `TMessagesProj/build/outputs/apk/afat/debug/`
- **Flavor Name**: `Afat` (Commonly stands for All-Flavors-All-Translation configuration, packaging multi-architecture binaries including arm64-v8a, armeabi-v7a, x86, x86_64).
- **Release Target**: `./gradlew assembleAfatStandalone` (Builds standalone APK with updater capabilities enabled).
- **Google Play Target**: `./gradlew bundleAfatRelease` (Builds Android App Bundle `.aab` for Google Play deployment).
