EcoreToWebEditor

Design and development of an automated process that generates web-based text modeling environments. In particular, starting from an Ecore meta-model, it has been defined:
- a canonical mapping for the definition of the textual (concrete) syntax;
- the generation of its modeling environment.
The whole process is carried out by a Java command line application that uses the interaction of transformation engines of models and frameworks such as Acceleo and Xtext.
more infos in the /doc directory

demo video: https://youtu.be/m8UzrMBbMNg

usage: 
`java -jar ecoreToWebEditor-1.0.jar -e [path_to_ecore_metamodel] -ep [name_of_entry_point_eclassifier] -efy`

generated project will be placed inside the `./out` folder
after the execution is completed, import the generated project into eclipse and then run the `ServerLauncher.java` class inside the `com.xtext.[ecore_name]Dsl.web` package, the application is deployed to localhost:8080; the generated grammar file (`[ecore_name]Dsl.xtext`) is placed inside the `com.xtext.[ecore_name]Dsl` package.

(the `-efy` flag is optional, it is used to refactor the project so it can be easily imported into eclipse)
