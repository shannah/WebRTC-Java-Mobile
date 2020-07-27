#!/bin/sh
set -e
./antw build-for-javascript -Dautomated=true
cd dist
rm -rf ../docs/demo
unzip result.zip
unzip WebRTCDemo-*.zip -d ../docs/demo

