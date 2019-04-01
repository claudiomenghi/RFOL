# This repository contains the  classes that support the usage of Restricted Signals First-Order Logic (RFOL)

This project is associated with a maven dependency that allows to use the patterns within your project.

To use it add this repository and dependency in your POM.xml

```
<repository>
	<id>RFOL-mvn-repo</id>
	<url>https://raw.github.com/claudiomenghi/RFOL/mvn-repo/</url>
	<snapshots>
		<enabled>true</enabled>
		<updatePolicy>always</updatePolicy>
	</snapshots>
</repository>

<dependency>
	<groupId>uni.lu</groupId>
	<artifactId>rfol</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>

```