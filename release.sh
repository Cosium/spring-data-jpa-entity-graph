#!/usr/bin/env bash
./mvnw --batch-mode clean release:prepare release:perform && git push && git push --tags
