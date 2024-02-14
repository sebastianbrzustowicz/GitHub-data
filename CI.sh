#!/bin/bash

cd GHData/

echo "Preparing environment"

mvn clean install

echo "Running tests"

mvn test

if [ $? -eq 0 ]; then

	cd ..
	echo "Tests completed succesfully"
	git add .
	git commit -m "Automated software integration"
	git push origin main
	
	echo "Software uploaded to remote repository"
	
else
	
	cd ..
	echo "Tests failed"
	
fi

