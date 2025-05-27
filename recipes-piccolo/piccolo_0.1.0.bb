SUMMARY = "recipe to install piccolo"
DESCRIPTION = "install piccolo"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = "gitsm://github.com/MCO-PICCOLO/Pullpiri.git;protocol=http;branch=arm"
SRCREV = "c23b77aaa038c5df3e90fb73edbcb8bcbe6f7972"

SRC_URI:append = " file://0001-add-file-for-yocto.patch"

S = "${WORKDIR}/git"

DEPENDS += "docker-moby"
RDEPENDS_${PN} += "podman"

PACKAGES = "${PN}"

FILES:${PN} += "/root/*"
FILES:${PN} += "/etc/containers/systemd/piccolo/*"

inherit image-oci container-host systemd

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "piccolo-pull.service"

FILES:${PN} += "/etc/systemd/system/*"

do_configure() {
    mkdir -p ${WORKDIR}/out
}

do_compile() {
    cd ${S}
    docker buildx create --name multiarch-builder --use

    docker pull --platform linux/arm64 gcr.io/etcd-development/etcd:v3.5.11
    docker pull --platform linux/arm64 public.ecr.aws/docker/library/rust:1.78-slim
    docker pull --platform linux/arm64 public.ecr.aws/docker/library/alpine:3.20
    docker pull --platform linux/arm64 public.ecr.aws/docker/library/ubuntu:20.04

    docker build . --platform=linux/arm64 --pull=false -t piccolo:1.0 -f ${S}/containers/Dockerfile-arm
 
    skopeo copy docker-daemon:piccolo:1.0 oci-archive:${WORKDIR}/out/piccolo.tar:piccolo:1.0
    skopeo copy docker-daemon:gcr.io/etcd-development/etcd:v3.5.11 oci-archive:${WORKDIR}/out/etcd.tar:gcr.io/etcd-development/etcd:v3.5.11
}

do_install() {
    install -d ${D}/root/piccolo_yaml
    install -d ${D}/root/piccolo_images
    install -d ${D}/etc/containers/systemd/piccolo
    install -d ${D}/etc/containers/systemd/piccolo/example
    install -d ${D}/etc/containers/systemd/piccolo/etcd-data
    install -d ${D}/etc/systemd/system
    install -m 0755 ${S}/src/settings.yaml ${D}/etc/containers/systemd/piccolo/
    install -m 0755 ${S}/containers/piccolo.kube ${D}/etc/containers/systemd/piccolo/
    install -m 0755 ${S}/containers/piccolo.yaml ${D}/etc/containers/systemd/piccolo/
    install -m 0755 ${S}/containers/piccolo.sh ${D}/root/piccolo_images/
    install -m 0644 ${S}/containers/piccolo-pull.service ${D}/etc/systemd/system/
    install -m 0755 ${WORKDIR}/out/* ${D}/root/piccolo_images/
    cp -r ${S}/examples/helloworld/* ${D}/root/piccolo_yaml/
    install -m 0755 ${S}/containers/agent-00-default.conf ${D}/root/piccolo_images/
    install -m 0755 ${S}/containers/controller-00-default.conf ${D}/root/piccolo_images/
    install -m 0755 ${THISDIR}/files/helloworld.tar ${D}/root/piccolo_images/
    install -m 0755 ${THISDIR}/files/hellosoafee.tar ${D}/root/piccolo_images/
    install -m 0755 ${THISDIR}/files/sendgear.tar ${D}/root/piccolo_images/
    install -m 0755 ${THISDIR}/files/sendDrive.sh ${D}/root/piccolo_yaml/
    install -m 0755 ${THISDIR}/files/sendNeutral.sh ${D}/root/piccolo_yaml/
    install -m 0755 ${THISDIR}/files/sendParking.sh ${D}/root/piccolo_yaml/
    install -m 0755 ${THISDIR}/files/sendReverse.sh ${D}/root/piccolo_yaml/
}

REQUESTED_DISTRO_FEATURES = "virtualization"
