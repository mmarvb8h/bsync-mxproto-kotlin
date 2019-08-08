package bsync.db.postgres

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database


object Postgres {

    val getConnection = fun() : Database {
        return Database.connect(dataSource())
    }

    val dataSource = fun() : HikariDataSource {

        val config = HikariConfig()
        config.setUsername("postgres")
        // config.setPassword("51mp50n")
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = "jdbc:postgresql://localhost:5433/bsync_proto"
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }
}
