#!/usr/bin/env bash
mvn --batch-mode clean release:prepare release:perform && git push && git push --tags