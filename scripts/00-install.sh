#!/usr/bin/env bash
# MAc
sudo pip install "apache-airflow[celery,devel,crypto,gcp_api,jdbc,hdfs,mysql,slack,redis]"
brew install redis
sudo pip install -U "celery[redis]"

airflow connections -d --conn_id=google_cloud_default
airflow connections -a --conn_id=google_cloud_default --conn_uri="" --conn_type="google_cloud_platform" --conn_extra='{"extra__google_cloud_platform__project": "training-167208"}'
