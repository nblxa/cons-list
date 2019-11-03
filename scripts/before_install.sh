#!/bin/bash

set -e

# export GPG details

echo $GPG_SECRET_KEYS | base64 --decode | $GPG_EXECUTABLE --import --no-tty --batch
echo $GPG_OWNERTRUST | base64 --decode | $GPG_EXECUTABLE --import-ownertrust

# allow entering passphrase without tty

if [ -d ~/.gnupg ]; then
  mkdir -p ~/.gnupg
  chmod 700 ~/.gnupg
fi
if [ -f ~/.gnupg/gpg-agent.conf ]; then
  touch ~/.gnupg/gpg-agent.conf
  chmod 600 ~/.gnupg/gpg-agent.conf
fi
echo "allow-loopback-pinentry" | cat >> ~/.gnupg/gpg-agent.conf
