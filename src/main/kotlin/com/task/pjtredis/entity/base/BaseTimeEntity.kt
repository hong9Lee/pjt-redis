package com.task.pjtredis.entity.base

import com.task.pjtredis.support.ZonedDateTimeUtils
import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

@MappedSuperclass
abstract class BaseTimeEntity(
    @Column(name = "reg_date_time", nullable = false, updatable = false)
    open var regDateTime: ZonedDateTime? = null,

    @Column(name = "mod_date_time", nullable = false)
    private var modDateTime: ZonedDateTime? = null,
) : BaseEntity() {
    @PrePersist
    fun prePersist() {
        val now: ZonedDateTime = ZonedDateTimeUtils.nowOfSeoul()
            .truncatedTo(ChronoUnit.MICROS)
        regDateTime = now
        modDateTime = now
    }

    @PreUpdate
    fun preUpdate() {
        modDateTime = ZonedDateTimeUtils.nowOfSeoul()
            .truncatedTo(ChronoUnit.MICROS)
    }
}
