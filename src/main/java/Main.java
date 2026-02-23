import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.modiff.Modiff;
import org.eclipse.epsilon.modiff.matcher.IdMatcher;
import org.eclipse.epsilon.modiff.matcher.Matcher;
import org.eclipse.epsilon.modiff.output.LabelProvider;
import org.eclipse.epsilon.modiff.output.MatcherBasedLabelProvider;
import org.eclipse.epsilon.modiff.output.graphical.PlantumlFormatter;
import org.eclipse.epsilon.modiff.output.textual.UnifiedDiffFormatter;

public class Main {
	public static void main(String[] args) throws IOException {
        // Register the default resource factory for .ecore and .model files
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap()
            .put("ecore", new XMIResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap()
    		.put("model", new XMIResourceFactoryImpl());

        // Create a resource set
        ResourceSet resourceSet = new ResourceSetImpl();

        // .ecore file
        String ecoreFile = "models/library/library.ecore";
        URI uri = URI.createFileURI(new File(ecoreFile).getAbsolutePath());

        // Load the .ecore file & get the root EPackage
        Resource resource = resourceSet.getResource(uri, true);
        EPackage ePackage = (EPackage) resource.getContents().get(0);

        // Show which metamodel was loaded for debugging purposes
        System.out.println("Loaded metamodel: " + ePackage.getName());
        System.out.println();

		for (EObject o : resource.getContents()) {
			ePackage = (EPackage) o;
			EPackage.Registry.INSTANCE.put(ePackage.getNsURI(), ePackage);
		}

		/*
		 * From this point on begins the part where the model changes are visualized
		 */

		Modiff modiff = new Modiff("models/library/alexandria.model", "models/library/alexandria_v2.model");

		Matcher matcher = new IdMatcher();
		modiff.setMatcher(matcher);

		modiff.compare();

		LabelProvider labelProvider = new MatcherBasedLabelProvider(matcher);

		System.out.println(new UnifiedDiffFormatter(modiff.getMunidiff(), labelProvider).format());
		System.out.println("**************************************************");
		System.out.println(new PlantumlFormatter(modiff.getMunidiff(), labelProvider).format());
	}
}
