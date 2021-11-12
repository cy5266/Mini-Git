# Gitlet Design Document

**Name**:

## Classes and Data Structures

Blobs:
String SHA- acts as encryption
String fileName - unique file name for each blob
Long serializableForm - a representation of a pointer to the file in byte form (?)

Trees: (linked list of commits)
Commit parentCom - will be “next” in the linked list, as in the most recent previous version
Commit head - the current head commit we are looking at

Commits:
String author, String commitDate, String log - metadata
String self_SHA1 - identifier for current commit
String parent_SHA1 - identifier for parent of commit
For parent commit (for the linked list)
Constructor params: log, parentSHA, blobs in commit
HashMap (array -> linked list) Files - key is name of File, and will point to the corresponding blobs

Main:
String parameter: to check the input that the user put e.g. “init”
Basically have call methods that will call on a tree:

Repo:
String headPointer: points to the most recent commit
String SHACommit: the SHA of the blob that we’re looking at
String directory: the directory we’re currently at

Serialization:



Testing: Includes jUnit tests that will allow us to test each functionality along the way.


## Algorithms

Blobs:
Void writemethod - will use writeContents from Utils to add text into the file



Trees:

Commits: this class handles creating a commit block and having pointers towards the parents
getParent: returns the sha1 and name and commit-date of the parent commit
changeHead: changes head to whatever commit we want in the linked list
If no matching commit exists, we throw exception
(basically moving the current pointer down the linked list until we get to the version that we want)



Main:
CheckParam: checks to see if the user has entered an existing parameter, whether
https://inst.eecs.berkeley.edu/~cs61b/fa21/materials/proj/proj3/index.html#the-commands
^ follow the spec for main

Repo: this class runs the results of the parameters that the user calls e.g. (“init, add, rm, merge”).

Edge cases: whether the commands are correct, the files exist

When merging, we first check that the SHA between files are not the same, then whether we need to merge, where to merge


## Persistence

