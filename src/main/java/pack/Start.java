package pack;

import java.io.File;
import java.io.OutputStream;
import java.net.URI;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.apps.MimeConstants;

public class Start {

    public static void main(String[] args) throws Exception {

        System.out.println("FOP ExampleXML2PDF");

        // Setup input and output files
        File xmlfile = new File("src/main/resources/article.xml");
        File xsltfile = new File("src/main/resources/article2fo.xsl");
        File pdffile = new File("article.pdf");
        System.out.println("Input: XML (" + xmlfile + ")");
        System.out.println("Stylesheet: " + xsltfile);
        System.out.println("Output: PDF (" + pdffile + ")");

        System.out.println("Transforming...");

        // NOTE: reuse fopFactory
        FopFactory fopFactory = getFopFactoryConfiguredFromClasspath("fop.xconf.xml");
//        FopFactory fopFactory = getFopFactoryConfiguredFromFileSystem("fop.xconf.xml");
        
        // with userAgent you can further configure some things:
        // https://xmlgraphics.apache.org/fop/2.2/embedding.html#user-agent
        //
        // FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        // configure foUserAgent as desired

        // Setup output
        OutputStream out = new java.io.FileOutputStream(pdffile);
        out = new java.io.BufferedOutputStream(out);

        try {
            // Construct fop with desired output format
//            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

            // Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));

            // Set the value of a <param> in the stylesheet
            transformer.setParameter("versionParam", "2.0");

            // Setup input for XSLT transformation
            Source src = new StreamSource(xmlfile);

            // Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            // Start XSLT transformation and FOP processing
            transformer.transform(src, res);
        } finally {
            out.close();
        }

        System.out.println("Success!");
    
    }
    
    /**
     * The fop configuration and referenced resources (ttf font) are loaded
     * from the file system
     * 
     */
    private static FopFactory getFopFactoryConfiguredFromFileSystem(String configFileName) throws Exception {
        FopFactory fopFactory = FopFactory.newInstance(new File("src/main/resources/" + configFileName));
        return fopFactory;
    }

    /**
     * The fop configuration and referenced resources (ttf font) are loaded
     * from the classpath
     * 
     */
    private static FopFactory getFopFactoryConfiguredFromClasspath(String configFileName) throws Exception {
        
        URI baseUri = URI.create("base_uri_is_meaningless_in_this_case");
        // System.out.println("uri: " + uri);
        
        // create a builder with a ResourceResolver which reads from the classpath. 
        // The resourceResolver will be called for loading the resources referenced 
        // in the configuration (for example a ttf font)
        FopFactoryBuilder builder = new FopFactoryBuilder(baseUri, new ClasspathResolverURIAdapter());

        // create a configuration which is based on a file read from the classpath
        DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
        Configuration cfg = cfgBuilder.build(Start.class.getResourceAsStream("/" + configFileName));

        // sets the configuration
        builder.setConfiguration(cfg);

        FopFactory fopFactory = builder.build();
        return fopFactory;
    }
}
