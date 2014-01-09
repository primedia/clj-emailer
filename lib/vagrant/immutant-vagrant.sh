#!/bin/bash

immutant='{:user {:plugins [[lein-immutant \"1.1.1\"]]}}'

if ! [ -f ~vagrant/.lein/profiles.clj ]
then
  su - vagrant -c "mkdir -p ~/.lein"
  su - vagrant -c "echo ${immutant} >> ~/.lein/profiles.clj"
fi
