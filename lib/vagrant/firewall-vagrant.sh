#!/bin/bash
sudo service iptables save
sudo service iptables stop
sudo chkconfig iptables off
