nohup  ./replication/primary/bin/mongod -f ./replication/primary/bin/mongodb.conf >./logs/primary.log  2>&1 &
nohup  ./replication/secondary1/bin/mongod -f ./replication/secondary1/bin/mongodb.conf >./logs/secondary1.log 2>&1 &
nohup  ./replication/secondary2/bin/mongod -f ./replication/secondary2/bin/mongodb.conf >./logs/secondary2.log 2>&1 &
nohup  ./mongodb-node1/bin/mongod -f ./mongodb-node1/bin/mongodb.conf >./logs/mongodb-node1.log 2>&1 &
nohup  ./mongodb-node2/bin/mongod -f ./mongodb-node2/bin/mongodb.conf >./logs/mongodb-node2.log 2>&1 &
nohup  ./config/mongodb-config1/bin/mongod -f ./config/mongodb-config1/bin/mongodb.conf >./logs/config1.log 2>&1 &
nohup  ./config/mongodb-config2/bin/mongod -f ./config/mongodb-config2/bin/mongodb.conf >./logs/config2.log 2>&1 &
nohup  ./config/mongodb-config3/bin/mongod -f ./config/mongodb-config3/bin/mongodb.conf >./logs/config3.log 2>&1 &
nohup  ./rounter/mongodb-rounter1/bin/mongos --configdb shardingConfig/192.168.1.142:27022,192.168.1.142:27023,192.168.1.142:27024 --port 27025 --logpath=/usr/local/apache/mongoDB/sharding/rounter/mongodb-rounter1/logs/mongodb.log >./logs/rounter.log 2>&1 &
