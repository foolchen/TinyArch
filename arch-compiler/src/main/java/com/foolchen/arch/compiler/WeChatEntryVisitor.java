package com.foolchen.arch.compiler;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor7;

/**
 * @author chenchong
 * 2018/7/12
 * 上午11:42
 */
public class WeChatEntryVisitor extends SimpleAnnotationValueVisitor7<Void, Void> {
  private final Filer mFiler;
  private String mPackageName;

  public WeChatEntryVisitor(Filer filer) {
    this.mFiler = filer;
  }

  @Override public Void visitString(String s, Void aVoid) {
    mPackageName = s;
    return aVoid;
  }

  @Override public Void visitType(TypeMirror typeMirror, Void aVoid) {
    generateJavaCode(typeMirror);
    return aVoid;
  }

  private void generateJavaCode(TypeMirror mirror) {
    final TypeSpec targetActivity = TypeSpec.classBuilder("WXEntryActivity")
        .addModifiers(Modifier.PUBLIC)
        .addModifiers(Modifier.FINAL)
        .superclass(TypeName.get(mirror))
        .build();

    final JavaFile javaFile =
        JavaFile.builder(mPackageName + ".wxapi", targetActivity).addFileComment("微信入口文件").build();

    try {
      javaFile.writeTo(mFiler);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
