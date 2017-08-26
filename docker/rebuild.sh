#!/usr/bin/env bash

set -e
docker build -t highload -f docker/Dockerfile .
docker tag highload stor.highloadcup.ru/travels/owl_huntery
docker push stor.highloadcup.ru/travels/owl_huntery