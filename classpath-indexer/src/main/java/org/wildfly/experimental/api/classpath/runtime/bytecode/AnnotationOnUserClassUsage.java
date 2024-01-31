package org.wildfly.experimental.api.classpath.runtime.bytecode;

import java.util.Objects;
import java.util.Set;

import static org.wildfly.experimental.api.classpath.runtime.bytecode.AnnotationUsageType.ANNOTATED_USER_CLASS;

/**
 * Records a class in user bytecode that has been annotated with one of the annotations
 * we recorded as 'experimental' in the {@link org.wildfly.experimental.api.classpath.index.OverallIndex}
 */
public class AnnotationOnUserClassUsage extends AnnotationUsage {
    private final String clazz;

    AnnotationOnUserClassUsage(String clazz, Set<String> annotations) {
        super(annotations, ANNOTATED_USER_CLASS);
        this.clazz = clazz;
    }

    @Override
    protected AnnotationOnUserClassUsage convertToDotFormat() {
        return this;
    }

    /**
     * Gets the name of the class
     * @return the class name
     */
    public String getClazz() {
        return clazz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AnnotationOnUserClassUsage that = (AnnotationOnUserClassUsage) o;
        return Objects.equals(clazz, that.clazz);
    }

    protected int calculateHash() {
        return Objects.hash(super.calculateHash(), clazz);
    }
}
