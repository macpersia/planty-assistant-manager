docker exec plantyassistantmanager_planty-assistant-manager-mariadb_1 sh -c 'exec mysqldump planty-assistant-manager' > ./mariadb-all-tables.$(date +%s).dump

