#!/usr/bin/env bash
set -x
gsutil mb -p training-167208 -c regional -l europe-west1 gs://meetup-bucket

# @todo change to your config (project, bucket)
echo "Launch Bitnami's ELK stack as well: https://console.cloud.google.com/launcher/config/bitnami-launchpad/elk"
