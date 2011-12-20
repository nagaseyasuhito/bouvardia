mvn clean package
mvn beanstalk:upload-source-bundle beanstalk:create-application-version beanstalk:update-environment
mvn beanstalk:clean-previous-versions -Dbeanstalk.versionsToKeep=1 -Dbeanstalk.dryRun=false