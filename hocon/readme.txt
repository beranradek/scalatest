HOCON (Human-Optimized Config Object Notation)
----------------------------------------------

https://github.com/typesafehub/config/blob/master/README.md
https://github.com/typesafehub/config/blob/master/HOCON.md

Goals of HOCON in comparison with JSON:
    less noisy / less pedantic syntax
    ability to refer to another part of the configuration (set a value to another value)
    import/include another configuration file into the current file
    a mapping to a flat properties list such as Java's system properties
    ability to get values from environment variables
    ability to write comments
    
JSON superset features:
    comments
    includes
    substitutions ("foo" : ${bar}, "bar" : Hello ${who})
    properties-like notation (a.b=c) (flattening of nested configurations)
    less noisy, more lenient syntax
    substitute environment variables

Own config loaded for e.g. from database can be written (with some custom code).
A Config can be created with the parser methods in ConfigFactory or built up from any 
file format or data source you like with the methods in ConfigValueFactory.


Loading
-------
The convenience method ConfigFactory.load() loads the following (first-listed are higher priority):

    system properties
    application.conf (all resources on classpath with this name)
    application.json (all resources on classpath with this name)
    application.properties (all resources on classpath with this name)
    reference.conf (all resources on classpath with this name)

Or use ConfigFactory.load("myapp") to load custom myapp.conf.
Libraries and frameworks should default to ConfigFactory.load() if the application does 
not provide a custom Config object. Libraries and frameworks should also allow the application 
to provide a custom Config object to be used instead of the default, in case the application 
needs multiple configurations in one JVM or wants to load extra config files from somewhere.

For applications using application.{conf,json,properties}, system properties can be used to force 
a different config source (replacement):

    config.resource specifies a resource name - not a basename, i.e. application.conf not application
    config.file specifies a filesystem path, again it should include the extension, not be a basename
    config.url specifies a URL
    
They only affect apps using the default ConfigFactory.load() configuration. In the replacement 
config file, you can use include "application" to include the original default config file; 
after the include statement you could go on to override certain settings.


Merging config trees
--------------------
Any two Config objects can be merged with an associative operation called withFallback, 
like merged = firstConfig.withFallback(secondConfig).
"lift" a subtree up to the root of the configuration; say you have something like:
Config devConfig = originalConfig
	.getConfig("dev")
    .withFallback(originalConfig)


Syntax
------
Files must be in UTF-8
Values have possible types: string, number, object, array, boolean, null
Comments: // or #

Root element:
In HOCON, if the file does not begin with a square bracket or curly brace, 
it is parsed as if it were enclosed with {} curly braces.

The = character can be used anywhere JSON allows :, i.e. to separate keys from values.
If a key is followed by {, the : or = may be omitted. So "foo" {} means "foo" : {}

Commas:
Values in arrays, and fields in objects, need not have a comma between them as 
long as they have at least one ASCII newline (\n, decimal value 10) between them.
The extra comma in the end of list (of fields, array values) is ommited, extra 
initial comma is an error.

Whitespaces:
Like isWhitespace() method in Java + non-breakable spaces.