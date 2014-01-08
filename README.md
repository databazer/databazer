Databazer
=========

Databazer is Java library for creating DataSources for popular SQL Databases

###Databased supported

Currently we implemented DataSources for MySQL and H2 Databases only.

###Why those databases only?

Just because primary we use those two DBs in our projects.
If databazer will be in demand we will provide support for needed Databases. So don't hesitate to raise feature request.

###Platform supported
Any platform that runs JRE 1.5 and higher is supported.

###Is it well tested?
Yes, we provide our unit tests, we supply them with sources.

###Is it documented?
Yes, we supply Javadoc as well. But nobody is perfect, so don't hesitate to ask questions.
Together we will make project better!

###What about code examples?
We didn't provide examples at wiki yet. Please browse unit tests.

###Where to report bug, problem, request and issue?
Here is our tracker for this project:
<http://track.virtadev.net/issues/DBZ>

##How to install databazer?
First clone repo from Github: 
`git clone git@github.com:virtalab/databazer.git databazer`

Move to databazer directory
`cd databazer`

Run 
`mvn clean package`
to get JAR file of library

Run
`mvn install`
to install databazer into your local maven repository


Also please wait a little, we publish artifact at Maven Central

