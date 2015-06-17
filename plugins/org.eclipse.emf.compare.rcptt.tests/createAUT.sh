#!/bin/bash
simrel=$1
workdir=$2

mkdir -p $workdir

env > ${workdir}/env.txt

PLATFORM_SHORT_SPECIFIER=""
if [[ "${OSTYPE}" == "linux"* || "${OSTYPE}" == "freebsd"* ]]; then
	OSWS="linux.gtk"
	PLATFORM_SHORT_SPECIFIER="linux-gtk"
	FILE_EXT="tar.gz"
elif [[ "${OSTYPE}" == "cygwin"* ]]; then
	OSWS="win32.win32"
	PLATFORM_SHORT_SPECIFIER="win32"
	FILE_EXT="zip"
elif [[ "${OSTYPE}" == "darwin"* ]]; then
	OSWS="macosx.cocoa"
	PLATFORM_SHORT_SPECIFIER="macosx-cocoa"
	FILE_EXT="tar.gz"
else
	echo "Unknown 'OSTYPE'=${OSTYPE}."
	exit -1
fi

if [[ $(uname -m) == *"64"* ]]; then
	ARCH="x86_64"
	PLATFORM_SHORT_SPECIFIER="${PLATFORM_SHORT_SPECIFIER}-${ARCH}"
else
	ARCH="x86"
fi

PLATFORM_SPECIFIER="${OSWS}.${ARCH}"

P2_ADMIN_VERSION="1.1.0"
P2_ADMIN_ZIPNAME="p2-admin-${P2_ADMIN_VERSION}-${PLATFORM_SPECIFIER}.${FILE_EXT}"
P2_ADMIN_URL="https://github.com/mbarbero/p2-admin/releases/download/v${P2_ADMIN_VERSION}/${P2_ADMIN_ZIPNAME}"
P2_ADMIN_ZIPPATH=$workdir/$P2_ADMIN_ZIPNAME
P2_ADMIN_PATH=$workdir/p2-admin

if [[ ! -f "$P2_ADMIN_ZIPPATH" ]]; then 
	echo "Downloading $P2_ADMIN_URL"
	wget --no-check-certificate -q $P2_ADMIN_URL -O - > $P2_ADMIN_ZIPPATH
fi

if [[ -d $P2_ADMIN_PATH ]]; then
	echo "Removing old p2-admin folder"
	rm -rf "p2-admin"
fi
echo "Unzipping $P2_ADMIN_ZIPNAME"
if [[ "$FILE_EXT" == "zip" ]]; then
	unzip -q "$P2_ADMIN_ZIPPATH" -d $workdir
else
	tar zxf "$P2_ADMIN_ZIPPATH" -C $workdir
fi

if [[ "$simrel" == "collaborative-modeling-luna"* ]]; then
	simrel_zip_name="Collaborative-Modeling-Luna-${PLATFORM_SHORT_SPECIFIER}.${FILE_EXT}"
	simrel_zip_url="http://download.eclipse.org/modeling/emf/compare/collaborative-modeling-luna-package/nightly/$simrel_zip_name"	
	p2_repositories=""
	p2_installIUs=""
elif [[ "$simrel" == "collaborative-modeling"* ]]; then
	simrel_zip_name="Collaborative-Modeling-${PLATFORM_SHORT_SPECIFIER}.${FILE_EXT}"
	simrel_zip_url="http://download.eclipse.org/modeling/emf/compare/collaborative-modeling-package/nightly/$simrel_zip_name"	
	p2_repositories=""
	p2_installIUs=""
