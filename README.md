# ASMStringBufferIssueReproduction

In this project I have tried to show that decompiled class of StringBuffer using ASM is returning methods of the super class.
For class `java.lang.StringBuffer`, when we will try to look for `delete` method, we will find two method instances. 
Description of these two methods are

- `(II)Ljava/lang/StringBuffer;`
- `(II)Ljava/lang/AbstractStringBuilder;`


ASM Version: 9.2 \
JarDirectory: This directory contains `rt.jar`



## Update:

This is expected behavior. Java allows to override method with a return type that is a subtype of the return type of the overridden method (covariant return type). But to facilitate that javac create an extra method (bridge method) with return type of the overridden method.
