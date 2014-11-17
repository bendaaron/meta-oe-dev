SUMMARY = "Small Footprint CIM Client Library"
DESCRIPTION = "Small Footprint CIM Client Library Runtime Libraries"
HOMEPAGE = "http://www.sblim.org"
SECTION = "Applications/System"

SRC_URI = "http://pkgs.fedoraproject.org/repo/pkgs/${PN}/${PN}-${PV}.tar.bz2/44a2893f0e2dbc37e60b91d8f7d7d83b/${PN}-${PV}.tar.bz2"

SRC_URI[md5sum] = "44a2893f0e2dbc37e60b91d8f7d7d83b"
SRC_URI[sha256sum] = "b22931c9247f07f9d842e50b0f4377a31752bb9e5aec6b529faea2ba9b8e862d"

LICENSE = "EPL-1.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=f300afd598546add034364cd0a533261"

DEPENDS = "curl libpam"
RDEPENDS_${PN} = "cim-schema"
INSANE_SKIP_${PN} = "unsafe-references-in-binaries"

inherit autotools

do_configure_prepend() {
	chmod a-x ${S}/backend/cimxml/*.[ch]
}

do_install() {
	oe_runmake DESTDIR=${D} install
	# remove unused libtool files
	rm -rf ${D}/${libdir}/*la
	rm -rf ${D}/usr/share/doc/sfcc-*
}
