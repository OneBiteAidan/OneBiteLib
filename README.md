# OneBiteLib
A library containing commonly used utilities inside of my Minecraft plugins developed using the PaperMC API.

## Directory
- [Installation](#installation)
  - [Maven](#maven)
  - [Gradle](#gradle)
- Package Documentation
  - [Config](./src/main/java/dev/onebiteaidan/Config/README.md)

## Installation
### Maven
Add the OneBitLib repository to your project's pom.xml
```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

Add OneBiteLib as a dependency in the project's pom.xml
```xml
	<dependency>
	    <groupId>com.github.OneBiteAidan</groupId>
	    <artifactId>OneBiteLib</artifactId>
	    <version>v1.0.0</version>
	</dependency>
```

### Gradle
Add JitPack to your root settings.gradle at the end of the repositories:
```groovy
	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```
And then add OneBiteLib as a dependency:
```groovy
	dependencies {
	        implementation 'com.github.OneBiteAidan:OneBiteLib:v1.0.0'
	}
```