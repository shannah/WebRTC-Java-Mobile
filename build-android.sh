#!/bin/sh
set -e
./antw build-for-android-device -Dautomated=true
cd dist
rm -rf ../docs/demo
unzip result.zip
cp WebRTCDemo-release.apk ../bin/

