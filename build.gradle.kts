plugins {
    id("java")
    id("war")
}


group = "com.roshka.tests"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    providedCompile(group = "javax.servlet", name = "javax.servlet-api", version =  "4.0.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

war {

}
