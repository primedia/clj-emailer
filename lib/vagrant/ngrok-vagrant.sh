# install ngrok if it not installed.

if ! [ -f /usr/local/bin/ngrok ] ; then 
  cd /usr/local/bin
  curl -LO https://dl.ngrok.com/linux_386/ngrok.zip
  unzip ngrok.zip
fi
