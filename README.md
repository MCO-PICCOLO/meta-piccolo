# meta-piccolo

To build a piccolo container within the Yocto build process, Docker was used. Docker and Skopeo need to be added to HOSTTOOLS in the bitbake.conf file of Poky git.
If you want to separate the Yocto build and the container build, you can build the container first and then skip docker build command in do compile section.

