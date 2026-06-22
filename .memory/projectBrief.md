# Project Brief: Гомін.UA (The Next-Gen Ukrainian Telegram Client)

## Мета
Створити новий, чистий форк офіційного Telegram Android (12.8.1) з українським брендом «Гомін», поступово переносячи перевірені фічі зі старого Гоміну (12.5.0/Cherrygram-based).

## Стратегія
- **Стара база** (`Gomin/`): Telegram 12.5.0 + Cherrygram. Відстала на 3 мажорних версії. Архів для копіювання фіч.
- **Нова база** (`Gomin-UA/`): Чистий форк DrKLO/Telegram 12.8.1. Мінімальні правки для брендингу, поступова інтеграція фіч.

## Git-стратегія оновлення
```
origin → https://github.com/HollyLight28/Gomin-UA.git
telegram → https://github.com/DrKLO/Telegram.git (upstream, ПОТРІБНО ДОДАТИ)
```

## App Configuration
- **App Package**: `ua.gomin.messenger`
- **App api_id**: 35162000
- **App api_hash**: `8686113844de267311e15037880ae97b`
- **Telegram Base Version**: 12.8.1 (build 6916)
- **Назва**: Гомін

## Поточний стан (21.06.2026)
- ✅ Пакет `ua.gomin.messenger` — встановлено в gradle.properties
- ✅ API ключі — замінені в BuildVars.java
- ✅ Каталог фіч для перенесення — GOMIN_FEATURES_CATALOG.md
- ❌ Upstream remote (DrKLO/Telegram) — НЕ ДОДАНО
- ❌ Назва додатку в UI — ще "Telegram"
- ❌ Іконка — ще оригінальна Telegram
- ❌ Жодна фіча зі старого Гоміну НЕ перенесена
