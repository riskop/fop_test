<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
      xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
      xmlns:fo="http://www.w3.org/1999/XSL/Format"
      xmlns:lgcy="urn:tarhely.gov.hu:1.0:legacy"
      >
  <xsl:output method="xml" indent="yes"/>
  
  <xsl:template match="header1">
            <fo:block font-size="24pt">
                <xsl:apply-templates select="node()"/>
          </fo:block>
  </xsl:template>
  
  <xsl:template match="para">
            <fo:block font-size="12pt">
                <xsl:apply-templates select="node()"/>
          </fo:block>
  </xsl:template>

  <xsl:template match="/">
  <!-- 
    <fo:root>
     -->
    <fo:root font-family="FreeSans">
    
      <fo:layout-master-set>
        <fo:simple-page-master master-name="A4-portrait"
              page-height="29.7cm" page-width="21.0cm" margin="2cm">
          <fo:region-body/>
        </fo:simple-page-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="A4-portrait">
        <fo:flow flow-name="xsl-region-body">
        
          <xsl:copy>
            <xsl:apply-templates select="node()"/>
          </xsl:copy>
          
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>
</xsl:stylesheet>