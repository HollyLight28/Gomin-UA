import os
import re
import glob

configs_dir = r"g:\Code\Java\Gomin-UA\TMessagesProj\src\main\java\ua\gomin\messenger\configs"

for kt_file in glob.glob(os.path.join(configs_dir, "*.kt")):
    with open(kt_file, "r", encoding="utf-8") as f:
        content = f.read()

    class_name = os.path.basename(kt_file).replace(".kt", "")
    java_file = kt_file.replace(".kt", ".java")

    # Extract PREFS_NAME
    prefs_name_match = re.search(r'const val PREFS_NAME = "(.*?)"', content)
    prefs_name = prefs_name_match.group(1) if prefs_name_match else "gomin_config"

    # Extract const val
    consts = re.findall(r'const val (\w+) = (.*?)\n', content)
    
    # Extract fun getXXX and setXXX
    # fun getAutoQuoteReplies(context: Context): Boolean = getPrefs(context).getBoolean("autoQuoteReplies", false)
    # fun getNotificationSound(context: Context): Int = getPrefs(context).getInt("notificationSound", NOTIF_SOUND_DEFAULT)
    getters = re.findall(r'fun get(\w+)\(context: Context\): (\w+)\s*=\s*getPrefs\(context\)\.get(\w+)\("(.*?)", (.*?)\)', content)
    
    # Extract setters
    setters = re.findall(r'fun set(\w+)\(context: Context, \w+: (\w+)\) \{\s*getPrefs\(context\)\.edit\(\)\.put\w+\("(.*?)", \w+\)\.apply\(\)\s*\}', content)

    java_code = f"""package ua.gomin.messenger.configs;

import android.content.Context;
import android.content.SharedPreferences;

public class {class_name} {{

    public static final {class_name} INSTANCE = new {class_name}();
    private static final String PREFS_NAME = "{prefs_name}";

"""
    for const_name, const_val in consts:
        if const_val.strip() in ['true', 'false']:
            java_code += f"    public static final boolean {const_name} = {const_val.strip()};\n"
        elif '"' in const_val:
            java_code += f"    public static final String {const_name} = {const_val.strip()};\n"
        else:
            java_code += f"    public static final int {const_name} = {const_val.strip()};\n"

    java_code += """
    private SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
"""
    for prop_name, ret_type, pref_type, key, default_val in getters:
        ret_type_java = "boolean" if ret_type == "Boolean" else "int" if ret_type == "Int" else "String"
        java_code += f"""
    public {ret_type_java} get{prop_name}(Context context) {{
        return getPrefs(context).get{pref_type}("{key}", {default_val});
    }}"""

    for prop_name, arg_type, key in setters:
        arg_type_java = "boolean" if arg_type == "Boolean" else "int" if arg_type == "Int" else "String"
        java_code += f"""
    public void set{prop_name}(Context context, {arg_type_java} value) {{
        getPrefs(context).edit().put{arg_type}("{key}", value).apply();
    }}"""

    java_code += "\n}\n"

    with open(java_file, "w", encoding="utf-8") as f:
        f.write(java_code)
    
    os.remove(kt_file)
    print(f"Converted {class_name}")
