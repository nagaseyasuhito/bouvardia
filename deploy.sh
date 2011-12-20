#!/bin/sh

mvn clean release:prepare release:perform beanstalk:upload-source-bundle beanstalk:create-application-version beanstalk:update-environment beanstalk:clean-previous-versions -Dbeanstalk.versionsToKeep=2 -Dbeanstalk.dryRun=false