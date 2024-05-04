# Copyright (C) 2013-2016 Freescale Semiconductor
# Copyright 2017 NXP
# Copyright 2018-2020 Variscite Ltd.
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Linux kernel provided and supported by QEI"
DESCRIPTION = "Linux kernel provided and supported by QEI (based on the kernel provided by NXP) \
with focus on i.MX Family SOMs. It includes support for many IPs such as GPU, VPU and IPU."

require recipes-kernel/linux/linux-imx.inc
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

FILES:${KERNEL_PACKAGE_NAME}-base += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/modules.builtin.modinfo "

DEPENDS += "lzop-native bc-native"

DEFAULT_PREFERENCE = "1"

KERNEL_SRC ?= "git://github.com/AYJF/linux-imx;protocol=https"

SRCBRANCH = "5.15-2.0.x-imx_qei"
SRCREV = "ec05be6a6271f224d3d5c526883ec2b38977408e"
LINUX_VERSION = "5.15.60"

SRC_URI = "${KERNEL_SRC};branch=${SRCBRANCH}"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

LOCALVERSION:imx8mq-qei-epaq9710 = "-imx8mq"

KBUILD_DEFCONFIG:imx8mq-qei-epaq9710 = "imx8mq_var_dart_defconfig"
DEFAULT_DTB:imx8mq-qei-epaq9710 = "sd-lvds"
DEFAULT_DTB_PREFIX:imx8mq-qei-epaq9710 = "imx8mq-var-dart-dt8mcustomboard"

pkg_postinst:kernel-devicetree:append () {
   rm -f $D/boot/devicetree-*
}

pkg_postinst:kernel-devicetree:append:imx8mq-qei-epaq9710 () {
    cd $D/boot
    ln -s ${DEFAULT_DTB_PREFIX}-${DEFAULT_DTB}.dtb ${DEFAULT_DTB_PREFIX}.dtb
    ln -s ${DEFAULT_DTB_PREFIX}-legacy-${DEFAULT_DTB}.dtb ${DEFAULT_DTB_PREFIX}-legacy.dtb
}

pkg_postinst:kernel-devicetree:append:imx8qxp-var-som () {
    cd $D/boot
    ln -s ${DEFAULT_DTB_PREFIX}-${DEFAULT_DTB}.dtb ${DEFAULT_DTB_PREFIX}.dtb
}

pkg_postinst:kernel-devicetree:append:imx8qxpb0-var-som () {
    cd $D/boot
    ln -s ${DEFAULT_DTB_PREFIX}-${DEFAULT_DTB}.dtb ${DEFAULT_DTB_PREFIX}.dtb
}

pkg_postinst:kernel-devicetree:append:imx8qm-var-som () {
    cd $D/boot
    ln -s ${DEFAULT_DTB_PREFIX}-${DEFAULT_DTB}.dtb ${DEFAULT_DTB_PREFIX}.dtb
    ln -s imx8qp-var-som-symphony-${DEFAULT_DTB}.dtb imx8qp-var-som-symphony.dtb
    ln -s imx8qm-var-spear-sp8customboard-${DEFAULT_DTB}.dtb imx8qm-var-spear-sp8customboard.dtb
    ln -s imx8qp-var-spear-sp8customboard-${DEFAULT_DTB}.dtb imx8qp-var-spear-sp8customboard.dtb
}

KERNEL_VERSION_SANITY_SKIP="1"
COMPATIBLE_MACHINE = "(mx8-nxp-bsp|mx9-nxp-bsp)"
