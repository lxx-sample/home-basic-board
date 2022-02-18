#!/usr/bin/env bash

cd /Users/dongpo.li/IdeaProjects_workspace/home-basic-board/src/main/angular && npm run build

cd /Users/dongpo.li/IdeaProjects_workspace/home-basic-board && ./gradlew build -x test

scp /Users/dongpo.li/IdeaProjects_workspace/home-basic-board/build/libs/home-basic-board-1.0.0.jar dongpo.li@39.105.88.127:/home/dongpo.li/home-basic-board/

