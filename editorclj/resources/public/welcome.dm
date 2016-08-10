# Welcome to Data in Motion - a networked editor for networked applications

Data in Motion is an opinionated Programmer's Editor based on the following principles.

* Your production build and compiler are the single source of truth for all editing metadata (e.g.: dependency management; refactoring support).  There is no way the IDE and the production build can return differing results because there is no IDE.
* Code should be editable from anywhere (by potentially many people at once) with nothing more than a web browser (including mobile browsers) and possibly an SSH connection for security.
* Github-style Markdown (like this file) is the new REPL / Code Notebook.  Just name your file with a .dm extension instead of a .md extension and fenced code blocks will be executed and results displayed inline.


Lastly:

* Data in Motion is written using itself, so anyone can extend it the same way it was originally written.
** (If you choose a dynamic JVM language, you can extend Data in Motion dynamically.)

## Configuration

Global configuration is stored in this file.  Project-specific configuration is stored in a data-in-motion notebook either next to this file or in your project's root.
