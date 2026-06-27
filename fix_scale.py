import glob, os
files = glob.glob(r'g:\Code\Java\Gomin-UA\TMessagesProj\src\main\res-gomin\drawable\icon_foreground_gomin_*.xml') + [r'g:\Code\Java\Gomin-UA\TMessagesProj\src\main\res\drawable\icon_foreground_gomin.xml']
for f in files:
    content = open(f, 'r', encoding='utf-8').read()
    content = content.replace('scaleX="0.85"', 'scaleX="0.6"').replace('scaleY="0.85"', 'scaleY="0.6"')
    open(f, 'w', encoding='utf-8').write(content)
    print(f"Updated {f}")
