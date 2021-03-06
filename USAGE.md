% GRICLI(1) Gricli user manual
% Yuriy Halytskyy, Markus Binsteiner
% July 1, 2011

<!-- don't edit the USAGE.md file directly since it'll be overwritten with regularly. Edit man/manpage-template.md instead -->

# NAME

gricli - grid commandline interface

# SYNOPSIS

gricli [*options*] 

# DESCRIPTION

Gricli is a shell that allows commandline interaction with the grid. You can use it to submit, control and monitor jobs. It also supports easy access to grid-filesystems and resource information.

Gricli is based on the *Grisu* framework and can connect to different *Grisu backends* by selecting the appropriate one as a commandline parameter, e. g.:

    gricli -b BeSTGRID
    
 or 
 
     gricli -b Local
     
 The above command would connect to the default *BeSTGRID* backend that publishes the *Grisu* API via SOAP. The latter would connect to a local *Grisu* backend which sits on the same computer as *gricli*, as long as the local backend jar (http://code.ceres.auckland.ac.nz/downloads/local-backend.jar) is in the classpath (either in the same folder as gricli or in %$HOME/.grisu.beta/lib/).

# OPTIONS

-b *BACKEND*, \--backend=*BACKEND*
:    the Grisu backend to connect to, default is *BeSTGRID*, other possible backends are *BeSTGRID-TEST*, *BeSTGRID-DEV*, *Local*.

-f  *SCRIPT*, \--file=*SCRIPT*
:    Executes a gricli script

-n, \--nologin
:    Disables login at gricli startup

# COMMANDS


`about`
:     Prints out version information.

`add`
:     Adds a value to list.

`add list`
:     optional example text explains what kinds of lists are available

`add value`
:     the value

`apropos`
:     Lists command that are associated with a keyword

`apropos keyword`
:     the keyword

`archive job`
:     Archives a job to the default archive location (DataFabric). The job will be deleted from the job database and the original jobdirectory will be deleted if the archiving was successful.

`archive job jobname`
:     the name of the job to archive

`attach`
:     Sets attached file list.Supports multiple arguments and glob regular expressions.

`attach files`
:     the files to attach

`batch add`
:     Add job to batch job container.

`batch add name`
:     the name of the batch job

`batch add command`
:     the (new) command to add

`batch create`
:     Creates a new batch job object. Batch job objects act as containers for jobs.

`batch create name`
:     the name of the new batchjob. Choose a meaningful name and make sure it is unique within your jobnames if possible.

`batch submit`
:     Submits a batch job (which should be created beforehand with 'batch create [name]' command.

`batch submit name`
:     the name of the batchjob to submit

`clean job`
:     Cleans a job from the database and deletes its job directory (also kills it beforehand if it is still running).

`clean job jobname`
:     the name of the job to clean

`clear`
:     Clears a list.

`clear list`
:     the list to clear (e.g. global)

`cd`
:     Change current directory. Used to change location of job downloads and as relative directory for attachments.

`cd dir`
:     Directory to change to

`destroy proxy`
:     Deletes the login information so you will have to enter it again on your next login.

`downloadClean job`
:     Downloads the whole jobdirectory. If successful, it then cleans the job from the jobdatabase and deletes the jobdirectory on the remote location.

`downloadClean job jobname`
:     the name of the job to download and clean

`download job`
:     Downloads the whole jobdirectory of the job.

`download job jobname`
:     the name of the job to download

`get`
:     experimental command

`filemanager`
:     experimental command

`gls`
:     Lists a directory in grid-space.

`gls path`
:     the url to list. Can be virtual (grid://...) or gridftp://...

`help`
:     Prints this help message.

`help keywords`
:     Optional keyword(s) to get more detailed information about a specific command

`ilogin`
:     Logs in to a grisu backend. If there is no certificate proxy, interactively asks user for details to create one.

`ilogin backend`
:     the grisu backend to log in

`kill job`
:     Kills a job. This stops the remote execution of the job but leaves the job in the job database and also the job directory intact. To delete the job directory you need to clean the job.

`kill job jobname`
:     the name of the job to kill

`login`
:     Logs in to a grisu backend with existing certificate proxy. Reports an error if there is no proxy.

`login backend`
:     the grisu backend to log in

`print applications`
:     List all application names available on the grid.

`print globals`
:     Lists all global variables.

`print hosts`
:     Lists all submission gateways.

`print jobs`
:     Lists all currently active (running or finished but not cleaned or archived) jobs.

`print queues`
:     Lists all queues that are available to you.

`print groups`
:     Lists all groups that are available to you.

`pwd`
:     Prints current directory.

`quit`
:     Logs out of this session but leaves your login information intact so you don't need to enter those on your next login (if still valid that is).

`run`
:     Runs a gricli script.

`run script`
:     path to the script file.

`set`
:     Sets a value for a variable.

`set var`
:     the name of the variable

`set value`
:     the value

`submit`
:     Submits a new job using the currently set environment and the specified commandline.

`submit commandline`
:     the commandline

`user clearCache`
:     Clears the Grisu filesystem cache. You need to logout and login again to see the effects of this command. Be aware that the next login will take longer than usual because the filesystem cache is rebuilt at that stage.

`wait job`
:     Waits for a job to finish on the remote compute resource. Useful for use within scripts where you want to automatically submit and download/archive jobs. At the moment allows to wait for single job only.

`wait job jobname`
:     the name of the job on which to wait. Regular expressions are not supported.

The Gricli source code and all documentation may be downloaded from
<http://github.com/grisu/gricli>.
