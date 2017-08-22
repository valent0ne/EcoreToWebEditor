
package it.univaq.ecoreToWebEditor.acceleo;

import org.eclipse.acceleo.parser.compiler.AbstractAcceleoCompiler;
import org.eclipse.emf.common.util.Monitor;

/**
 * The Acceleo Stand Alone compiler.
 * 
 */
public class AcceleoCompiler extends AbstractAcceleoCompiler {

    /**
     * Launches the compilation of the mtl files in the generator.
     * 
     */
    @Override
    public void doCompile(Monitor monitor) {
        super.doCompile(monitor);
    }
    
    /**
     * Registers the packages of the metamodels used in the generator.
     * 
     */
    @Override
    protected void registerPackages() {
        super.registerPackages();
        /*
         * If you want to add the other packages used by your generator, for example UML:
         * org.eclipse.emf.ecore.EPackage.Registry.INSTANCE.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
         **/
    }

    /**
     * Registers the resource factories.
     * 
     */
    @Override
    protected void registerResourceFactories() {
        super.registerResourceFactories();
        /*
         * If you want to add other resource factories, for example if your metamodel uses a specific serialization and it is not contained in a ".ecore" file:
         * org.eclipse.emf.ecore.resource.Resource.Factory.Registry.getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
         **/
    }
}