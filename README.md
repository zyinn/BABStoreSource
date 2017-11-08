# BABStoreSource
BABStoreSource
项目主pom.xml
--------------
```
  <repositories>
        <repository>
            <id>fa center</id>
            <url>http://localhost:8081/content/groups/public</url>
            <releases>
                <enabled>true</enabled>
            </releases>
        </repository>
    </repositories>

    <parent>
        <groupId>com.sumscope.bab</groupId>
        <artifactId>parent</artifactId>
        <version>1.1.0</version>
    </parent>

    <modules>
        <module>store_shared</module>
        <module>store_server</module>
        <module>store_client</module>
    </modules>

    <properties>
        <!--Pom-Sumscope-Parent 要求给出master分支名称，以支持版本发布-->
        <gitflow.masterbranch.name>release_branch_1.0.x</gitflow.masterbranch.name>
    </properties>
```
