SUMMARY = "SSL encryption wrapper between remote client and local (inetd-startable) or remote server."
SECTION = "net"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=45e8e7befe9a0f7e0543b78dfeebde20"

DEPENDS = "openssl zlib tcp-wrappers"
SRC_URI = "https://www.stunnel.org/downloads/${BP}.tar.gz"

SRC_URI[md5sum] = "c10edd84ecbc676a5a48c7e34ab3d1c5"
SRC_URI[sha256sum] = "505c6c63c4a20fc0cce8c35ef1ab7626c7b01071e3fca4ac6ea417afe8065309"


inherit autotools

EXTRA_OECONF += "--with-ssl='${STAGING_INCDIR}' --disable-fips"
