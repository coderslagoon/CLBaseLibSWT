#!/bin/bash

set -e

rm -rf target

mvn install -Dmaven.test.skip=true
mvn package -Dmaven.test.skip=true
