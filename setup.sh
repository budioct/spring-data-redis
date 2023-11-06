# setup menggunakan redis di docker

# pull image dari docker hub
docker pull redis:7.2 / versi tag latest

# membuat container dengan image yang kita gunakan
# production
docker container create --name redis-stack-server -p 6379:6379 redis:7.2 # hanya dibuatkan
docker run -d --name redis-stack-server -p 6379:6379 redis:7.2 # dibuatkan sekaligus di jalanakan

# local
docker container create --name redis-stack -p 6379:6379 -p 8001:8001 redis:7.2 # hanya dibuatkan
docker run -d --name redis-stack -p 6379:6379 -p 8001:8001 redis:7.2 # dibuatkan sekaligus di jalanakan

# menjalankan container
docker container start redis-stack

# masuk ke dalam container untuk, bisa menjalankan redis-server dan redis-cli
docker container exec -it redis-stack /bin/bash
root@c73c3d897209:/data#

# menjalankan redis server
redis-server
root@c73c3d897209:/data# redis-server
# feedback cmd redis-server
 Running in standalone mode
 port: 6379 (port default redis)

# jalankan redis server dengan custom configuration
root@c73c3d897209:/data# redis-server redis.conf

# menjalankan redis CLI langsung dari luar container
docker exec -it redis-stack redis-cli
127.0.0.1:6379>

# menjalankan redis CLI di dalam container
docker container exec -it redis-stack /bin/bash
root@71ae6829b569:/hello#  redis-cli -h localhost -p 6379
localhost:6379>

# cek apakah memang sudah di dalam redis
localhost:6379> ping
PONG



# docker Dockerfile config
# instruksi FROM (dijalankan untuk pull image yang ada di docker hub di proses build image)
FROM redis:7.2

# intruksi WORKDIR (untuk menentukan direktory/folder untuk menjalankan instruksi RUN, CMD, ENTRYPOINT, COPY, dan ADD)
WORKDIR /hello

# intruksi COPY (dijalankan untuk menambahkan file dari source ke dalam folder destination di Docker Image)
# menambahkan semua file .txt ke folder hello
COPY config/*.conf /hello

EXPOSE 6379

# instruksi COMMAND/CMD (dijalankan saat container running)
#CMD cat "hello/redis.conf"

# cmd instruksi
docker build -t redis-alphine . # akan membaca Docker file dalama directory

docker container create --name redis-stack -p 6379:6379 -p 8001:8001 redis-alphine # hanya dibuatkan
docker run -d --name redis-stack -p 6379:6379 -p 8001:8001 redis-alphine # dibuatkan sekaligus di jalanakan



# docker compose config .yml
version: "3.3"
services:
  redis-stack:
    image: redis:7.2
    container_name: redis-stack
    restart: always
    volumes:
      - redis_volume_data:/data
    ports:
      - 6379:6379
  redis-stack-insight:
    image: redislabs/redisinsight:latest
    container_name: redis-stack-insight
    restart: always
    ports:
      - 8001:8001
    volumes:
      - redis_insight_volume_data:/db
volumes:
  redis_volume_data:
  redis_insight_volume_data:

# cmd instruksi
docker-compose -f redis-docker-compose.yml up

# docker compose config .yml versi 2
version: "3.8"

services:
  redis-stack:
    container_name: "redis-stack"
    image: redis:alpine
    command : redis-server /usr/local/etc/redis/redis.conf --requirepass rahasia
    ports:
      - "6379:6379"
    volumes:
      - $PWD/redis.conf:/usr/local/etc/redis/redis.conf

# cmd instruksi
docker-compose -f redis-docker-compose.yml up
docker exec -it redis-stack sh
/data # redis-cli
127.0.0.1:6379> auth rahasia
OK
127.0.0.1:6379> ping
PONG



########################################################################
# Databases
# Namun sedikit berbeda, jika di relational database kita bisa membuat database dengan menggunakan nama database, di redis kita hanya bisa menggunakan angka sebagai database
# Secara default database di redis adalah 0 (nol)
#Kita bisa menggunakan database sejumlah maksimal sesuai dengan konfigurasi yang kita gunakan di file konfigurasi
# redis.conf
# search keyword= databases.. default value databases 16, jadi dimulai dari 0 samapi 16 -1

#untuk berpindah database dengan cara
#select databases --> select 0 ~ 16 -1  or max databases