package org.apache.maven.scm.provider.synergy.command.tag;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmTagParameters;
import org.apache.maven.scm.command.tag.AbstractTagCommand;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.synergy.command.SynergyCommand;
import org.apache.maven.scm.provider.synergy.repository.SynergyScmProviderRepository;
import org.apache.maven.scm.provider.synergy.util.SynergyRole;
import org.apache.maven.scm.provider.synergy.util.SynergyUtil;

/**
 * @author <a href="mailto:julien.henry@capgemini.com">Julien Henry</a>
 * @version $Id$
 */
public class SynergyTagCommand
    extends AbstractTagCommand
    implements SynergyCommand
{
    
    protected ScmResult executeTagCommand( ScmProviderRepository repository, ScmFileSet fileSet, String tag,
                                           String message )
        throws ScmException
    {
        return executeTagCommand( repository, fileSet, tag, new ScmTagParameters( message ) );
    }

    /** {@inheritDoc} */
    protected ScmResult executeTagCommand( ScmProviderRepository repository, ScmFileSet fileSet, String tag,
                                           ScmTagParameters scmTagParameters )
        throws ScmException
    {
        if ( getLogger().isDebugEnabled() )
        {
            getLogger().debug( "executing tag command..." );
        }

        SynergyScmProviderRepository repo = (SynergyScmProviderRepository) repository;

        if ( getLogger().isDebugEnabled() )
        {
            getLogger().debug( "basedir: " + fileSet.getBasedir() );
        }

        String ccmAddr = SynergyUtil.start( getLogger(), repo.getUser(), repo.getPassword(), SynergyRole.BUILD_MGR );

        try
        {
            SynergyUtil.createBaseline( getLogger(), repo.getProjectSpec(), tag, repo.getProjectRelease(), repo
                .getProjectPurpose(), ccmAddr );
        }
        finally
        {
            SynergyUtil.stop( getLogger(), ccmAddr );
        }

        return new TagScmResult( "", fileSet.getFileList() );
    }

}
