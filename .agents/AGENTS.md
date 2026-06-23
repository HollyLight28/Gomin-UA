# Project Rules for Gomin-UA

## Language Constraints
* **NO KOTLIN IN :TMessagesProj**: The `:TMessagesProj` module is a pure Java module. Writing Kotlin code (`.kt` files) or adding Kotlin dependencies inside this module is strictly forbidden.
* All new features, controllers, helpers, and activities inside `:TMessagesProj` must be implemented in clean, compatible, and modern **Java**.
* Legacy hooking pattern rules must be followed (maximum of 10-15 lines modified in original files, enclosed in `/** Gomin start */` and `/** Gomin end */` comments).
