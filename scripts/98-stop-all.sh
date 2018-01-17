#!/usr/bin/env bash
ps aux | grep airflow | grep -v grep | awk '{print $2}' | xargs -I{} kill {}
ps aux | grep celeryd | grep -v grep | awk '{print $2}' | xargs -I{} kill {}
ps aux | grep redis | grep -v grep | awk '{print $2}' | xargs -I{} kill {}

ps aux | grep airflow | grep -v grep | awk '{print $2}' | xargs -I{} kill -9 {}
ps aux | grep celeryd | grep -v grep | awk '{print $2}' | xargs -I{} kill -9 {}
ps aux | grep redis | grep -v grep | awk '{print $2}' | xargs -I{} kill -9 {}
rm dump.rdb