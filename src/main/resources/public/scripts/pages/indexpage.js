import { showAlert } from "../common/alerts.js";
import { apiRequest } from "../common/apiClient.js";
import { API_BASE_URL } from "../common/constants.js";
import { DataGrid } from "../components/datagrid.js";

const purchaseTypeId = 1
const positionTypeId = 2;
const electroTypeId = 5;

export async function initializePage() {
    const form = document.getElementById('importDataForm');
    form.addEventListener('submit', importData);

    await getRevenueByCash();
    await getBestJuniorSaler();
    await getBestEmployeesByTypeSales();
    await getBestEmployeesByTypeAmount();
}

async function importData(e) {
    e.preventDefault();
    const fileInput = document.getElementById('import-zip-file');
    const file = fileInput.files[0];

    if (!file) {
        showAlert('Пожалуйста, выберите файл');
        return;
    }

    const formData = new FormData();
        formData.append('file', file);

        try {
            await apiRequest(`${API_BASE_URL}/import/zip-with-csv`, {
                method: 'POST',
                body: formData,
            });

            showAlert('Импорт успешно завершен, страница перезагрузится автоматически через 3 сек.', 'success');
            setTimeout(() => {
                window.location.reload();
            }, 3000)
        } catch (error) {
            console.error('Ошибка:', error);
            showAlert('Импорт не удалось выполнить');
        }
}

async function getRevenueByCash() {
    const data = await apiRequest(`${API_BASE_URL}/statistic/amount-of-funds-received-through?purchaseTypeId=${purchaseTypeId}`);
    const block = document.getElementById('revenueByCash');
    block.textContent = `- Cумма денежных средств, полученной магазином через оплату способом "${data.purchaseName}": ${data.amount}₽`
}

async function getBestJuniorSaler() {
    const data = await apiRequest(`${API_BASE_URL}/statistic/best-employee-position-type-sold?positionTypeId=${positionTypeId}&itemId=${electroTypeId}`);
    if (!data) {
        return;
    }
    const block = document.getElementById('bestJuniorSoldSmartWatches');
    block.textContent = `- Лучший ${data.positionName}, продавший больше всех ${itemName}: ${data.firstName} ${data.lastName} (#${data.id}) - ${data.itemsSold} шт.`
}

async function getBestEmployeesByTypeSales() {
    let data = await apiRequest(`${API_BASE_URL}/statistic/best-employees-by-position-type?criteria=sales`);

    data = data.map((elem) => ({
        ...elem,
        employeeName: `${elem.firstName} ${elem.lastName} ${elem.patronymic}`,
        amountItemsSold: '',
    }));

    const modelModule = await import(`../models/BestEmployeeyCriteria.js`);
    const model = modelModule.default;
    const { modelInfo } = modelModule;

    const about = document.createElement('strong');
    about.textContent = 'Лучшие сотрудники в зависимости от занимаемой должности по критерию количества проданных товаров за последний год.'

    const dataGrid = new DataGrid(model, data, modelInfo, { sortable: false });

    const dataGridDiv =  document.getElementById('dataGridContainerSales');
    dataGridDiv.appendChild(about);
    dataGridDiv.appendChild(dataGrid.render());
}

async function getBestEmployeesByTypeAmount() {
    let data = await apiRequest(`${API_BASE_URL}/statistic/best-employees-by-position-type?criteria=revenue`);

    data = data.map((elem) => ({
        ...elem,
        employeeName: `${elem.firstName} ${elem.lastName} ${elem.patronymic}`,
        numberItemsSold: ''
    }));

    const modelModule = await import(`../models/BestEmployeeyCriteria.js`);
    const model = modelModule.default;
    const { modelInfo } = modelModule;

    model.numberItemsSold;

    const dataGrid = new DataGrid(model, data, modelInfo, { sortable: false });
    const about = document.createElement('strong');
    about.textContent = 'Лучшие сотрудники в зависимости от занимаемой должности по суммы количества проданных товаров за последний год.'

    const dataGridDiv =  document.getElementById('dataGridContainerRevenue');
    dataGridDiv.appendChild(about);
    dataGridDiv.appendChild(dataGrid.render());
}