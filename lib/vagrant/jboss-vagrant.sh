#!/bin/bash

if ! [ -d /opt/jboss ] ; then
  echo "Downloading JBoss ..."
  cd /tmp
  curl -s -S http://ec2-54-224-24-95.compute-1.amazonaws.com/kits/jboss-as-7.1.1.Final.tar.gz -o /tmp/jboss-as-7.1.1.Final.tar.gz
  echo "Installing JBoss AS 7.1.1"
  tar xzf jboss-as-7.1.1.Final.tar.gz
  mv /tmp/jboss-as-7.1.1.Final /opt/jboss
  rm /tmp/jboss-as-7.1.1.Final.tar.gz
  mkdir -p /var/log/jboss
fi

echo "Starting JBoss AS 7"
/opt/jboss/bin/standalone.sh >> /var/log/jboss/jboss.log 2>&1 &
