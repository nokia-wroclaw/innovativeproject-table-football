#!/bin/bash
REDIS_IMAGE_NAME=innovativeproject-table-football_redis_1
REDIS_DUMP_LOCATION=./Redis/data
BACKUP_LOCATION=./Redis/backup

LASTSAVE=`/usr/bin/docker exec $REDIS_IMAGE_NAME redis-cli LASTSAVE`

/usr/bin/docker exec $REDIS_IMAGE_NAME redis-cli BGSAVE
while [ `/usr/bin/docker exec $REDIS_IMAGE_NAME redis-cli LASTSAVE` -eq $LASTSAVE ]
do
sleep 1
done

DATE=`date +%Y\%m\%d_%H:%M:%S`
mkdir $BACKUP_LOCATION 2> /dev/null
cp $REDIS_DUMP_LOCATION/redis_dump.rdb $BACKUP_LOCATION/redis_dump_$DATE.rdb

