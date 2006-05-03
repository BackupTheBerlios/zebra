package com.anite.maven.plugin.hivedoc;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.impl.XmlModuleDescriptorProvider;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.DocumentSource;

public class Dom4JRegistrySerializerTest extends TestCase {
	private static final Log log = LogFactory.getLog(Dom4JRegistrySerializerTest.class);
    private static final String XSLT_FILE = "hivemind.xsl";

	public void testSerializeCastries() throws Exception{
		Dom4JRegistrySerializer dom4JRegistrySerializer = new Dom4JRegistrySerializer();
		
		XmlModuleDescriptorProvider xmlModuleDescriptorProvider = new XmlModuleDescriptorProvider(new DefaultClassResolver(
                this.getClass().getClassLoader()), "castries/hivemodule.xml");

		dom4JRegistrySerializer.addModuleDescriptorProvider(xmlModuleDescriptorProvider);
		
		Document document = dom4JRegistrySerializer.createRegistryDocument();
		assertNotNull(document);
		
		log.debug(document.asXML());

		Element registry = document.getRootElement();
		assertEquals(12, registry.elements().size());
		
		 // Run the XSL over it to generate Hivedoc
        File outputFolder = new File("/tmp/hivedoc");
        outputFolder.mkdirs();
        File outputFile = new File(outputFolder, "index.html");
        DocumentSource registryDocument = new DocumentSource(document);
        Source xsltFile = new StreamSource(this.getClass().getClassLoader().getResourceAsStream(XSLT_FILE));
        Result output = new StreamResult(new FileOutputStream(outputFile));

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer trans = transformerFactory.newTransformer(xsltFile);
        trans.setParameter("base.dir", outputFolder.getAbsolutePath());

        trans.transform(registryDocument, output);
		
		
	}
	
	
	
}
