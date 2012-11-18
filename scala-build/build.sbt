// Some own imports: 
// import my.sbt._

// ===== Configuration as Map with keys =====
// SBT build file is translated to settings Map with (key, value) pairs.
// SBT comes with its initial Map, that is transformed in build.sbt to a resulting Map of settings.
// Keys are defined as fields in Key class, keys can have different associated values for different scopes (e.g. test scope).

// ===== Scopes for keys =====
// Often the scope is implied or has a default, but if the defaults are wrong, you'll need to mention the desired scope in build.sbt
// Scopes have three axis that together determine concrete scope with concrete scoped values for keys: 
// 1) Project, 2) Configuration, 3) Task 
// A configuration defines a flavor of build, potentially with its own classpath, sources, generated packages, etc.
// Predefined configurations are: Compile, Test, Runtime (with different classpaths).
// Tasks are compile, package etc.
// Global scope with default values can be predefined, fallback to more general scopes is applied.
// More specific scopes inherit values from more general scopes.
// Examples of scoped key notation:
// General scoped key syntax: {<build-uri>}<project-id>/config:intask::key 
// Concrete examples: 
// full-classpath: just a key, so the default scopes are used: current project, a key-dependent configuration, and global task scope.
// test:full-classpath: specifies the configuration, so this is full-classpath in the test configuration, with defaults for the other two scope axes.
// *:full-classpath: specifies Global for the configuration, rather than the default configuration.
// doc::full-classpath: specifies the full-classpath key scoped to the doc task, with the defaults for the project and configuration axes.
// {file:/home/hp/checkout/hello/}default-aea33a/test:full-classpath specifies a project, {file:/home/hp/checkout/hello/}default-aea33a, where the project is identified with the build {file:/home/hp/checkout/hello/} and then a project id inside that build default-aea33a. Also specifies configuration test, but leaves the default task axis.
// {file:/home/hp/checkout/hello/}/test:full-classpath sets the project axis to "entire build" where the build is {file:/home/hp/checkout/hello/}
// {.}/test:full-classpath sets the project axis to "entire build" where the build is {.}. {.} can be written ThisBuild in Scala code.
// {file:/home/hp/checkout/hello/}/compile:doc::full-classpath sets all three scope axes.
// Values from scopes can be checked by: inspect <syntax of scoped key>, for e.g.: inspect test:full-classpath, inspect name
// If you create a setting in build.sbt with a stand-alone key, it will be scoped to the current project, configuration Global and task Global
// build.sbt always defines settings for a single project, so the "current project" is the project you're defining in that particular build.sbt. 
// (For multi-project builds, each project has its own build.sbt.)
// in build.sbt, keys can be defined in scope axis with syntax <keyName> in <instance of any scope axis>, e.g.:
// name in Compile := "hello"; name in packageBin := "hello"; name in (Compile, packageBin) := "hello"; name in Global := "hello" // Global used for all three axis
// To change the value associated with the compile key, you need to write compile in Compile or compile in Test. 
// Using plain compile would define a new compile task scoped to the current project, rather than overriding the standard compile tasks which are scoped to a configuration.
// If you get an error like "Reference to undefined setting", often you've failed to specify a scope, or you've specified the wrong scope.
// In reality, all keys consist of both a name, and a scope (where the scope has three axes). 
// The entire expression packageOptions in (Compile, packageBin) is a key name, in other words. 
// Simply packageOptions is also a key name, but a different one (for keys with no in, a scope is implicitly assumed: current project, global config, global task).

// ===== Settings (modifying resulting Map) =====
// *** Replacing values := *** 
// The Setting which := creates/puts a fixed, constant value in the new, transformed map.
// *** Appending to previous values: += and ++= *** 
// (if the T in SettingKey[T] is a sequence, i.e. the key's value type is a sequence)
// For example, the key sourceDirectories in Compile has a Seq[File] as its value. By default this key's value 
// would include src/main/scala. If you wanted to also compile source code in a directory called source 
// (since you just have to be nonstandard), you could add that directory: 
// sourceDirectories in Compile += new File("source"); or sourceDirectories in Compile += file("source"); or sourceDirectories in Compile ++= Seq(file("sources1"), file("sources2"))
// *** Transforming a value: ~= ***
// E.g. filtering out src/main/scala: sourceDirectories in Compile ~= { srcDirs => srcDirs filter(!_.getAbsolutePath.endsWith("src/main/scala")) }
// E.g. make the project name upper case: name ~= { _.toUpperCase }
// *** Computing a value based on other keys' values: <<= ***
// E.g. name our organization after our project (both are SettingKey[String]): organization <<= name
// E.g. name the project after the directory it's inside (name is a Key[String], baseDirectory is a Key[File], we need conversion using apply): name <<= baseDirectory.apply(_.getName); or shorter: name <<= baseDirectory(_.getName)
// name is then "dependent" on baseDirectory (dependencies appear in inspect sbt command)
// Some settings describe tasks, so this approach also creates dependencies between tasks (sbt then automatically runs tasks that the task is dependent on).
// E.g. <<= with multiple dependant keys: name <<= (name, organization, version) { (n, o, v) => "project " + n + " from " + o + " version " + v } (apply called on a tuple, apply accepts transforming function)
// Whenever a setting uses ~= or <<= to create a dependency on itself or another key's value, the value it depends on must exist. If it does not, sbt will complain. It might say "Reference to undefined setting"
// Task keys create a Setting[Task[T]] rather than a Setting[T] when you build a setting with :=, <<=, etc. The practical importance of this is that you can't have tasks as dependencies for a non-task setting.
// However, task can depend on both settings and other tasks, though, just use map rather than apply to build an Initialize[Task[T]] rather than an Initialize[T]:
// E.g.: packageBin in Compile <<= (name, organization, version) map { (n, o, v) => file(o + "-" + n + "-" + v + ".jar") }
// Creation of alias of a key is done using <<=. Not very useful example can be: packageBin in Compile <<= packageDoc in Compile
// *** Appending with dependencies: <+= and <++= ***
// Appending to list which combines += and ++= with <<= (appending some dependencies - other keys - to list)
// For <++=, the function you write to convert the dependencies' values into a new value should create a Seq[T] instead of a T
// E.g.: A coverage report named after the project, and you want to add it to the files removed by clean:
// Adding to task means adding a function returning a value (for lazy evaluation).
// cleanFiles <+= (name) { n => file("coverage-report-" + n + ".txt") }

