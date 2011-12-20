#!/bin/sh

mvn clean release:prepare release:perform -Dbeanstalk.versionsToKeep=2 -Dbeanstalk.dryRun=false