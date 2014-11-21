DESCRIPTION = "netkit-telnet includes the telnet daemon and client."
SECTION = "base"
DEPENDS = "ncurses"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://telnet/telnet.cc;beginline=2;endline=3;md5=780868e7b566313e70cb701560ca95ef"
PR = "r1"

SRC_URI = "ftp://ftp.uk.linux.org/pub/linux/Networking/netkit/netkit-telnet-${PV}.tar.gz \
    file://configure-modify-parameter.patch \
    file://telnet-modify-files.patch \
    file://telnetd-Makefile-modify-parameter.patch \
    file://telnet-xinetd \
"

EXTRA_OEMAKE = "INSTALLROOT=${D} SBINDIR=${sbindir} DAEMONMODE=755 \
    MANMODE=644 MANDIR=${mandir}"

do_configure () {
    ./configure --prefix=${prefix}
    echo "LDFLAGS=${LDFLAGS}" > MCONFIG
}

do_compile () {
    oe_runmake 'CC=${CC}' 'LD=${LD}' 'LDFLAGS=${LDFLAGS}' SUB=telnet
    oe_runmake 'CC=${CC}' 'LD=${LD}' 'LDFLAGS=${LDFLAGS}' LIBS=-lutil SUB=telnetd        
}

do_install () {
    install -d ${D}${bindir}
    install -m 0755 telnet/telnet ${D}${bindir}/telnet.${PN}
    install -d ${D}${sbindir}
    install -d ${D}${mandir}/man{1,5,8}
    oe_runmake SUB=telnetd install
    rm -rf ${D}${mandir}/man1
    install -d  ${D}/etc/xinetd.d/
    install -p -m644 ${WORKDIR}/telnet-xinetd ${D}/etc/xinetd.d/telnet
}

pkg_postinst_${PN} () {
#!/bin/sh
    update-alternatives --install ${bindir}/telnet telnet telnet.${PN} 100
}

pkg_prerm_${PN} () {
#!/bin/sh
    update-alternatives --remove telnet telnet.${PN} 100
}

SRC_URI[md5sum] = "d6beabaaf53fe6e382c42ce3faa05a36"
SRC_URI[sha256sum] = "9c80d5c7838361a328fb6b60016d503def9ce53ad3c589f3b08ff71a2bb88e00"
