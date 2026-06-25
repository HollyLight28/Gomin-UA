import os
import re
import shutil

# The aliases and their respective backgrounds and foregrounds
icons = {
    "DefaultIcon": ("icon_background_dark", "icon_foreground_gomin_dark"),
    "GoldIcon": ("icon_background_gold", "icon_foreground_gomin_gold"),
    "WhiteIcon": ("icon_background_white", "icon_foreground_gomin_white"),
    "AquaIcon": ("icon_background_aqua", "icon_foreground_gomin_aqua"),
    "LavandaIcon": ("icon_background_lavanda", "icon_foreground_gomin_lavanda"),
    "SunsetIcon": ("icon_background_sunset", "icon_foreground_gomin_sunset"),
    "UkraineIcon": ("icon_background_ukraine", "icon_foreground_gomin_yellow"),
}

res_dir = "TMessagesProj/src/main/res-gomin"
anydpi_dir = os.path.join(res_dir, "mipmap-anydpi-v26")
os.makedirs(anydpi_dir, exist_ok=True)

# 1. Create adaptive icon XMLs
for alias, (bg, fg) in icons.items():
    icon_name = alias.lower()

    xml_content = f"""<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@drawable/{bg}" />
    <foreground android:drawable="@drawable/{fg}" />
</adaptive-icon>
"""
    with open(os.path.join(anydpi_dir, f"ic_{icon_name}.xml"), "w", encoding="utf-8") as f:
        f.write(xml_content)

# 2. Copy fallback PNGs for older Android versions
densities = ["hdpi", "mdpi", "xhdpi", "xxhdpi", "xxxhdpi"]
for density in densities:
    src_png = f"TMessagesProj/src/main/res/mipmap-{density}/ic_launcher.png"
    if os.path.exists(src_png):
        dest_dir = os.path.join(res_dir, f"mipmap-{density}")
        os.makedirs(dest_dir, exist_ok=True)
        for alias in icons.keys():
            icon_name = alias.lower()
            shutil.copy(src_png, os.path.join(dest_dir, f"ic_{icon_name}.png"))

# 3. Update AndroidManifest.xml
manifest_path = "TMessagesProj/src/main/AndroidManifest.xml"
with open(manifest_path, "r", encoding="utf-8") as f:
    manifest_content = f.read()

# Update AndroidManifest.xml to set correct mipmap icons
for alias in icons.keys():
    icon_name = alias.lower()
    
    # We find the activity-alias with name "ua.gomin.messenger.{alias}"
    # Case A: Alias already has an android:icon="..." attribute
    # We will replace android:icon="..." with android:icon="@mipmap/ic_{icon_name}"
    pattern_with_icon = (
        r'(<activity-alias[^>]*?android:name="ua\.gomin\.messenger\.' + alias + r'"[^>]*?)'
        r'android:icon="[^"]+"([^>]*?>)'
    )
    if re.search(pattern_with_icon, manifest_content, flags=re.DOTALL):
        manifest_content = re.sub(
            pattern_with_icon,
            r'\1android:icon="@mipmap/ic_' + icon_name + r'"\2',
            manifest_content,
            flags=re.DOTALL
        )
    else:
        # Case B: Alias doesn't have android:icon="..." attribute (like DefaultIcon)
        # We will inject android:icon="@mipmap/ic_{icon_name}" right after android:name="..."
        pattern_no_icon = (
            r'(<activity-alias[^>]*?android:name="ua\.gomin\.messenger\.' + alias + r'"([^>]*?))>'
        )
        manifest_content = re.sub(
            pattern_no_icon,
            r'\1\n            android:icon="@mipmap/ic_' + icon_name + r'">',
            manifest_content,
            flags=re.DOTALL
        )

with open(manifest_path, "w", encoding="utf-8") as f:
    f.write(manifest_content)

print("Icons generated and AndroidManifest.xml updated successfully.")
