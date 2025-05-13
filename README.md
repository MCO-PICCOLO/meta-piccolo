# meta-piccolo
This repository provides a Yocto layer for the purpose of integrating the PICCOLO project into EWAOL.
Since we are syncing with the verified published version, it may differ from the latest pullpiri version.

## Regarding container builds in the Yocto build process
To build a piccolo container within the Yocto build process, Docker was used. Docker and Skopeo need to be added to HOSTTOOLS in the bitbake.conf file of Poky git.
If you want to separate the Yocto build and the container build, you can build the container first and then skip docker build command in do compile section.

## Set bluechi node
To execute PICCOLO examples, the setting of the bluechi node is required. This git is initially set with the hostname fvp-rd-kronos of the corellium target.

### 1. change your hostname to fvp-rd-kronos.
> hostnamectl hostname fvp-rd-kronos
> 
> systemctl restart bluechi-controller.service bluechi-agent.service podman.service

### 2. change your bluechi nodename to your hostname.
> cd /etc/bluechi/agent.conf.d
> 
Edit 00-default.conf and change bluechi-agent NodeName to your hostname.
> cd /etc/bluechi/controller.conf.d/
> 
Edit 00-default.conf and change bluechi-controller AllowedNodeNames to your bluechi-agent NodeName.
> cd /etc/containers/systemd/piccolo/
> 
Edit settings.yaml and change host name to your hostname.
> systemctl restart bluechi-controller.service bluechi-agent.service podman.service
> 
> systemctl restart piccolo.service
