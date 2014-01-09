#!/bin/bash
# rbenv
if ! [ -f /etc/profile.d/term.sh ] ; then
  echo "export TERM=$1" > /etc/profile.d/term.sh
fi
