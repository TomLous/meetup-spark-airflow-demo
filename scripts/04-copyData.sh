#!/usr/bin/env bash
set -x
gsutil -m cp -r ../data/facebook-nl.json gs://meetup-bucket/data/
gsutil -m cp -r ../data/factual-nl.json gs://meetup-bucket/data/