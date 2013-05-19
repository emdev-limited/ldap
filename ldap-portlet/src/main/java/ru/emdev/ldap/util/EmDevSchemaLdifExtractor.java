/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package ru.emdev.ldap.util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Stack;
import java.util.UUID;

import org.apache.directory.shared.i18n.I18n;
import org.apache.directory.shared.ldap.constants.SchemaConstants;
import org.apache.directory.shared.ldap.exception.LdapException;
import org.apache.directory.shared.ldap.ldif.LdapLdifException;
import org.apache.directory.shared.ldap.ldif.LdifEntry;
import org.apache.directory.shared.ldap.ldif.LdifReader;
import org.apache.directory.shared.ldap.schema.ldif.extractor.SchemaLdifExtractor;
import org.apache.directory.shared.ldap.schema.ldif.extractor.UniqueResourceException;
import org.apache.directory.shared.ldap.schema.ldif.extractor.impl.DefaultSchemaLdifExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;


/**
 * Extracts LDIF files for the schema repository onto a destination directory.
 *
 * Problem is - DefaultSchemaLdifExtractor (http://svn.apache.org/repos/asf/directory/shared/branches/ldap-schema-index/src/main/java/org/apache/directory/shared/ldap/schema/ldif/extractor/impl/DefaultSchemaLdifExtractor.java)
 * is not working under Tomcat (or any other webapp).
 * Problem explained here: http://www.mail-archive.com/users@directory.apache.org/msg03211.html
 * Related Issue store here: https://issues.apache.org/jira/browse/DIRSERVER-1504
 * 
 * So, I've changed way, how ldif files found for extracting - but using Spring's PathMatchingResourcePatternResolver
 * 
 */
public class EmDevSchemaLdifExtractor implements SchemaLdifExtractor
{

    /** The base path. */
    private static final String BASE_PATH = "";

    /** The schema sub-directory. */
    private static final String SCHEMA_SUBDIR = "schema";

    /** The logger. */
    private static final Logger LOG = LoggerFactory.getLogger( EmDevSchemaLdifExtractor.class );

    /** The extracted flag. */
    private boolean extracted;

    /** The output directory. */
    private File outputDirectory;

    /** The schema directory. */
    private File schemaDirectory;


    /**
     * Creates an extractor which deposits files into the specified output
     * directory.
     *
     * @param outputDirectory the directory where the schema root is extracted
     */
    public EmDevSchemaLdifExtractor( File outputDirectory )
    {
        LOG.debug( "BASE_PATH set to {}, outputDirectory set to {}", BASE_PATH, outputDirectory );
        this.outputDirectory = outputDirectory;
        this.schemaDirectory = new File( outputDirectory, SCHEMA_SUBDIR );

        if ( !outputDirectory.exists() )
        {
            LOG.debug( "Creating output directory: {}", outputDirectory );
            if ( !outputDirectory.mkdir() )
            {
                LOG.error( "Failed to create outputDirectory: {}", outputDirectory );
            }
        }
        else
        {
            LOG.debug( "Output directory exists: no need to create." );
        }

        if ( !schemaDirectory.exists() )
        {
            LOG.info( "Schema directory '{}' does NOT exist: extracted state set to false.", schemaDirectory );
            extracted = false;
        }
        else
        {
            LOG.info( "Schema directory '{}' does exist: extracted state set to true.", schemaDirectory );
            extracted = true;
        }
    }


    /**
     * Gets whether or not schema folder has been created or not.
     *
     * @return true if schema folder has already been extracted.
     */
    public boolean isExtracted()
    {
        return extracted;
    }


