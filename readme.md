apache fop 2.2 demonstration
----------------------------

fop 2.2

fop usage from java standalone application

generate pdf from xml with the help of xslt

embed font into pdf

deal with latin2 characters

embed ttf in application (don't use File to load configuration or font, instead load everything from the classpath. Achieved via ConfigurationBuilder for the config file and org.apache.xmlgraphics.io.ResourceResolver for the ttf font) 