package com.foolchen.arch.compiler;

import com.foolchen.arch.annotations.WeChatEntryGenerator;
import com.google.auto.service.AutoService;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * 该类为注解处理器,用于解释项目中定义的注解
 *
 * 使用了Google的auto-service工具,不需要手动编写META-INF等
 */
@AutoService(Processor.class) public class ArchProcessor extends AbstractProcessor {

  @Override public Set<String> getSupportedAnnotationTypes() {
    final Set<String> types = new HashSet<>();
    types.add(WeChatEntryGenerator.class.getCanonicalName());
    return types;
  }

  @Override public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
    System.out.println("ArchProcessor执行了");
    generateWeChatEntry(env);
    return true;
  }

  private void generateWeChatEntry(RoundEnvironment env) {
    final WeChatEntryVisitor visitor = new WeChatEntryVisitor(processingEnv.getFiler());

    scan(env, WeChatEntryGenerator.class, visitor);
  }

  private void scan(RoundEnvironment env, Class<? extends Annotation> annotation,
      AnnotationValueVisitor visitor) {
    Set<? extends Element> typeElements = env.getElementsAnnotatedWith(annotation);
    for (Element typeElement : typeElements) {
      List<? extends AnnotationMirror> annotationMirrors = typeElement.getAnnotationMirrors();
      for (AnnotationMirror annotationMirror : annotationMirrors) {
        Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues =
            annotationMirror.getElementValues();

        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : elementValues
            .entrySet()) {
          entry.getValue().accept(visitor, null);
        }
      }
    }
  }
}
