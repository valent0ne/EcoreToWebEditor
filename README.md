EcoreToWebEditor

Design and development of an automated process that generates web-based text modeling environments. In particular, starting from a meta-model, it has been defined:
- a canonical mapping for the definition of the textual (concrete) syntax;
- the generation of its modeling environment.
The whole process is carried out by a Java command line application that uses the interaction of transformation engines of models and frameworks such as Acceleo and Xtext.
more infos in the /doc directory

demo: https://www.youtube.com/watch?v=VszKqYxvRgg

usage: java -jar ecoreToWebEditor-1.0.jar -e [path_to_ecore_metamodel] -ep [name_of_entry_point_eclassifier] -efy

(the -efy flag is optional, it is used to prepare the project so it can be easily imported into eclipse)
