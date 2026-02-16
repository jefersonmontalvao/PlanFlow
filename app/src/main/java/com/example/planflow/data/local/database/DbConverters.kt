package com.example.planflow.data.local.database

import androidx.room.TypeConverter
import com.example.planflow.domain.models.TransactionType
import java.math.BigDecimal
import java.time.LocalDate

class DbConverters {

    @TypeConverter
    fun fromTransactionType(type: TransactionType): String = type.name

    @TypeConverter
    fun toTransactionType(value: String): TransactionType = TransactionType.valueOf(value)

    @TypeConverter
    fun fromBigDecimal(value: BigDecimal): String = value.toPlainString()

    @TypeConverter
    fun toBigDecimal(bigDecimalString: String): BigDecimal = bigDecimalString.toBigDecimal()

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String = date.toString()

    @TypeConverter
    fun toLocalDate(localDateString: String): LocalDate = LocalDate.parse(localDateString)

}