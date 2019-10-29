#!/usr/bin/env bash

if [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then
  echo "Deploying the new snapshot."
  ./mvnw -pl cons-list deploy
else
  echo "Building a pull request--no deployment is done."
fi
