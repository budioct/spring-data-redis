# instruksi FROM (dijalankan untuk pull image yang ada di docker hub di proses build image)
FROM redis:7.2

# intruksi WORKDIR (untuk menentukan direktory/folder untuk menjalankan instruksi RUN, CMD, ENTRYPOINT, COPY, dan ADD)
#WORKDIR /hello

# intruksi COPY (dijalankan untuk menambahkan file dari source ke dalam folder destination di Docker Image)
# menambahkan semua file .txt ke folder hello
#COPY config/*.conf /hello
#COPY config/redis.conf /usr/local/etc/redis/redis.conf

EXPOSE 6379

# instruksi COMMAND/CMD (dijalankan saat container running)
CMD ["redis-server"]