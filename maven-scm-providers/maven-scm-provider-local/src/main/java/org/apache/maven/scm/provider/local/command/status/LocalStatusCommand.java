package org.apache.maven.scm.provider.local.command.status;

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
import org.apache.maven.scm.command.status.AbstractStatusCommand;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.local.command.LocalCommand;
import java.util.List;
import java.util.Collections;

/**
 * @author <a href="mailto:matthewm@ambientideas.com">Matthew McCullough</a>
 * @version $Id$
 */
public class LocalStatusCommand
    extends AbstractStatusCommand
    implements LocalCommand
{
    /** {@inheritDoc} */
    protected StatusScmResult executeStatusCommand( ScmProviderRepository repository, ScmFileSet fileSet)
        throws ScmException
    {
        List fileList = Collections.EMPTY_LIST;
        return new StatusScmResult( null, fileList );
    }
}
