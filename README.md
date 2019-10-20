
# <span style="color:blue"> Sgit</span> 

This school project is a git like in scala implemented in order to have a first approach with functional programing and deal with I/O in this context.
Only some of the features are implemented.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes (Linux deb only). 

### Prerequisites

We assume that you have sbt (using to build the project) installed on your machine.


### Installing

To install the program, open a terminal and follow the next steps :


```
git clone https://github.com/ines-missoum/sgit.git
cd sgit
sbt assembly
source sgit_install.sh
```
On the same terminal*, go to the directory that you want and start using the program:

```
sgit <command>
```
*It should be on the same terminal because during installation the path is not modified so when you close the terminal the sgit commands will not be available. You can change the path if you want to but it’s not necessary to test the program.

## Running the tests
You can run the tests with the simple command: 
```
sbt test
```
For now 80 tests are implemented with scalatest, FlatSpec, Idiomatic Mockito and Matchers.
It covers around 60% of the code.


## Features implemented

```
sgit init
```
This command is used to start a new repository.
```
sgit branch <branchName>
```
This command is used to create a new branch linked to the current commit.
```
sgit tag <tagName>
```
This command is used to create a new tag linked to the current commit.
```
sgit branch -av
```
This command is used to list all the existing tags and branches and precises the current branch.
```
sgit add <filename/filenames or regexp>
```
This command adds file(s) to the staging area.
```
sgit status
```
This commands shows the state of each file.
```
sgit commit
```
This command records or snapshots the staging area permanently in the version history.
```
sgit log
```
This command is used to list the version history for the current branch.
```
sgit diff
```
This command shows the file differences which are not yet staged.
```
sgit checkout <branch or tag or commit hash>
```
This command is used to switch from one branch to another or to a specific commit. In this last case it will not be possible to have access to the log but any other command is available.

## Authors

* **Inès MISSOUM BENZIANE** - *Initial work* - [ines-missoum](https://github.com/ines-missoum/)


