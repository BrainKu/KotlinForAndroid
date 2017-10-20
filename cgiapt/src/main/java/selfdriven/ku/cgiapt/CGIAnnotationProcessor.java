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
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Created by zhengxinwei@N3072 on 2017/10/19.
 */

@SupportedAnnotationTypes("selfdriven.ku.cgiapt.CGIAnnotation")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class CGIAnnotationProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        StringBuilder builder = new StringBuilder()
                .append("package com.github.brainku.kotlinforandroid.gen;\n")
                .append("public class CGIConstants {\n") // open class
                .append("\tpublic String getMessage() {\n") // open method
                .append("\t\treturn ");
        // for each javax.lang.model.element.Element annotated with the CustomAnnotation
        for (Element element : roundEnv.getElementsAnnotatedWith(CGIAnnotation.class)) {
            String objectType = element.getSimpleName().toString();
            // this is appending to the return statement
            builder.append("\"" + objectType).append(" says hello!\";\n");
        }

        builder.append("\t}\n") // close method
                .append("}\n"); // close class

        try { // write the file
            JavaFileObject source = processingEnv.getFiler().createSourceFile("com.github.brainku.kotlinforandroid.gen.CGIConstants");
            Writer writer = source.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getLocalizedMessage());
            // Note: calling e.printStackTrace() will print IO errors
            // that occur from the file already existing after its first run, this is normal
        }
        return true;
    }
}
