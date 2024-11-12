package be.tvw.customer.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import liquibase.Liquibase;
import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.command.core.helpers.DatabaseChangelogCommandStep;
import liquibase.command.core.helpers.DbUrlConnectionArgumentsCommandStep;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.HashMap;
import java.util.Map;

public abstract class JpaRepositoryTest<T> {

    private static final PostgreSQLContainer DATABASE = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("myDatabase")
            .withUsername("myUsername")
            .withPassword("myPassword");
    private static EntityManager entityManager;
    private static EntityManagerFactory entityManagerFactory;
    protected T repository;
    private boolean activeTransactionBeforeTest;

    @BeforeAll
    static void startDatabase() {
        DATABASE.start();
        Map<String,String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", DATABASE.getJdbcUrl());
        properties.put("javax.persistence.jdbc.user", DATABASE.getUsername());
        properties.put("javax.persistence.jdbc.password", DATABASE.getPassword());
        properties.put("javax.persistence.jdbc.driver", DATABASE.getDriverClassName());

        entityManagerFactory = Persistence.createEntityManagerFactory("customer-test-unit",properties);
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    static void stopDatabase() {
        entityManager.close();
        entityManagerFactory.close();
        DATABASE.stop();
    }

    public JpaRepositoryTest() {
        JpaRepositoryFactory jpaRepositoryFactory = new JpaRepositoryFactory(entityManager);
        repository = getRepository(jpaRepositoryFactory);
    }

    private EntityTransaction getTransaction() {
        return entityManager.getTransaction();
    }

    private void beginTransaction() {
        this.activeTransactionBeforeTest = TransactionSynchronizationManager.isActualTransactionActive();
        TransactionSynchronizationManager.setActualTransactionActive(true);
        this.getTransaction().begin();
    }

    @BeforeEach
    void setUp() {
        Session session = entityManager.unwrap(Session.class);
        session.doWork((connection) -> {
            try {
                Liquibase liquibase = new Liquibase("liquibase/master.xml", new ClassLoaderResourceAccessor(), new JdbcConnection(connection));
                CommandScope updateCommand = new CommandScope(UpdateCommandStep.COMMAND_NAME);
                updateCommand.addArgumentValue(DbUrlConnectionArgumentsCommandStep.DATABASE_ARG, liquibase.getDatabase());
                updateCommand.addArgumentValue(UpdateCommandStep.CHANGELOG_ARG, liquibase.getDatabaseChangeLog());
                updateCommand.addArgumentValue(UpdateCommandStep.CHANGELOG_FILE_ARG, liquibase.getChangeLogFile());
                updateCommand.addArgumentValue(DatabaseChangelogCommandStep.CHANGELOG_PARAMETERS, liquibase.getChangeLogParameters());
                updateCommand.execute();
            } catch (LiquibaseException ex) {
                throw new RuntimeException(ex);
            }
        });
        getTransaction().setRollbackOnly();
        this.beginTransaction();
    }

    @AfterEach
    public void rollBack() {
        this.getTransaction().rollback();
        TransactionSynchronizationManager.setActualTransactionActive(this.activeTransactionBeforeTest);
    }

    protected abstract T getRepository(JpaRepositoryFactory factory);

}
