# Introduction
The Virtual Imaging Platform is a web portal for pipeline execution on
distributed systems. An instance of this portal deployed on the
European Grid Infrastructure is accessible at
[https://vip.creatis.insa-lyon.fr](https://vip.creatis.insa-lyon.fr).

# Credits

The VIP logo was designed by Max Langer under the [CC BY-NC-SA license](https://creativecommons.org/licenses/by-nc-sa/2.0/).

# Development guidelines

0. Your Git username and email should be set to your First-Last name and actual email:
```
git config --global user.name "Jeanne Tartempion"                  # not "tartempion" or "jtartempion" or "root"
git config --global user.email "jeanne.tartempion@university.fr"   # not "root@localhost"
```
1. Developments are made in forks from the base repository. Developments lead to pull-requests that are merged by the owner(s) of the base repository.
2. Starting from release 1.15, we adopt the branching model described [here](http://nvie.com/posts/a-successful-git-branching-model). 
3. When working on a new feature: 
* In your fork, create a new feature branch from the development branch:
```
git checkout -b new_feature develop
```
You may name your branch anything except master, develop, release-*, or hotfix-*
3. When feature development is finished:
* Push your commits to Github (your fork).
* Make a pull request to merge feature branch (in your fork) to development branch (in the base repository).

# Production installation instructions

You can follow the instruction [here](https://github.com/virtual-imaging-platform/Complementary-tools/blob/develop/README.md)

You can contact us at [vip-support@creatis.insa-lyon.fr](vip-support@creatis.insa-lyon.fr) for precisions.

# Local test installation instructions

It is possible to launch a local vip instance in a tomcat on a linux machine.
This is only useful for testing purposes as most of the features are simulated and many are still to be implemented and do not work yet.
Here are the instructions to configure a local vip instance :
- create a empty folder that will contain all the configuration files and simulated data. `/path/to/vip/local/folder` will be its path used in the next instructions
- unzip the `local-config.zip` archive available in `vip-local/src/main/resources` in `/path/to/vip/local/folder`. This should contain 3 `.conf` files
- create a `$HOME/.vip` directory and a `$HOME/.vip/local-config-folder.properties` file
- put `vipConfigFolder = /path/to/vip/local/folder` in the `$HOME/.vip/local-config-folder.properties` file

Then, install vip in your local tomcat :

1. Build the vip-local war with `mvn clean package` at the root of the `VIP-portal` project.
2. Put the `vip-portal/target/vip-portal-[...]-local.war` file in the `$TOMCAT_HOME/webapps` directory
3. Add the folowing lines in `$TOMCAT_HOME/conf/context.xml` (database jndi configuration), and edit `path/to/vip/local/folder/vip` to the real path in `url`
```
<Resource name="jdbc/vip" auth="Container"  type="javax.sql.DataSource" 
       username="sa"     
       password="" 
       driverClassName="org.h2.Driver" 
       description="VIP local h2 Connection" 
       url="jdbc:h2:/path/to/vip/local/folder/vip" 
       maxActive="100" 
       maxIdle="50" />
```
4. Create or adapt the `$TOMCAT_HOME/bin/setenv.sh` file with these lines (edit `path/to/vip/local/folder/vip` to the real path) :

```
export CATALINA_OPTS="$CATALINA_OPTS -Dspring.profiles.active=local,config-file,jndi-db"
export CATALINA_OPTS="$CATALINA_OPTS -DvipConfigFolder=/path/to/vip/local/folder"
```

5. That's it, start tomcat (`bin/startup.sh`). Access vip on `localhost:8080/vip-portal-[...]-local` (adapt with the war name and your tomcat host/port configuration)

### Local instance notes

- The default admin email/password is `admin@vip-local-test.local`/`localAdminPassword`.
- all is not working perfectly yet, expect to see some error messages. The home page, files transfers and execution launchs shoud work, but application imports and the page with execution details are not implemented at the moment.
- vip do not send email but logs them
- at the moment, logging is done in the `$HOME/.vip/vip.log` file






