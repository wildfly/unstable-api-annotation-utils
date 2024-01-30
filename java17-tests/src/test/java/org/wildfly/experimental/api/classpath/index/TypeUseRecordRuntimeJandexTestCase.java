package org.wildfly.experimental.api.classpath.index;

import org.jboss.jandex.Index;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.wildfly.experimental.api.classpath.index.java17.classes.AnnotationWithExperimentalTypeUse;
import org.wildfly.experimental.api.classpath.index.java17.classes.ExperimentalTypeUse;
import org.wildfly.experimental.api.classpath.index.java17.classes.usage.RecordNoUsage;
import org.wildfly.experimental.api.classpath.index.java17.classes.usage.annotation.typeuse.RecordAnnotatedTypeUse;
import org.wildfly.experimental.api.classpath.index.java17.classes.usage.annotation.typeuse.RecordConstructorCanonicalAnnotatedTypeUse;
import org.wildfly.experimental.api.classpath.index.java17.classes.usage.annotation.typeuse.RecordConstructorCanonicalParameterAnnotatedTypeUse;
import org.wildfly.experimental.api.classpath.index.java17.classes.usage.annotation.typeuse.RecordConstructorCompactAnnotatedTypeUse;
import org.wildfly.experimental.api.classpath.index.java17.classes.usage.annotation.typeuse.RecordConstructorCompactParameterAnnotatedTypeUse;
import org.wildfly.experimental.api.classpath.index.java17.classes.usage.annotation.typeuse.RecordConstructorParameterAnnotatedTypeUse;
import org.wildfly.experimental.api.classpath.index.java17.classes.usage.annotation.typeuse.RecordGetterAnnotatedTypeUse;
import org.wildfly.experimental.api.classpath.index.java17.classes.usage.annotation.typeuse.RecordMethodAnnotatedTypeUse;
import org.wildfly.experimental.api.classpath.index.java17.classes.usage.annotation.typeuse.RecordMethodParameterAnnotatedTypeUse;
import org.wildfly.experimental.api.classpath.index.java17.classes.usage.annotation.typeuse.TypeRecordConstructorCanonicalParameterAnnotatedTypeUse;
import org.wildfly.experimental.api.classpath.index.java17.classes.usage.annotation.typeuse.TypeRecordConstructorCompactParameterAnnotatedTypeUse;
import org.wildfly.experimental.api.classpath.index.java17.classes.usage.annotation.typeuse.TypeRecordConstructorParameterAnnotatedTypeUse;
import org.wildfly.experimental.api.classpath.index.java17.classes.usage.annotation.typeuse.TypeRecordGetterReturnTypeUse;
import org.wildfly.experimental.api.classpath.index.java17.classes.usage.annotation.typeuse.TypeRecordMethodParameterAnnotatedTypeUse;
import org.wildfly.experimental.api.classpath.index.java17.classes.usage.annotation.typeuse.TypeRecordMethodReturnAnnotatedTypeUse;
import org.wildfly.unstable.api.annotation.classpath.index.OverallIndex;
import org.wildfly.unstable.api.annotation.classpath.index.RuntimeIndex;
import org.wildfly.unstable.api.annotation.classpath.runtime.bytecode.AnnotationOnUserClassUsage;
import org.wildfly.unstable.api.annotation.classpath.runtime.bytecode.AnnotationOnUserMethodUsage;
import org.wildfly.unstable.api.annotation.classpath.runtime.bytecode.AnnotationUsage;
import org.wildfly.unstable.api.annotation.classpath.runtime.bytecode.AnnotationUsageType;
import org.wildfly.unstable.api.annotation.classpath.runtime.bytecode.ClassInfoScanner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static org.wildfly.unstable.api.annotation.classpath.runtime.bytecode.AnnotationUsageType.ANNOTATED_USER_CLASS;
import static org.wildfly.unstable.api.annotation.classpath.runtime.bytecode.AnnotationUsageType.ANNOTATED_USER_METHOD;

public class TypeUseRecordRuntimeJandexTestCase {

    private static final String EXPERIMENTAL_ANNOTATION = ExperimentalTypeUse.class.getName();
    RuntimeIndex runtimeIndex;
    @Before
    public void createRuntimeIndex() throws IOException {
        OverallIndex overallIndex = new OverallIndex();
        File file = TestUtils.createJar(
                AnnotationWithExperimentalTypeUse.class);
        overallIndex.scanJar(file, EXPERIMENTAL_ANNOTATION, Collections.emptySet());

        Path p = Paths.get("target/index/runtime-test");
        overallIndex.save(p);

        runtimeIndex = RuntimeIndex.load(p);
    }

