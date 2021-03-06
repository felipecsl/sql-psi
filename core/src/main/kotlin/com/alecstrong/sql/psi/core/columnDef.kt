package com.alecstrong.sql.psi.core

import com.alecstrong.sql.psi.core.postgresql.psi.PostgreSqlBigSerialDataType
import com.alecstrong.sql.psi.core.postgresql.psi.PostgreSqlSerialDataType
import com.alecstrong.sql.psi.core.postgresql.psi.PostgreSqlSmallSerialDataType
import com.alecstrong.sql.psi.core.psi.SqlColumnDef
import com.alecstrong.sql.psi.core.psi.SqlTypes

fun SqlColumnDef.hasDefaultValue(): Boolean {
  return columnConstraintList.any {
    it.defaultConstraint != null ||
        it.node.findChildByType(SqlTypes.AUTOINCREMENT) != null || isSerial()
  } || columnConstraintList.none {
    it.node.findChildByType(SqlTypes.NOT) != null
  } || (
      // An INTEGER PRIMARY KEY is still considered to have a default value, even without specifying AUTOINCREMENT:
      // https://www.sqlite.org/autoinc.html
      // "On an INSERT, if the ROWID or INTEGER PRIMARY KEY column is not explicitly given a value, then it will be
      // filled automatically with an unused integer .. regardless of whether or not the AUTOINCREMENT keyword is used."
      this.typeName.text == "INTEGER" && this.columnConstraintList.any {
        it.node.findChildByType(SqlTypes.PRIMARY) != null
      }
      )
}

private fun SqlColumnDef.isSerial(): Boolean {
  return (this.typeName is PostgreSqlSmallSerialDataType ||
    this.typeName is PostgreSqlSerialDataType ||
    this.typeName is PostgreSqlBigSerialDataType)
}
