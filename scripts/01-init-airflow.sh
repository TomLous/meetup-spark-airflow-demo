#!/usr/bin/env bash
airflow resetdb
airflow initdb
airflow connections -d --conn_id=google_cloud_default
airflow connections -a --conn_id=google_cloud_default --conn_uri="" --conn_type="google_cloud_platform" --conn_extra='{"extra__google_cloud_platform__project": "training-167208"}'
