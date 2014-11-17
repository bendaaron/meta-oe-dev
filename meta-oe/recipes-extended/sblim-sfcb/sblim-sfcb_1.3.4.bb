SUMMARY = "Small Footprint CIM Broker"
DESCRIPTION = "Small Footprint CIM Broker (sfcb) is a CIM server conforming to the \
CIM Operations over HTTP protocol. \
It is robust, with low resource consumption and therefore specifically \
suited for embedded and resource constrained environments. \
sfcb supports providers written against the Common Manageability \
Programming Interface (CMPI)."
HOMEPAGE = "http://www.sblim.org"
SECTION = "Applications/System"

inherit autotools

SRC_URI = "http://pkgs.fedoraproject.org/repo/pkgs/${PN}/${PN}-${PV}.tar.bz2/486fe536ad5ac5c58dd60ea03fc1bb21/${PN}-${PV}.tar.bz2 \
           file://1000sblim-sfcb-disable_auto_service_start1.patch \
	   file://config-site.${P} \
	   file://cimv217Final-MOFs.zip.org \
	   file://sfcb.service \
	   "

SRC_URI[md5sum] = "486fe536ad5ac5c58dd60ea03fc1bb21"
SRC_URI[sha256sum] = "e48253208034218f95e5c7076af6c1f6a533595df2f6cafc073173f8ac54ff02"

LICENSE = "EPL-1.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=f300afd598546add034364cd0a533261"

inherit ${@base_contains('VIRTUAL-RUNTIME_init_manager','systemd','systemd','', d)}
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "sblim-sfcb.service"
SYSTEMD_AUTO_ENABLE = "enable"

EXTRA_OECONF = '--enable-debug \
      	        --enable-ssl \
		--enable-pam \
		--enable-ipv6 \
		CFLAGS="${CFLAGS} -D_GNU_SOURCE" \
	       '

DEPENDS = "curl libpam openssl"

INSANE_SKIP_${PN} = "dev-so"

FILES_${PN} += "${libdir}/sfcb \
	        ${datadir}/sfcb \
	       "

FILES_${PN}-dbg += "${libdir}/sfcb/.debug"

# make all with -j option is unsafe.
PARALLEL_MAKE = ""

CONFIG_SITE="${WORKDIR}/config-site.${P}"

do_install() {
	oe_runmake DESTDIR=${D} install
	
	if ${@base_contains('DISTRO_FEATURES','systemd','true','false',d)}; then
	    install -d ${D}${systemd_unitdir}/system
    	install -m 0644 ${WORKDIR}/sfcb.service ${D}${systemd_unitdir}/system/sblim-sfcb.service
	fi 

        install -d ${D}/etc/init.d
	mv ${D}/etc/init.d/sfcb ${D}/etc/init.d/sblim-sfcb
	sed -i -e 's/\/var\/lock\/subsys\/sfcb/\/var\/lock\/subsys\/sblim-sfcb/g' ${D}/etc/init.d/sblim-sfcb
	rm -rf ${D}/${libdir}/sfcb/*.la

	export CIMSCHEMA_SOURCE="${WORKDIR}/cimv217Final-MOFs.zip.org"
	${D}${datadir}/sfcb/getSchema.sh -f ${D}/${datadir}/sfcb
	export CIMSCHEMA_SOURCE=""
}

pkg_postinst_${PN} () {
	OPTS=""

	if [ x"$D" != "x" ]; then
		OPTS="--root=$D"
		if type systemctl >/dev/null 2>/dev/null; then
			systemctl $OPTS ${SYSTEMD_AUTO_ENABLE} ${SYSTEMD_SERVICE}
		fi
		exit 1
	fi
	${datadir}/sfcb/genSslCert.sh /etc/sfcb
	${bindir}/sfcbrepos -f 
}