    @Test
    public void testNoUsage() throws Exception {
        ClassInfoScanner scanner = new ClassInfoScanner(runtimeIndex);
        boolean ok = checkJandex(scanner, RecordNoUsage.class);
        Assert.assertTrue(ok);
        Assert.assertEquals(0, scanner.getUsages().size());
    }

    @Test
    public void testRecordAnnotationUsage() throws Exception {
        AnnotationOnUserClassUsage usage =
                checkJandexAndGetSingleAnnotationUsage(RecordAnnotatedTypeUse.class, ANNOTATED_USER_CLASS)
                        .asAnnotationOnUserClassUsage();

        Assert.assertEquals(1, usage.getAnnotations().size());
        Assert.assertTrue(usage.getAnnotations().contains(AnnotationWithExperimentalTypeUse.class.getName()));
        Assert.assertEquals(RecordAnnotatedTypeUse.class.getName(), usage.getClazz());
    }

    @Test
    public void testRecordConstructorCanonicalAnnotationUsage() throws Exception {
        AnnotationOnUserMethodUsage usage =
                checkJandexAndGetSingleAnnotationUsage(RecordConstructorCanonicalAnnotatedTypeUse.class, ANNOTATED_USER_METHOD)
                        .asAnnotationOnUserMethodUsage();

        Assert.assertEquals(1, usage.getAnnotations().size());
        Assert.assertTrue(usage.getAnnotations().contains(AnnotationWithExperimentalTypeUse.class.getName()));
        Assert.assertEquals(RecordConstructorCanonicalAnnotatedTypeUse.class.getName(), usage.getClazz());
        Assert.assertEquals(RuntimeIndex.BYTECODE_CONSTRUCTOR_NAME, usage.getMethodName());
        Assert.assertEquals("()V", usage.getDescriptor());
    }


    @Test
    public void testRecordConstructorCompactAnnotationUsage() throws Exception {
        AnnotationOnUserMethodUsage usage =
                checkJandexAndGetSingleAnnotationUsage(RecordConstructorCompactAnnotatedTypeUse.class, ANNOTATED_USER_METHOD)
                        .asAnnotationOnUserMethodUsage();

        Assert.assertEquals(1, usage.getAnnotations().size());
        Assert.assertTrue(usage.getAnnotations().contains(AnnotationWithExperimentalTypeUse.class.getName()));
        Assert.assertEquals(RecordConstructorCompactAnnotatedTypeUse.class.getName(), usage.getClazz());
        Assert.assertEquals(RuntimeIndex.BYTECODE_CONSTRUCTOR_NAME, usage.getMethodName());
        Assert.assertEquals("()V", usage.getDescriptor());
    }

    @Test
    public void testRecordConstructorParameterAnnotationUsage() throws Exception {
        AnnotationOnUserMethodUsage usage =
                checkJandexAndGetSingleAnnotationUsage(RecordConstructorParameterAnnotatedTypeUse.class, ANNOTATED_USER_METHOD)
                        .asAnnotationOnUserMethodUsage();

        Assert.assertEquals(1, usage.getAnnotations().size());
        Assert.assertTrue(usage.getAnnotations().contains(AnnotationWithExperimentalTypeUse.class.getName()));
        Assert.assertEquals(RecordConstructorParameterAnnotatedTypeUse.class.getName(), usage.getClazz());
        Assert.assertEquals(RuntimeIndex.BYTECODE_CONSTRUCTOR_NAME, usage.getMethodName());
        Assert.assertEquals("(I)V", usage.getDescriptor());
    }

    @Test
    public void testRecordConstructorCanonicalParameterAnnotationUsage() throws Exception {
        AnnotationOnUserMethodUsage usage =
                checkJandexAndGetSingleAnnotationUsage(RecordConstructorCanonicalParameterAnnotatedTypeUse.class, ANNOTATED_USER_METHOD)
                        .asAnnotationOnUserMethodUsage();

        Assert.assertEquals(1, usage.getAnnotations().size());
        Assert.assertTrue(usage.getAnnotations().contains(AnnotationWithExperimentalTypeUse.class.getName()));
        Assert.assertEquals(RecordConstructorCanonicalParameterAnnotatedTypeUse.class.getName(), usage.getClazz());
        Assert.assertEquals(RuntimeIndex.BYTECODE_CONSTRUCTOR_NAME, usage.getMethodName());
        Assert.assertEquals("(I)V", usage.getDescriptor());
    }