elif [[ "$simrel" == "mars"* ]]; then
	simrel_zip_name="eclipse-SDK-4.5RC4-${PLATFORM_SHORT_SPECIFIER}.${FILE_EXT}"
	simrel_zip_url="http://download.eclipse.org/eclipse/downloads/drops4/S-4.5RC4-201506032000/$simrel_zip_name"	
	p2_repositories="http://download.eclipse.org/releases/mars/,\
http://download.eclipse.org/modeling/emf/compare/updates/nightly/latest/,\
http://download.eclipse.org/modeling/emf/compare/updates/egit-logical/nightly/"
	p2_installIUs="org.eclipse.emf.compare.ide.ui.feature.group,\
org.eclipse.egit.feature.group,\
org.eclipse.emf.sdk.feature.group,\
org.eclipse.uml2.uml.feature.group,\
org.eclipse.papyrus.sdk.feature.feature.group,\
org.eclipse.emf.compare.uml2.feature.group,\
org.eclipse.emf.compare.diagram.gmf.feature.group,\
org.eclipse.emf.compare.diagram.papyrus.feature.group,\
org.eclipse.emf.compare.egit.feature.group"
elif [[ "$simrel" == "luna"* ]]; then
	simrel_zip_name="eclipse-SDK-4.4.2-${PLATFORM_SHORT_SPECIFIER}.${FILE_EXT}"
	simrel_zip_url="http://download.eclipse.org/eclipse/downloads/drops4/R-4.4.2-201502041700/$simrel_zip_name"
	p2_repositories="http://download.eclipse.org/releases/luna/,\
http://download.eclipse.org/modeling/emf/compare/updates/nightly/latest/,\
http://download.eclipse.org/modeling/emf/compare/updates/egit-logical/nightly/"
	p2_installIUs="org.eclipse.emf.compare.ide.ui.feature.group,\
org.eclipse.egit.feature.group,\
org.eclipse.emf.sdk.feature.group,\
org.eclipse.uml2.uml.feature.group,\
org.eclipse.papyrus.sdk.feature.feature.group,\
org.eclipse.emf.compare.uml2.feature.group,\
org.eclipse.emf.compare.diagram.gmf.feature.group,\
org.eclipse.emf.compare.diagram.papyrus.feature.group,\
org.eclipse.emf.compare.egit.feature.group"
elif [[ "$simrel" == "kepler"* ]]; then
	simrel_zip_name="eclipse-SDK-4.3.2-${PLATFORM_SHORT_SPECIFIER}.${FILE_EXT}"
	simrel_zip_url="http://archive.eclipse.org/eclipse/downloads/drops4/R-4.3.2-201402211700/$simrel_zip_name"	
	p2_repositories="http://download.eclipse.org/releases/kepler/,\
http://download.eclipse.org/releases/luna/,\
http://download.eclipse.org/modeling/emf/compare/updates/nightly/latest/,\
http://download.eclipse.org/modeling/emf/compare/updates/egit-logical/nightly/"
	p2_installIUs="org.eclipse.emf.compare.ide.ui.feature.group,\
org.eclipse.egit.feature.group,\
org.eclipse.emf.sdk.feature.group,\
org.eclipse.uml2.uml.feature.group,\
org.eclipse.papyrus.uml.feature.feature.group,\
org.eclipse.emf.compare.uml2.feature.group,\
org.eclipse.emf.compare.diagram.gmf.feature.group,\
org.eclipse.emf.compare.diagram.papyrus.feature.group"
else
	echo "Unknown 'simrel'=$simrel."
	exit 1
fi


simrel_zip_path=$workdir/$simrel_zip_name
if [[ ! -f "$simrel_zip_path" ]]; then 
	echo "Downloading $simrel_zip_url"
	wget --no-check-certificate -q "$simrel_zip_url" -O - > $simrel_zip_path
fi

simrel_path=$workdir/$simrel
mkdir -p $simrel_path

if [[ -d "$simrel_path/Collaborative-Modeling-${PLATFORM_SHORT_SPECIFIER}" ]]; then
  echo "Removing old Collaborative-Modeling-${PLATFORM_SHORT_SPECIFIER} folder"
  rm -rf "$simrel_path/Collaborative-Modeling-${PLATFORM_SHORT_SPECIFIER}"
fi

if [[ -d "$simrel_path/Collaborative-Modeling-Luna-${PLATFORM_SHORT_SPECIFIER}" ]]; then
  echo "Removing old Collaborative-Modeling-Luna-${PLATFORM_SHORT_SPECIFIER} folder"
  rm -rf "$simrel_path/Collaborative-Modeling-Luna-${PLATFORM_SHORT_SPECIFIER}"
fi

if [[ -d "$simrel_path/eclipse" ]]; then
  echo "Removing old eclipse folder"
  rm -rf "$simrel_path/eclipse"
fi

echo "Unzipping $simrel_zip_name"
if [[ "$FILE_EXT" == "zip" ]]; then
	unzip -q "$simrel_zip_path" -d $simrel_path
else
	tar zxf "$simrel_zip_path" -C $simrel_path
fi

if [[ -d "$simrel_path/Collaborative-Modeling-${PLATFORM_SHORT_SPECIFIER}" ]]; then
  echo "Renaming Collaborative-Modeling-${PLATFORM_SHORT_SPECIFIER} to eclipse"
  mv "$simrel_path/Collaborative-Modeling-${PLATFORM_SHORT_SPECIFIER}" "$simrel_path/eclipse"
fi

if [[ -d "$simrel_path/Collaborative-Modeling-Luna-${PLATFORM_SHORT_SPECIFIER}" ]]; then
  echo "Renaming Collaborative-Modeling-Luna-${PLATFORM_SHORT_SPECIFIER} to eclipse"
  mv "$simrel_path/Collaborative-Modeling-Luna-${PLATFORM_SHORT_SPECIFIER}" "$simrel_path/eclipse"
fi

echo "Provisioning AUT"
echo "  Repositories: $p2_repositories"
echo "  IUs: $p2_installIUs"
if [ -n "$p2_repositories" ]; then 
	$P2_ADMIN_PATH/p2-admin -vm $JAVA_HOME/bin/java -application org.eclipse.equinox.p2.director -repository "$p2_repositories" -installIU "$p2_installIUs" -tag RCPTT_AUT -destination "$simrel_path/eclipse" -profile SDKProfile
fi
