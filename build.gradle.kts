plugins {
    java
    application
    `maven-publish`
}

group = "com.nikkibuild.websocket"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    val okV = "4.10.0"
    implementation("com.squareup.okhttp3:logging-interceptor:$okV")
    implementation("com.squareup.okhttp3:okhttp:$okV")
    implementation("com.squareup.okhttp:okhttp-ws:2.7.5")

    implementation("org.slf4j:slf4j-simple:1.7.36")
    implementation("io.reactivex.rxjava3:rxjava:3.1.5")

    implementation("com.google.dagger:dagger:2.42")
    annotationProcessor("com.google.dagger:dagger-compiler:2.42")

    implementation("com.google.code.gson:gson:2.9.0")
    implementation("commons-codec:commons-codec:1.15")

    implementation("com.github.vladimir-bukhtoyarov:bucket4j-core:7.3.0")

    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
}

application {
    mainClass.set("com.nikkibuild.websocket.japp.Main")
}

tasks {
    val fatJar = register<Jar>("fatJar") {
        dependsOn.addAll(
            listOf(
                "compileJava",
                "processResources"
            )
        ) // We need this for Gradle optimization to work
        archiveClassifier.set("standalone") // Naming the jar
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest { attributes(mapOf("Main-Class" to application.mainClass)) } // Provided we set it up in the application plugin configuration
        val sourcesMain = sourceSets.main.get()
        val contents = configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it) } +
                sourcesMain.output
        from(contents)
    }
    build {
        dependsOn(fatJar) // Trigger fat jar creation during build
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.nikkibuild.websocket"
            artifactId = "java"
            version = "1.0"
            from(components["java"])
        }
    }
}