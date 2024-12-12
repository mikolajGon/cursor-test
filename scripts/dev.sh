#!/bin/bash

# Create a new tmux session named 'library'
tmux new-session -d -s library -n "db"
tmux send-keys 'yarn start:db' C-m
tmux send-keys 'yarn build:shared' C-m

# Create windows for each service
tmux new-window -t library:1 -n "book-service"
tmux send-keys 'yarn dev:book-service' C-m

tmux new-window -t library:2 -n "user-service"
tmux send-keys 'yarn dev:user-service' C-m

tmux new-window -t library:3 -n "auth-service"
tmux send-keys 'yarn dev:auth-service' C-m

tmux new-window -t library:4 -n "api-gateway"
tmux send-keys 'yarn dev:api-gateway' C-m

tmux new-window -t library:5 -n "frontend"
tmux send-keys 'yarn start:frontend' C-m

# Add Jaeger UI window
tmux new-window -t library:6 -n "tracing-ui"
tmux send-keys 'docker run -d --name jaeger \
  -e COLLECTOR_OTLP_ENABLED=true \
  -p 16686:16686 \
  -p 4317:4317 \
  -p 4318:4318 \
  jaegertracing/all-in-one:latest && \
  echo "Jaeger UI available at http://localhost:16686"' C-m

# Select the first window
tmux select-window -t library:0

# Attach to the session
tmux attach-session -t library 