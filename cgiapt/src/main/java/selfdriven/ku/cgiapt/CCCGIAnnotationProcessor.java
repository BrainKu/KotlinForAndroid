package selfdriven.ku.cgiapt;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

public class CCCGIAnnotationProcessor extends AbstractProcessor {
    private static String PACKAGE;
    private static ClassName CONSTANTS_CLASS;
    private static final String CLASS = "CGIApi";
    private static final String DEBUG_POSTFIX = "_DEBUG";
    // TODO this can get by another annotation？
    private static final ClassName BUILD_CONFIG = ClassName.get("com.github.brainku.kotlinforandroid", "BuildConfig");
    private static final String METHOD_INIT_CGI = "initCGI";

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(Arrays.asList("selfdriven.ku.cgiapt.CGI"
                , "selfdriven.ku.cgiapt.SimpleCGI"));
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        if (set.isEmpty()) return false;
        TypeSpec.Builder cgiClassBuilder = createClass(CLASS);
        MethodSpec.Builder initCGIBuilder = createInitMethod();
        processCGI(initCGIBuilder, roundEnv);
        processSimpleCGI(initCGIBuilder, roundEnv);
        cgiClassBuilder.addMethod(initCGIBuilder.build());
        JavaFile file = JavaFile.builder(PACKAGE, cgiClassBuilder.build())
                .addStaticImport(CONSTANTS_CLASS, "*")
                .build();
        try {
            file.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getLocalizedMessage());
        }
        return false;
    }

    private void processSimpleCGI(MethodSpec.Builder methodBuilder, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(SimpleCGI.class);
        if (checkSetEmpty(elements)) {
            return;
        }
        initConstantsClass(elements.iterator().next());
        for (Element element : elements) {
            SimpleCGI simpleCGI = element.getAnnotation(SimpleCGI.class);
            if (!checkValid(element, simpleCGI.value())) {
                printMessage(Diagnostic.Kind.ERROR, String.format("Invalid %s @SimpleCGI annotation with values: %s", element.getSimpleName(), Arrays.toString(simpleCGI.value())));
                continue;
            }
            String name = element.getSimpleName().toString();
            String values[] = simpleCGI.value();
            methodBuilder.addStatement("$L = $T.DEBUG && debug ? $L$L + $S : $L + $S",
                    name, BUILD_CONFIG,
                    values[0], DEBUG_POSTFIX, values[1],
                    values[0], values[1]
            );
        }
    }

    private void processCGI(MethodSpec.Builder methodBuilder, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(CGI.class);
        if (checkSetEmpty(elements)) {
            return;
        }
        initConstantsClass(elements.iterator().next());
        for (Element element : elements) {
            CGI host = element.getAnnotation(CGI.class);
            if (!checkValid(element, host.value())) {
                printMessage(Diagnostic.Kind.ERROR, String.format("Invalid %s @CCHOST annotation with values: %s", element.getSimpleName(), Arrays.toString(host.value())));
                continue;
            }
            String name = element.getSimpleName().toString();
            String[] values = host.value();
            methodBuilder.addStatement("$L = $T.DEBUG && debug ? $S : $S", name, BUILD_CONFIG, values[1], values[0]);
        }
    }

    private void initConstantsClass(Element element) {
        if (PACKAGE != null && !PACKAGE.isEmpty()) return;
        Element enclosedElement = element.getEnclosingElement();
        String FQ = enclosedElement.toString();
        int lastIndexOfDot = FQ.lastIndexOf(".");
        String constantsClass = FQ.substring(lastIndexOfDot + 1, FQ.length());
        PACKAGE = FQ.substring(0, lastIndexOfDot);
        CONSTANTS_CLASS = ClassName.get(PACKAGE, constantsClass);
    }

    private void printMessage(Diagnostic.Kind kind, String msg) {
        processingEnv.getMessager().printMessage(kind, msg);
    }

    private boolean checkSetEmpty(Set<? extends Element> elements) {
        return elements == null || elements.isEmpty();
    }

    private MethodSpec.Builder createInitMethod() {
        return MethodSpec.methodBuilder(METHOD_INIT_CGI)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .addParameter(boolean.class, "debug");
    }

    private TypeSpec.Builder createClass(String name) {
        TypeSpec.Builder builder = TypeSpec.classBuilder(name);
        builder.addModifiers(Modifier.FINAL);
        return builder;
    }

    private boolean checkValid(Element element, String[] values) {
        if (!element.getModifiers().contains(Modifier.STATIC)) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "invalid modifier");
            return false;
        }
        return values.length == 2;
    }
}