    @Test
    public void testRecordConstructorCompactParameterAnnotationUsage() throws Exception {
        AnnotationOnUserMethodUsage usage =
                checkJandexAndGetSingleAnnotationUsage(RecordConstructorCompactParameterAnnotatedTypeUse.class, ANNOTATED_USER_METHOD)
                        .asAnnotationOnUserMethodUsage();

        Assert.assertEquals(1, usage.getAnnotations().size());
        Assert.assertTrue(usage.getAnnotations().contains(AnnotationWithExperimentalTypeUse.class.getName()));
        Assert.assertEquals(RecordConstructorCompactParameterAnnotatedTypeUse.class.getName(), usage.getClazz());
        Assert.assertEquals(RuntimeIndex.BYTECODE_CONSTRUCTOR_NAME, usage.getMethodName());
        Assert.assertEquals("(I)V", usage.getDescriptor());
    }

    @Test
    public void testRecordGetterAnnotationUsage() throws Exception {
        AnnotationOnUserMethodUsage usage =
                checkJandexAndGetSingleAnnotationUsage(RecordGetterAnnotatedTypeUse.class, ANNOTATED_USER_METHOD)
                        .asAnnotationOnUserMethodUsage();

        Assert.assertEquals(1, usage.getAnnotations().size());
        Assert.assertTrue(usage.getAnnotations().contains(AnnotationWithExperimentalTypeUse.class.getName()));
        Assert.assertEquals(RecordGetterAnnotatedTypeUse.class.getName(), usage.getClazz());
        Assert.assertEquals("i", usage.getMethodName());
        Assert.assertEquals("()I", usage.getDescriptor());
    }

    @Test
    public void testRecordMethodAnnotationUsage() throws Exception {
        AnnotationOnUserMethodUsage usage =
                checkJandexAndGetSingleAnnotationUsage(RecordMethodAnnotatedTypeUse.class, ANNOTATED_USER_METHOD)
                        .asAnnotationOnUserMethodUsage();

        Assert.assertEquals(1, usage.getAnnotations().size());
        Assert.assertTrue(usage.getAnnotations().contains(AnnotationWithExperimentalTypeUse.class.getName()));
        Assert.assertEquals(RecordMethodAnnotatedTypeUse.class.getName(), usage.getClazz());
        Assert.assertEquals("test", usage.getMethodName());
        Assert.assertEquals("()V", usage.getDescriptor());
    }


    @Test
    public void testRecordMethodParameterAnnotationUsage() throws Exception {
        AnnotationOnUserMethodUsage usage =
                checkJandexAndGetSingleAnnotationUsage(RecordMethodParameterAnnotatedTypeUse.class, ANNOTATED_USER_METHOD)
                        .asAnnotationOnUserMethodUsage();

        Assert.assertEquals(1, usage.getAnnotations().size());
        Assert.assertTrue(usage.getAnnotations().contains(AnnotationWithExperimentalTypeUse.class.getName()));
        Assert.assertEquals(RecordMethodParameterAnnotatedTypeUse.class.getName(), usage.getClazz());
        Assert.assertEquals("test", usage.getMethodName());
        Assert.assertEquals("(Ljava/lang/String;)V", usage.getDescriptor());
    }


    @Ignore("When happening through a type, we don't get the RECORD_COMPONENT entry so we can't trim the entries")
    @Test
    public void testTypeRecordConstructorParameterAnnotationUsage() throws Exception {
        AnnotationOnUserMethodUsage usage =
                checkJandexAndGetSingleAnnotationUsage(TypeRecordConstructorParameterAnnotatedTypeUse.class, ANNOTATED_USER_METHOD)
                        .asAnnotationOnUserMethodUsage();

        Assert.assertEquals(1, usage.getAnnotations().size());
        Assert.assertTrue(usage.getAnnotations().contains(AnnotationWithExperimentalTypeUse.class.getName()));
        Assert.assertEquals(TypeRecordConstructorParameterAnnotatedTypeUse.class.getName(), usage.getClazz());
        Assert.assertEquals(RuntimeIndex.BYTECODE_CONSTRUCTOR_NAME, usage.getMethodName());
        Assert.assertEquals("(Ljava/util/List;)V", usage.getDescriptor());
    }

    @Test
    public void testTypeRecordConstructorCanonicalParameterAnnotationUsage() throws Exception {
        AnnotationOnUserMethodUsage usage =
                checkJandexAndGetSingleAnnotationUsage(TypeRecordConstructorCanonicalParameterAnnotatedTypeUse.class, ANNOTATED_USER_METHOD)
                        .asAnnotationOnUserMethodUsage();

        Assert.assertEquals(1, usage.getAnnotations().size());
        Assert.assertTrue(usage.getAnnotations().contains(AnnotationWithExperimentalTypeUse.class.getName()));
        Assert.assertEquals(TypeRecordConstructorCanonicalParameterAnnotatedTypeUse.class.getName(), usage.getClazz());
        Assert.assertEquals(RuntimeIndex.BYTECODE_CONSTRUCTOR_NAME, usage.getMethodName());
        Assert.assertEquals("(Ljava/util/List;)V", usage.getDescriptor());
    }

