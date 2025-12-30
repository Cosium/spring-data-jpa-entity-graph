#!/usr/bin/env bash
# Prime gpg-agent caches. See https://maven.apache.org/plugins/maven-gpg-plugin/usage.html
echo "test" | gpg --clearsign > /dev/null
./mvnw --batch-mode clean release:prepare release:perform && git push && git push --tags
