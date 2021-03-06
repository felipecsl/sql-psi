package com.alecstrong.sql.psi.core.psi.mixins

import com.alecstrong.sql.psi.core.SqlAnnotationHolder
import com.alecstrong.sql.psi.core.psi.SqlCompositeElementImpl
import com.alecstrong.sql.psi.core.psi.SqlDropIndexStmt
import com.intellij.lang.ASTNode

internal abstract class DropIndexMixin(
  node: ASTNode
) : SqlCompositeElementImpl(node),
    SqlDropIndexStmt {
  override fun annotate(annotationHolder: SqlAnnotationHolder) {
    indexName?.let { indexName ->
      if (containingFile.indexes(this).none { it != this && it.indexName.text == indexName.text }) {
        annotationHolder.createErrorAnnotation(indexName, "No index found with name ${indexName.text}")
      }
    }

    super.annotate(annotationHolder)
  }
}
