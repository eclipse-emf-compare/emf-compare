#!/bin/bash
set -vx

os=$1
ws=$2
arch=$3
simrel=$4

if [[ ! -f "p2-admin-1.0.0-$os.$ws.$arch.tar.gz" ]]; then 
	wget --no-check-certificate -q "http://github.com/mbarbero/p2-admin/releases/download/v1.0/p2-admin-1.0.0-$os.$ws.$arch.tar.gz"
fi
if [[ -d "p2-admin" ]]; then
	rm -rf "p2-admin"
fi
tar zxf "p2-admin-1.0.0-$os.$ws.$arch.tar.gz"

alias p2-director="$(pwd)/p2-admin/p2-admin -application org.eclipse.equinox.p2.director"

target_env=$os
if [[ $ws != $os ]]; then
	target_env="$target_env-$ws"
fi
if [[ $arch != "x86" ]]; then
	target_env="$target_env-$arch"
fi

if [[ "$simrel" == "luna"* ]]; then
	simrel_zip_name="eclipse-SDK-4.4-$os-$ws-$arch.tar.gz"
	simrel_zip_url="http://download.eclipse.org/eclipse/downloads/drops4/R-4.4-201406061215/$simrel_zip_name"
	p2_repositories="http://download.eclipse.org/releases/luna/,\
http://download.eclipse.org/modeling/emf/compare/updates/nightly/latest/,\
http://download.eclipse.org/egit/updates-nightly"
	p2_installIUs="org.eclipse.emf.compare.ide.ui.feature.group,\
org.eclipse.egit.feature.group,\
org.eclipse.emf.compare.uml2.feature.group,\
org.eclipse.emf.compare.diagram.gmf.feature.group,\
org.eclipse.emf.compare.diagram.papyrus.feature.group"
elif [[ "$simrel" == "kepler"* ]]; then
	simrel_zip_name="eclipse-SDK-4.3.2-$os-$ws-$arch.tar.gz"
	simrel_zip_url="http://archive.eclipse.org/eclipse/downloads/drops4/R-4.3.2-201402211700/$simrel_zip_name"	
	p2_repositories="http://download.eclipse.org/releases/kepler/,\
http://download.eclipse.org/modeling/emf/compare/updates/nightly/latest/,\
http://download.eclipse.org/egit/updates-nightly"
	p2_installIUs="org.eclipse.emf.compare.ide.ui.feature.group,\
org.eclipse.egit.feature.group"
else
	echo "Unknown 'simrel'=$simrel."
	exit -1
fi

if [[ ! -f "$simrel_zip_url" ]]; then 
	wget --no-check-certificate -q "$simrel_zip_url"
fi

if [[ -d "eclipse" ]]; then
  rm -rf "eclipse"
fi

tar zxf "$simrel_zip_name"
p2-director -repository "$p2_repositories" -installIU "$p2_installIUs" -tag Q7_AUT -destination "$(pwd)/eclipse" -profile SDKProfile
zip -qr "AUT-$os.$ws.$arch.zip" eclipse
