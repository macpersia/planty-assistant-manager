docker exec docker_prototyping-manager-mongodb_1 sh -c 'exec mongodump -d prototyping-manager --archive' > ./all-collections.$(date +%s).archive

