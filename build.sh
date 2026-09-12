#!/usr/bin/env bash

set -euo pipefail

project_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
package_root="${project_dir}/target/debian-package-root"
package_file="${project_dir}/target/alien-force_1.0.1_all.deb"

cd "${project_dir}"
mvn clean package

rm -rf "${package_root}"
install -Dm0644 deb/control "${package_root}/DEBIAN/control"
install -Dm0644 target/alien-force.jar \
    "${package_root}/usr/share/alien-force/alien-force.jar"
install -Dm0755 deb/launcher.sh "${package_root}/usr/bin/alien-force"
install -Dm0644 deb/alien-force.desktop \
    "${package_root}/usr/share/applications/alien-force.desktop"
install -Dm0644 deb/alien-force.png \
    "${package_root}/usr/share/icons/hicolor/256x256/apps/alien-force.png"
find "${package_root}" -type d -exec chmod 0755 {} +

dpkg-deb --root-owner-group --build "${package_root}" "${package_file}"
echo "Built ${package_file}"
