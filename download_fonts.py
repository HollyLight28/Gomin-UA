import urllib.request
import os
import ssl

ssl._create_default_https_context = ssl._create_unverified_context

urls = {
    "plusjakarta_regular.ttf": "https://raw.githubusercontent.com/tokotype/PlusJakartaSans/master/fonts/ttf/PlusJakartaSans-Regular.ttf",
    "plusjakarta_medium.ttf": "https://raw.githubusercontent.com/tokotype/PlusJakartaSans/master/fonts/ttf/PlusJakartaSans-Medium.ttf",
    "plusjakarta_bold.ttf": "https://raw.githubusercontent.com/tokotype/PlusJakartaSans/master/fonts/ttf/PlusJakartaSans-Bold.ttf",
    "plusjakarta_italic.ttf": "https://raw.githubusercontent.com/tokotype/PlusJakartaSans/master/fonts/ttf/PlusJakartaSans-Italic.ttf"
}

out_dir = r"g:\Code\Java\Gomin-UA\TMessagesProj\src\main\assets\fonts"

for name, url in urls.items():
    out_path = os.path.join(out_dir, name)
    try:
        urllib.request.urlretrieve(url, out_path)
        print(f"Downloaded {name} to {out_path}")
    except Exception as e:
        print(f"Failed to download {name}: {e}")
