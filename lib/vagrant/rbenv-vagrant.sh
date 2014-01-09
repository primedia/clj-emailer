#!/bin/bash

ruby=1.9.3.p484
jruby=jruby-1.7.9

# rbenv
if ! [ -d ~vagrant/.rbenv ] ; then
  su - vagrant -c "git clone git://github.com/sstephenson/rbenv.git ~/.rbenv"
  su - vagrant -c "echo 'export PATH="~vagrant/.rbenv/bin:~vagrant/.rbenv/shims:/usr/local/bin:${PATH}"' >> ~/.bash_profile"
  su - vagrant -c "git clone https://github.com/sstephenson/ruby-build.git ~/.rbenv/plugins/ruby-build"
  su - vagrant -c "rbenv rehash"

  echo "Installing Ruby ${ruby}"
  su - vagrant -c "rbenv install ${ruby}"
  su - vagrant -c "rbenv rehash"
  su - vagrant -c "rbenv global ${ruby}"
  su - vagrant -c "rbenv exec gem update --system"
  su - vagrant -c "rbenv exec gem install --no-document bundler pry rake thor"
  su - vagrant -c "rbenv rehash"

  echo "Installing JRuby ${jruby}"
  su - vagrant -c "rbenv install ${jruby}"
  su - vagrant -c "rbenv rehash"
  su - vagrant -c "export RBENV_VERSION=${jruby} && rbenv exec gem update --system"
  su - vagrant -c "export RBENV_VERSION=${jruby} && rbenv exec gem install --no-document bundler pry rake thor"
  su - vagrant -c "export RBENV_VERSION=${jruby} && rbenv rehash"
fi