// Setting key is evaluated only once the project is load (unlike the task key). For e.g. name is setting key.
// Example of task keys: compile, package, run, ...
name := "scala-build"

version := "1.0"

description := "Test Scala project built with SBT"

scalaVersion := "2.9.2"

// ===== Resources =====
// Keys of sbt eclipse plugin are in EclipseKeys 
// Let's create also resource source folders (then reload, eclipse with-source=true)
// See: https://github.com/typesafehub/sbteclipse/wiki/Using-sbteclipse
EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

// Options for packaging jars:
// packageOptions
// Other keys:
// sourceDirectories
// scalacOptions
// fullClasspath
// artifactName

// ===== Managed dependencies (in IVY syntax) =====
// Library dependencies can be added as:
// 1) Unmanaged dependencies - just jars dropped into the lib directory
//    Dependencies in lib go on all the classpaths (for compile, test, run, and console)
//    If you wanted to change the classpath for just one of those, you would adjust dependencyClasspath in Compile 
//    or dependencyClasspath in Runtime for example. You could use transformation (filtering out) using ~=
//    You can change the unmanaged-base key if you'd like to use a different directory rather than lib: unmanagedBase <<= baseDirectory { base => base / "custom_lib" } (baseDirectory is the project's root directory)
//    There's also an unmanaged-jars task which lists the jars from the unmanaged-base directory. If you wanted 
//    to use multiple directories or do something else complex, you might need to replace the whole unmanaged-jars
//    task with one that does something else.
// 2) Managed dependencies - configured in the build definition and downloaded automatically from repositories
// Listing dependencies using: libraryDependencies += groupID % artifactID % revision
// It's also possible to write a Maven POM file or Ivy configuration file to externally configure your dependencies, 
// and have sbt use those external configuration files.
// libraryDependencies is declared in Keys like this: val libraryDependencies = SettingKey[Seq[ModuleID]]("library-dependencies", "Declares managed dependencies.")
// The % methods create ModuleID objects from strings, then you add those ModuleID to libraryDependencies.
// Of course, sbt (via Ivy) has to know where to download the module. If your module is in one of the default repositories 
// sbt comes with, this will just work. If you type dependencies in build.sbt and then update (resolves and optionally retrieves dependencies, producing a report), 
// sbt should download libraries to ~/.ivy2/cache. Update is dependency of compile, so there is no need to call it directly most of the time.
// Also libraryDependencies ++= Seq( groupID % artifactID % revision, groupID % otherID % otherRevision) can be used
// *** Getting the right Scala version with %% ***
// libraryDependencies += "org.scala-tools" % "scala-stm_2.9.1" % "0.3" can be equally written as:
// libraryDependencies += "org.scala-tools" %% "scala-stm" % "0.3" (%% adds current Scala version to next artifactID, does not accept libraries with lower versions for e.g.)
// The idea is that many dependencies are compiled for multiple Scala versions, and you'd like to get the one that matches your project.
// *** IVY revisions ***
// The revision in groupID % artifactID % revision does not have to be a single fixed version. 
// Ivy can select the latest revision of a module according to constraints you specify: "latest.integration", "2.9.+", or "[1.0,)"
// *** Resolvers ***
// sbt uses the standard Maven2 repository by default. If your dependency isn't on one of the default repositories, you'll have to add a resolver to help Ivy find it.
// resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
// resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository" 
// Type of resolvers is: SettingKey[Seq[Resolver]]("resolvers", "The user-defined additional resolvers for automatically managed dependencies.")
// *** Overriding default resolvers ***
// Resolvers does not contain the default resolvers; sbt combines resolvers with some default repositories to form external-resolvers.
// Therefore, to change or remove the default resolvers, you would need to override external-resolvers instead of resolvers.
// *** Per-configuration dependencies ***
// Often a dependency is used by your test code (in src/test/scala, which is compiled by the Test configuration) but not your main code. E.g. only for the Test configuration:
// libraryDependencies += "org.apache.derby" % "derby" % "10.4.1.3" % "test"
// Zavislosti lze zobrazit pomoci: show compile:dependency-classpath, show test:dependency-classpath 

libraryDependencies += "org.apache.derby" % "derby" % "10.4.1.3"

libraryDependencies += "org.scala-tools" % "scala-stm_2.9.1" % "0.3"

// ===== sbt commands (tasks) =====
// clean, compile, run, package-bin, package, package-src, package-doc