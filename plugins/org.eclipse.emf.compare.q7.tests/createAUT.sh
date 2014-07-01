#!/bin/bash
os=$1
ws=$2
arch=$3
simrel=$4

env > env.txt

P2_ADMIN_VERSION="1.0.2"
P2_ADMIN_ZIPNAME="p2-admin-$P2_ADMIN_VERSION-$os.$ws.$arch.tar.gz"
P2_ADMIN_URL="https://github.com/mbarbero/p2-admin/releases/download/v$P2_ADMIN_VERSION/$P2_ADMIN_ZIPNAME"

if [[ ! -f "$P2_ADMIN_ZIPNAME" ]]; then 
	echo "Downloading $P2_ADMIN_URL"
	wget --no-check-certificate -q $P2_ADMIN_URL -O - > $P2_ADMIN_ZIPNAME
fi
if [[ -d "p2-admin" ]]; then
	echo "Removing old p2-admin folder"
	rm -rf "p2-admin"
fi
echo "Unzipping $P2_ADMIN_ZIPNAME"
tar zxf "$P2_ADMIN_ZIPNAME"

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

if [[ ! -f "$simrel_zip_name" ]]; then 
	echo "Downloading $simrel_zip_url"
	wget --no-check-certificate -q "$simrel_zip_url"
fi

if [[ -d "eclipse" ]]; then
  echo "Removing old eclipse folder"
  rm -rf "eclipse"
fi

echo "Unzipping $simrel_zip_name"
tar zxf "$simrel_zip_name"

echo "Provisioning AUT"
echo "  Repositories: $p2_repositories"
echo "  IUs: $p2_installIUs"
$(pwd)/p2-admin/p2-admin -vm $JAVA_HOME/bin/java -application org.eclipse.equinox.p2.director -repository "$p2_repositories" -installIU "$p2_installIUs" -tag Q7_AUT -destination "$(pwd)/eclipse" -profile SDKProfile

echo "Zipping AUT AUT-$os.$ws.$arch.zip"
zip -qr "AUT-$os.$ws.$arch.zip" eclipse
