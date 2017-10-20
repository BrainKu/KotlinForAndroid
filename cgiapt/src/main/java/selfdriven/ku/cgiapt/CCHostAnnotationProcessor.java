package selfdriven.ku.cgiapt;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Created by zhengxinwei@N3072 on 2017/10/20.
 */
@SupportedAnnotationTypes("selfdriven.ku.cgiapt.CCHost")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class CCHostAnnotationProcessor extends AbstractProcessor {
    public static final String PACKAGE = "com.github.brainku.kotlinforandroid.gen";
    public static final String CLASS = "CGIApi";

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        StringBuilder builder = new StringBuilder()
                .append("package " + PACKAGE + ";\n")
                .append("public class " + CLASS +" {\n") // open class
                .append("\tpublic static void initHost(boolean debug) {\n");
        Set<? extends Element> hostAnnotation = roundEnv.getElementsAnnotatedWith(CCHost.class);
        processHostAnnotation(hostAnnotation, builder);
        builder.append("\t}\n") // close method
                .append("}\n"); // close class
        try { // write the file
            JavaFileObject source = processingEnv.getFiler().createSourceFile(PACKAGE + "." + CLASS);
            Writer writer = source.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getLocalizedMessage());
            // Note: calling e.printStackTrace() will print IO errors
            // that occur from the file already existing after its first run, this is normal
        }
        return false;
    }

    private void processHostAnnotation(Set<? extends Element> hostAnnotation, StringBuilder builder) {
        if (hostAnnotation == null) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "empty");
            return;
        }
        for (Element element : hostAnnotation) {
            CCHost host = element.getAnnotation(CCHost.class);
            if (!checkValid(element, host)) return;
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "type: " + element.getClass());
            String name = element.getSimpleName().toString();
            String[] hosts = host.value();
            builder.append("\t\t String ").append(name).append(" = debug ? \"").append(hosts[1]).append("\" : \"").append(hosts[0]).append("\";\n");
        }
    }

    private boolean checkValid(Element element, CCHost host) {
        if (!element.getKind().isField()) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "invalid field");
            return false;
        }
        if (!element.getModifiers().contains(Modifier.STATIC)) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "invalid modifier");
            return false;
        }
        String[] values = host.value();
        return values.length == 2;
    }
}
