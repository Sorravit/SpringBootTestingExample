#!/bin/bash
DATABASE_DOCKER_NAME="mysql"

goal_database_start() {
  docker run --detach --env MYSQL_ROOT_PASSWORD=sorravit --env MYSQL_USER=sorravit --env MYSQL_PASSWORD=sorravit --env MYSQL_DATABASE=big --name ${DATABASE_DOCKER_NAME} --publish 3306:3306 mysql
}
goal_database_stop() {
  docker stop ${DATABASE_DOCKER_NAME}
}
goal_database_clear() {
  docker stop ${DATABASE_DOCKER_NAME}
  docker rm ${DATABASE_DOCKER_NAME}
}

if type -t "goal_$1" &>/dev/null; then
  goal_"$1" "${@:-2}"
else
  echo "usage: $0 <goal>
    goal:
        database_start                      -- start database from mysql docker image
        database_stop                       -- stop database from mysql docker image
        "
  exit 1
fi