    /**
     * Extracts the LDIF files from a Jar file or copies exploded LDIF resources.
     *
     * @param overwrite over write extracted structure if true, false otherwise
     * @throws IOException if schema already extracted and on IO errors
     */
    public void extractOrCopy( boolean overwrite ) throws IOException
    {
        if ( !outputDirectory.exists() && !outputDirectory.mkdirs() )
        {
            throw new IOException( "Directory Creation Failed: " +  outputDirectory.getAbsolutePath() );
        }

        File schemaDirectory = new File( outputDirectory, SCHEMA_SUBDIR );

        if ( !schemaDirectory.exists() )
        {
            if ( !schemaDirectory.mkdirs() )
            {
                throw new IOException( I18n.err( "Directory Creation Failed: " +  schemaDirectory.getAbsolutePath() ) );
            }
        }
        else if ( !overwrite )
        {
            throw new IOException( I18n.err( I18n.ERR_08001, schemaDirectory.getAbsolutePath() ) );
        }

        
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        for (Resource resource : resolver.getResources("classpath:/schema/**/*.ldif")) {
        	LOG.debug("Schema Found: " + resource.getURL().toString());
        	
        	String path = "schema/" + resource.getURL().toString().split("/schema/")[1];
        	LOG.info("Extracting from path: " + path);
        	
        	extractFromJar(path);
        }
    }


    /**
     * Extracts the LDIF files from a Jar file or copies exploded LDIF
     * resources without overwriting the resources if the schema has
     * already been extracted.
     *
     * @throws IOException if schema already extracted and on IO errors
     */
    public void extractOrCopy() throws IOException
    {
        extractOrCopy( false );
    }


    /**
     * Copies a file line by line from the source file argument to the 
     * destination file argument.
     *
     * @param source the source file to copy
     * @param destination the destination to copy the source to
     * @throws IOException if there are IO errors or the source does not exist
     */
    private void copyFile( File source, File destination ) throws IOException
    {
        LOG.debug( "copyFile(): source = {}, destination = {}", source, destination );

        if ( !destination.getParentFile().exists() && !destination.getParentFile().mkdirs() )
        {
            throw new IOException( I18n.err( "Directory Creation Failed: " +  destination.getParentFile().getAbsolutePath() ) );
        }

        if ( !source.getParentFile().exists() )
        {
            throw new FileNotFoundException( I18n.err( I18n.ERR_08002, source.getAbsolutePath() ) );
        }

        FileWriter out = new FileWriter( destination );

        try
        {
            LdifReader ldifReader = new LdifReader( source );
            boolean first = true;
            LdifEntry ldifEntry = null;

            while ( ldifReader.hasNext() )
            {
                if ( first )
                {
                    ldifEntry = ldifReader.next();

                    if ( ldifEntry.get( SchemaConstants.ENTRY_UUID_AT ) == null )
                    {
                        // No UUID, let's create one
                        UUID entryUuid = UUID.randomUUID();
                        ldifEntry.addAttribute( SchemaConstants.ENTRY_UUID_AT, entryUuid.toString() );
                    }

                    first = false;
                }
                else
                {
                    // throw an exception : we should not have more than one entry per schema ldif file
                    String msg = I18n.err( I18n.ERR_08003, source );
                    LOG.error( msg );
                    throw new InvalidObjectException( msg );
                }
            }

            ldifReader.close();

            // Add the version at the first line, to avoid a warning
            String ldifString = "version: 1\n" + ldifEntry.toString();

            out.write( ldifString );
            out.flush();
        }
        catch ( LdapLdifException ne )
        {
            // throw an exception : we should not have more than one entry per schema ldif file
            String msg = I18n.err( I18n.ERR_08004, source, ne.getLocalizedMessage() );
            LOG.error( msg );
            throw new InvalidObjectException( msg );
        }
        catch ( LdapException ne )
        {
            // throw an exception : we should not have more than one entry per schema ldif file
            String msg = I18n.err( I18n.ERR_08004, source, ne.getLocalizedMessage() );
            LOG.error( msg );
            throw new InvalidObjectException( msg );
        }
        finally
        {
            out.close();
        }
    }


    /**
     * Assembles the destination file by appending file components previously
     * pushed on the fileComponentStack argument.
     *
     * @param fileComponentStack stack containing pushed file components
     * @return the assembled destination file
     */
    private File assembleDestinationFile( Stack<String> fileComponentStack )
    {
        File destinationFile = outputDirectory.getAbsoluteFile();

        while ( !fileComponentStack.isEmpty() )
        {
            destinationFile = new File( destinationFile, fileComponentStack.pop() );
        }

        return destinationFile;
    }


