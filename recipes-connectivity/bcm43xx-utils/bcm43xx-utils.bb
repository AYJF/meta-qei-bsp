DESCRIPTION = "Startup and config files for use with BCM43XX WIFI and Bluetooth"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES','systemd','','update-rc.d-native',d)}"

SRC_URI = " \
	file://qei-wifi \
	file://qei-wifi.service \
	file://qei-bt \
	file://qei-bt.service \
"

FILES:${PN} = " \ 
	${sysconfdir}/wifi/*  \
	${sysconfdir}/bluetooth/*  \
	${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '${systemd_unitdir}/system/* ${sysconfdir}/systemd/system/multi-user.target.wants/*', \
			'${sysconfdir}/init.d ${sysconfdir}/rcS.d ${sysconfdir}/rc2.d ${sysconfdir}/rc3.d ${sysconfdir}/rc4.d ${sysconfdir}/rc5.d', d)} \
"

RDEPENDS:${PN}:imx6ul-var-dart = "i2c-tools"
RDEPENDS:${PN}:imx8mq-var-dart = "i2c-tools"
RDEPENDS:${PN}:imx8mm-var-dart = "i2c-tools"
RDEPENDS:${PN}:imx8mn-var-som = "i2c-tools"
RDEPENDS:${PN}:imx8mp-var-dart = "i2c-tools"
RDEPENDS:${PN}:imx93-var-som = "i2c-tools"
RDEPENDS:${PN}:append = " base-files libgpiod-tools"

S = "${WORKDIR}"

do_install() {
	install -d ${D}${sysconfdir}/wifi
	install -m 0755 ${WORKDIR}/qei-wifi ${D}/${sysconfdir}/wifi

	install -d ${D}${sysconfdir}/bluetooth
	install -m 0755 ${WORKDIR}/qei-bt ${D}/${sysconfdir}/bluetooth

	if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
		install -d ${D}${systemd_unitdir}/system
		install -d ${D}${sysconfdir}/systemd/system/multi-user.target.wants
		install -m 0644 ${WORKDIR}/qei-wifi.service ${D}/${systemd_unitdir}/system
		install -m 0644 ${WORKDIR}/qei-bt.service ${D}/${systemd_unitdir}/system
 
		ln -sf ${systemd_unitdir}/system/qei-wifi.service \
			${D}${sysconfdir}/systemd/system/multi-user.target.wants/qei-wifi.service
		ln -sf ${systemd_unitdir}/system/qei-bt.service \
			${D}${sysconfdir}/systemd/system/multi-user.target.wants/qei-bt.service
	else
		install -d ${D}${sysconfdir}/init.d
		ln -s ${sysconfdir}/wifi/qei-wifi ${D}${sysconfdir}/init.d/qei-wifi
		update-rc.d -r ${D} qei-wifi start 5 S .

		ln -s ${sysconfdir}/bluetooth/qei-bt ${D}${sysconfdir}/init.d/qei-bt
		update-rc.d -r ${D} qei-bt start 99 2 3 4 5 .
	fi
}

COMPATIBLE_MACHINE = "(imx6ul-var-dart|imx7-var-som|imx8mm-var-dart|imx8mn-var-som|imx8mq-var-dart|imx8qm-var-som|imx8qxp-var-som|imx8qxpb0-var-som|imx8mp-var-dart|imx93-var-som)"
