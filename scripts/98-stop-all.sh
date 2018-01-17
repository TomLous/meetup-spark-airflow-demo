#!/usr/bin/env bash
ps aux | grep airflow | grep -v grep | awk '{print $2}' | xargs -I{} kill {}
ps aux | grep celeryd | grep -v grep | awk '{print $2}' | xargs -I{} kill {}
ps aux | grep redis | grep -v grep | awk '{print $2}' | xargs -I{} kill {}
rm dump.rdb