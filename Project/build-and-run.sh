#!/bin/bash

echo "Building Spring Boot services..."

echo "Building student-service..."
cd student-service
./mvnw clean package -DskipTests
cd ..

echo "Building teacher-service..."
cd teacher-service
./mvnw clean package -DskipTests
cd ..

echo "Building user-service..."
cd user-service
./mvnw clean package -DskipTests
cd ..

echo "Starting Docker containers..."
docker-compose up -d

echo "Services should be available at:"
echo "- Frontend: http://localhost:3000"
echo "- Student Service: http://localhost:8083"
echo "- Teacher Service: http://localhost:8084"
echo "- User Service: http://localhost:8089" 