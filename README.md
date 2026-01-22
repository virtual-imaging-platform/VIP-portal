# Introduction

The Virtual Imaging Platform is a web portal for pipeline execution on
distributed systems. An instance of this portal using the
European Grid Infrastructure is accessible at
[https://vip.creatis.insa-lyon.fr](https://vip.creatis.insa-lyon.fr).

# Credits

The VIP logo was designed by Max Langer under the [CC BY-NC-SA license](https://creativecommons.org/licenses/by-nc-sa/2.0/).

# Development installation instructions

The easiest way is to use the latest docker image:

```
docker run -it --name vip -p 8080:8080 viplatform/vip-portal:latest
```

You can also use a docker volume for persistence:

```
docker run -it --name vip -p 8080:8080 -v vipvolume:/vip/storage viplatform/vip-portal:latest
```

See on https://hub.docker.com/r/viplatform/vip-portal for specific VIP versions instead of `latest`.

You can also use the vip-ansible project to install VIP in a virtual machine (contact team members).

# Production installation instructions

Contact us at [vip-support@creatis.insa-lyon.fr](vip-support@creatis.insa-lyon.fr).
