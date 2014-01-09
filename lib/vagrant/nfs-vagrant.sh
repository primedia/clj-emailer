if ! [ -L /etc/rc3.d/S95nfs ] ; then
  ln -nfs /etc/init.d/nfs /etc/rc3.d/S95nfs
  service nfs start
fi