    /**
     * Calculates the destination file.
     *
     * @param resource the source file
     * @return the destination file's parent directory
     */
    private File getDestinationFile( File resource )
    {
        File parent = resource.getParentFile();
        Stack<String> fileComponentStack = new Stack<String>();
        fileComponentStack.push( resource.getName() );

        while ( parent != null )
        {
            if ( parent.getName().equals( "schema" ) )
            {
                // All LDIF files besides the schema.ldif are under the 
                // schema/schema base path. So we need to add one more 
                // schema component to all LDIF files minus this schema.ldif
                fileComponentStack.push( "schema" );

                return assembleDestinationFile( fileComponentStack );
            }

            fileComponentStack.push( parent.getName() );

            if ( parent.equals( parent.getParentFile() ) || parent.getParentFile() == null )
            {
                throw new IllegalStateException( I18n.err( I18n.ERR_08005 ) );
            }

            parent = parent.getParentFile();
        }

        /*

           this seems retarded so I replaced it for now with what is below it
           will not break from loop above unless parent == null so the if is
           never executed - just the else is executed every time

        if ( parent != null )
        {
            return assembleDestinationFile( fileComponentStack );
        }
        else
        {
            throw new IllegalStateException( "parent cannot be null" );
        }
        
        */

        throw new IllegalStateException( I18n.err( I18n.ERR_08006 ) );
    }


    /**
     * Gets the DBFILE resource from within a jar off the base path.  If another jar
     * with such a DBFILE resource exists then an error will result since the resource
     * is not unique across all the jars.
     *
     * @param resourceName the file name of the resource to load
     * @param resourceDescription human description of the resource
     * @return the InputStream to read the contents of the resource
     * @throws IOException if there are problems reading or finding a unique copy of the resource
     */
    public static InputStream getUniqueResourceAsStream( String resourceName, String resourceDescription )
        throws IOException
    {
        resourceName = BASE_PATH + resourceName;
        URL result = getUniqueResource( resourceName, resourceDescription );
        return result.openStream();
    }


    /**
     * Gets a unique resource from a Jar file.
     * 
     * @param resourceName the name of the resource
     * @param resourceDescription the description of the resource
     * @return the URL to the resource in the Jar file
     * @throws IOException if there is an IO error
     */
    public static URL getUniqueResource( String resourceName, String resourceDescription ) throws IOException
    {
        Enumeration<URL> resources = DefaultSchemaLdifExtractor.class.getClassLoader().getResources( resourceName );
        if ( !resources.hasMoreElements() )
        {
            throw new UniqueResourceException( resourceName, resourceDescription );
        }
        URL result = resources.nextElement();
        if ( resources.hasMoreElements() )
        {
            throw new UniqueResourceException( resourceName, result, resources, resourceDescription );
        }
        return result;
    }


    /**
     * Extracts the LDIF schema resource from a Jar.
     *
     * @param resource the LDIF schema resource
     * @throws IOException if there are IO errors
     */
    private void extractFromJar( String resource ) throws IOException
    {
        byte[] buf = new byte[512];
        InputStream in = DefaultSchemaLdifExtractor.getUniqueResourceAsStream( resource,
            "LDIF file in schema repository" );

        try
        {
            File destination = new File( outputDirectory, resource );

            /*
             * Do not overwrite an LDIF file if it has already been extracted.
             */
            if ( destination.exists() )
            {
                return;
            }

            if ( !destination.getParentFile().exists() && !destination.getParentFile().mkdirs() )
            {
                throw new IOException( I18n.err( "Directory Creation Failed: " +  destination.getParentFile().getAbsolutePath() ) );
            }

            FileOutputStream out = new FileOutputStream( destination );
            try
            {
                while ( in.available() > 0 )
                {
                    int readCount = in.read( buf );
                    out.write( buf, 0, readCount );
                }
                out.flush();
            }
            finally
            {
                out.close();
            }
        }
        finally
        {
            in.close();
        }
    }
}
