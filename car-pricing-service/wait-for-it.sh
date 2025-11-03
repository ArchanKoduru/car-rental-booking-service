#!/usr/bin/env bash
# wait-for-it.sh

set -e

host="mysql"
port="3306"

echo "Waiting for MySQL at $host:$port..."

while ! (echo > /dev/tcp/$host/$port) >/dev/null 2>&1; do
  sleep 2
done

echo "MySQL is ready on $host:$port!"
exec "$@"
