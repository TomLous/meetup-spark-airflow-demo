#!/usr/bin/env bash
redis-server &
airflow worker &
airflow scheduler &
airflow webserver &

sleep 5
open http://localhost:8085 &
# @todo change to your local port wher you run airflow
