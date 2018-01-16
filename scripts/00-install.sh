#!/usr/bin/env bash
# MAc
sudo pip install "apache-airflow[celery,devel,crypto,gcp_api,jdbc,hdfs,mysql,slack,redis]"
brew install redis
sudo pip install -U "celery[redis]"

