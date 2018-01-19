#!/usr/bin/env bash
DEPENDENCYSCOPE=${1:-provided}
cd ..
set -x
sbt -DsparkDependencyScope=$DEPENDENCYSCOPE 'set test in assembly := {}' clean assembly
gsutil -m cp -r target/scala-2.11/meetup-spark-airflow-demo-assembly-0.1.jar gs://meetup-bucket/jobs/
# @todo change to your bucket location
