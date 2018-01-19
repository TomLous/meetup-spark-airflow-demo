#!/usr/bin/env bash
# Mac
# install a mysql database somewhere
sudo pip install "apache-airflow[celery,devel,crypto,gcp_api,jdbc,hdfs,mysql,slack,redis]"
brew install redis
sudo pip install -U "celery[redis]"

# @todo install gcloud
echo "Install gcloud CLI tools and point to your project!"


# @todo Change airflow.cfg
# logging_level=WARN
# executor = CeleryExecutor
# sql_alchemy_conn = mysql://root@localhost/airflow # Change accroding to db
# celery_app_name = airflow.executors.celery_executor
# broker_url =redis://localhost:6379/0


