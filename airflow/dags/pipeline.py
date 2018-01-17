from airflow import DAG
from airflow.operators.bash_operator import BashOperator
from airflow.contrib.operators.dataproc_operator import DataProcSparkOperator, DataprocClusterCreateOperator, DataprocClusterDeleteOperator, DataProcPySparkOperator
from datetime import datetime, timedelta


default_args = {
    'owner': 'airflow',
    'depends_on_past': False,
    'start_date': datetime(2017, 01, 16),
    'email': ['tomlous@gmail.com'],
    'email_on_failure': True,
    'email_on_retry': False,
    'retries': 1,
    'retry_delay': timedelta(minutes=3)
}

dag = DAG('pipeline', default_args=default_args, schedule_interval=None, catchup=False)

gcpProjectName = "training-167208"
gcpClusterName = "meetup-cluster"
gcpRegion = "global"
gcpZone = "europe-west1-d"
gcpDataStorage = "gs://meetup-bucket"
gcpJar = "gs://meetup-bucket/jobs/meetup-spark-airflow-demo-assembly-0.1.jar"
gcpInitActions = 'gs://datlinq/dev/jobs/install-sql-proxy.sh'

# Create a Spark Cluster
create_spark_cluster = DataprocClusterCreateOperator(
    task_id='create_spark_cluster',
    trigger_rule='one_success',
    execution_timeout=timedelta(minutes=10),
    cluster_name=gcpClusterName,
    project_id=gcpProjectName,
    num_workers=2,
    zone=gcpZone,
    # init_actions_uris=[gcpInitActions],
    master_machine_type='n1-highmem-4',
    master_disk_size=500,
    worker_machine_type='n1-highmem-8',
    worker_disk_size=500,
    region=gcpRegion,
    dag=dag)


# # Destroy a Spark Cluster
# destroy_spark_cluster = DataprocClusterDeleteOperator(
#     task_id='destroy_spark_cluster',
#     trigger_rule='all_done',
#     execution_timeout=timedelta(minutes=10),
#     cluster_name=gcpClusterName,
#     project_id=gcpProjectName,
#     region=gcpRegion,
#     dag=dag)


# Ia ETL Facebook
etl_facebook = DataProcSparkOperator(
    task_id='etl_facebook',
    execution_timeout=timedelta(minutes=30),
    cluster_name=gcpClusterName,
    dataproc_spark_jars=[gcpJar],
    main_class='etl.Ia_Facebook',
    arguments=[gcpDataStorage + "/data/facebook-nl.json",
               gcpDataStorage + "/data/facebook-nl.parquet"],
    dag=dag)

# Ib ETL Factual
etl_factual = DataProcSparkOperator(
    task_id='etl_factual',
    execution_timeout=timedelta(minutes=30),
    cluster_name=gcpClusterName,
    dataproc_spark_jars=[gcpJar],
    main_class='etl.Ib_Factual',
    arguments=[gcpDataStorage + "/data/factual-nl.json",
               gcpDataStorage + "/data/factual-nl.parquet"],
    dag=dag)


# IIa Link on FacebookId
match_facebook_id = DataProcSparkOperator(
    task_id='match_facebook_id',
    execution_timeout=timedelta(minutes=30),
    cluster_name=gcpClusterName,
    dataproc_spark_jars=[gcpJar],
    main_class='link.IIa_MatchByFacebookId',
    arguments=[gcpDataStorage + "/data/facebook-nl.parquet",
               gcpDataStorage + "/data/factual-nl.parquet",
               gcpDataStorage + "/data/match-facebook-id.parquet"],
    dag=dag)

# IIb Link on Name & Address
match_name_address = DataProcSparkOperator(
    task_id='match_name_address',
    execution_timeout=timedelta(minutes=30),
    cluster_name=gcpClusterName,
    dataproc_spark_jars=[gcpJar],
    main_class='link.IIb_MatchByNameAddress',
    arguments=[gcpDataStorage + "/data/facebook-nl.parquet",
               gcpDataStorage + "/data/factual-nl.parquet",
               gcpDataStorage + "/data/match-name-address.parquet"],
    dag=dag)

# III Merge & dedupe
merge_dedupe = DataProcSparkOperator(
    task_id='merge_dedupe',
    execution_timeout=timedelta(minutes=30),
    cluster_name=gcpClusterName,
    dataproc_spark_jars=[gcpJar],
    main_class='link.III_MergeDedupe',
    arguments=[gcpDataStorage + "/data/match-facebook-id.parquet",
               gcpDataStorage + "/data/match-name-address.parquet",
               gcpDataStorage + "/data/match-all.parquet"],
    dag=dag)


# IV Enrich with keywords
enrich_keywords = DataProcSparkOperator(
    task_id='enrich_keywords',
    execution_timeout=timedelta(minutes=30),
    cluster_name=gcpClusterName,
    dataproc_spark_jars=[gcpJar],
    main_class='enrich.IV_Keywords',
    arguments=[gcpDataStorage + "/data/match-all.parquet",
               gcpDataStorage + "/data/match-all-enriched.parquet"],
    dag=dag)


# Show Output
show_output = DataProcSparkOperator(
    task_id='show_output',
    execution_timeout=timedelta(minutes=30),
    cluster_name=gcpClusterName,
    dataproc_spark_jars=[gcpJar],
    main_class='helper.ParquetShow',
    arguments=[gcpDataStorage + "/data/match-all-enriched.parquet"],
    dag=dag)

# Export to ElasticSearch
export_elasticsearch = DataProcSparkOperator(
    task_id='export_elasticsearch',
    execution_timeout=timedelta(minutes=30),
    cluster_name=gcpClusterName,
    dataproc_spark_jars=[gcpJar],
    main_class='etl.V_ExportToElasticSearch',
    arguments=[gcpDataStorage + "/data/match-all-enriched.parquet",
               "records/nl"],
    dataproc_spark_properties={
        'es.nodes': "elk-1-vm"
    },
    dag=dag)



### Flow

# destroy_spark_cluster << export_elasticsearch
# destroy_spark_cluster << show_output

export_elasticsearch << enrich_keywords
show_output << enrich_keywords

enrich_keywords << merge_dedupe

merge_dedupe << match_name_address
merge_dedupe << match_facebook_id


match_facebook_id << etl_facebook
match_facebook_id << etl_factual

match_name_address << etl_facebook
match_name_address << etl_factual

etl_facebook << create_spark_cluster
etl_factual << create_spark_cluster
