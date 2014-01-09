#!/bin/bash

if ! [ -d /opt/jenkins ] ; then
  echo "Installing Jenkins ..."
  mkdir -p /opt/jenkins/bin /var/log/jenkins
  chown -R vagrant:vagrant /opt/jenkins /var/log/jenkins
  curl -s -S -L http://mirrors.jenkins-ci.org/war/latest/jenkins.war -o /opt/jenkins/bin/jenkins.war
fi

if ! [ -f /etc/init.d/jenkins ] ; then
tee << EOF > /etc/init.d/jenkins
#!/bin/bash

. /etc/rc.d/init.d/functions

jenkins_pid() {
  jenkins_pid=\$(ps aux | grep 'jenkins.war' | grep -v grep | awk '{print \$2}')  
}

start_jenkins() {
  jenkins_pid
  if [ "\$jenkins_pid" != "" ]
  then
    echo "Jenkins is already running, pid = \$jenkins_pid"
    RETVAL=1
  else
    echo "Starting Jenkins ..."
    su - vagrant -c "java -jar /opt/jenkins/bin/jenkins.war --httpPort=8090 >> /var/log/jenkins/jenkins.log 2>&1 &"
    RETVAL=\$?
    if [ \$RETVAL == 0 ]
    then
      jenkins_pid
      echo "Jenkins started on PID \$jenkins_pid"
    fi
  fi
}

stop_jenkins() {
  jenkins_pid
  if [ "\$jenkins_pid" != "" ]
  then
    echo "Jenkins is running on PID \$jenkins_pid"
    RETVAL=\$?
  else
    echo "Jenkins is not running."
  fi
}

jenkins_status() {
  jenkins_pid
  if [ "$jenkins_pid" != "" ]
  then
    echo "Jenkins is running on PID $jenkins_pid"
    RETVAL=$?
  else
    echo "Jenkins is not running."
  fi
}

# See how we were called.
case "\$1" in
  start)
    start_jenkins
  ;;
  stop)
    stop_jenkins
  ;;
  restart)
    stop_jenkins
    start_jenkins
  ;;
  status)
    jenkins_status
  ;;
  *)
    echo "Usage: jenkins {start|stop|status|restart}"
    RETVAL=2
esac

exit \${RETVAL}
EOF

  chmod 755 /etc/init.d/jenkins

  cd /etc/rc1.d/
  ln -nfs ../init.d/jenkins K01jenkins

  cd /etc/rc2.d/
  ln -nfs ../init.d/jenkins K01jenkins

  cd /etc/rc3.d/
  ln -nfs ../init.d/jenkins S97jenkins

  cd /etc/rc4.d/
  ln -nfs ../init.d/jenkins K01jenkins

  cd /etc/rc5.d/
  ln -nfs ../init.d/jenkins K01jenkins

  cd /etc/rc6.d/
  ln -nfs ../init.d/jenkins K01jenkins
fi