    @Ignore("When happening through a type, we don't get the RECORD_COMPONENT entry so we can't trim the entries")
    @Test
    public void testTypeRecordConstructorCompactParameterAnnotationUsage() throws Exception {
        AnnotationOnUserMethodUsage usage =
                checkJandexAndGetSingleAnnotationUsage(TypeRecordConstructorCompactParameterAnnotatedTypeUse.class, ANNOTATED_USER_METHOD)
                        .asAnnotationOnUserMethodUsage();

        Assert.assertEquals(1, usage.getAnnotations().size());
        Assert.assertTrue(usage.getAnnotations().contains(AnnotationWithExperimentalTypeUse.class.getName()));
        Assert.assertEquals(TypeRecordConstructorCanonicalParameterAnnotatedTypeUse.class.getName(), usage.getClazz());
        Assert.assertEquals(RuntimeIndex.BYTECODE_CONSTRUCTOR_NAME, usage.getMethodName());
        Assert.assertEquals("(Ljava/util/List;)V", usage.getDescriptor());
    }

    @Test
    public void testTypeRecordGetterReturnTypeAnnotationUsage() throws Exception {
        AnnotationOnUserMethodUsage usage =
                checkJandexAndGetSingleAnnotationUsage(TypeRecordGetterReturnTypeUse.class, ANNOTATED_USER_METHOD)
                        .asAnnotationOnUserMethodUsage();

        Assert.assertEquals(1, usage.getAnnotations().size());
        Assert.assertTrue(usage.getAnnotations().contains(AnnotationWithExperimentalTypeUse.class.getName()));
        Assert.assertEquals(TypeRecordGetterReturnTypeUse.class.getName(), usage.getClazz());
        Assert.assertEquals("i", usage.getMethodName());
        Assert.assertEquals("()Ljava/util/List;", usage.getDescriptor());
    }

    @Test
    public void testTypeRecordGetterMethodParameterAnnotationUsage() throws Exception {
        AnnotationOnUserMethodUsage usage =
                checkJandexAndGetSingleAnnotationUsage(TypeRecordMethodParameterAnnotatedTypeUse.class, ANNOTATED_USER_METHOD)
                        .asAnnotationOnUserMethodUsage();

        Assert.assertEquals(1, usage.getAnnotations().size());
        Assert.assertTrue(usage.getAnnotations().contains(AnnotationWithExperimentalTypeUse.class.getName()));
        Assert.assertEquals(TypeRecordMethodParameterAnnotatedTypeUse.class.getName(), usage.getClazz());
        Assert.assertEquals("test", usage.getMethodName());
        Assert.assertEquals("(Ljava/util/List;)V", usage.getDescriptor());
    }

    @Test
    public void testTypeRecordGetterMethodReturnTypeAnnotationUsage() throws Exception {
        AnnotationOnUserMethodUsage usage =
                checkJandexAndGetSingleAnnotationUsage(TypeRecordMethodReturnAnnotatedTypeUse.class, ANNOTATED_USER_METHOD)
                        .asAnnotationOnUserMethodUsage();

        Assert.assertEquals(1, usage.getAnnotations().size());
        Assert.assertTrue(usage.getAnnotations().contains(AnnotationWithExperimentalTypeUse.class.getName()));
        Assert.assertEquals(TypeRecordMethodReturnAnnotatedTypeUse.class.getName(), usage.getClazz());
        Assert.assertEquals("test", usage.getMethodName());
        Assert.assertEquals("()Ljava/util/List;", usage.getDescriptor());
    }


    AnnotationUsage checkJandexAndGetSingleAnnotationUsage(
            Class<?> clazz, AnnotationUsageType type) throws IOException {
        ClassInfoScanner scanner = new ClassInfoScanner(runtimeIndex);
        scanClass(scanner, clazz);
        checkJandex(scanner, clazz);
        Assert.assertEquals(1, scanner.getUsages().size());
        AnnotationUsage usage = scanner.getUsages().iterator().next();
        Assert.assertEquals(type, usage.getType());
        return usage;
    }

    private void scanClass(ClassInfoScanner scanner, Class<?> clazz) throws IOException {
        String classLocation = clazz.getName().replaceAll("\\.", "/") + ".class";
        URL url = TypeUseRecordRuntimeJandexTestCase.class.getClassLoader().getResource(classLocation);
        try (InputStream in = url.openStream()) {
            scanner.scanClass(in);
        }
    }

    private boolean checkJandex(ClassInfoScanner inspector, Class<?> clazz) throws IOException {
        Index index = Index.of(clazz);
        return inspector.checkAnnotationIndex(annotationName -> index.getAnnotations(annotationName));
    }


}
