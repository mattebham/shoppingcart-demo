#!/usr/bin/env bash

PROJECT_DIR="shopping-cart-demo"

echo "Building ${PROJECT_DIR} app..."
mvn clean install -DskipTests

rc=$?
if [ $rc -ne 0 ]; then
  echo 'Maven build failed. Exiting!'
  exit $rc
fi

echo "Building Docker image for ${PROJECT_DIR} app..."

docker build -t shoppingcartdemo:1.0 .

echo "Running the container..."
docker run -p 8080:8080 shoppingcartdemo:1.0

echo "ALL DONE!"
exit
