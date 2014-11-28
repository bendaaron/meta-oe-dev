DESCRIPTION = "A sophisticated console ftp client"
SECTION = "console/network"
PRIORITY = "optional"
LICENSE = "ClArtistic"
LIC_FILES_CHKSUM = "file://ncftp/cmds.c;beginline=3;endline=4;md5=9de76faeaedc4f908082e3f8142715f4"

SRC_URI = "ftp://ftp.ncftp.com/ncftp/older_versions/ncftp-${PV}-src.tar.bz2 \
    file://acinclude.m4 \
    file://Fix-errors-AR-Command-not-found.patch \
    file://Fix-error-expected-before-PRINTF_LONG_LONG.patch"

inherit autotools-brokensep

do_configure_prepend () {
    install -m 0644 ${WORKDIR}/acinclude.m4 acinclude.m4
}
do_install () {
    install -d ${D}${bindir} ${D}${sysconfdir} ${D}${mandir}
    oe_runmake 'prefix=${D}${prefix}' 'BINDIR=${D}${bindir}' \
        'SYSCONFDIR=${D}${sysconfdir}' 'mandir=${D}${mandir}' \
        install
}
SRC_URI[md5sum] = "384b7f01d725c89ccd30692628b3ac1b"
SRC_URI[sha256sum] = "2ebc7b51af96cb0fa8b703c7cb995bfb46ccf5312e335270d0420e260544c376"
