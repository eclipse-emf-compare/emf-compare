export P2_ADMIN_PATH="/jobs/genie.modeling.emfcompare/p2-admin/lastStable/archive/org.eclipselabs.equinox.p2.admin.product/target/products/org.eclipse.equinox.p2.admin.rcp.product/linux/gtk/x86_64/p2-admin"

alias p2-admin="${JAVA_HOME}/bin/java -jar $P2_ADMIN_PATH/plugins/org.eclipse.equinox.launcher_*.jar"
alias p2-director="p2-admin -application org.eclipse.equinox.p2.director"

simrel=$1

if [[ "$simrel" == "luna"* ]]; then
	simrel_zip_name="eclipse-SDK-4.4RC3-linux-gtk-x86_64.tar.gz"
	simrel_zip_url="http://download.eclipse.org/eclipse/downloads/drops4/S-4.4RC3-201405282000/$simrel_zip_name"
	p2_repositories="http://download.eclipse.org/releases/staging/,http://download.eclipse.org/modeling/emf/compare/updates/nightly/latest,http://download.eclipse.org/egit/updates-nightly"
	p2_installIUs="org.eclipse.emf.compare.ide.ui.feature.group,org.eclipse.egit.feature.group"
elif [[ "$simrel" == "kepler" ]]; then
	simrel_zip_name="eclipse-SDK-4.3.2-linux-gtk-x86_64.tar.gz"
	simrel_zip_url="http://download.eclipse.org/eclipse/downloads/drops4/R-4.3.2-201402211700/$simrel_zip_name"	
	p2_repositories="http://download.eclipse.org/releases/staging/,http://download.eclipse.org/modeling/emf/compare/updates/nightly/latest,http://download.eclipse.org/egit/updates-nightly"
	p2_installIUs="org.eclipse.emf.compare.ide.ui.feature.group,org.eclipse.egit.feature.group,org.eclipse.emf.compare.uml2.feature.group,org.eclipse.emf.compare.diagram.gmf.feature.group,org.eclipse.emf.compare.diagram.papyrus.feature.group"
else
	echo "Unknown 'simrel'=$simrel."
	exit -1
fi

if [[ -d "eclipse" ]]; then
  rm -rf "$eclipse"
fi

if [ -f "$simrel_zip_name" ]; then
  rm -f "$simrel_zip_name"
fi

wget -q "$simrel_zip_url"
tar zxf "$simrel_zip_name"
p2-director -repository "$p2_repositories" -installIU "$p2_installIUs" -tag Q7_AUT -destination "./eclipse" -profile SDKProfile
zip -qr AUT.zip eclipse
