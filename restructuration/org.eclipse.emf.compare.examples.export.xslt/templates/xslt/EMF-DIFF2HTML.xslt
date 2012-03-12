<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:xmi="http://schema.omg.org/spec/XMI/2.1"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xmlns:diff="http://www.eclipse.org/emf/compare/diff/1.1"
		xmlns:match="http://www.eclipse.org/emf/compare/match/1.1"
		xmlns:ecore="http://www.eclipse.org/emf/2002/Ecore"
		xmlns:notation="http://www.ibm.com/xtools/1.5.0/Notation"
		xmlns:uml="http://www.eclipse.org/uml2/2.1.0/UML"
		xmlns:umlnotation="http://www.ibm.com/xtools/1.5.0/Umlnotation">
	<xsl:output method="html" version="1.0" encoding="utf-8" indent="yes" />

	<xsl:template match="/">
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template name="substring-after-last">
		<xsl:param name="string" />
		<xsl:param name="delimiter" />
		
		<xsl:choose>
			<xsl:when test="contains($string, $delimiter)">
				<xsl:call-template name="substring-after-last">
					<xsl:with-param name="string" select="substring-after($string, $delimiter)" />
					<xsl:with-param name="delimiter" select="$delimiter" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$string" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="elementName">
		<xsl:param name="URI" />
		
		<xsl:variable name="name">
			<xsl:call-template name="substring-after-last">
				<xsl:with-param name="string" select="substring-after($URI, '#')" />
				<xsl:with-param name="delimiter" select="'/'" />
			</xsl:call-template>
		</xsl:variable>
		
		<xsl:choose>
			<xsl:when test="string-length($name)&gt;0">
				<xsl:value-of select="$name" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="'/'" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="subDiffCount">
		<xsl:param name="diffGroup" />
		
		<xsl:value-of select="count($diffGroup//descendant::subDiffElements[not(@xsi:type='diff:DiffGroup')])" />
	</xsl:template>
	
	<xsl:template name="print-td">
		<xsl:param name="count" />
		
		<xsl:if test="$count">
			<td class="filler_td">
			</td>
			<xsl:call-template name="print-td">
				<xsl:with-param name="count" select="$count -1" />
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="AddedText">
		<xsl:element name="span">
			<xsl:attribute name="class">
						<xsl:value-of select="'added'" />	
						</xsl:attribute>
			<xsl:value-of select="'added'" />
		</xsl:element>
	</xsl:template>

	<xsl:template name="RemovedText">
		<xsl:element name="span">
			<xsl:attribute name="class">
						<xsl:value-of select="'removed'" />	
						</xsl:attribute>
			<xsl:value-of select="'removed'" />
		</xsl:element>
	</xsl:template>

	<xsl:template name="ChangedText">
		<xsl:element name="span">
			<xsl:attribute name="class">
						<xsl:value-of select="'changed'" />	
						</xsl:attribute>
			<xsl:value-of select="'changed'" />
		</xsl:element>
	</xsl:template>

	<xsl:template name="MovedText">
		<xsl:element name="span">
			<xsl:attribute name="class">
						<xsl:value-of select="'moved'" />	
						</xsl:attribute>
			<xsl:value-of select="'moved'" />
		</xsl:element>
	</xsl:template>

	<xsl:template match="diff:ModelInputSnapshot">
		<xsl:variable name="date">
			<xsl:value-of select="@date" />
		</xsl:variable>

		<xsl:variable name="leftModel">
			<xsl:value-of select="match/@leftModel" />
		</xsl:variable>
		
		<xsl:variable name="rightModel">
			<xsl:value-of select="match/@rightModel" />
		</xsl:variable>

		<xsl:variable name="leftModelName">
			<xsl:call-template name="substring-after-last">
				<xsl:with-param name="string" select="$leftModel" />
				<xsl:with-param name="delimiter" select="'/'" />
			</xsl:call-template>
		</xsl:variable>
		
		<xsl:variable name="rightModelName">
			<xsl:call-template name="substring-after-last">
				<xsl:with-param name="string" select="$rightModel" />
				<xsl:with-param name="delimiter" select="'/'" />
			</xsl:call-template>
		</xsl:variable>

		<xsl:call-template name="processDiff">
			<xsl:with-param name="differences" select="diff/ownedElements" />
			<xsl:with-param name="date" select="$date" />
			<xsl:with-param name="leftModel" select="$leftModelName" />
			<xsl:with-param name="rightModel" select="$rightModelName" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="processDiff">
		<xsl:param name="differences" />
		<xsl:param name="date" />
		<xsl:param name="leftModel" />
		<xsl:param name="rightModel" />

		<xsl:variable name="modelName">
			<xsl:call-template name="elementName">
				<xsl:with-param name="URI" select="$differences/subDiffElements[1]/leftParent/@href" />
			</xsl:call-template>
		</xsl:variable>
		
		<xsl:variable name="modelExtension">
			<xsl:call-template name="substring-after-last">
				<xsl:with-param name="string" select="$leftModel" />
				<xsl:with-param name="delimiter" select="'.'" />
			</xsl:call-template>
		</xsl:variable>

		<html xmlns="http://www.w3.org/1999/xhtml">
			<head>
				<meta content="text/html; charset=UTF-8"
					http-equiv="content-type" />
				<title>View of EMF-DIFF</title>
				<style type="text/css">
					<xsl:comment>
						<![CDATA[
	.docTitle{font-size:18.0pt;font-family:Arial;color:darkblue;font-weight: bold;text-align:center;}
	.docSubTitle{font-size:14.0pt;font-family:Arial;color:darkblue;font-weight: bold;text-align:center;}
	.docSubSubTitle{font-size:12.0pt;color:darkblue;font-weight: bold;text-align:left;}
	.docBoldBlue{color:darkblue;font-weight: bold;text-align:left;}
	.docBlue{color:darkblue;text-align:left;}
	.divCenter{text-align:center;}	
	.nameResult{font-weight: bold;}

	h1 {font-size: 24px;line-height: normal;font-weight: bold;background-color: #f0f0f0;color: #003366;border-bottom: 1px solid #3c78b5;padding: 2px;margin: 36px 0px 4px 0px;} 
	td.tableheader{text-align: left;color:darkblue;font-weight: normal;background:#f0f0f0;padding:0cm 5.4pt 0cm 5.4pt;font-size:12.0pt;}
	td.W10{width: 10%;text-align:left;font-size:12.0pt;} 
	td.W20{width: 20%;text-align:left;font-size:12.0pt;}
	td.W80{width: 80%;text-align:left;font-size:12.0pt;}

	.filler_td{background-color:lightyellow}
	body{font-family:Arial;font-size:12.0pt;}

	.StdDiv{font-size:12.0pt;}
	.Model{color:darkblue;font-weight: bold}

	table {border-collapse:	collapse;}

	.changed{color:blue}
	.added{color:red}
	.removed{color:purple}
	.moved{color:darkgreen}
						]]>
</xsl:comment>
				</style>
			</head>
			<body>
				<div class="divCenter">
					<span class="docTitle"><xsl:value-of select="$modelExtension" /> Model Comparison</span>
				</div>
				<h1>
					<span class="docSubTitle">Model Information</span>
				</h1>
				<table style="text-align: left; width: 100%;" border="0" cellpadding="2" cellspacing="2">
					<tbody>
						<tr>
							<td class="docBoldBlue">Model :</td>
							<td class="Model">
								<xsl:value-of select="$modelName" />
							</td>
						</tr>
						<tr>
							<td class="docBoldBlue">Compared on :</td>
							<td>
								<xsl:value-of select="substring-before($date, 'T')" />
							</td>
						</tr>
						<tr>
							<td class="docBoldBlue">Left hand Document :</td>
							<td>
								<xsl:value-of select="$leftModel" />
							</td>
						</tr>
						<tr>
							<td class="docBoldBlue">Right hand Document :</td>
							<td>
								<xsl:value-of select="$rightModel" />
							</td>
						</tr>
					</tbody>
				</table>
				<h1>
					<span class="docSubTitle">Comparison</span>
				</h1>
				<table style="text-align: left; width: 100%;" border="0" cellpadding="2" cellspacing="2">
					<tbody>
						<tr>
							<th>
								<xsl:value-of select="$modelName" />
							</th>
						</tr>
						
						<xsl:for-each select="$differences/subDiffElements[@xsi:type != 'diff:DiffGroup']">
							<tr>
								<xsl:call-template name="subdiff">
									<xsl:with-param name="diff" select="." />
									<xsl:with-param name="counter" select="'0'" />
								</xsl:call-template>
							</tr>
						</xsl:for-each>

						<xsl:for-each select="$differences/subDiffElements[@xsi:type = 'diff:DiffGroup']">
							<xsl:call-template name="subdiff">
								<xsl:with-param name="diff" select="." />
								<xsl:with-param name="counter" select="'1'" />
							</xsl:call-template>
						</xsl:for-each>
					</tbody>
				</table>
			</body>
		</html>
	</xsl:template>

	<xsl:template name="subdiff">
		<xsl:param name="diff" />
		<xsl:param name="counter" />
		
		<xsl:variable name="diffType">
			<xsl:value-of select="$diff/@xsi:type" />
		</xsl:variable>
		
		<xsl:variable name="subDiffCount">
			<xsl:value-of select="$counter +1" />
		</xsl:variable>

		<xsl:choose>
			<!-- DiffGroup -->
			<xsl:when test="$diffType='diff:DiffGroup'">
				<tr>
					<xsl:if test="$counter &gt; 0">
						<xsl:call-template name="print-td">
							<xsl:with-param name="count" select="$counter" />
						</xsl:call-template>
					</xsl:if>
					<td class="DiffGroup">
						<xsl:call-template name="subDiffCount">
							<xsl:with-param name="diffGroup" select="$diff" />
						</xsl:call-template>
						<xsl:value-of select="' change(s) in : '" />
						<xsl:call-template name="elementName">
							<xsl:with-param name="URI" select="$diff/leftParent/@href" />
						</xsl:call-template>
					</td>
				</tr>
				<xsl:for-each select="$diff/subDiffElements">
					<xsl:call-template name="subdiff">
						<xsl:with-param name="diff" select="." />
						<xsl:with-param name="counter" select="$subDiffCount" />
					</xsl:call-template>
				</xsl:for-each>
			</xsl:when>
			
			<!-- Remove Model Element -->
			<xsl:when test="$diffType='diff:RemoveModelElement'">
				<tr>
					<xsl:if test="$counter &gt; 0">
						<xsl:call-template name="print-td">
							<xsl:with-param name="count" select="$counter" />
						</xsl:call-template>
					</xsl:if>
					<td class="RemoveModelElement">
						<xsl:text>Model Element </xsl:text>
						<xsl:call-template name="elementName">
							<xsl:with-param name="URI" select="$diff/leftElement/@href" />
						</xsl:call-template>
						<xsl:text> has been </xsl:text>
						<xsl:call-template name="RemovedText" />
					</td>
				</tr>
			</xsl:when>
			
			<!-- Add Model Element -->
			<xsl:when test="$diffType='diff:AddModelElement'">
				<tr>
					<xsl:if test="$counter &gt; 0">
						<xsl:call-template name="print-td">
							<xsl:with-param name="count" select="$counter" />
						</xsl:call-template>
					</xsl:if>
					<td class="AddModelElement">
						<xsl:text>Model Element </xsl:text>
						<xsl:call-template name="elementName">
							<xsl:with-param name="URI" select="$diff/rightElement/@href" />
						</xsl:call-template>
						<xsl:text> has been </xsl:text>
						<xsl:call-template name="AddedText" />
					</td>
				</tr>
			</xsl:when>
			
			<!-- Move Model Element -->
			<xsl:when test="$diffType='diff:MoveModelElement'">
				<tr>
					<xsl:if test="$counter &gt; 0">
						<xsl:call-template name="print-td">
							<xsl:with-param name="count" select="$counter" />
						</xsl:call-template>
					</xsl:if>
					<td class="MoveModelElement">
						<xsl:text>Model Element </xsl:text>
						<xsl:call-template name="elementName">
							<xsl:with-param name="URI" select="$diff/leftParent/@href" />
						</xsl:call-template>
						<xsl:text> has been </xsl:text>
						<xsl:call-template name="MovedText" />
					</td>
				</tr>
			</xsl:when>
			
			<!-- Update Attribute -->
			<xsl:when test="$diffType='diff:UpdateAttribute'">
				<tr>
					<xsl:if test="$counter &gt; 0">
						<xsl:call-template name="print-td">
							<xsl:with-param name="count" select="$counter" />
						</xsl:call-template>
					</xsl:if>
					<td class="UpdateAttribute">
						<xsl:text>Attribute </xsl:text>
						<xsl:call-template name="elementName">
							<xsl:with-param name="URI" select="$diff/attribute/@href" />
						</xsl:call-template>
						<xsl:text> has been </xsl:text>
						<xsl:call-template name="ChangedText" />
					</td>
				</tr>
			</xsl:when>
			
			<!-- Add Reference Value -->
			<xsl:when test="$diffType='diff:AddReferenceValue'">
				<tr>
					<xsl:if test="$counter &gt; 0">
						<xsl:call-template name="print-td">
							<xsl:with-param name="count"
								select="$counter" />
						</xsl:call-template>
					</xsl:if>
					<td class="AddReferenceValue">
						<xsl:call-template name="elementName">
							<xsl:with-param name="URI" select="$diff/rightAddedTarget/@href" />
						</xsl:call-template>
						<xsl:text> has been </xsl:text>
						<xsl:call-template name="AddedText" />
						<xsl:text> to reference </xsl:text>
						<xsl:call-template name="elementName">
							<xsl:with-param name="URI" select="$diff/reference/@href" />
						</xsl:call-template>
					</td>
				</tr>
			</xsl:when>
			
			<!-- Remove Reference Value -->
			<xsl:when test="$diffType='diff:RemoveReferenceValue'">
				<tr>
					<xsl:if test="$counter &gt; 0">
						<xsl:call-template name="print-td">
							<xsl:with-param name="count" select="$counter" />
						</xsl:call-template>
					</xsl:if>
					<td class="RemoveReferenceValue">
						<xsl:call-template name="elementName">
							<xsl:with-param name="URI" select="$diff/leftRemovedTarget/@href" />
						</xsl:call-template>
						<xsl:text> has been </xsl:text>
						<xsl:call-template name="RemovedText" />
						<xsl:text> to reference </xsl:text>
						<xsl:call-template name="elementName">
							<xsl:with-param name="URI" select="$diff/reference/@href" />
						</xsl:call-template>
					</td>
				</tr>
			</xsl:when>
			
			<!-- Update Unique Reference Value -->
			<xsl:when test="$diffType='diff:UpdateUniqueReferenceValue'">
				<tr>
					<xsl:if test="$counter &gt; 0">
						<xsl:call-template name="print-td">
							<xsl:with-param name="count" select="$counter" />
						</xsl:call-template>
					</xsl:if>
					<td class="UpdateUniqueReferenceValue">
						<xsl:text>Reference </xsl:text>
						<xsl:call-template name="elementName">
							<xsl:with-param name="URI" select="$diff/reference/@href" />
						</xsl:call-template>
						<xsl:text> has been </xsl:text>
						<xsl:call-template name="ChangedText" />
					</td>
				</tr>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
