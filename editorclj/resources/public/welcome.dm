# Welcome to Data in Motion

Data in Motion is an opinionated Programmer's Editor based on the following principles.

* Data in Motion is polyglot.  The front-end can be written and customized in JavaScript or anything that compiles to it.
* The editor and coding environment is written in itself; it is immediately and completely customizable, live, as it is running.
* CodeMirror is the new de facto programmer's editor for all kinds of projects.  You can tweak your environment to do anything CodeMirror can do.

Further:

* Fast streaming data is becoming the new normal.  Why not stream code directly from the editor into a live (dev) system as you type too?
* Your production build and compiler are the single source of truth for all editing metadata (e.g.: dependency management; refactoring support).  There is no way the IDE and the production build can return differing results because there is no IDE.
* Code should be editable from anywhere (by potentially many people at once) with nothing more than a web browser (including mobile browsers) and possibly an SSH connection for security.
* Github-style Markdown (like this file) is the new REPL / Code Notebook.  Just name your file with a .dm extension instead of a .md extension and fenced code blocks will be executed and results displayed with the code.



## Configuration

Global configuration is stored in all files in the .dm directory inside your home directory.  ```welcome.dm``` (this file) is the main configuration file.

Project-specific configuration is stored in a data-in-motion notebook in your project's root.

--------------------------

# TODO

* Write (navbar brand-cell workspaces-cell menubar-cell)
* Make workspace switching work with hard-coded workspaces
  * Define cell for current workspace navbar metadata
  * This also is set to the new workspace's metadata when the workspace changes
* Figure out how to separate plugins from core functionality in the code base
  * Just a separate "plugins" sub-namespace?
    * build.boot will have to scan plugins folder and add subfolders to build
    * Still need a way for the runtime to discover the plugins...  Scan the folder and try to run plugin-name.core/init?
  * git submodule add... to import other people's plugins?
    * checkout a version
    * Update by pulling and switching to newer version tag

## API exported from HTML page

* Branding element cell
* Menu bar tree cell (translated via menu bar widget)
* Cell with a collection of editor widgets for open workspaces (alphabetized)
* documentReady callback
* Route handling callback

## Kinds of workspaces

* (Define/undefine workspaces in a workspaces.dm file inside Home)

* Project workspaces
  * Text editors based on file types
  * Configured using .dm files
  * Root filesystem folder tree displayed in .dm file
    * (Text results as collapsed outline)
    * Clickable links to files in tree
    * if inside a Github repo, supports Git operations
  * Gorilla notebooks?

* REPL workspaces?  *Or should we just support extending workspaces with new editor types?*
  * Ammonite?
  * SSH console?

## Workspace API

A map containing:

* Cell holding the "brand" text
  * For Project Workspaces: the project name or ".dm" for the home workspace + "//current-file.blah"
* Cell holding menu bar tree for the workspace
  * For Project Workspace: Top level - open file types; drop-downs: open files
* Factory function returning an editor widget for the workspace
* URL handler mapping URL routes to actions

How to define / register?
Download sources from Git?  If so, how to get dependencies?

## Project workspace

The workspace definition plus:

* Project root folder
  * Substitution envars supported as in: "${HOME}/dev/editordujour"
* Project name (derived from project root folder?)
* API to load/save (initially); WOOT (eventually)
