/*
 *  Copyright (C) 2010 John Casey.
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.apache.maven.scm.provider.git.gitexe.command.export;

import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.export.AbstractExportCommand;
import org.apache.maven.scm.command.export.ExportScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.git.command.GitCommand;
import org.apache.maven.scm.provider.git.gitexe.command.checkout.GitCheckOutCommand;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class GitExportCommand
    extends AbstractExportCommand
    implements GitCommand
{

    private final GitCheckOutCommand checkoutCommand = new GitCheckOutCommand();

    /**
     * {@inheritDoc}
     * @see org.apache.maven.scm.command.export.AbstractExportCommand#executeExportCommand(org.apache.maven.scm.provider.ScmProviderRepository, org.apache.maven.scm.ScmFileSet, org.apache.maven.scm.ScmVersion, java.lang.String)
     */
    protected ExportScmResult executeExportCommand( final ScmProviderRepository repository, final ScmFileSet fileSet,
                                                    final ScmVersion scmVersion, final String outputDirectory )
        throws ScmException
    {
        final CommandParameters parameters = new CommandParameters();
        parameters.setScmVersion( CommandParameter.SCM_VERSION, scmVersion );
        parameters.setString( CommandParameter.RECURSIVE, Boolean.TRUE.toString() );

        final ScmResult checkoutResult = checkoutCommand.execute( repository, fileSet, parameters );

        final StringBuffer providerMessage = new StringBuffer();
        final String checkoutProviderMessage = checkoutResult.getProviderMessage();
        if ( checkoutProviderMessage != null )
        {
            providerMessage.append( checkoutProviderMessage );
        }

        boolean success = checkoutResult.isSuccess();
        if ( success )
        {
            final File basedir = fileSet.getBasedir();
            final File gitDir = new File( basedir, ".git" );
            if ( gitDir.exists() )
            {
                try
                {
                    FileUtils.deleteDirectory( gitDir );
                }
                catch ( final IOException e )
                {
                    success = false;
                    final StringWriter sw = new StringWriter();
                    final PrintWriter pw = new PrintWriter( sw );
                    e.printStackTrace( pw );

                    providerMessage.append( "\n\n" ).append( sw.toString() );
                }
            }

            if ( success )
            {
                final DirectoryScanner ds = new DirectoryScanner();
                ds.setBasedir( basedir );
                ds.setIncludes( new String[] { "**/*" } );

                ds.scan();

                final List exportedFiles = new ArrayList();
                final String[] files = ds.getIncludedFiles();
                for ( int i = 0; i < files.length; i++ )
                {
                    exportedFiles.add( new ScmFile( files[i], ScmFileStatus.ADDED ) );
                }

                return new ExportScmResult( checkoutResult.getCommandLine(), exportedFiles );
            }
        }

        return new ExportScmResult( checkoutResult.getCommandLine(), providerMessage.toString(),
                                    checkoutResult.getCommandOutput(), success );
    }

}
