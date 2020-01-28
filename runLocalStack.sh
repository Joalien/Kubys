cd backend && mvn package -DskipTests
cd ..

cd front && npm run build-dev
cd ..

docker-compose -f docker-compose.dev.yml up --build --force-recreate
