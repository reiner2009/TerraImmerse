rm -r dist
python3.12 -m PyInstaller \
  --name "TerraImmerse" \
  --windowed \
  --contents-directory . \
  --icon "icon.icns" \
  --add-data "assets:assets" \
  terraimmerse/client/TerraImmerse.py
cp -R icon.icns dist
cd dist
mkdir dmg_dist
cp -R TerraImmerse.app dmg_dist
cd dmg_dist
create-dmg --volname "TerraImmerse" --icon-size 32 --icon "TerraImmerse.app" 175 120 --hide-extension "TerraImmerse.app" --app-drop-link 425 120 "dist/TerraImmerse.dmg" "dist/dmg/"