/**
 * Sonatype Nexus (TM) [Open Source Version].
 * Copyright (c) 2008 Sonatype, Inc. All rights reserved.
 * Includes the third-party code listed at ${thirdPartyUrl}.
 *
 * This program is licensed to you under Version 3 only of the GNU
 * General Public License as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License Version 3 for more details.
 *
 * You should have received a copy of the GNU General Public License
 * Version 3 along with this program. If not, see http://www.gnu.org/licenses/.
 */
package org.sonatype.nexus.test.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.codehaus.plexus.util.StringUtils;
import org.junit.Assert;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.sonatype.nexus.integrationtests.RequestFacade;
import org.sonatype.nexus.rest.model.NexusArtifact;
import org.sonatype.nexus.rest.model.RepositoryResource;
import org.sonatype.nexus.rest.model.RepositoryResourceResponse;
import org.sonatype.nexus.rest.model.SearchResponse;
import org.sonatype.plexus.rest.representation.XStreamRepresentation;

import com.thoughtworks.xstream.XStream;

public class SearchMessageUtil
{

    private Logger log = Logger.getLogger( getClass() );

    private static XStream xstream;

    static
    {
        xstream = XStreamFactory.getXmlXStream();
    }

    public Response doSearchFor( String query )
        throws Exception
    {
        String serviceURI = "service/local/data_index?q=" + query;

        return RequestFacade.doGetRequest( serviceURI );
    }

    public Response doSearchFor( Map<String, String> queryArgs )
        throws Exception
    {
        StringBuffer serviceURI = new StringBuffer( "service/local/data_index?" );

        for ( Entry<String, String> entry : queryArgs.entrySet() )
        {
            serviceURI.append( entry.getKey() ).append( "=" ).append( entry.getValue() ).append( "&" );
        }

        return RequestFacade.doGetRequest( serviceURI.toString() );
    }

    @SuppressWarnings( "unchecked" )
    public List<NexusArtifact> searchFor( String query )
        throws Exception
    {
        HashMap<String, String> queryArgs = new HashMap<String, String>();
        queryArgs.put( "q", query );
        return searchFor( queryArgs );
    }

    @SuppressWarnings( "unchecked" )
    public List<NexusArtifact> searchFor( Map<String, String> queryArgs )
        throws Exception
    {
        String responseText = doSearchFor( queryArgs ).getEntity().getText();

        XStreamRepresentation representation =
            new XStreamRepresentation( xstream, responseText, MediaType.APPLICATION_XML );

        SearchResponse searchResponde = (SearchResponse) representation.getPayload( new SearchResponse() );

        return searchResponde.getData();
    }

    public NexusArtifact searchForSHA1( String sha1 )
        throws Exception
    {
        String serviceURI = "service/local/identify/sha1/" + sha1;

        String responseText = RequestFacade.doGetRequest( serviceURI ).getEntity().getText();

        log.debug( "responseText: \n" + responseText );

        if ( StringUtils.isEmpty( responseText ) )
        {
            return null;
        }

        return (NexusArtifact) xstream.fromXML( responseText );
    }

    public void allowBrowsing( String repositoryName, boolean allowBrowsing )
        throws Exception
    {
        RepositoryResource repository = getRepository( repositoryName );

        repository.setBrowseable( allowBrowsing );

        saveRepository( repository, repositoryName );
    }

    public void allowSearch( String repositoryName, boolean allowSearch )
        throws Exception
    {
        RepositoryResource repository = getRepository( repositoryName );

        repository.setIndexable( allowSearch );

        saveRepository( repository, repositoryName );
    }

    private RepositoryResource getRepository( String repositoryName )
        throws IOException
    {
        String serviceURI = "service/local/repositories/" + repositoryName;
        String responseText = RequestFacade.doGetRequest( serviceURI ).getEntity().getText();

        RepositoryResourceResponse repository = (RepositoryResourceResponse) xstream.fromXML( responseText );
        return (RepositoryResource) repository.getData();
    }

    private void saveRepository( RepositoryResource repository, String repositoryName )
        throws IOException
    {
        String serviceURI = "service/local/repositories/" + repositoryName;

        RepositoryResourceResponse repositoryResponse = new RepositoryResourceResponse();
        XStreamRepresentation representation = new XStreamRepresentation( xstream, "", MediaType.APPLICATION_XML );
        repositoryResponse.setData( repository );
        representation.setPayload( repositoryResponse );

        Status status = RequestFacade.sendMessage( serviceURI, Method.PUT, representation ).getStatus();
        Assert.assertEquals( Status.SUCCESS_OK.getCode(), status.getCode() );

    }

    public void allowDeploying( String repositoryName, boolean allowDeploying )
        throws Exception
    {
        RepositoryResource repository = getRepository( repositoryName );

        repository.setAllowWrite( allowDeploying );

        saveRepository( repository, repositoryName );
    }

    @SuppressWarnings( "unchecked" )
    public static List<NexusArtifact> searchClassname( String classname )
        throws Exception
    {
        String responseText = doSearchForClassName( classname ).getEntity().getText();

        XStreamRepresentation representation =
            new XStreamRepresentation( xstream, responseText, MediaType.APPLICATION_XML );

        SearchResponse searchResponde = (SearchResponse) representation.getPayload( new SearchResponse() );

        return searchResponde.getData();
    }

    private static Response doSearchForClassName( String classname )
        throws IOException
    {
        String serviceURI = "service/local/data_index?cn=" + classname;

        return RequestFacade.doGetRequest( serviceURI );
    }

    public List<NexusArtifact> searchFor( String groupId, String artifactId, String version )
        throws Exception
    {
        Map<String, String> args = new HashMap<String, String>();
        args.put( "g", groupId );
        args.put( "a", artifactId );
        args.put( "v", version );

        return searchFor( args );
    }

}
