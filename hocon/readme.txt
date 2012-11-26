HOCON (Human-Optimized Config Object Notation)
----------------------------------------------

https://github.com/typesafehub/config/blob/master/HOCON.md

Goals of HOCON in comparison with JSON:
    less noisy / less pedantic syntax
    ability to refer to another part of the configuration (set a value to another value)
    import/include another configuration file into the current file
    a mapping to a flat properties list such as Java's system properties
    ability to get values from environment variables
    ability to write comments

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

Duplicate keys and object merging:
