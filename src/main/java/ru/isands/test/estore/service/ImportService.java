package ru.isands.test.estore.service;

import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.isands.test.estore.model.*;
import ru.isands.test.estore.repository.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class ImportService {

    private final PurchaseRepository purchaseRepository;
    private final ShopRepository shopRepository;
    private final PurchaseTypeRepository purchaseTypeRepository;
    private final EmployeeRepository employeeRepository;
    private final ElectroItemRepository electroItemRepository;
    private final ElectroShopRepository electroShopRepository;
    private final PositionTypeRepository positionTypeRepository;
    private final ElectroTypeRepository electroTypeRepository;
    private final DatabaseTruncationService databaseTruncationService;

    public ImportService(PurchaseRepository purchaseRepository, ShopRepository shopRepository, PurchaseTypeRepository purchaseTypeRepository, EmployeeRepository employeeRepository, ElectroItemRepository electroItemRepository, ElectroShopRepository electroShopRepository, ModelMapper modelMapper, PositionTypeRepository positionTypeRepository, ElectroTypeRepository electroTypeRepository, DatabaseTruncationService databaseTruncationService) {
        this.purchaseRepository = purchaseRepository;
        this.shopRepository = shopRepository;
        this.purchaseTypeRepository = purchaseTypeRepository;
        this.employeeRepository = employeeRepository;
        this.electroItemRepository = electroItemRepository;
        this.electroShopRepository = electroShopRepository;
        this.positionTypeRepository = positionTypeRepository;
        this.electroTypeRepository = electroTypeRepository;
        this.databaseTruncationService = databaseTruncationService;
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void importDataFromZipWithCsv(MultipartFile zip) {
        Map<String, List<String>> entityMap = new HashMap<>();

        try (ZipInputStream zis = new ZipInputStream(zip.getInputStream())) {
            for (ZipEntry entry; (entry = zis.getNextEntry()) != null;) {
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(zis.readAllBytes()), Charset.forName("windows-1251")))) {
                    entityMap.put(entry.getName(), bufferedReader.lines().collect(Collectors.toList()));
                }
                zis.closeEntry();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Shop> shops = readEntityFromCsvLines(entityMap.get("Shop.csv"), 3, this::readShop);
        shopRepository.saveAll(shops);

        List<PurchaseType> purchaseTypes = readEntityFromCsvLines(entityMap.get("PurchaseType.csv"), 2, this::readPurchaseType);
        purchaseTypeRepository.saveAll(purchaseTypes);

        List<PositionType> positionTypes = readEntityFromCsvLines(entityMap.get("PositionType.csv"), 2, this::readPositionType);
        positionTypeRepository.saveAll(positionTypes);

        List<ElectroType> electroTypes = readEntityFromCsvLines(entityMap.get("ElectroType.csv"), 2, this::readElectroType);
        electroTypeRepository.saveAll(electroTypes);

        List<Employee> employees = readEntityFromCsvLines(entityMap.get("Employee.csv"), 8, this::readEmployee);
        employeeRepository.saveAll(employees);

        List<Employee> electroEmployee = readEntityFromCsvLines(entityMap.get("ElectroEmployee.csv"), 2, this::readElectroEmployee);
        employeeRepository.saveAll(electroEmployee);

        List<ElectroItem> electroItems = readEntityFromCsvLines(entityMap.get("ElectroItem.csv"), 7, this::readElectroItem);
        electroItemRepository.saveAll(electroItems);

        List<ElectroShop> electroShops = readEntityFromCsvLines(entityMap.get("ElectroShop.csv"), 3, this::readElectroShop);
        electroShopRepository.saveAll(electroShops);

        List<Purchase> purchases = readEntityFromCsvLines(entityMap.get("Purchase.csv"), 6, this::readPurchase);
        purchaseRepository.saveAll(purchases);
    }

    private <T> List<T> readEntityFromCsvLines(List<String> lines, int colCount, Function<String[], T> callback) {
        return lines.stream()
                .skip(1)
                .map(line -> {
                    String[] itemsInfo = line.split(";");
                    if (itemsInfo.length != colCount) {
                        return null;
                    }

                    return callback.apply(itemsInfo);
                })
                .collect(Collectors.toList());
    }

    private Shop readShop(String[] itemsInfo) {
        Shop shop = new Shop();
        shop.setId(Long.valueOf(itemsInfo[0]));
        shop.setName(itemsInfo[1]);
        shop.setAddress(itemsInfo[2]);
        return shop;
    }

    private PurchaseType readPurchaseType(String[] itemsInfo) {
        PurchaseType purchaseType = new PurchaseType();
        purchaseType.setId(Long.valueOf(itemsInfo[0]));
        purchaseType.setName(itemsInfo[1]);
        return purchaseType;
    }

    private PositionType readPositionType(String[] itemsInfo) {
        PositionType positionType = new PositionType();
        positionType.setId(Long.valueOf(itemsInfo[0]));
        positionType.setName(itemsInfo[1]);
        return positionType;
    }

    private ElectroType readElectroType(String[] itemsInfo) {
        ElectroType electroType = new ElectroType();
        electroType.setId(Long.valueOf(itemsInfo[0]));
        electroType.setName(itemsInfo[1]);
        return electroType;
    }

    @SneakyThrows
    private Employee readEmployee(String[] itemsInfo) {
        Employee employee = new Employee();
        employee.setId(Long.valueOf(itemsInfo[0]));
        employee.setLastName(itemsInfo[1]);
        employee.setFirstName(itemsInfo[2]);
        employee.setPatronymic(itemsInfo[3]);
        employee.setBirthDate(new SimpleDateFormat("dd.MM.yy").parse(itemsInfo[4]));
        employee.setPositionType(positionTypeRepository.getReferenceById(Long.valueOf(itemsInfo[5])));
        employee.setShop(shopRepository.getReferenceById(Long.valueOf(itemsInfo[6])));
        employee.setGender(itemsInfo[7].equals("1"));
        return employee;
    }

    private Employee readElectroEmployee(String[] itemsInfo) {
        Employee employee = employeeRepository.findById(Long.valueOf(itemsInfo[0])).orElseThrow();
        if (employee.getElectroTypes() == null) {
            employee.setElectroTypes(new HashSet<>());
        }
        employee.getElectroTypes().add(electroTypeRepository.getReferenceById(Long.valueOf(itemsInfo[1])));
        return employee;
    }

    private ElectroItem readElectroItem(String[] itemsInfo) {
        ElectroItem electroItem = new ElectroItem();
        electroItem.setId(Long.valueOf(itemsInfo[0]));
        electroItem.setName(itemsInfo[1]);
        electroItem.setElectroType(electroTypeRepository.getReferenceById(Long.valueOf(itemsInfo[2])));
        electroItem.setPrice(Long.valueOf(itemsInfo[3]));
        electroItem.setTotalCount(Integer.parseInt(itemsInfo[4]));
        electroItem.setArchive(Boolean.parseBoolean(itemsInfo[5]));
        electroItem.setDescription(itemsInfo[6]);
        return electroItem;
    }

    private ElectroShop readElectroShop(String[] itemsInfo) {
        ElectroShop electroShop = new ElectroShop();
        electroShop.setShopId(Long.valueOf(itemsInfo[0]));
        electroShop.setElectroItemId(Long.valueOf(itemsInfo[1]));
        electroShop.setCount(Integer.parseInt(itemsInfo[2]));
        return electroShop;
    }

    @SneakyThrows
    private Purchase readPurchase(String[] itemsInfo) {
        Purchase purchase = new Purchase();
        purchase.setId(Long.valueOf(itemsInfo[0]));
        purchase.setElectro(electroItemRepository.getReferenceById(Long.valueOf(itemsInfo[1])));
        purchase.setEmployee(employeeRepository.getReferenceById(Long.valueOf(itemsInfo[2])));
        purchase.setPurchaseDate(new SimpleDateFormat("dd.MM.yy hh:mm").parse(itemsInfo[3]));
        purchase.setType(purchaseTypeRepository.getReferenceById(Long.valueOf(itemsInfo[4])));
        purchase.setShop(shopRepository.getReferenceById(Long.valueOf(itemsInfo[5])));
        return purchase;
    }
}
