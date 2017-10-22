package selfdriven.ku.cgiapt;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeScanner;
import com.sun.tools.javac.util.List;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public class CGIAnnotationProcessor extends AbstractProcessor {
    private static final String CLASS = "CGIInitHelper";
    private static final String METHOD_INIT_CGI = "initCGI";
    private static final String DEBUG_POSTFIX = "_DEBUG";
    private Map<String, String> mHostConstantsMap = new HashMap<>();

    private static ClassName CONSTANTS_CLASS;
    private static ClassName BUILD_CONFIG;
    private static String PACKAGE;

    private Trees mTrees;
    private Elements mElementUtils;
    private Types mTypes;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportAnnotations = new HashSet<>();
        for (Class<? extends Annotation> annotation : getSupportAnnotations()) {
            supportAnnotations.add(annotation.getCanonicalName());
        }
        return supportAnnotations;
    }

    private Set<Class<? extends Annotation>> getSupportAnnotations() {
        Set<Class<? extends Annotation>> annotations = new HashSet<>();
        annotations.add(CGI.class);
        annotations.add(SimpleCGI.class);
        annotations.add(BuildConfigFile.class);
        return annotations;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mTrees = Trees.instance(processingEnvironment);
        mTypes = processingEnvironment.getTypeUtils();
        mElementUtils = processingEnvironment.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        if (set.isEmpty()) return false;
        initFileLocation(roundEnv);
        TypeSpec.Builder cgiClassBuilder = createClass(CLASS);
        MethodSpec.Builder initCGIBuilder = createInitMethod();
        processBuildConfigFile(roundEnv);
        processCGI(initCGIBuilder, roundEnv);
        processSimpleCGI(initCGIBuilder, roundEnv);
        cgiClassBuilder.addMethod(initCGIBuilder.build());
        JavaFile file = JavaFile.builder(PACKAGE, cgiClassBuilder.build())
                .addStaticImport(CONSTANTS_CLASS, "*")
                .build();
        try {
            file.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            error(e.getLocalizedMessage());
        }
        return false;
    }

    private void initFileLocation(RoundEnvironment roundEnv) {
        Set<? extends Element> buildConfigFile = roundEnv.getElementsAnnotatedWith(BuildConfigFile.class);
        if (buildConfigFile == null || buildConfigFile.isEmpty()) {
            error("@BuildConfigFile must be define");
            return;
        }
        Element element = buildConfigFile.iterator().next();
        PACKAGE = mElementUtils.getPackageOf(element).getQualifiedName().toString();
        CONSTANTS_CLASS = ClassName.get((TypeElement) element);
    }

    private void processBuildConfigFile(RoundEnvironment roundEnv) {
        Set<? extends Element> buildConfigFile = roundEnv.getElementsAnnotatedWith(BuildConfigFile.class);
        if (buildConfigFile == null || buildConfigFile.isEmpty()) {
            error("@BuildConfigFile must be define");
            return;
        }
        BuildConfigFile file = buildConfigFile.iterator().next().getAnnotation(BuildConfigFile.class);
        TypeElement typeElement = (TypeElement) mTypes.asElement(getTypeMirror(file));
        BUILD_CONFIG = ClassName.get(typeElement);
    }

    private TypeMirror getTypeMirror(BuildConfigFile annotation) {
        try {
            annotation.value();
        } catch (MirroredTypeException mte) {
            return mte.getTypeMirror();
        }
        return null;
    }

    private void processSimpleCGI(MethodSpec.Builder methodBuilder, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(SimpleCGI.class);
        if (checkSetEmpty(elements)) {
            return;
        }
        for (Element element : elements) {
            SimpleCGI simpleCGI = element.getAnnotation(SimpleCGI.class);
            if (!checkValid(element, simpleCGI.value())) {
                error(String.format("Invalid field:%s @SimpleCGI annotation with values: %s", element.getSimpleName(), Arrays.toString(simpleCGI.value())));
                continue;
            }
            String name = element.getSimpleName().toString();
            String values[] = simpleCGI.value();
            String hostFieldName = getHostNameFromSimpleCGI(element, values[0]);
            methodBuilder.addStatement("$L = $T.DEBUG && debug ? $L + $S : $L + $S",
                    name, BUILD_CONFIG,
                    hostFieldName + DEBUG_POSTFIX, values[1],
                    hostFieldName, values[1]
            );
        }
    }

    private String getHostNameFromSimpleCGI(Element element, final String hostValue) {
        JCTree jcTree = getElementJCTree(element, SimpleCGI.class);
        if (jcTree == null) return "";
        if (mHostConstantsMap.containsKey(hostValue)) {
            return mHostConstantsMap.get(hostValue);
        }
        jcTree.accept(new TreeScanner() {
            @Override
            public void visitAnnotation(JCTree.JCAnnotation jcAnnotation) {
                List<JCTree.JCExpression> expressions = jcAnnotation.args;
                String expression = expressions.get(0).toString();
                Matcher matcher = Pattern.compile("\\{.*, .*}").matcher(expression);
                if (matcher.find()) {
                    String[] names = matcher.group().split(",");
                    mHostConstantsMap.put(hostValue, names[0].substring(1, names[0].length()));
                }
            }
        });
        return mHostConstantsMap.get(hostValue);
    }

    private JCTree getElementJCTree(Element element, Class<? extends Annotation> annotation) {
        return (JCTree) mTrees.getTree(element, getMirror(element, annotation));
    }

    private AnnotationMirror getMirror(Element element, Class<? extends Annotation> annotation) {
        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            if (annotationMirror.getAnnotationType().toString().equals(annotation.getCanonicalName())) {
                return annotationMirror;
            }
        }
        return null;
    }

    private void processCGI(MethodSpec.Builder methodBuilder, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(CGI.class);
        if (checkSetEmpty(elements)) {
            return;
        }
        for (Element element : elements) {
            CGI host = element.getAnnotation(CGI.class);
            if (!checkValid(element, host.value())) {
                error(String.format("Invalid field:%s @CCHOST annotation with values: %s", element.getSimpleName(), Arrays.toString(host.value())));
                continue;
            }
            String name = element.getSimpleName().toString();
            String[] values = host.value();
            methodBuilder.addStatement("$L = $T.DEBUG && debug ? $S : $S", name, BUILD_CONFIG, values[1], values[0]);
        }
    }

    private void debug(String msg) {
        printMessage(Diagnostic.Kind.NOTE, msg);
    }

    private void error(String msg) {
        printMessage(Diagnostic.Kind.ERROR, msg);
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
