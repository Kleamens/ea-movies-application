package cz.mendelu.ea.xzirchuk.project.moviesAPI.utility

import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.DirectorContoller
import groovy.sql.Sql
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator

import javax.sql.DataSource
import java.sql.Statement

class Utils{


    def static  execPopulator(DataSourceBuilder builder,
                              String databaseUrl,
                              String userName,
                              String password,
                              ResourceDatabasePopulator populator,
                              String errorMessage){
        Logger logger = LoggerFactory.getLogger(DirectorContoller.class);
        DataSource source = builder.url(databaseUrl)
                .username(userName)
                .password(password)
                .build();
        try {
            populator.execute(source)
        } catch (javax.script.ScriptException e) {
            logger.error(errorMessage)
        }
    }

}