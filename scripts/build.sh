#!/bin/bash

set -e

RELEASE_PROFILE=""
SCRPT="[scripts/build.sh]"

if [ "$TRAVIS_PULL_REQUEST" == "false" ] && [[ "$TRAVIS_BRANCH" == release/* ]]; then
  echo "$SCRPT Building a new release."
  RELEASE_PROFILE="-Prelease --settings scripts/release-settings.xml"
fi

if [ "$TRAVIS_PULL_REQUEST" == "false" ] && [[ "$TRAVIS_BRANCH" == test/testreleases ]]; then
  echo "$SCRPT Building a new test release."
  RELEASE_PROFILE="-Prelease --settings scripts/release-settings.xml"
fi

if [ "$TRAVIS_PULL_REQUEST" == "true" ]; then
  echo "$SCRPT Building a pull request."
fi

./mvnw jacoco:prepare-agent \
       install \
       sonar:sonar -Dsonar.projectKey=cons-list \
       jacoco:report \
       coveralls:report \
       -e -B $RELEASE_PROFILE
