# Scripting with JTerm

## Table of Contents

* [Example script](#example-script)
* [Run a script](#run-a-script)
* [Commenting a script](#commenting-a-script)
* [Startup script](#startup-script)

## Example script

An example on how a script could look like can be found [here](Examples/example.jts) inside the Examples folder.

## Run a script

A script can be executed in two ways, either by using the `run` command with the path to the script or by adding `./` to the beginning of the path and executing this as a command.

## Commenting a script

A line in a script is treated as comment if it starts with either `#`, this makes documenting your code really easy.

>Currently it is only supported to comment out complete lines.

## Startup script

There is a special kind of script supported by JTerm, being the startup script. This script will be exectuted every time JTerm is started.

A startup script is a file with the name `.jtermrc` placed inside your home directory. Apart from that there is no difference to a normal script.

An example for a simple startup script is [here](Examples/.jtermrc).