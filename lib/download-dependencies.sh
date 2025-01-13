#!/bin/bash

# Create lib directory if it doesn't exist
mkdir -p lib

# Download JavaFX SDK
wget https://download2.gluonhq.com/openjfx/21.0.1/openjfx-21.0.1_linux-x64_bin-sdk.zip
unzip openjfx-21.0.1_linux-x64_bin-sdk.zip
mv javafx-sdk-21.0.1/lib/*.jar lib/
rm -rf javafx-sdk-21.0.1
rm openjfx-21.0.1_linux-x64_bin-sdk.zip
