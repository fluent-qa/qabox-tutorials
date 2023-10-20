 mvn clean package -Dmaven.test.skip=true
 cp -rf qaworkspace/target/*.jar .
 java -jar qaworkspace-1.0-SNAPSHOT.jar