#!/usr/bin/env sh

kotlinc-native $1 -o e && ./e.kexe
