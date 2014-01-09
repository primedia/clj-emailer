if ! [ -d /usr/local/idg ] ; then
  mkdir -p /usr/local/idg
  cd /usr/local/idg
  curl -L -o /tmp/idgtools.tar.bz2 http://ec2-54-224-24-95.compute-1.amazonaws.com/kits/idgtools.tar.bz2
  tar xjf /tmp/idgtools.tar.bz2
  rm /tmp/idgtools.tar.bz2
  echo "export PATH=/usr/local/idg/bin:$PATH"      > /etc/profile.d/idgtools.sh
  echo "set path = \( /usr/local/idg/bin $path \)" > /etc/profile.d/idgtools.csh
  chmod 755 /etc/profile.d/{idgtools.sh,idgtools.csh}
fi
