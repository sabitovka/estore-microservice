import { showAlert } from '../common/alerts.js';
import { API_BASE_URL } from '../common/constants.js';
import { DataGrid } from '../components/datagrid.js';
import { PagesSlider } from '../components/pages-slider.js';

export async function initializePage() {
    const urlParams = new URLSearchParams(window.location.search);
    const modelName = urlParams.get('model');

    const page = urlParams.get('page');
    const size = urlParams.get('size');
    const sortField = urlParams.get('sortField');
    const sortType = urlParams.get('sortType');

    if (modelName) {
        try {
            // Получаем данные их апи и создаем таблицу
            const { model, data, modelInfo, deleteElement } = await loadData(modelName, { page, size, sortField, sortType });
            const dataGrid = new DataGrid(model, data, modelInfo, {
                editLinkFactory: getEditModelLinkCallback(modelName),
                deleteEventCallback: getDeleteEventCallback(deleteElement),
            });
            document.getElementById('dataGridContainer').appendChild(dataGrid.render());

            document.getElementById('modelName').textContent = modelInfo.modelName;

            const pageSlider = new PagesSlider();
            document.getElementById('pageSlider').appendChild(pageSlider.render());

            const createButton = document.getElementById('createButton');
            let modelRef = modelName === 'Purchase' ? 'PurchaseExtra' : modelName;
            createButton.setAttribute('href', `..\\pages\\editdata.html?model=${modelRef}&action=create`);
        } catch (error) {
            showAlert(`Ошибка при загрузке данных: ${error.message}`, 'danger');
            console.error('Ошибка при загрузке данных:', error);
        }
    } else {
        console.error('Модель не определена');
        showAlert(`Модель не определена. Проверьте правильность строки URL`);
    }
}

function getEditModelLinkCallback(modelName) {
    return (id) => (`..\\pages\\editdata.html?model=${modelName}&modelId=${id}&action=edit`);
}

function getDeleteEventCallback(deleteElement) {
    return async (id) => {
        const confirmDelete = confirm('Удалить данную запись?');
        if (!confirmDelete) return;

        try {
            await deleteElement(id);
            window.location.reload();
        } catch (e) {
            console.error('Не удалось удалить', e);
            showAlert('Ошибка удаления')
        }
    }
}

async function loadData(modelName, params) {
    try {
        const modelModule = await import(`../models/${modelName}.js`);
        const model = modelModule.default;
        const { modelInfo } = modelModule;

        const { fetchData, deleteElement } = await import(`../api/${modelName}Api.js`);
        const data = await fetchData(params);

        return { model, data, modelInfo, deleteElement };
    } catch (error) {
        console.error('Ошибка при загрузке модели или данных:', error);
        throw error;
    }
}
