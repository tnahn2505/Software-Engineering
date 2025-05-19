@echo off
echo Starting Docker containers...
docker-compose down
docker-compose up -d

echo Services should be available at:
echo - Frontend: http://localhost:3000
echo - Student Service: http://localhost:8083
echo - Teacher Service: http://localhost:8084
echo - User Service: http://localhost:8089 