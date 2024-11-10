package be.tvw.customer.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import liquibase.Liquibase;
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

public abstract class JpaRepositoryTest<T> {

    private static EntityManager entityManager;
    private static EntityManagerFactory entityManagerFactory;
    protected T repository;
    private boolean activeTransactionBeforeTest;

    @BeforeAll
    static void startDatabase() {
        entityManagerFactory = Persistence.createEntityManagerFactory("customer-test-unit");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    static void stopDatabase() {
        entityManager.close();
        entityManagerFactory.close();
    }

    public JpaRepositoryTest() {
        JpaRepositoryFactory jpaRepositoryFactory = new JpaRepositoryFactory(entityManager);
        repository = getRepository(jpaRepositoryFactory);
    }

    private EntityTransaction getTransaction() {
        return this.entityManager.getTransaction();
    }

    private void beginTransaction() {
        this.activeTransactionBeforeTest = TransactionSynchronizationManager.isActualTransactionActive();
        TransactionSynchronizationManager.setActualTransactionActive(true);
        this.getTransaction().begin();
    }

    @BeforeEach
    void setUp() {
        Session session = (Session) this.entityManager.unwrap(Session.class);
        session.doWork((connection) -> {
            try {
                Liquibase liquibase = new Liquibase("liquibase/master.xml", new ClassLoaderResourceAccessor(), new JdbcConnection(connection));
                liquibase.update("");
            } catch (LiquibaseException var3) {
                LiquibaseException e = var3;
                throw new RuntimeException(e);
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