#!/usr/bin/env bash
set -x
gsutil mb -p training-167208 -c regional -l europe-west1 gs://meetup-bucket


gcloud sql instances