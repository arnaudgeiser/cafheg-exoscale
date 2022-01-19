# Demo

## Provision one VM

git checkout tags/demo1

## Initialize through cloud-init

git checkout tags/demo2

## Provision more VMs

git checkout tags/demo3

## Managed Elastic IP

git checkout tags/demo4

## MariaDB Database

git checkout tags/demo5

- terraform apply
- modify the JDBC connection in Database.java with the correct IP
- mvn package
- upload the new artifact to SOS
- terraform replace the cafheg instances
