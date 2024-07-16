package ru.isands.test.estore.repository;

import org.springframework.stereotype.Repository;
import ru.isands.test.estore.model.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class StatisticRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> getTopEmployeesBySalesAndRevenue() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);

        Root<Purchase> purchaseRoot = query.from(Purchase.class);
        Join<Purchase, Employee> employeeJoin = purchaseRoot.join("employee");
        Join<Employee, PositionType> positionJoin = employeeJoin.join("positionType");

        // Берем текущий год и отнимаем 1 - получаем предыдущий год.
        // Не совсем понял, за какой год нужно брать статистику, поэтому взял предыдущий
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date oneYearAgo = calendar.getTime();

        query.multiselect(
                positionJoin.get("name"),
                employeeJoin.get("id"),
                employeeJoin.get("firstName"),
                employeeJoin.get("lastName"),
                employeeJoin.get("patronymic"),
                builder.count(purchaseRoot.get("id")).alias("itemsSold"),
                builder.sum(purchaseRoot.get("electro").get("price")).alias("revenue")
        );

        query.groupBy(
                positionJoin.get("name"),
                employeeJoin.get("id"),
                employeeJoin.get("firstName"),
                employeeJoin.get("lastName")
        );


        query.where(builder.greaterThanOrEqualTo(purchaseRoot.get("purchaseDate"), oneYearAgo));

        query.orderBy(
                builder.desc(builder.count(purchaseRoot.get("id"))),
                builder.desc(builder.sum(purchaseRoot.get("electro").get("price")))
        );

        return entityManager.createQuery(query).getResultList();
    }

    public List<Object[]> getTopEmployeeBySalesOfItemId(Long positionTypeId, Long itemId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);

        Root<Purchase> purchaseRoot = query.from(Purchase.class);
        Join<Purchase, Employee> employeeJoin = purchaseRoot.join("employee");
        Join<Employee, PositionType> positionTypeJoin = employeeJoin.join("positionType");
        Join<Purchase, ElectroItem> electroItemJoin = purchaseRoot.join("electro");
        Join<ElectroItem, ElectroType> electroTypeJoin = electroItemJoin.join("electroType");

        query.multiselect(
                employeeJoin.get("id"),
                employeeJoin.get("firstName"),
                employeeJoin.get("lastName"),
                employeeJoin.get("patronymic"),
                positionTypeJoin.get("name").alias("positionName"),
                electroTypeJoin.get("name").alias("electroTypeName"),
                builder.count(purchaseRoot.get("id")).alias("itemsSold")
        );

        query.groupBy(
                positionTypeJoin.get("name"),
                electroTypeJoin.get("name"),
                employeeJoin.get("id"),
                employeeJoin.get("firstName"),
                employeeJoin.get("lastName")
        );

        query.where(builder.and(
                builder.equal(positionTypeJoin.get("id"), positionTypeId),
                builder.equal(electroTypeJoin.get("id"), itemId)
        ));

        query.orderBy(
                builder.desc(builder.count(purchaseRoot.get("id")))
        );

        return entityManager.createQuery(query).getResultList();
    }

    public Object[] calculateAmountOfFundsReceivedThrough(Long purchaseTypeId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);

        Root<Purchase> purchaseRoot = query.from(Purchase.class);
        Join<Purchase, PurchaseType> purchaseTypeJoin = purchaseRoot.join("type");
        Join<Purchase, ElectroItem> electroItemJoin = purchaseRoot.join("electro");

        query.multiselect(
                builder.sum(electroItemJoin.get("price")).alias("amount"),
                purchaseTypeJoin.get("name").alias("purchaseTypeName")
        );

        query.where(builder.equal(purchaseRoot.get("type").get("id"), purchaseTypeId));

        query.groupBy(purchaseTypeJoin.get("name"));

        return entityManager.createQuery(query).getSingleResult();
    }
}
