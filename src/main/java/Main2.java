import java.io.IOException;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.modiff.Modiff;
import org.eclipse.epsilon.modiff.matcher.EcoreMatcher;
import org.eclipse.epsilon.modiff.matcher.Matcher;
import org.eclipse.epsilon.modiff.output.LabelProvider;
import org.eclipse.epsilon.modiff.output.MatcherBasedLabelProvider;
import org.eclipse.epsilon.modiff.output.graphical.PlantumlFormatter;
import org.eclipse.epsilon.modiff.output.textual.UnifiedDiffFormatter;

public class Main2 {

	public static void main(String[] args) throws IOException {
        // Register the default resource factory for .ecore
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap()
            .put("ecore", new XMIResourceFactoryImpl());

		/*
		 * From this point on begins the part where the model changes are visualized
		 */

		Modiff modiff = new Modiff("models/library/library.ecore", "models/library/library_v2.ecore");
		Matcher matcher = new EcoreMatcher();
		modiff.setMatcher(matcher);
		modiff.compare();

		LabelProvider labelProvider = new MatcherBasedLabelProvider(matcher);

		System.out.println(new UnifiedDiffFormatter(modiff.getMunidiff(), labelProvider).format());
		System.out.println("**************************************************");
		System.out.println(new PlantumlFormatter(modiff.getMunidiff(), labelProvider).format());
	}
}
