# Introduction

The Virtual Imaging Platform is a web portal for pipeline execution on
distributed systems. An instance of this portal deployed on the
European Grid Infrastructure is accessible at
[http://vip.creatis.insa-lyon.fr](http://vip.creatis.insa-lyon.fr).

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

# Installation instructions

To be done. Install at your own risks or contact us at [vip-support@creatis.insa-lyon.fr](vip-support@creatis.insa-lyon.fr).
