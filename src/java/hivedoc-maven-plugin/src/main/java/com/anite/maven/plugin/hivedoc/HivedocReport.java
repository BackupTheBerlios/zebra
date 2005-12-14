/*
 * Copyright 2004, 2005 Anite - Central Government Division
 *    http://www.anite.com/publicsector
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package com.anite.maven.plugin.hivedoc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Locale;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.hivemind.ModuleDescriptorProvider;
import org.apache.hivemind.ant.RegistrySerializer;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.impl.XmlModuleDescriptorProvider;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.codehaus.doxia.sink.Sink;
import org.w3c.dom.Document;

/**
 * @goal hivedoc
 * @description builds hivedoc for the current project
 * @author ben.gidley
 *
 */
public class HivedocReport extends AbstractMojo implements MavenReport {

    private static final String XSLT_FILE = "hivemind.xsl";

    /**
     * @readonly
     * @parameter expression="${project}"
     * @required
     */
    private org.apache.maven.project.MavenProject project;

    public void execute() throws MojoExecutionException, MojoFailureException {

        try {
            // Construct the registry and output the XML File 
            ModuleDescriptorProvider provider = new XmlModuleDescriptorProvider(new DefaultClassResolver(
                    getClassLoader()));

            RegistrySerializer serializer = new RegistrySerializer();
            serializer.addModuleDescriptorProvider(provider);

            Document result = serializer.createRegistryDocument();

            // Run the XSL over it to generate Hivedoc
            File outputFolder = new File(project.getReporting().getOutputDirectory() + "/hivedoc");
            outputFolder.mkdirs();
            File outputFile = new File(outputFolder, "index.html");
            Source registryDocument = new DOMSource(result);
            Source xsltFile = new StreamSource(this.getClass().getClassLoader().getResourceAsStream(XSLT_FILE));
            Result output = new StreamResult(new FileOutputStream(outputFile));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer trans = transformerFactory.newTransformer(xsltFile);
            trans.setParameter("base.dir", outputFolder.getAbsolutePath());

            trans.transform(registryDocument, output);

            // Copy the resources to the output folder
            copyResouceDir("private.png", outputFolder);
            copyResouceDir("public.png", outputFolder);
            copyResouceDir("hivemind.css", outputFolder);
            

        } catch (MalformedURLException e) {
            this.getLog().error(e);
            throw new MojoExecutionException("", e);
        } catch (DependencyResolutionRequiredException e) {
            this.getLog().error(e);
            throw new MojoExecutionException("", e);
        } catch (FileNotFoundException e) {
            this.getLog().error(e);
            throw new MojoExecutionException("", e);
        } catch (TransformerConfigurationException e) {
            this.getLog().error(e);
            throw new MojoExecutionException("", e);
        } catch (TransformerException e) {
            this.getLog().error(e);
            throw new MojoExecutionException("", e);
        } catch (ClassCastException e) {
            this.getLog().error(e);
            throw new MojoExecutionException("", e);
        } catch (IOException e) {
            this.getLog().error(e);
            throw new MojoExecutionException("", e);
        }
    }

    /**
     * Get a class loader with the runtime dependencies of the application + the compiled output
     * 
     * It is assumed (probably not unreasonably) that this includes Hivemind 1.1
     * 
     * @return
     * @throws MalformedURLException
     * @throws DependencyResolutionRequiredException
     */
    private URLClassLoader getClassLoader() throws MalformedURLException, DependencyResolutionRequiredException {
        String[] classpath = getClasspath();

        URL[] urls = new URL[classpath.length];

        for (int i = urls.length - 1; i >= 0; i--) {
            urls[i] = new URL("file:" + classpath[i]);

            this.getLog().debug("cp [" + urls[i] + "]");
        }

        return new URLClassLoader(urls);
    }

    /**
     * Resolve the runtime class path + what has been compiled
     * @return
     * @throws DependencyResolutionRequiredException
     */
    private String[] getClasspath() throws DependencyResolutionRequiredException {
        List paths = project.getRuntimeClasspathElements();
        paths.add(new File(project.getBuild().getOutputDirectory()).getAbsolutePath() + "/");

        return (String[]) paths.toArray(new String[0]);
    }

    private void copyResouceDir(String resourceName, File outputDirectory) throws IOException {

        InputStream source = this.getClass().getClassLoader().getResourceAsStream(resourceName);
        FileOutputStream output = new FileOutputStream(new File(outputDirectory, resourceName));
        
        copy (source, output);
        
        source.close();
        
    }

    /**
     * Copies an inputstream to an output stream
     * @param input
     * @param output
     * @throws IOException 
     * @throws Exception
     */
    private void copy(InputStream input, OutputStream output) throws IOException  {
        byte[] buffer = new byte[1024];

        int n;

        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        
        output.flush();
        output.close();
    }

    public void generate(Sink sink, Locale locale) throws MavenReportException {
       //noop - done in execute
        
    }

    public String getOutputName() {
        return "hivedoc/index";
    }

    public String getName(Locale locale) {
        return "Hivedoc";
    }

    public String getCategoryName() {
        return AbstractMavenReport.CATEGORY_PROJECT_REPORTS;
    }

    public String getDescription(Locale locale) {       
        return "Hivedoc";
    }

    public void setReportOutputDirectory(File outputDirectory) {
        //noop
        
    }

    public File getReportOutputDirectory() {
        //noop
        return null;
    }

    public boolean isExternalReport() {
        return true;
    }

    public boolean canGenerateReport() {
        return true;
    }

}
