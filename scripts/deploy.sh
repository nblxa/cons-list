#!/bin/bash

set -e

if [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then

  echo "Deploying a new snapshot."
  ./mvnw -pl cons-list deploy --settings scripts/deploy-settings.xml -B

elif [ "$TRAVIS_PULL_REQUEST" == "false" ] && [[ "$TRAVIS_BRANCH" == release/* ]]; then

  echo "Deploying a new release."
  ./mvnw -pl cons-list deploy --settings scripts/deploy-settings.xml -B -Prelease

else

  echo "Building a pull request--no deployment is done."

fi
