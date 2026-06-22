# System Patterns (Gomin-UA)

This document maps out the core code structures, naming conventions, and integration strategies used within the Gomin-UA codebase. Developers and AI agents must follow these patterns to maintain visual and functional consistency with Telegram's core.

---

## 1. The Minimal Intervention Pattern (Hooks)
To ensure future merges with DrKLO/Telegram are straightforward, all modifications in native Telegram packages (`org.telegram.*`) must strictly function as gates.
- Modifications in Telegram files are capped at **10 lines** per file.
- All code inserts must be isolated with clear boundary comments:
```java
/** Gomin start */
if (ua.gomin.messenger.hooks.GominFeatureHooks.INSTANCE.shouldGhostRead()) {
    // Override behaviour
}
/** Gomin end */
```
- Injected entry points must delegate complex operations to helper utilities inside `ua.gomin.messenger.*`.

---

## 2. Settings Row Injection Pattern (SettingsActivity)
Injecting rows into the main Telegram settings page (`org.telegram.ui.SettingsActivity`) requires hooking into row listing and list click event managers.

### 2.1. Item Registration inside `fillItems(ArrayList<UItem> items, UniversalAdapter adapter)`
We inject the Gomin entry cell above default account settings or general sections using `SettingCell.Factory.of`:
```java
/** Gomin start */
items.add(SettingCell.Factory.of(100, 0xff000000, 0xff000000, R.drawable.bird, "Налаштування Гоміна", "Тюнінг, приватність, вигляд"));
/** Gomin end */
```
*Note: Brand accent gradients are pure OLED black (`0xff000000`). Custom assets use `R.drawable.bird` mapped to `res-gomin/drawable/bird.xml`.*

### 2.2. Click Dispatch inside `onClick(UItem item, View view, int position, float x, float y)`
Catch row index `100` and trigger the preferences navigator to open `GominSettingsEntry`:
```java
/** Gomin start */
if (item.id == 100) {
    ua.gomin.messenger.preferences.GominPreferencesNavigator.INSTANCE.createGominSettings(this);
    return;
}
/** Gomin end */
```

---

## 3. UI Implementation via UniversalFragment & UItem (Settings UI)
All custom preference layout lists are built subclassing `org.telegram.ui.Components.UniversalFragment`. Layout elements are mapped as lists of `UItem` types inside the `fillItems` method.

### 3.1. Subclass Skeleton
```java
package ua.gomin.messenger.preferences;

import android.content.Context;
import android.view.View;
import java.util.ArrayList;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;

public class MyCustomPreferences extends UniversalFragment {
    
    private final int myToggleRowId = 1;

    @Override
    protected CharSequence getTitle() {
        return "My Custom Feature";
    }

    @Override
    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        Context context = getContext();
        if (context == null) return;

        // Creating Section Header
        items.add(SettingsHelper.asHeaderWithIcon(context, R.drawable.msg_settings, "Category Header"));
        
        // Creating Switch Config
        items.add(SettingsHelper.asSwitchCG(myToggleRowId, "Toggle Label", "Detail description text")
                .setChecked(GominCoreConfig.INSTANCE.getAirAlertEnabled(context)));
        
        // Creating Separator Shadow
        items.add(UItem.asShadow(null));
    }

    @Override
    protected void onClick(UItem item, View view, int position, float x, float y) {
        Context context = getContext();
        if (context == null) return;

        if (item.id == myToggleRowId) {
            boolean currentVal = GominCoreConfig.INSTANCE.getAirAlertEnabled(context);
            GominCoreConfig.INSTANCE.setAirAlertEnabled(context, !currentVal);
            SettingsHelper.updateCheckState(view, !currentVal);
        }
    }
}
```

### 3.2. Settings Elements Creation (SettingsHelper)
Use `SettingsHelper.java` to format UI elements correctly:
- Headers with vector resource icons: `SettingsHelper.asHeaderWithIcon(context, iconRes, text)`
- Toggle Checkbox Rows: `SettingsHelper.asSwitchCG(id, title, description)`
- Toggling rendering state dynamically: `SettingsHelper.updateCheckState(view, isChecked)`

---

## 4. Configuration Persistence Pattern (SharedPreferences)
Since Kotlin is disabled within the application module, settings are tracked via static Java wrapper classes managing isolated `SharedPreferences`.

```java
package ua.gomin.messenger.configs;

import android.content.Context;
import android.content.SharedPreferences;

public class GominConfigTemplate {
    public static final GominConfigTemplate INSTANCE = new GominConfigTemplate();
    private static final String PREFS_NAME = "gomin_prefs_filename";

    private SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean getToggledValue(Context context) {
        return getPrefs(context).getBoolean("pref_key", false);
    }

    public void setToggledValue(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("pref_key", value).apply();
    }
}
```

---

## 5. Event Bus Integration (NotificationCenter)
Gomin uses Telegram's built-in event engine to coordinate asynchronous events (like Air Alert triggers or UI updates).
- **Observer Registration**: Register using `getNotificationCenter().addObserver(this, NotificationCenter.eventName)` within fragment creation methods.
- **Observer Removal**: Clean up using `getNotificationCenter().removeObserver(this, NotificationCenter.eventName)` within fragment destruction lifecycles.
- **Dispatching Events**: Invoke `getNotificationCenter().postNotificationName(NotificationCenter.eventName, argumentList)` to notify observers.
