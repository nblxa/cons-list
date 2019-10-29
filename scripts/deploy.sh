#!/usr/bin/env bash

if [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then
  echo "Deploying the new snapshot."
  openssl aes-256-cbc -K $encrypted_bca66c76142d_key -iv $encrypted_bca66c76142d_iv \
          -in  scripts/settings-maven-central.xml.enc \
          -out scripts/settings-maven-central.xml -d
  ./mvnw -pl cons-list deploy --settings scripts/settings-maven-central.xml
  rm scripts/settings-maven-central.xml
else
  echo "Building a pull request--no deployment is done."
fi
