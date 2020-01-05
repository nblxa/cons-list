#!/bin/bash

set -e

SCRPT="[scripts/deploy.sh]"

if [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == master ]; then
  echo "$SCRPT Deploying a new snapshot."
  ./mvnw -pl cons-list \
         clean deploy \
         --settings scripts/release-settings.xml \
         -Dmaven.test.skip=true -e -B
  exit 0
fi

if [ "$TRAVIS_PULL_REQUEST" == "false" ] && [[ "$TRAVIS_BRANCH" == release/* ]]; then
  echo "$SCRPT Deploying a new release."
  ./mvnw -pl cons-list \
         clean deploy \
         --settings scripts/release-settings.xml \
         -Prelease -Dmaven.test.skip=true -e -B
  exit 0
fi

if [ "$TRAVIS_PULL_REQUEST" == "false" ] && [[ "$TRAVIS_BRANCH" == test/testreleases ]]; then
  echo "$SCRPT Doing a release deployment test."
  ./mvnw -pl cons-list \
         clean deploy \
         --settings scripts/release-settings.xml \
         -Prelease -Dmaven.test.skip=true -e -B
  exit 0
fi

if [ "$TRAVIS_PULL_REQUEST" == "true" ]; then
  echo "$SCRPT Not deploying a pull request."
  exit 0
fi

echo "$SCRPT Branch not recognized, not deploying."
