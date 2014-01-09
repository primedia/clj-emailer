#!/bin/bash

if ! [ -f /etc/profile.d/ulimit.sh ] ; then
  echo "ulimit -S -n 4096" >> /etc/profile.d/ulimit.sh
fi
