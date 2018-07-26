docker exec docker_planty-assistant-manager-mongodb_1 sh -c 'exec mongodump -d planty-assistant-manager --archive' > ./all-collections.$(date +%s).archive

