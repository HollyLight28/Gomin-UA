package org.telegram.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.R;

public class LauncherIconController {
    public static void tryFixLauncherIconIfNeeded() {
        try {
            for (LauncherIcon icon : LauncherIcon.values()) {
                if (isEnabled(icon)) {
                    return;
                }
            }
            setIcon(LauncherIcon.DEFAULT);
        } catch (Throwable e) {
            // Safe fallback
        }
    }

    public static boolean isEnabled(LauncherIcon icon) {
        try {
            Context ctx = ApplicationLoader.applicationContext;
            int i = ctx.getPackageManager().getComponentEnabledSetting(icon.getComponentName(ctx));
            return i == PackageManager.COMPONENT_ENABLED_STATE_ENABLED || i == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT && icon == LauncherIcon.DEFAULT;
        } catch (Throwable e) {
            return icon == LauncherIcon.DEFAULT;
        }
    }

    public static void setIcon(LauncherIcon icon) {
        Context ctx = ApplicationLoader.applicationContext;
        PackageManager pm = ctx.getPackageManager();
        for (LauncherIcon i : LauncherIcon.values()) {
            try {
                pm.setComponentEnabledSetting(i.getComponentName(ctx), i == icon ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED :
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            } catch (Throwable e) {
                // Ignore missing/unregistered components in Standalone build
            }
        }
        // Clean restart after 500ms to apply icon change smoothly and avoid abrupt system crash
        org.telegram.messenger.AndroidUtilities.runOnUIThread(() -> {
            try {
                ua.gomin.messenger.helpers.GominRestartHelper.restartApp(ctx);
            } catch (Throwable e) {
                System.exit(0);
            }
        }, 500);
    }

    public enum LauncherIcon {
        /** Gomin start - 7 launcher icon variants */
        DEFAULT("DefaultIcon", R.drawable.icon_background_dark, R.drawable.icon_foreground_gomin_dark, R.string.AppIconDefault),
        GOLD("GoldIcon", R.drawable.icon_background_gold, R.drawable.icon_foreground_gomin_gold, R.string.AppIconGold),
        WHITE("WhiteIcon", R.drawable.icon_background_white, R.drawable.icon_foreground_gomin_white, R.string.AppIconWhite),
        AQUA("AquaIcon", R.drawable.icon_background_aqua, R.drawable.icon_foreground_gomin_aqua, R.string.AppIconAqua),
        LAVANDA("LavandaIcon", R.drawable.icon_background_lavanda, R.drawable.icon_foreground_gomin_lavanda, R.string.AppIconLavanda),
        SUNSET("SunsetIcon", R.drawable.icon_background_sunset, R.drawable.icon_foreground_gomin_sunset, R.string.AppIconSunset),
        UKRAINE("UkraineIcon", R.drawable.icon_background_ukraine, R.drawable.icon_foreground_gomin_yellow, R.string.AppIconUkraine);
        /** Gomin end */

        public final String key;
        public final int background;
        public final int foreground;
        public final int title;
        public final boolean premium;

        private ComponentName componentName;

        public ComponentName getComponentName(Context ctx) {
            if (componentName == null) {
                componentName = new ComponentName(ctx.getPackageName(), "ua.gomin.messenger." + key);
            }
            return componentName;
        }

        LauncherIcon(String key, int background, int foreground, int title) {
            this(key, background, foreground, title, false);
        }

        LauncherIcon(String key, int background, int foreground, int title, boolean premium) {
            this.key = key;
            this.background = background;
            this.foreground = foreground;
            this.title = title;
            this.premium = premium;
        }
    }
}
