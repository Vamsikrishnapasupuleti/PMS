# PMS

Pre-requisites:

1. JDK8

https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

Based on your system (64bit or 32 bit)
Windows x86	197.66 MB  	jdk-8u201-windows-i586.exe
(OR)
Windows x64	207.46 MB  	jdk-8u201-windows-x64.exe

install after downloading software.



2.Tomcat 8.5

https://tomcat.apache.org/download-80.cgi

32-bit Windows zip (pgp, sha512)
(OR)
64-bit Windows zip (pgp, sha512)

unZip folder after download


3.Oracle XE 18c

https://www.oracle.com/technetwork/database/database-technologies/express-edition/downloads/index.html

Oracle Database 18c Express Edition for Windows x64 Download

Install 

After Insatallation:

1,press win+R,type sqlplus.

follow below steps:

username: sys as sysdba
press enter

no need password 

press enter

alter session set "_ORACLE_SCRIPT"=true;

create user hr identified by "hr";

GRANT CONNECT, RESOURCE, DBA TO hr;

GRANT UNLIMITED TABLESPACE TO hr;


GRANT CREATE session, CREATE table, CREATE view, 
      CREATE procedure,CREATE synonym,
      ALTER table, ALTER view, ALTER procedure,ALTER synonym,
      DROP table, DROP view, DROP procedure,DROP synonym,
      TO hr;
      

4.Eclipse IDE for Java JEE Developer (Latest version)

https://www.eclipse.org/downloads/packages/release/indigo/sr2/eclipse-ide-java-ee-developers

Windows 32-bit
(OR)
Windows 64-bit


After Installation of all Softwares and setup 

1,Open Eclipse -->Window on top bar -->show View-->Other-->server

2,in Server tab click for new server -->apache-->tomcat 8.5-->location of unziped tomcat folder
