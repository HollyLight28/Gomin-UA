import urllib.request
import os
import ssl

ssl._create_default_https_context = ssl._create_unverified_context

urls = {
    "gomin_regular.ttf": "https://raw.githubusercontent.com/MacPaw/Fixel/main/fonts/ttf/FixelText-Regular.ttf",
    "gomin_medium.ttf": "https://raw.githubusercontent.com/MacPaw/Fixel/main/fonts/ttf/FixelText-Medium.ttf",
    "gomin_bold.ttf": "https://raw.githubusercontent.com/MacPaw/Fixel/main/fonts/ttf/FixelText-Bold.ttf"
}

# Визначаємо шлях відносно розташування скрипта
script_dir = os.path.dirname(os.path.abspath(__file__))
out_dir = os.path.join(script_dir, "TMessagesProj", "src", "main", "assets", "fonts")

os.makedirs(out_dir, exist_ok=True)

for name, url in urls.items():
    out_path = os.path.join(out_dir, name)
    try:
        urllib.request.urlretrieve(url, out_path)
        print(f"Downloaded {name} to {out_path}")
    except Exception as e:
        print(f"Failed to download {name}: {e}")
