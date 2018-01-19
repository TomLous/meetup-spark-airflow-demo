#!/usr/bin/env bash
gsutil rm -r gs://meetup-bucket # @todo Change to your bucket name
gcloud compute instances delete elk-1-vm

# Remove your elk vm as well


